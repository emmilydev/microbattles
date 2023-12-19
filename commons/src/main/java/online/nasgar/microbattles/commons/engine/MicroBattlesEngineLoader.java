package online.nasgar.microbattles.commons.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import online.nasgar.microbattles.api.model.engine.MicroBattlesEngine;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;

public class MicroBattlesEngineLoader {
  private final ObjectMapper objectMapper;

  @Inject
  public MicroBattlesEngineLoader(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public MicroBattlesEngine loadEngine(Plugin plugin) {
    MicroBattlesEngine engine = loadEngineFromFile(plugin, "microbattles-engine.json");

    if (engine == null) {
      engine = loadEngineFromFile(plugin, "mb-engine.json");
    }

    if (engine == null) {
      Bukkit.getPluginManager().disablePlugin(plugin);

      return null;
    }

    return engine;
  }

  public MicroBattlesEngine loadEngineFromFile(Plugin plugin,
                                               String filename) {
    try {
      File engineFile = new File(plugin.getDataFolder(), filename);

      if (!engineFile.exists()) {
        plugin.getLogger().severe("Unable to find the " + filename + " file.");

        return null;
      }

      return objectMapper.readValue(engineFile, MicroBattlesEngine.class);
    } catch (IOException e) {
      plugin.getLogger().severe("Unable to load " + filename + ": " + e);

      Bukkit.getPluginManager().disablePlugin(plugin);

      return null;
    }
  }
}
