package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MySQLGameDAO implements GameDAO
{
    private static Gson serializer = new Gson();

    public MySQLGameDAO()
    {
        initializeTableDB();
    }

    @Override
    public void clear()
    {
        String statement = "TRUNCATE games";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public void createGame(GameData gameData)
    {
        String gameJSON = serializer.toJson(gameData.game());
        String statement = "INSERT INTO games (whiteUsername, blackUsername, gameName, game) VALUES (?,?,?,?)";
        DatabaseManager.executeUpdate(statement, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), gameJSON);
    }

    @Override
    public GameData getGame(int gameID)
    {
        try (Connection conn = DatabaseManager.getConnection())
        {
            String statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games WHERE gameID=?";
            PreparedStatement ps = conn.prepareStatement(statement);
            ps.setInt(1, gameID);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
            {
                int gameIDreturn = rs.getInt("gameID");
                String whiteUsername = rs.getString("whiteUsername");
                String blackUsername = rs.getString("blackUsername");
                String gameName = rs.getString("gameName");
                ChessGame game = serializer.fromJson(rs.getString("game"), ChessGame.class);
                return new GameData(gameIDreturn, whiteUsername, blackUsername, gameName, game);
            }
        } catch (SQLException e)
        {
            System.out.println("NOTICE: " + e.getMessage());
            return null;
        } catch (DataAccessException e)
        {
            return null;
        }
        return null;
    }

    @Override
    public List<GameData> listGames()
    {
        return List.of();
    }

    @Override
    public void updateGame(GameData gameData)
    {
        String gameJSON = serializer.toJson(gameData.game());
        String statement = "UPDATE games SET whiteUsername=?, blackUsername=?, gameName=?, game=? WHERE gameID=?";
        DatabaseManager.executeUpdate(statement, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), gameJSON, gameData.gameID());
    }

    private final String[] createStatement = {
            """
            CREATE TABLE IF NOT EXISTS games (
            gameID int NOT NULL AUTO_INCREMENT,
            whiteUsername varchar(256) NOT NULL,
            blackUsername varchar(256) NOT NULL,
            gameName varchar(256) NOT NULL,
            game TEXT DEFAULT NULL,
            PRIMARY KEY (gameID)
            )
            """
    };

    private void initializeTableDB()
    {
        try
        {
            DatabaseManager.createDatabase();
            try (Connection conn = DatabaseManager.getConnection())
            {
                for (String statement : createStatement)
                {
                    try (var preparedStatement = conn.prepareStatement(statement))
                    {
                        preparedStatement.executeUpdate();
                    }
                }
            } catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        } catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
}
