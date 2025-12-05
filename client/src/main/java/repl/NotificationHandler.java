package repl;

import websocket.messages.ErrorMessage;
import websocket.messages.NotificationMessage;

import static ui.EscapeSequences.*;

public class NotificationHandler {

    private final static String endOL = SET_TEXT_COLOR_LIGHT_GREY + "\n>>>" + RESET_TEXT_COLOR;

    public static void notify(NotificationMessage message) {
        String msg = message.getMessage();
        if (!msg.contains("move:")) {
            msg = msg + endOL;
        }
        System.out.print(msg);
    }

    public static void notify(ErrorMessage message) {
        System.out.print('\n' + SET_TEXT_COLOR_RED + message.getErrorMessage() + endOL);
    }
}
