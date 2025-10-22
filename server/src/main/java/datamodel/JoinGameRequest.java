package datamodel;

import chess.ChessGame.TeamColor;

public record JoinGameRequest(String authToken, TeamColor playerColor, int gameID)
{
    public JoinGameRequest addAuth(String authToken)
    {
        return new JoinGameRequest(authToken, playerColor, gameID);
    }
}
