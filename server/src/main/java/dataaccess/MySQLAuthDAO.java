package dataaccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MySQLAuthDAO implements AuthDAO
{
    public MySQLAuthDAO()
    {
        initializeTableDB();
    }

    @Override
    public void clear()
    {

    }

    @Override
    public void createAuth(AuthData authData)
    {

    }

    @Override
    public AuthData getAuth(String authToken)
    {
        return null;
    }

    @Override
    public List<AuthData> getAuthByUsername(String username)
    {
        return List.of();
    }

    @Override
    public void deleteAuth(AuthData authData)
    {

    }

    private final String[] createStatement = {
            """
            CREATE TABLE IF NOT EXISTS auths (
            authToken varchar(256) NOT NULL,
            username varchar(256) NOT NULL,
            PRIMARY KEY (authToken)
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
