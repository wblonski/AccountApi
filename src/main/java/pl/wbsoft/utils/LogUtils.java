package pl.wbsoft.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;

public class LogUtils {
    private static final int PARENT_METHOD_LEVEL = 3;
    private static final String HEADER_STR = ".%1$s, %2$s";

    private LogUtils() {
    }

    public static void logMethodMsg(Object obj, Level level, String msg, Exception ex) {
        Logger.getLogger(obj.getClass().getName()).log(level,
                HEADER_STR + getParentMethodName(PARENT_METHOD_LEVEL) + ". " + msg, ex);
    }

    public static void logMethodMsg(Object obj, Level level, Exception ex) {
        Logger.getLogger(obj.getClass().getName()).log(level, HEADER_STR + getParentMethodName(PARENT_METHOD_LEVEL), ex);
    }

    public static void logMethodMsg(Object obj, Level level, String msg) {
        Logger.getLogger(obj.getClass().getName()).log(level,
                format(HEADER_STR, getParentMethodName(PARENT_METHOD_LEVEL), msg));
    }

    private static String getParentMethodName(final int depth) {
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        return ste[depth].getMethodName() + ":" + ste[depth].getLineNumber();
    }

}
