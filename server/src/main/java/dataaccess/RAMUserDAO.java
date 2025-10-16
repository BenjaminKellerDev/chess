package dataaccess;

import model.UserData;

import java.util.ArrayList;
import java.util.List;

public class RAMUserDAO implements UserDAO
{

    private List<UserData> users = new ArrayList<>();

    @Override
    public void clear()
    {
        users.clear();
    }

    @Override
    public void createUser(UserData userData)
    {
        users.add(userData);
    }

    @Override
    public UserData getUser(String username)
    {
        for (var user : users)
        {
            if (user.username().equals(username))
            {
                return user;
            }
        }
        return null;
    }
}
