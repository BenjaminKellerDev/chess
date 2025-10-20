package dataaccess;

import model.GameData;

import java.util.List;

public interface GameDAO
{
    void clear();

    void createGame(GameData gameData);

    GameData getGame();

    List<GameData> listGames();

    void updateGame(GameData gameData);
}
