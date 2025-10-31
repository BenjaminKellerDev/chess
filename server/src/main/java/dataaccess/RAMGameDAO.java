package dataaccess;

import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RAMGameDAO implements GameDAO
{

    private List<GameData> gameList = new ArrayList<>();

    private int nextGameID = 1;

    @Override
    public void clear()
    {
        gameList.clear();
    }

    @Override
    public int createGame(GameData gameData)
    {
        gameList.add(gameData);
        nextGameID++;
        return nextGameID - 1;
    }

    @Override
    public GameData getGame(int gameID)
    {
        for (var game : gameList)
        {
            if (game.gameID() == gameID)
            {
                return game;
            }
        }
        return null;
    }

    @Override
    public List<GameData> listGames()
    {
        return Collections.unmodifiableList(gameList);
    }

    @Override
    public void updateGame(GameData gameData)
    {
        for (int i = 0; i < gameList.size(); i++)
        {
            if (gameList.get(i).gameID() == gameData.gameID())
            {
                gameList.set(i, gameData);
                return;
            }
        }
    }
}
