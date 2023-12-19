package online.nasgar.microbattles.commons.loader;

import online.nasgar.microbattles.api.loader.Loader;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.util.Set;

public class ListenerLoader implements Loader {
  private final Set<Listener> listeners;
  private final JavaPlugin javaPlugin;

  @Inject
  public ListenerLoader(Set<Listener> listeners,
                        JavaPlugin javaPlugin) {
    this.listeners = listeners;
    this.javaPlugin = javaPlugin;
  }

  @Override
  public void load() {
    PluginManager pluginManager = javaPlugin.getServer().getPluginManager();

    for (Listener listener : listeners) {
      pluginManager.registerEvents(listener, javaPlugin);
    }
  }
}
