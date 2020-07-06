package net.cubiness.colachampionship.minigame.config;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.cubiness.colachampionship.minigame.MinigameManager;

public class ConfigManager {
  private final MinigameManager minigames;
  private final Map<String, Config> configs = new HashMap<>();

  public ConfigManager(MinigameManager minigames) {
    this.minigames = minigames;
  }

  /**
   * Will clear all previously loaded configs, and load new ones from the given directory
   *
   * @param path The directory to read configs from
   */
  public void load(File path) {
    assert path != null;
    assert path.isDirectory();
    configs.clear();
    Arrays.asList(path.listFiles()).forEach(f -> {
      configs.put(f.getName().split("\\.")[0], new Config(this, f));
    });
  }

  public Config loadFile(File path) {
    assert path != null;
    assert path.isFile();
    return new Config(this, path);
  }

  /**
   * Checks if there is a config for the minigame name
   *
   * @param minigame The name of the minigame
   */
  public boolean hasConfig(String minigame) {
    return configs.containsKey(minigame);
  }

  /**
   * Gets the config for the given minigame
   *
   * @param minigame The name of the minigame
   * @return The config for that minigame
   */
  public Config get(String minigame) {
    assert configs.containsKey(minigame);
    return configs.get(minigame);
  }
}
