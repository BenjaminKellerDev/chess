package dataaccess;

import model.GameData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MySQLGameDAO implements GameDAO
{
    public MySQLGameDAO()
    {
        initializeTableDB();
    }

    @Override
    public void clear()
    {

    }

    @Override
    public void createGame(GameData gameData)
    {

    }

    @Override
    public GameData getGame(int gameID)
    {
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
