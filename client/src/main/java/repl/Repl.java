package repl;

import serveraccess.ServerAccessException;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public abstract class Repl {

    protected abstract String getAwaitUserInputText();

    protected abstract String getFirstMessageText();

    protected abstract String getEscapePhrase();

    protected abstract void onStart();

    public void run() {
        onStart();
        
        System.out.print(getFirstMessageText());

        Scanner scanner = new Scanner(System.in);
        String result = "";

        while (!result.equals(getEscapePhrase())) {
            String line = scanner.nextLine();
            try {
                result = eval(line);
                System.out.print(result);
            } catch (ServerAccessException e) {
                if (e.getMessage().contains("Server")) {
                    System.out.print(SET_TEXT_COLOR_RED + "Server Error\n"
                            + getAwaitUserInputText());
                } else {
                    System.out.print(SET_TEXT_COLOR_RED + e.getMessage() + "\n" + getAwaitUserInputText());
                    //System.out.print(SET_TEXT_COLOR_RED + "Invalid\n" + getAwaitUserInputText());
                }
            }
        }
    }

    protected abstract String eval(String input) throws ServerAccessException;
}
