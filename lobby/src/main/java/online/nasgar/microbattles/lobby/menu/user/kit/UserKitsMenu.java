package online.nasgar.microbattles.lobby.menu.user.kit;

import com.cryptomorin.xseries.XSound;
import net.cosmogrp.storage.dist.CachedRemoteModelService;
import online.nasgar.microbattles.api.model.kit.Kit;
import online.nasgar.microbattles.api.model.user.User;
import online.nasgar.microbattles.api.sound.SoundPlayer;
import online.nasgar.microbattles.commons.enums.PrettyEnums;
import online.nasgar.microbattles.commons.menu.AbstractMenu;
import online.nasgar.microbattles.commons.menu.kit.AbstractKitMenu;
import online.nasgar.microbattles.lobby.MicroBattlesLobbyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import team.unnamed.gui.item.ItemBuilder;
import team.unnamed.gui.menu.item.ItemClickable;
import team.unnamed.gui.menu.type.MenuInventory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UserKitsMenu extends AbstractKitMenu {
  private final CachedRemoteModelService<Kit> kitService;
  private final MicroBattlesLobbyPlugin plugin;

  @Inject
  public UserKitsMenu(CachedRemoteModelService<Kit> kitService,
                      MicroBattlesLobbyPlugin plugin) {
    super("user-kits");
    this.kitService = kitService;
    this.plugin = plugin;
  }

  public void open(Player player,
                   User user) {
    Player thatPlayer = user.player();

    CompletableFuture.runAsync(() -> {
      List<Kit> kits = new ArrayList<>();

      for (String id : user.getKits()) {
        Kit kit = kitService.getOrFindSync(id);

        if (kit != null) {
          kits.add(kit);
        }
      }

      Bukkit.getScheduler().runTask(
        plugin,
        () -> player.openInventory(MenuInventory
          .newPaginatedBuilder(Kit.class, getTitle(
            player,
            "%name%", thatPlayer.getName()
          ))
          .itemsPerRow(7)
          .entityParser(kit -> buildKitItem(
            player,
            kit,
            inventory -> {
              SoundPlayer.playNormal(player, XSound.UI_BUTTON_CLICK);

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
