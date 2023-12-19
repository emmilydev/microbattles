package online.nasgar.microbattles.api.model.kit.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ClickableItemApplier {
  void apply(Player player,
             ClickableItem itemStack);

  void apply(Player player,
             ClickableItem itemStack,
             int index);

  void apply(Player player,
             String itemStack);

  void apply(Player player,
             String itemStack,
             int index);

  ItemStack buildItem(Player player,
                      ClickableItem itemStack);
}
