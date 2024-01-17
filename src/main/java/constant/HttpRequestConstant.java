package constant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

public class HttpRequestConstant {
    public static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    public static final String END = "";
    public static final String CHARSET_NAME = "UTF-8";
    public static final String HOST = "Host:";
    public static final String CONNECTION = "Connection:";
    public static final String ACCEPT = "Accept:";
    public static final String NEW_LINE = "\n";
    public static final int HTTP_METHOD_POS = 0;
    public static final int PATH_POS = 1;
    public static final int HOST_POS = 1;
    public static final int CONNECTION_POS = 2;
    public static final int ACCEPT_POS = 3;
    public static final String PATH_DELIMITER = " ";
    public static final int EXTENSION_POS = 1;
    public static final String EXTENSION_DELIMITER = "/";
}
