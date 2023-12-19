package online.nasgar.microbattles.commons.menu.kit;

import online.nasgar.microbattles.api.model.kit.Kit;
import online.nasgar.microbattles.commons.enums.PrettyEnums;
import online.nasgar.microbattles.commons.menu.AbstractMenu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import team.unnamed.gui.item.ItemBuilder;
import team.unnamed.gui.menu.item.ItemClickable;

import java.util.function.Predicate;

public abstract class AbstractKitMenu extends AbstractMenu {
  public AbstractKitMenu(String menuId) {
    super(menuId);
  }

  public ItemClickable buildKitItem(Player player,
                                    Kit kit,
                                    Predicate<Inventory> action,
                                    int slot) {
    return ItemClickable
      .builder(slot)
      .item(ItemBuilder
        .builder(kit.getIcon())
        .name(messageHandler.get(player, kit.getName()))
        .lore(messageHandler.replacingMany(
          player,
          kit.getDescription(),
          "%id%", kit.getId(),
          "%name%", "%path_" + kit.getName() + "%",
          "%icon%", PrettyEnums.prettify(kit.getIcon()),
          "%delay%", kit.getDelay(),
          "%permission%", kit.getPermission(),
          "%price%", kit.getPrice()
        ))
        .build()
      )
      .action(action)
      .build();
  }

  public ItemClickable buildKitItem(Player player,
                                    Kit kit,
                                    Predicate<Inventory> action) {
    return buildKitItem(player, kit, action, -1);
  }
}
