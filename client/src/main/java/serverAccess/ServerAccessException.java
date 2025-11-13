package serverAccess;

/**
 * Indicates there was an error connecting to the database
 */
public class ServerAccessException extends Exception {
    public ServerAccessException(String message) {
        super(message);
    }

    public ServerAccessException(String message, Throwable ex) {
        super(message, ex);
    }
}
