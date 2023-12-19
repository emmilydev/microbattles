package online.nasgar.microbattles.setup.listener.item;

import online.nasgar.microbattles.api.event.arena.ArenaBarrierEditEvent;
import online.nasgar.microbattles.api.event.arena.ArenaBarrierEditSessionEndEvent;
import online.nasgar.microbattles.api.model.point.CoordinatePoint;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class BarrierEditorItemListener implements Listener {
  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    Action action = event.getAction();
    Player player = event.getPlayer();
    
    if (
      action == Action.RIGHT_CLICK_AIR
        || action == Action.RIGHT_CLICK_BLOCK
    ) {
      Bukkit.getPluginManager().callEvent(new ArenaBarrierEditSessionEndEvent(player));
    } else {
      Block block = event.getClickedBlock();

      if (block == null) {
        return;
      }

      Bukkit.getPluginManager().callEvent(new ArenaBarrierEditEvent(
        player,
        new CoordinatePoint(block.getLocation())
      ));
    }
  }
}
