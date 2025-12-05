package repl;

import websocket.messages.ErrorMessage;
import websocket.messages.NotificationMessage;

import static ui.EscapeSequences.*;

public class NotificationHandler {
    public static void notify(NotificationMessage message) {
        System.out.println(message.getMessage());
    }

    public static void notify(ErrorMessage message) {
        System.out.println(SET_TEXT_COLOR_RED + "ERROR: " + message.getErrorMessage() + RESET_TEXT_COLOR);
    }
}
