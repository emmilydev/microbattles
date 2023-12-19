package online.nasgar.microbattles.lobby.listener;

import online.nasgar.microbattles.api.model.kit.KitApplier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.inject.Inject;

public class PlayerJoinListener implements Listener {

  private final KitApplier kitApplier;

  @Inject
  public PlayerJoinListener(KitApplier kitApplier) {
    this.kitApplier = kitApplier;
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();

    kitApplier.apply(player, "lobby");
  }
}
