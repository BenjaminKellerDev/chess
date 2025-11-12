package repl;

import dataaccess.DataAccessException;
import model.AuthData;
import serverFacade.ServerFacade;

import static ui.EscapeSequences.*;

public class PostLoginRepl extends Repl {
    private static ServerFacade facade;
    private AuthData myAuthData;

    public PostLoginRepl(String serverURL, AuthData authData) {
        facade = new ServerFacade(serverURL);
        myAuthData = authData;
    }

    @Override
    protected String getAwaitUserInputText() {
        return SET_TEXT_COLOR_LIGHT_GREY + SET_TEXT_FAINT
                + "Chess >>>" + RESET_TEXT_BOLD_FAINT + SET_TEXT_COLOR_WHITE;
    }

    @Override
    protected String getFirstMessageText() {
        return SET_TEXT_COLOR_GREEN + "Success!!\n" + getAwaitUserInputText();
    }

    @Override
    public String eval(String input) throws DataAccessException {
        return "x";
    }
}
