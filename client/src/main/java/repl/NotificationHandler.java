package repl;

import websocket.messages.ErrorMessage;
import websocket.messages.NotificationMessage;

import static ui.EscapeSequences.*;

public class NotificationHandler {

    private final static String endOL = SET_TEXT_COLOR_LIGHT_GREY + "\nGame >>>" + RESET_TEXT_COLOR;

    public static void notify(NotificationMessage message) {
        String msg = message.getMessage() + endOL;

        System.out.print(SET_TEXT_COLOR_WHITE + msg);
    }

    public static void notify(ErrorMessage message) {
        System.out.print(SET_TEXT_COLOR_RED + message.getErrorMessage() + endOL);
    }
}
