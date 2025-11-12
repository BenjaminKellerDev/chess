package repl;

import dataaccess.DataAccessException;
import serverFacade.ServerFacade;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_WHITE;

public class GameRepl extends Repl {
    public static ServerFacade facade;

    public GameRepl(String serverURL) {
        facade = new ServerFacade(serverURL);
    }

    @Override
    protected String getAwaitUserInputText() {
        return SET_TEXT_COLOR_LIGHT_GREY + SET_TEXT_FAINT
                + "Game >>>" + RESET_TEXT_BOLD_FAINT + SET_TEXT_COLOR_WHITE;
    }

    @Override
    protected String getFirstMessageText() {
        return SET_TEXT_COLOR_GREEN + "Entered Game\n" + getAwaitUserInputText();
    }

    @Override
    protected String getEscapePhrase() {
        return SET_TEXT_BLINKING + "quiting game...";
    }

    @Override
    protected String eval(String input) throws DataAccessException {
        return "g\n" + getAwaitUserInputText();
    }
}
