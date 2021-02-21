package net.cubiness.colachampionship.minigame.config;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.cubiness.colachampionship.ColaCore;
import net.cubiness.colachampionship.minigame.MinigameManager;

public class ConfigManager {
  private final File path;
  private final MinigameManager minigames;
  private final Map<String, Config> configs = new HashMap<>();

  public ConfigManager(MinigameManager minigames, File path) {
    this.minigames = minigames;
    this.path = path;
    this.reload();
  }

  /**
   * Will clear all previously loaded configs, and load new ones from the given directory
   *
   * @param path The directory to read configs from
   */
  public void reload() {
    configs.clear();
    if (!path.exists()) {
      path.mkdir();
    }
    Arrays.asList(path.listFiles()).forEach(f -> {
      configs.put(f.getName().split("\\.")[0], new Config(this, f));
    });
  }

  public Config loadFile(File path) {
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
    if (configs.containsKey(minigame)) {
      return configs.get(minigame);
    } else {
      File f = new File(path, minigame + ".conf");
      if (!f.exists()) {
        try {
          f.createNewFile();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      Config c = new Config(this, f);
      configs.put(minigame, c);
      return c;
    }
  }
}
