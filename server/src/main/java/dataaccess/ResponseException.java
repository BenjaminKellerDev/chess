package dataaccess;

public class ResponseException extends Exception
{
    int code;

    public ResponseException(int code, String message)
    {
        super(message);
        this.code = code;
    }
}
