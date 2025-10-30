package dataaccess;

import model.UserData;

import java.sql.Connection;
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

    }

    @Override
    public void createUser(UserData userData)
    {

    }

    @Override
    public UserData getUser(String username)
    {
        return null;
    }

    @Override
    public UserData getUserByEmail(String email)
    {
        return null;
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
