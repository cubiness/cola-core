package net.cubiness.colachampionship.minigame.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {
  private final ConfigManager manager;
  private final String minigame;
  private final Map<String, String> values = new HashMap<>();

  /**
   * Constructor for Config. Will load and parse config from given file.
   *
   * @param manager The configManager
   * @param file The file to load the config from
   */
  public Config(ConfigManager manager, File file) {
    this.manager = manager;
    minigame = file.getName().split("\\.")[0];
    BufferedReader r = null;
    try {
      r = new BufferedReader(new FileReader(file));
      String line;
      int lineNum = 0;
      while ((line = r.readLine()) != null) {
        lineNum++;
        line = line.trim();
        if (line.equals("")) {
          continue;
        }
        List<String> values = Arrays.asList(line.split("="));
        if (values.size() != 2) {
          r.close();
          throw new RuntimeException("Cannot parse line " + lineNum + " of " + file);
        }
        this.values.put(values.get(0).trim(), values.get(1).trim());
      }
      r.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Gets the minigame name
   *
   * @return The name of the minigame that this config is for
   */
  public String getMinigame() {
    return minigame;
  }

  /**
   * Gets a value out for the given key
   *
   * @param key The key to get
   * @return The value
   */
  public String get(String key) {
    return values.get(key);
  }
}
