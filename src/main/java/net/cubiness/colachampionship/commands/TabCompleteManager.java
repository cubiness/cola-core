package net.cubiness.colachampionship.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TabCompleteManager implements TabCompleter {

  public Map<String, Map<List<String>, List<String>>> completions = new HashMap<>();

  public TabCompleteManager() {

  }

  public void addCompletion(String command, List<String> prefix, List<String> options) {
    completions.computeIfAbsent(command, k -> new HashMap<>());
    Map<List<String>, List<String>> commandCompletions = completions.get(command);
    commandCompletions.put(prefix, options);
  }

  @Override
  public List<String> onTabComplete(CommandSender commandSender, Command command,
      String commandText,
      String[] strings) {
    Map<List<String>, List<String>> commandCompletions = completions.get(command.getLabel());
    if (commandCompletions == null) {
      return null;
    }
    for (List<String> prefix : commandCompletions.keySet()) {
      if (strings.length - 1 != prefix.size()) {
        continue;
      }
      boolean matches = true;
      for (int i = 0; i < prefix.size(); i++) {
        if (!strings[i].equals(prefix.get(i))) {
          matches = false;
          break;
        }
      }
      if (!matches) {
        continue;
      }
      List<String> options = commandCompletions.get(prefix);
      String partialCommand = strings[strings.length - 1];
      return options.stream().filter(i -> i.startsWith(partialCommand)).collect(
          Collectors.toList());
    }
    return new ArrayList<>();
  }
}
