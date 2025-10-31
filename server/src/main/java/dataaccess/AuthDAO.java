package dataaccess;

import model.AuthData;

import java.util.List;

public interface AuthDAO
{
    //if I have time I want to redo these to throw the dataAccess Exception
    //and then Services handel Response Exception with appropriate error code
    //Perhaps even just pass an exception all the way up to SERVER and redesign tests
    void clear();

    void createAuth(AuthData authData);

    AuthData getAuth(String authToken);

    //TO-DO rewrite test to remove the need for this function
    List<AuthData> getAuthByUsername(String username);

    void deleteAuth(AuthData authData);
}
