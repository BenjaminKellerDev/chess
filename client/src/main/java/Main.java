import repl.PreLoginRepl;

public class Main {

    public static void main(String[] args) {
        String serverURL = "http://localhost:8080";
        if (args.length == 1)
            serverURL = args[0];
        new PreLoginRepl(serverURL).run();
    }
}