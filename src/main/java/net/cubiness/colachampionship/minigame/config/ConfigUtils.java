package net.cubiness.colachampionship.minigame.config;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class ConfigUtils {
  public static Location locationFromString(String str) {
    if (str == null) {
      throw new RuntimeException("Invalid location string!");
    }
    List<String> values = Arrays.asList(str.split(","));
    if (values.size() != 4) {
      throw new RuntimeException("Invalid location string!");
    }
    return new Location(Bukkit.getWorld(values.get(0).trim()),
        Float.parseFloat(values.get(1).trim()),
        Float.parseFloat(values.get(2).trim()),
        Float.parseFloat(values.get(3).trim()));
  }
}
