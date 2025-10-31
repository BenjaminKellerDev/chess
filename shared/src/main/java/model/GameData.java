package model;

import chess.ChessGame;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game)
{
    public GameData(int gameID, String gameName)
    {
        this(gameID, null, null, gameName, new ChessGame());
    }

    //-100 means not set yet
    public GameData(String gameName)
    {
        this(-100, null, null, gameName, new ChessGame());
    }

    public GameData updateWhiteUsername(String whiteUsername)
    {
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }

    public GameData updateBlackUsername(String blackUsername)
    {
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }
}
