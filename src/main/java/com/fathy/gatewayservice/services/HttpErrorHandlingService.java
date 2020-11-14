package com.fathy.gatewayservice.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fathy.gatewayservice.common.dto.FieldErrorDTO;
import com.fathy.gatewayservice.common.dto.GsmaErrorDTO;
import com.fathy.gatewayservice.common.dto.ReasonDTO;
import com.fathy.gatewayservice.common.enums.OrangeErrorInfo;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

/**
 * A service class used to handle operations when HTTP errors occur.
 * @since 1.1
 */
@Service
public class HttpErrorHandlingService {

    /**
     * builds a response body depending on the request context and client error
     *
     * @param request         The current HTTP request
     * @param orangeErrorInfo the custom error code and message (ODI standardized)
     * @param errorMessage    the custom error description message
     * @return a string representing the response body, could be different depending on the request URL
     * @throws IOException
     */
    public String buildErrorResponseBody(HttpServletRequest request, OrangeErrorInfo orangeErrorInfo, String errorMessage)
            throws IOException {
        //do further processing with request
        return buildErrorResponseBody(request.getRequestURI(), request.getMethod(), orangeErrorInfo, errorMessage);
    }

    public String buildErrorResponseBody(final String URI, final String httpMethod, OrangeErrorInfo orangeErrorInfo, String errorMessage)
            throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        if (URI.contains("/gsma")) {
            ReasonDTO reasonDTO = new ReasonDTO(Integer.valueOf(orangeErrorInfo.code), errorMessage);
            JSONObject RCSMessage = new JSONObject();
            RCSMessage.put("msgId", "NO_MESSAGE_UUID");
            RCSMessage.put("status", "failed");
            RCSMessage.put("timestamp", convertDateToTimeStamp());
            GsmaErrorDTO errorDTO = createGsmaErrorObject(RCSMessage, reasonDTO, URI, httpMethod);
            return objectMapper.writeValueAsString(errorDTO);
        } else
            return objectMapper.writeValueAsString(
                    new FieldErrorDTO(orangeErrorInfo.code, orangeErrorInfo.message, errorMessage));
    }

    private GsmaErrorDTO createGsmaErrorObject(JSONObject rcsMessage, ReasonDTO reasonDTO, String apiPath, String httpMethod) {
        final GsmaApi[] rcsReasonApis = {
                new GsmaApi("POST", "/+gsma/+bot/+v1/+.*/+messages"),
                new GsmaApi("GET", "/+gsma/+bot/+v1/+.*/+messages/+.*/+status")
        };
        return new GsmaErrorDTO(Arrays.stream(rcsReasonApis).anyMatch(gsmaApi ->
                gsmaApi.getMethod().equals(httpMethod) && apiPath.matches(gsmaApi.getPath())) ? rcsMessage : null,
                reasonDTO);
    }

    private static String convertDateToTimeStamp() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df.format(new Date()).concat("Z");
    }

    //Utility class
    private final class GsmaApi {
        private String method;
        private String path;

        public GsmaApi(String method, String path) {
            this.method = method;
            this.path = path;
        }

        public String getMethod() {
            return method;
        }

        public String getPath() {
            return path;
        }
    }
}