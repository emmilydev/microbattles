package online.nasgar.microbattles.setup.menu.material;

import online.nasgar.microbattles.api.adapt.MaterialProvider;
import online.nasgar.microbattles.commons.menu.AbstractMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import team.unnamed.gui.item.ItemBuilder;
import team.unnamed.gui.menu.item.ItemClickable;
import team.unnamed.gui.menu.type.MenuInventory;

import javax.inject.Inject;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class MaterialsMenu extends AbstractMenu {
  private final MaterialProvider materialProvider;

  @Inject
  public MaterialsMenu(MaterialProvider materialProvider) {
    super("materials");
    this.materialProvider = materialProvider;
  }

  public void open(Player player,
                   Consumer<Material> action,
                   Predicate<Inventory> closeAction) {
    player.openInventory(MenuInventory
      .newPaginatedBuilder(Material.class, getTitle(player))
      .itemsPerRow(7)
      .entityParser(material -> ItemClickable
        .builder(-1)
        .item(new ItemStack(material, 1))
        .action(event -> {
          action.accept(material);

          return true;
        })
        .build()
      )
      .nextPageItem(page -> ItemClickable.onlyItem(ItemBuilder
          .builder(Material.PAPER)
          .name(getItemName(player, "next-page", "%page%", page))
          .lore(getItemLore(player, "next-page", "%page%", page))
          .build()
        )
      )
      .previousPageItem(page -> ItemClickable.onlyItem(ItemBuilder
          .builder(Material.PAPER)
          .name(getItemName(player, "previous-page", "%page%", page))
          .lore(getItemLore(player, "previous-page", "%page%", page))
          .build()
        )
      )
      .entities(materialProvider.getAvailableMaterials())
      .closeAction(closeAction)
      .build()
    );
  }
}
