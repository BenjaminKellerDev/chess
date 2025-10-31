package dataaccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Types.NULL;

public class MySQLAuthDAO implements AuthDAO
{
    private final String[] createStatement = {
            """
            CREATE TABLE IF NOT EXISTS auths (
            authToken varchar(256) NOT NULL,
            username varchar(256) NOT NULL,
            PRIMARY KEY (authToken)
            )
            """
    };

    public MySQLAuthDAO()
    {
        DatabaseManager.initializeTable(createStatement);
    }

    @Override
    public void clear()
    {
        String statement = "TRUNCATE auths";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public void createAuth(AuthData authData)
    {
        String statement = "INSERT INTO auths (authToken, username) VALUES (?, ?)";
        DatabaseManager.executeUpdate(statement, authData.authToken(), authData.username());
    }

    @Override
    public AuthData getAuth(String authToken)
    {
        try (Connection conn = DatabaseManager.getConnection())
        {
            String statement = "SELECT authToken, username FROM auths WHERE authToken=?";
            try (PreparedStatement ps = conn.prepareStatement(statement))
            {
                ps.setString(1, authToken);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        String authT = rs.getString("authToken");
                        String username = rs.getString("username");
                        return new AuthData(authT, username);
                    }
                    else
                    {
                        return null;
                    }
                }
            }
        } catch (SQLException e) // see note on AuthDAO for refactor idea
        {
            return null;
        } catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthData> getAuthByUsername(String username)
    {
        List<AuthData> matchingAuths = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection())
        {
            String statement = "SELECT authToken, username FROM auths WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement))
            {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery())
                {
                    while (rs.next())
                    {
                        String authToken = rs.getString("authToken");
                        String usernameReturn = rs.getString("username");
                        matchingAuths.add(new AuthData(authToken, usernameReturn));
                    }
                }
            }
        } catch (SQLException e) // see note on AuthDAO for refactor idea
        {
            return null;
        } catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
        return matchingAuths;
    }

    @Override
    public void deleteAuth(AuthData authData)
    {
        try (Connection conn = DatabaseManager.getConnection())
        {
            String statement = "DELETE FROM auths WHERE authToken=? AND username=? ";
            try (PreparedStatement ps = conn.prepareStatement(statement))
            {
                ps.setString(1, authData.authToken());
                ps.setString(2, authData.username());
                ps.executeUpdate();
            }
        } catch (SQLException e)
        {
            System.out.println("NOTICE: " + e.getMessage());
        } catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
}
