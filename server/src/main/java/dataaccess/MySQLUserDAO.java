package dataaccess;

import model.AuthData;
import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLUserDAO implements UserDAO
{
    public MySQLUserDAO()
    {
        initializeTableDB();
    }

    @Override
    public void clear()
    {
        String statement = "TRUNCATE users";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public void createUser(UserData userData)
    {
        String statement = "INSERT INTO users (username, password, email) VALUES (?,?,?)";
        DatabaseManager.executeUpdate(statement, userData.username(), userData.password(), userData.email());
    }

    @Override
    public UserData getUser(String username)
    {
        try (Connection conn = DatabaseManager.getConnection())
        {
            String statement = "SELECT username, password, email FROM users WHERE username=?";
            //based on my research, this should auto close with the try-with-resources
            PreparedStatement ps = conn.prepareStatement(statement);
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    String userN = rs.getString("username");
                    String password = rs.getString("password");
                    String email = rs.getString("email");
                    return new UserData(userN, password, email);
                }
                else
                {
                    return null;
                }
            }
        } catch (SQLException e)
        {
            System.out.println("NOTICE: " + e.getMessage());
            return null;
        } catch (DataAccessException e)
        {
            return null;
        }
    }

    @Override
    public UserData getUserByEmail(String email)
    {
        try (Connection conn = DatabaseManager.getConnection())
        {
            String statement = "SELECT username, password, email FROM users WHERE email=?";
            //based on my research, this should auto close with the try-with-resources
            PreparedStatement ps = conn.prepareStatement(statement);
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    String emailReturned = rs.getString("email");
                    return new UserData(username, password, emailReturned);
                }
                else
                {
                    return null;
                }
            }
        } catch (SQLException e)
        {
            System.out.println("NOTICE: " + e.getMessage());
            return null;
        } catch (DataAccessException e)
        {
            return null;
        }
    }

    //String username, String password, String email
    private final String[] createStatement = {
            """
            CREATE TABLE IF NOT EXISTS users (
            username varchar(256) NOT NULL,
            password varchar(256) NOT NULL,
            email varchar(256) NOT NULL,
            PRIMARY KEY (username),
            INDEX(email)
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
