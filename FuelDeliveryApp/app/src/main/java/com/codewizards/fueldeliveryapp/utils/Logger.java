package com.codewizards.fueldeliveryapp.utils;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

/**
 * Created by dmikhov on 21.10.2016.
 */
public class Logger {

    private static final String FILTER_BODY_NAME = "<body>";
    private static final String FILTER_MSG_NAME = " <msg> ";
    private static final String TAG_NAME = "FUEL_APP";
    private static final String CHUNK_FORMAT = "CHUNK %d OF %d:  %s";
    private static final int CONSOLE_LOG_LINE_LENGTH = 4000;
    private boolean isEnabled = true;
    private Class<?> cls;

    private Logger() {}

    private Logger(Class<?> cls) {
        this.cls = cls;
    }

    private Logger(Class<?> cls, boolean isEnabled) {
        this.cls = cls;
        this.isEnabled = isEnabled;
    }

    public static Logger getLogger(Class<?> cls) {
        return new Logger(cls);
    }

    public static Logger getLogger(Class<?> cls, boolean isEnabled) {
        return new Logger(cls, isEnabled);
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    /**********************************************************************************************
     * DEBUG
     *********************************************************************************************/
    public void debug(String message) {
        d(message);
    }

    public void d(String message) {
        if(isEnabled) {
            debug(cls, message);
        }
    }

    public static void debug(Class<?> cls, String message) {
        String text = cls.getSimpleName() + FILTER_MSG_NAME + message;
        print(Log.DEBUG, text);
    }


    /**********************************************************************************************
     * INFO
     *********************************************************************************************/
    public void info(String message) {
        i(message);
    }

    public void i(String message) {
        if(isEnabled) {
            info(cls, message);
        }
    }

    public static void info(Class<?> cls, String message) {
        String text = cls.getSimpleName() + FILTER_MSG_NAME + message;
        infoOutput(text);
    }

    private static void infoOutput(String message) {
        print(Log.INFO, message);
    }


    /**********************************************************************************************
     * WARN
     *********************************************************************************************/
    public void warn(String message) {
        if(isEnabled) {
            warn(cls, message);
        }
    }

    public void w(String msg) {
        warn(msg);
    }

    public static void warn(Class<?> cls, String message) {
        String text = cls.getSimpleName() + FILTER_MSG_NAME + message;
        warnOutput(text);
    }

    private static void warnOutput(String message) {
        print(Log.WARN, message);
    }


    /**********************************************************************************************
     * ERROR
     *********************************************************************************************/
    public void error(String message, Exception e) {
        error(cls, message, e);
    }

    public void e(String msg) {
        error(msg);
    }
    public void error(String message) {
        if(isEnabled) {
            error(cls, message);
        }
    }

    public static void error(Class<?> cls, String message, Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String text = cls.getSimpleName() + FILTER_MSG_NAME + message + "\n" + sw.toString();
        errorOutput(text);
    }

    public static void error(Class<?> cls, String message) {
        String text = cls.getSimpleName() + FILTER_MSG_NAME + message;
        errorOutput(text);
    }

    private static void errorOutput(String message) {
        print(Log.ERROR, message);
    }

    private static void print(int priority, String message) {
        String fullMsg = FILTER_BODY_NAME + message;
        if(fullMsg.length() > CONSOLE_LOG_LINE_LENGTH) {
            int chunkCount = fullMsg.length() / CONSOLE_LOG_LINE_LENGTH;     // integer division
            for (int i = 0; i <= chunkCount; i++) {
                int max = CONSOLE_LOG_LINE_LENGTH * (i + 1);
                String textToPrint;
                if (max >= fullMsg.length()) {
                    textToPrint = String.format(Locale.ENGLISH, CHUNK_FORMAT, i, chunkCount, fullMsg.substring(CONSOLE_LOG_LINE_LENGTH * i));
                } else {
                    textToPrint = String.format(Locale.ENGLISH, CHUNK_FORMAT, i, chunkCount, fullMsg.substring(CONSOLE_LOG_LINE_LENGTH * i, max));
                }
                Log.println(priority, TAG_NAME, textToPrint);
            }
        } else {
            Log.println(priority, TAG_NAME, FILTER_BODY_NAME + message);
        }
    }
}
