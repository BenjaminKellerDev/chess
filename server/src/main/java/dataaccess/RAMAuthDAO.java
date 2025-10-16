package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.List;

public class RAMAuthDAO implements AuthDAO
{
    private List<AuthData> authorizations = new ArrayList<>();

    @Override
    public void clear()
    {
        authorizations.clear();
    }

    @Override
    public void createAuth(AuthData authData)
    {
        authorizations.add(authData);
    }

    @Override
    public AuthData getAuth(String authToken)
    {
        for (var auth : authorizations)
        {
            if (auth.authToken() == authToken)
            {
                return auth;
            }
        }
        return null;
    }

    @Override
    public void deleteAuth(AuthData authData)
    {
        authorizations.remove(authData);
    }
}
