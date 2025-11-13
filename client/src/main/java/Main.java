import chess.ChessBoard;
import chess.ChessMove;
import repl.GameRepl;
import repl.PreLoginRepl;

public class Main {

    public static void main(String[] args) {
        String serverURL = "http://localhost:8080";
        if (args.length == 1)
            serverURL = args[0];
        ChessBoard cb = new ChessBoard();
        cb.resetBoard();
        System.out.print(GameRepl.buildBoard(cb));
        new PreLoginRepl(serverURL).run();
    }
}