package dataaccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static java.sql.Types.NULL;

public class MySQLAuthDAO implements AuthDAO
{
    public MySQLAuthDAO()
    {
        initializeTableDB();
    }

    @Override
    public void clear() throws ResponseException
    {
        String statement = "TRUNCATE auths";
        executeUpdate(statement);
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

    private void executeUpdate(String statement, Object... params) throws ResponseException
    {
        try (Connection conn = DatabaseManager.getConnection())
        {
            try (PreparedStatement ps = conn.prepareStatement(statement))
            {
                for (int i = 0; i < params.length; i++)
                {
                    if (params[i] instanceof String p)
                    {
                        ps.setString(i + 1, p);
                    }
                    else if (params[i] instanceof Integer p)
                    {
                        ps.setInt(i + 1, p);
                    }
                    else if (params[i] == null)
                    {
                        ps.setNull(i + 1, NULL);
                    }
                }
                ps.executeUpdate();
            }
        } catch (Exception e)
        {
            throw new ResponseException(500, e.getMessage());
        }
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
