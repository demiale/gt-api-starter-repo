package common.constants;

public final class ResponseCode {

    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int NO_CONTENT = 204;

    public static final int MULTIPLE_CHOICES = 300;
    public static final int MOVED_PERMANENTLY = 301;
    public static final int FOUND = 302;
    public static final int NOT_MODIFIED = 304;
    public static final int TEMPORARY_REDIRECT = 307;

    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int GONE = 410;
    public static final int UNPROCESSABLE_ENTITY = 422;

    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int NOT_IMPLEMENTED = 501;
    public static final int BAD_GATEWAY = 502;
    public static final int SERVICE_UNAVAILABLE = 503;
    public static final int PERMISSION_DENIED = 550;
}
