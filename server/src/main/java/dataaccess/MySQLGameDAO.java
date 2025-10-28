package dataaccess;

import model.GameData;

import java.util.List;

public class MySQLGameDAO implements GameDAO
{
    @Override
    public void clear()
    {
        
    }

    @Override
    public void createGame(GameData gameData)
    {

    }

    @Override
    public GameData getGame(int gameID)
    {
        return null;
    }

    @Override
    public List<GameData> listGames()
    {
        return List.of();
    }

    @Override
    public void updateGame(GameData gameData)
    {

    }
}
