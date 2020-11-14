package com.fathy.gatewayservice.filters;

import com.fathy.gatewayservice.repositories.UserRepository;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.fathy.gatewayservice.common.enums.OrangeErrorInfo;
import com.fathy.gatewayservice.entities.User;
import com.fathy.gatewayservice.services.HttpErrorHandlingService;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * A filter to check and forbid deleted accounts and invalidate tokens that haven't yet expired but holding claims that
 * are inconsistent with the current user's state
 *
 * @since 1.1
 */
@Component
public class UserFilter extends ZuulFilter {

  private final UserRepository userRepository;
  private final HttpErrorHandlingService errorHandlingService;

  @Value("${token.validitySeconds}")
  private long validitySeconds;

  private static final Logger LOGGER = LoggerFactory.getLogger("kibana-logger");

  public UserFilter(UserRepository deletedUserRepository, HttpErrorHandlingService errorHandlingService) {
    this.userRepository = deletedUserRepository;
    this.errorHandlingService = errorHandlingService;
  }


  @Override
  public String filterType() {
    return FilterConstants.PRE_TYPE; //Executed before the request is routed
  }

  @Override
  public int filterOrder() {
    return 0;
  }

  @Override
  public boolean shouldFilter() {
    return true; //Indicates that run() method should be invoked
  }

  @Override
  @SuppressWarnings("unchecked")
  public Object run() {
    //A wrapper around the request, shared by all filters and is unique to each request
    RequestContext requestContext = RequestContext.getCurrentContext();
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    final String email = authentication.getName();
    final User user = userRepository.findByEmail(email);
    try {
      //Set Response Body here
      if(Objects.nonNull(user)) {
        if(user.isDeleted())
          buildResponse(requestContext, HttpStatus.FORBIDDEN, OrangeErrorInfo.FORBIDDEN_USER, "User is deleted");
        else {
          //The JWT details are only available for secured routes (i.e. routes requiring access token)
          if(authentication.getDetails() instanceof OAuth2AuthenticationDetails) {
            final Map<String, Object> jwtClaims = (Map<String, Object>)((OAuth2AuthenticationDetails)authentication.getDetails())
                    .getDecodedDetails();
            if(isInvalidToken(user.getTokenLastUpdated(), (Long)jwtClaims.get("exp")))
              buildResponse(requestContext, HttpStatus.UNAUTHORIZED, OrangeErrorInfo.EXPIRED_CREDENTIALS, "COMBO token expired");
          }
        }
      }
    } catch (IOException e) {
      LOGGER.error("Error while serializing object to json");
    }
    return null;
  }

  /**
   * builds a response depending on the request context and client error
   * @param requestContext the current request context
   * @param httpStatus the response HTTP status code
   * @param orangeErrorInfo the custom error code and message (ODI standardized)
   * @param errorMessage the custom error description message
   * @throws IOException if converting the response body object to JSON has failed (propagated)
   */
  private void buildResponse(RequestContext requestContext, HttpStatus httpStatus, OrangeErrorInfo orangeErrorInfo,
                             String errorMessage) throws IOException {
    requestContext.setSendZuulResponse(false);
    requestContext.setResponseStatusCode(httpStatus.value());
    requestContext.getResponse().setContentType(MediaType.APPLICATION_JSON_VALUE);

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
    requestContext.setResponseBody(errorHandlingService.buildErrorResponseBody(
            requestContext.getRequest(), orangeErrorInfo, errorMessage));
  }

  /**
   * computes the creation date of the token using the validity duration defined in different environments
   * (creation date = expiration date - validity duration) and checks whether this date is earlier that the token's
   * last updated date
   *
   * @param tokenLastUpdated the date where the token was last modified/updated
   * @param expiration the token expiration date in seconds
   * @return true if the creation date is earlier than last updated date
   */
  private boolean isInvalidToken(Date tokenLastUpdated, Long expiration) {
    Instant expirationDateTime = Instant.ofEpochSecond(expiration);
    Instant tokenLastUpdatedDateTime = tokenLastUpdated.toInstant();
    Duration duration = Duration.ofSeconds(validitySeconds);
    return expirationDateTime.minus(duration).isBefore(tokenLastUpdatedDateTime.truncatedTo(ChronoUnit.SECONDS));
  }
}
