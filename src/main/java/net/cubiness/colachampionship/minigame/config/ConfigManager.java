package net.cubiness.colachampionship.minigame.config;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import net.cubiness.colachampionship.minigame.MinigameManager;

public class ConfigManager {
  private final File path;
  private final MinigameManager minigames;
  private final Map<String, YamlConfiguration> configs = new HashMap<>();

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
      YamlConfiguration c = new YamlConfiguration();
      try {
        c.load(f);
      } catch (InvalidConfigurationException e) {
        throw new RuntimeException("Invalid configuration file:", e);
      } catch (IOException e) {
        e.printStackTrace();
      }
      configs.put(f.getName().split("\\.")[0], c);
    });
  }

  public void save() {
    configs.forEach((k, v) -> {
      try {
        v.save(new File(path, k + ".yml"));
      } catch (IOException e) {
        e.printStackTrace();
      }
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
  public YamlConfiguration get(String minigame) {
    if (configs.containsKey(minigame)) {
      return configs.get(minigame);
    } else {
      YamlConfiguration c = new YamlConfiguration();
      configs.put(minigame, c);
      return c;
    }
  }
}
