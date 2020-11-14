package com.fathy.gatewayservice.common.enums;

/**
 * ODI HTTP error messages and codes
 * @since 1.0
 * @see <a href="//https://developer.orange.com/tech_guide/orange-apis-error-handling/">Orange APIs Error Handling</a>
 */
public enum OrangeErrorInfo {
    //400: Bad Request
    INVALID_URL_PARAMETER_VALUE("20", "Invalid URL parameter value"),
    MISSING_BODY("21", "Missing body"),
    INVALID_BODY("22", "Invalid body"),
    MISSING_BODY_FIELD("23", "Missing body field"),
    INVALID_BODY_FIELD("24", "Invalid body field"),
    MISSING_HEADER("25", "Missing header"),
    INVALID_HEADER_VALUE("26", "Invalid header value"),
    MISSING_QUERY_PARAMETER("27", "Missing query-string parameter"),
    INVALID_QUERY_PARAMETER("28", "Invalid query-string parameter value"),

    //401: Unauthorized
    MISSING_CREDENTIALS("40", "Missing credentials"),
    INVALID_CREDENTIALS("41", "Invalid credentials"),
    EXPIRED_CREDENTIALS("42", "Expired credentials"),

    //403: Forbidden
    ACCESS_DENIED("50", "Access denied"),
    FORBIDDEN_REQUESTER("51", "Forbidden requester"),
    FORBIDDEN_USER("52", "Forbidden user"),
    TOO_MANY_REQUESTS("53", "Too many requests"),

    //404: Not Found
    RESOURCE_NOT_FOUND("60", "Resource not found"),

    //405: Method Not Allowed
    METHOD_NOT_ALLOWED("61", "Method not allowed"),

    //406: Not Acceptable
    NOT_ACCEPTABLE("62", "Not acceptable"),

    //408: Request Timeout
    REQUEST_TIMEOUT("63", "Request time-out"),

    //409: Conflict
    CONFLICT("", "Conflict"),

    //411: Length Required
    LENGTH_REQUIRED("64", "Length required"),

    //412: Precondition Failed
    PRECONDITION_FAILED("65", "Precondition failed"),

    //413: Payload Too Large
    REQUEST_ENTITY_TOO_LARGE("66", "Request entity too large"),

    //414: URI Too Long
    REQUEST_URI_TOO_LONG("67", "Request-URI too long"),

    //415: Unsupported Media Type
    UNSUPPORTED_MEDIA_TYPE("68", "Unsupported Media Type"),

    //500: Internal Server Error
    INTERNAL_ERROR("1", "Internal error"),

    //503: Service Unavailable
    SERVICE_UNAVAILABLE("5", "The service is temporarily unavailable"),
    API_OVER_CAPACITY("6", "Orange API is over capacity, retry later !");

    public final String code;
    public final String message;

    OrangeErrorInfo(final String code, final String message) {
        this.code = code;
        this.message = message;
    }
}