package repl;

import serverAccess.ServerAccessException;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public abstract class Repl {

    protected abstract String getAwaitUserInputText();

    protected abstract String getFirstMessageText();

    protected abstract String getEscapePhrase();

    public void run() {
        System.out.print(getFirstMessageText());

        Scanner scanner = new Scanner(System.in);
        String result = "";

        while (!result.equals(getEscapePhrase())) {
            String line = scanner.nextLine();
            try {
                result = eval(line);
                System.out.print(result);
            } catch (ServerAccessException e) {
                System.out.print(SET_TEXT_COLOR_RED + "Invalid\n"
                        + getAwaitUserInputText());
            }
        }
    }

    protected abstract String eval(String input) throws ServerAccessException;
}
