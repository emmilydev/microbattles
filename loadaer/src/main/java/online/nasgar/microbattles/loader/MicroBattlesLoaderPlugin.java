package online.nasgar.microbattles.loader;

import org.bukkit.plugin.java.JavaPlugin;

public class MicroBattlesLoaderPlugin extends JavaPlugin {

  @Override
  public void onEnable() {
    getLogger().info("MicroBattles loader successfully enabled!");
  }

  @Override
  public void onDisable() {
    getLogger().info("MicroBattles loader successfully disabled!");
  }
}
