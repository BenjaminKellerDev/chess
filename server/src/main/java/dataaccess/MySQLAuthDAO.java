package dataaccess;

import model.AuthData;

import java.util.List;

public class MySQLAuthDAO implements AuthDAO
{
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
}
