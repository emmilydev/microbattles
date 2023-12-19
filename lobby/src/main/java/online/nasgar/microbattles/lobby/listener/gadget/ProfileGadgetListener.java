package online.nasgar.microbattles.lobby.listener.gadget;

import com.cryptomorin.xseries.XSound;
import online.nasgar.microbattles.api.adapt.NbtHandler;
import online.nasgar.microbattles.api.sound.SoundPlayer;
import online.nasgar.microbattles.commons.item.ItemActionExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;

public class ProfileGadgetListener implements Listener {
  private final ItemActionExecutor itemActionExecutor;

  @Inject
  public ProfileGadgetListener(ItemActionExecutor itemActionExecutor) {
    this.itemActionExecutor = itemActionExecutor;
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    Player player = event.getPlayer();

    itemActionExecutor.handle(
      event,
      () -> {
        SoundPlayer.playNormal(player, XSound.UI_BUTTON_CLICK);
        player.performCommand("profile");
      }
    );
  }
}
