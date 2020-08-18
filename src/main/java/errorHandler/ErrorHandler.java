package errorHandler;

import Log.Log;

/**
 * Created by Alireza on 6/28/2015.
 */
public class ErrorHandler {
    public static boolean hasError = false;

    public static void printError(String msg) {
        hasError = true;
        Log.print(msg);
    }
}
