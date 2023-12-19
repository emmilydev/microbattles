package online.nasgar.microbattles.setup.menu.kit;

import com.cryptomorin.xseries.XSound;
import net.cosmogrp.storage.dist.CachedRemoteModelService;
import online.nasgar.microbattles.api.message.MessageMode;
import online.nasgar.microbattles.api.model.kit.Kit;
import online.nasgar.microbattles.api.sound.SoundPlayer;
import online.nasgar.microbattles.commons.menu.AbstractMenu;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import team.unnamed.gui.item.util.DecorateItemUtils;
import team.unnamed.gui.menu.item.ItemClickable;
import team.unnamed.gui.menu.type.MenuInventory;
import team.unnamed.gui.menu.type.MenuInventoryBuilder;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class KitEditorMenu extends AbstractMenu {
  private final CachedRemoteModelService<Kit> kitService;

  @Inject
  public KitEditorMenu(CachedRemoteModelService<Kit> kitService) {
    super("kit-editor");
    this.kitService = kitService;
  }

  public void open(Player player,
                   String kitId,
                   List<ItemStack> originalItems,
                   Predicate<Inventory> closeAction) {
    List<ItemClickable> parsedItems = new ArrayList<>(originalItems.size());

    for (ItemStack itemStack : originalItems) {
      parsedItems.add(ItemClickable.onlyItem(itemStack));
    }

    MenuInventoryBuilder builder = MenuInventory
      .newStringLayoutBuilder(getTitle(player, "%kit%", kitId))
      .items(parsedItems)
      .layoutLines(
        "",
        "",
        "",
        "",
        "",
        "",
        "cnnnrnnns"
      )
      .layoutItem('c', ItemClickable
        .builder(-1)
        .item(DecorateItemUtils
          .stainedPaneBuilder(DyeColor.RED)
          .name(getItemName(player, "cancel"))
          .lore(getItemLore(player, "cancel"))
          .build()
        )
        .action(inventory -> {
          SoundPlayer.playNormal(player, XSound.UI_BUTTON_CLICK);

          player.getInventory().addItem(getItemsBack(inventory, originalItems));

          messageHandler.sendReplacingIn(
            player,
            MessageMode.ERROR,
            "kit.edition-cancelled",
            "%kit%", kitId
          );

          return true;
        })
        .build()
      )
      .layoutItem('r', ItemClickable
        .builder(-1)
        .item(DecorateItemUtils
          .stainedPaneBuilder(DyeColor.YELLOW)
          .name(getItemName(player, "clear"))
          .lore(getItemLore(player, "clear"))
          .build()
        )
        .action(inventory -> {
         SoundPlayer.playNormal(player, XSound.UI_BUTTON_CLICK);

          player.getInventory().addItem(getItemsBack(inventory, originalItems));

          return true;
        })
        .build()
      )
      .layoutItem('s', ItemClickable
        .builder(-1)
        .item(DecorateItemUtils
          .stainedPaneBuilder(DyeColor.GREEN)
          .name(getItemName(player, "save"))
          .lore(getItemLore(player,"lore"))
          .build()
        )
        .action(inventory -> {
          SoundPlayer.playNormal(player, XSound.UI_BUTTON_CLICK);
          List<ItemStack> bukkitItems = Arrays.asList(getItemsBack(inventory, Collections.emptyList()));

          Kit kit = new Kit(
            kitId,
            "kit." + kitId + ".description",
            null,
            null,
            null,
            0,
            "",
            0
          );

          kitService.save(kit);

          messageHandler.sendReplacingIn(
            player,
            MessageMode.SUCCESS,
            "kit.created",
            "%kit%", kitId
          );

          return true;
        })
        .build()
      )
      .closeAction(closeAction);
  }

  public void open(Player player,
                   String kit,
                   List<ItemStack> originalItems) {
    open(player, kit, originalItems, inventory -> false);
  }

  private ItemStack[] getItemsBack(Inventory inventory,
                                   List<ItemStack> originalItems) {
    List<ItemStack> putItems = new ArrayList<>(originalItems);
    List<ItemStack> inventoryContents = Arrays.asList(inventory.getContents());

    inventoryContents.remove(53);
    inventoryContents.remove(49);
    inventoryContents.remove(45);

    putItems.addAll(inventoryContents);

    return putItems.toArray(new ItemStack[0]);
  }
}
