package online.nasgar.microbattles.setup.menu.kit;

import com.cryptomorin.xseries.XSound;
import net.cosmogrp.storage.dist.CachedRemoteModelService;
import online.nasgar.microbattles.api.error.ErrorNotifier;
import online.nasgar.microbattles.api.message.MessageMode;
import online.nasgar.microbattles.api.model.kit.Kit;
import online.nasgar.microbattles.api.sound.SoundPlayer;
import online.nasgar.microbattles.commons.enums.PrettyEnums;
import online.nasgar.microbattles.commons.menu.AbstractMenu;
import online.nasgar.microbattles.commons.menu.kit.AbstractKitMenu;
import online.nasgar.microbattles.setup.MicroBattlesSetupPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import team.unnamed.gui.item.ItemBuilder;
import team.unnamed.gui.menu.item.ItemClickable;
import team.unnamed.gui.menu.type.MenuInventory;
import team.unnamed.gui.menu.type.PaginatedMenuInventoryBuilder;

import javax.inject.Inject;

public class KitsMenu extends AbstractKitMenu {
  private final CachedRemoteModelService<Kit> kitService;
  private final KitSettingsMenu kitSettingsMenu;
  private final MicroBattlesSetupPlugin plugin;

  @Inject
  public KitsMenu(CachedRemoteModelService<Kit> kitService,
                  KitSettingsMenu kitSettingsMenu,
                  MicroBattlesSetupPlugin plugin) {
    super("kits");
    this.kitService = kitService;
    this.kitSettingsMenu = kitSettingsMenu;
    this.plugin = plugin;
  }

  public void open(Player player) {
    messageHandler.sendIn(
      player,
      MessageMode.INFO,
      "kit.opening-menu"
    );

    kitService
      .findAll()
      .whenComplete((kits, error) -> {
        if (error != null) {
          ErrorNotifier.notify(error);

          messageHandler.sendIn(
            player,
            MessageMode.ERROR,
            "kit.error.opening-menu"
          );

          return;
        }

        Bukkit.getScheduler().runTask(
          plugin,
          () -> player.openInventory(MenuInventory
            .newPaginatedBuilder(Kit.class, getTitle(player))
            .itemsPerRow(7)
            .entityParser(kit -> buildKitItem(
              player,
              kit,
              inventory -> {
                SoundPlayer.playNormal(player, XSound.UI_BUTTON_CLICK);

                kitSettingsMenu.open(player, kit);

                return true;
              }
            ))
            .nextPageItem(page -> ItemClickable
              .onlyItem(ItemBuilder
                .builder(Material.PAPER)
                .name(getItemName(player, "next-page", "%page%", page))
                .lore(getItemLore(player, "next-page", "%page%", page))
                .build()
              )
            )
            .previousPageItem(page -> ItemClickable
              .onlyItem(ItemBuilder
                .builder(Material.PAPER)
                .name(getItemName(player, "previous-page", "%page%", page))
                .lore(getItemLore(player, "previous-page", "%page%", page))
                .build()
              )
            )
            .entities(kits)
            .build()
          )
        );
      });
  }
}
