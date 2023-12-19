package online.nasgar.microbattles.commons.item;

import online.nasgar.microbattles.api.adapt.NbtHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;
import java.util.function.Consumer;

public class ItemActionExecutor {
  private final NbtHandler nbtHandler;

  @Inject
  public ItemActionExecutor(NbtHandler nbtHandler) {
    this.nbtHandler = nbtHandler;
  }

  public void handle(PlayerInteractEvent event,
                     Runnable action) {
    ItemStack itemStack = event.getItem();

    if (itemStack == null) {
      return;
    }

    String clickable = nbtHandler.getTag(
      itemStack,
      "microbattles:clickable"
    );

    if (clickable == null || !clickable.equals("matches")) {
      return;
    }

    action.run();
  }
}
