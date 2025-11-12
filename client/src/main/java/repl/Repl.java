package repl;

import dataaccess.DataAccessException;
import serverFacade.ServerFacade;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public abstract class Repl {

    private static ServerFacade facade;

    protected abstract String getAwaitUserInputText();

    protected abstract String getFirstMessageText();

    public void run() {
        System.out.print(getFirstMessageText());

        Scanner scanner = new Scanner(System.in);
        String result = "";

        while (!result.equals("quit")) {
            String line = scanner.nextLine();
            try {
                result = eval(line);
                System.out.print(result);
            } catch (DataAccessException e) {
                System.out.print(SET_TEXT_COLOR_RED + "Invalid\n"
                        + getAwaitUserInputText());
            }
        }
    }

    protected abstract String eval(String input) throws DataAccessException;
}
