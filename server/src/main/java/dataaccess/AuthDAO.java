package dataaccess;

import model.AuthData;

import java.util.List;

public interface AuthDAO
{
    void clear();

    void createAuth(AuthData authData);

    AuthData getAuth(String authToken);

    List<AuthData> getAuthByUsername(String username);

    void deleteAuth(AuthData authData);
}
