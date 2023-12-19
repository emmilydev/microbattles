package online.nasgar.microbattles.lobby;

import me.yushust.inject.Injector;
import online.nasgar.microbattles.api.loader.Loader;
import online.nasgar.microbattles.lobby.bind.BinderModule;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.util.Set;
import java.util.logging.Logger;

public class MicroBattlesLobbyPlugin extends JavaPlugin {

  @Inject
  private Set<Loader> loaders;

  @Override
  public void onEnable() {
    long start = System.currentTimeMillis();
    Logger logger = getLogger();

    logger.info("Setting up injector...");

    Injector injector = Injector.create(new BinderModule(this));
    injector.injectMembers(this);

    logger.info("Setting up loaders...");

    for (Loader loader : loaders) {
      loader.load();
    }

    logger.info(String.format(
      "Loaded %s loaders in %sms",
      loaders.size(),
      (System.currentTimeMillis() - start)
    ));
  }

  @Override
  public void onDisable() {
    for (Loader loader : loaders) {
      loader.unload();
    }
  }
}
