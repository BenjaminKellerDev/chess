
import ui.EscapeSequences;

import java.util.Scanner;

import client.*;

public class Repl {
    private final PreLoginClient preLoginClient;
    private final PostLoginClient postLoginClient;
    private final GameClient gameClient;

    private enum state {
        preLogin, postLogin, game
    }

    private state currentReplState;

    public Repl(String serverURL) {
        preLoginClient = new PreLoginClient(serverURL);
        postLoginClient = new PostLoginClient(serverURL);
        gameClient = new GameClient(serverURL);
        currentReplState = state.preLogin;
    }

    public void run() {
        System.out.println("♕ 240 Chess Client: ");
        System.out.print("Chess Login >>>");

        Scanner scanner = new Scanner(System.in);
        String result = "";

        while (!result.equals("quit")) {
            String line = scanner.nextLine();
            switch (currentReplState) {
                case preLogin:
                    result = preLoginClient.eval(line);
                    System.out.print(result);
                    break;
                case postLogin:
                    result = postLoginClient.eval(line);
                    System.out.print(result);
                    break;
                case game:
                    result = gameClient.eval(line);
                    System.out.print(result);
                    break;
            }
            if (result.contains("Chess Login >>>"))
                currentReplState = state.preLogin;
            else if (result.contains("Chess >>>"))
                currentReplState = state.postLogin;
            else if (result.contains("Chess Game >>>"))
                currentReplState = state.game;
        }
    }


}
