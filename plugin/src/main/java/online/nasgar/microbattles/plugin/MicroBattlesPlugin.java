package online.nasgar.microbattles.plugin;

import com.mongodb.client.MongoClient;
import me.yushust.inject.Injector;
import net.cosmogrp.storage.redis.connection.Redis;
import online.nasgar.microbattles.api.MicroBattlesAPI;
import online.nasgar.microbattles.api.loader.Loader;
import online.nasgar.microbattles.plugin.bind.BinderModule;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Logger;

public class MicroBattlesPlugin extends JavaPlugin {
  @Inject private Set<Loader> loaders;
  @Inject private MongoClient mongoClient;
  @Inject private Redis redis;
  @Inject private MicroBattlesAPI microBattlesAPI;

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

    getServer().getServicesManager().register(
      MicroBattlesAPI.class,
      microBattlesAPI,
      this,
      ServicePriority.Normal
    );

    logger.info(String.format(
      "Loaded %s loaders in %sms",
      loaders.size(),
      (System.currentTimeMillis() - start)
    ));
  }

  @Override
  public void onDisable() {
    mongoClient.close();
    try {
      redis.close();
    } catch (IOException e) {
      getLogger().info("Unable to close Redis connection: " + e);
    }

    for (Loader loader : loaders) {
      loader.unload();
    }
  }
}
