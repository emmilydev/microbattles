package online.nasgar.microbattles.plugin.listener.player;

import net.cosmogrp.storage.dist.CachedRemoteModelService;
import net.cosmogrp.storage.dist.RemoteModelService;
import online.nasgar.microbattles.api.error.ErrorNotifier;
import online.nasgar.microbattles.api.model.match.Match;
import online.nasgar.microbattles.api.model.user.User;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

public class PlayerDamageListener implements Listener {
  private final CachedRemoteModelService<User> userService;
  private final RemoteModelService<Match> matchService;

  @Inject
  public PlayerDamageListener(CachedRemoteModelService<User> userService,
                              RemoteModelService<Match> matchService) {
    this.userService = userService;
    this.matchService = matchService;
  }

  @EventHandler
  public void onPlayerDamage(EntityDamageByEntityEvent event) {
    Entity unsafeShooter = event.getDamager();
    Entity unsafeShot = event.getEntity();

    if (!(unsafeShooter instanceof Player) || !(unsafeShot instanceof Player)) {
      if (!(unsafeShooter instanceof Projectile)) {
        return;
      }

      ProjectileSource projectileShooter = ((Projectile) unsafeShooter).getShooter();

      if (!(projectileShooter instanceof Player)) {
        return;
      }
    }

    userService
      .getOrFind(unsafeShooter.getUniqueId().toString())
      .whenComplete((shooter, error) -> {
        if (error != null) {
          ErrorNotifier.notify(error);

          return;
        }

        User shot = userService.getOrFindSync(unsafeShot.getUniqueId().toString());

        if (shooter == null || shot == null) {
          event.setCancelled(true);

          return;
        }

        if (shooter.getCurrentMatch() == null || shot.getCurrentMatch() == null) {
          event.setCancelled(true);

          return;
        }

        if (shooter.getCurrentTeam() == shot.getCurrentTeam()) {
          Match match = matchService.findSync(shooter.getCurrentMatch());

          if (match == null || !match.isFriendlyFire()) {
            event.setCancelled(true);
          }
        }
      });
  }
}
