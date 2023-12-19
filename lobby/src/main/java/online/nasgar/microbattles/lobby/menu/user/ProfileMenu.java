package online.nasgar.microbattles.lobby.menu.user;

import com.cryptomorin.xseries.XSound;
import online.nasgar.microbattles.api.model.user.User;
import online.nasgar.microbattles.api.sound.SoundPlayer;
import online.nasgar.microbattles.commons.menu.AbstractMenu;
import online.nasgar.microbattles.lobby.menu.user.kit.UserKitsMenu;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import team.unnamed.gui.item.ItemBuilder;
import team.unnamed.gui.item.util.DecorateItemUtils;
import team.unnamed.gui.menu.item.ItemClickable;
import team.unnamed.gui.menu.type.MenuInventory;

import javax.inject.Inject;

public class ProfileMenu extends AbstractMenu {
  private final UserKitsMenu userKitsMenu;

  @Inject
  public ProfileMenu(UserKitsMenu userKitsMenu) {
    super("profile");
    this.userKitsMenu = userKitsMenu;
  }

  public void open(Player player,
                   User user) {
    Player thatPlayer = user.player();

    player.openInventory(MenuInventory
      .newStringLayoutBuilder(getTitle(
        player,
        "%name%", thatPlayer.getName()
      ))
      .layoutLines(
        "----P----",
        "ddddddddd",
        "--DkKlq--",
        "--WtDwE--",
        "---aRA---",
        "---------"
      )
      .layoutItem('P', ItemClickable
        .builder(-1)
        .item(ItemBuilder
          .skullBuilder(1)
          .owner(thatPlayer.getName())
          .name(getItemName(
            player,
            "player-format",
            "%name%", thatPlayer.getName()
          ))
          .lore(getItemLore(
            player,
            "player-format",
            "%name%", thatPlayer.getName()
          ))
          .build()
        )
        .build()
      )
      .layoutItem('d', ItemClickable
        .builder(-1)
        .item(DecorateItemUtils.stainedPane(DyeColor.ORANGE))
        .action(inventory -> {
          SoundPlayer.playNormal(player, XSound.UI_BUTTON_CLICK);

          return true;
        })
        .build()
      )
      .layoutItem('D', ItemClickable
        .builder(-1)
        .item(ItemBuilder
          .skullBuilder(1)
          .url("http://textures.minecraft.net/texture/124c129ba96d237ba65f9e062958d27abaafcb1b422930b72e1c52f0bbdab92")
          .name(getItemName(
            player,
            "deaths",
            "%name%", thatPlayer.getName(),
            "%deaths%", user.getDeaths()
          ))
          .lore(getItemLore(
            player,
            "deaths",
            "%name%", thatPlayer.getName(),
            "%deaths%", user.getDeaths()
          ))
          .build()
        )
        .action(inventory -> {
          SoundPlayer.playNormal(player, XSound.UI_BUTTON_CLICK);

          return true;
        })
        .build()
      )
      .layoutItem('k', ItemClickable
        .builder(-1)
        .item(ItemBuilder
          .builder(Material.LEATHER_CHESTPLATE)
          .name(getItemName(
            player,
            "kits",
            "%name%", thatPlayer.getName()
          ))
          .build()
        )
        .action(inventory -> {
          SoundPlayer.playNormal(player, XSound.UI_BUTTON_CLICK);

          userKitsMenu.open(player, user);

          return true;
        })
        .build()
      )
      .layoutItem('K', ItemClickable
        .builder(-1)
        .item(ItemBuilder
          .builder(Material.WOOD_SWORD)
          .name(getItemName(
            player,
            "kills",
            "%name%", thatPlayer.getName(),
            "%kills%", user.getKills()
          ))
          .lore(getItemLore(
            player,
            "deaths",
            "%name%", thatPlayer.getName(),
            "%kills%", user.getKills()
          ))
          .build()
        )
        .action(inventory -> {
          SoundPlayer.playNormal(player, XSound.UI_BUTTON_CLICK);

          return true;
        })
        .build()
      )
      .layoutItem('l', ItemClickable
        .builder(-1)
        .item(ItemBuilder
          .builder(Material.BOWL)
          .name(getItemName(
            player,
            "%name%", thatPlayer.getName(),
            "%lose-matches%", user.getLoseMatches()
          ))
          .lore(getItemLore(
            player,
            "%name%", thatPlayer.getName(),
            "%lose-matches%", user.getLoseMatches()
          ))
          .build()
        )
        .action(inventory -> {
          SoundPlayer.playNormal(player, XSound.UI_BUTTON_CLICK);

          return true;
        })
        .build()
      )
      .layoutItem('q', ItemClickable
        .builder(-1)
        .build()
      )
      .build()
    );
  }
}
