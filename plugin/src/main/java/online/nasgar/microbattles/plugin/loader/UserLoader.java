package online.nasgar.microbattles.plugin.loader;

import net.cosmogrp.storage.dist.CachedRemoteModelService;
import online.nasgar.microbattles.api.loader.Loader;
import online.nasgar.microbattles.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

public class UserLoader implements Loader {
  private final CachedRemoteModelService<User> userService;

  @Inject
  public UserLoader(CachedRemoteModelService<User> userService) {
    this.userService = userService;
  }

  @Override
  public void unload() {
    CompletableFuture.runAsync(() -> {
      for (Player player : Bukkit.getOnlinePlayers()) {
        User user = userService.getSync(player.getUniqueId().toString());

        if (user != null) {
          userService.saveSync(user);
        }
      }
    });
  }
}
