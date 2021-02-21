package net.cubiness.colachampionship.minigame;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class MinigameAPI {

  private final MinigameManager manager;

  public MinigameAPI(MinigameManager manager) {
    this.manager = manager;
  }

  /**
   * Called from another plugin, when it wishes to add a new minigame
   *
   * @param minigame The new minigame to be added
   */
  public void registerMinigame(Minigame minigame) {
    manager.registerMinigame(minigame);
  }

  /**
   * Gets the config object for a minigame
   *
   * @param minigame The minigame
   */
  public YamlConfiguration getConfig(Minigame minigame) {
    return manager.getConfigs().get(minigame.getName());
  }

  /**
   * Called from another plugin, when the scoreboard needs to be updated
   */
  public void updateScoreboard() {
    manager.getPlayers().forEach(p -> p.updateScoreboard());
  }

  /**
   * Called from another plugin, when the minigame has finished
   * This will fail an assert if the minigame passed in is now running
   *
   * @param game The minigame that has finished running
   */
  public void finish(Minigame game) {
    manager.finish(game);
  }

  /**
   * Called from another plugin, when it needs to teleport players back to the hub
   *
   * @return The location of the world spawn
   */
  public Location getSpawn() {
    return manager.getSpawn();
  }

  /**
   * Gets the MinigamePlayer for the given player
   *
   * @param player The player
   * @return The minigame player
   */
  public MinigamePlayer getMinigamePlayer(Player player) {
    return manager.getPlayer(player);
  }
}
