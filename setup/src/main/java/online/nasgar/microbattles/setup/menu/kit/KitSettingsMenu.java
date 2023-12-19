package online.nasgar.microbattles.setup.menu.kit;

import com.cryptomorin.xseries.XSound;
import online.nasgar.microbattles.api.message.MessageMode;
import online.nasgar.microbattles.api.model.kit.Kit;
import online.nasgar.microbattles.api.sound.SoundPlayer;
import online.nasgar.microbattles.commons.enums.PrettyEnums;
import online.nasgar.microbattles.commons.menu.AbstractMenu;
import online.nasgar.microbattles.commons.metadata.MetadataExtractor;
import online.nasgar.microbattles.setup.MicroBattlesSetupPlugin;
import online.nasgar.microbattles.setup.menu.kit.prompt.KitSettingPromptFactory;
import online.nasgar.microbattles.setup.menu.material.MaterialsMenu;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import team.unnamed.gui.item.ItemBuilder;
import team.unnamed.gui.item.util.DecorateItemUtils;
import team.unnamed.gui.menu.item.ItemClickable;
import team.unnamed.gui.menu.type.MenuInventory;
import team.unnamed.gui.menu.type.MenuInventoryBuilder;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class KitSettingsMenu extends AbstractMenu {
  private static final String METADATA_KEY = "microbattles:kit-settings-session";

  private final MicroBattlesSetupPlugin plugin;
  private final KitSettingPromptFactory kitSettingPromptFactory;
  private final MaterialsMenu materialsMenu;
  private final KitEditorMenu kitEditorMenu;

  @Inject
  public KitSettingsMenu(MicroBattlesSetupPlugin plugin,
                         KitSettingPromptFactory kitSettingPromptFactory,
                         MaterialsMenu materialsMenu,
                         KitEditorMenu kitEditorMenu) {
    super("kit-settings");
    this.plugin = plugin;
    this.kitSettingPromptFactory = kitSettingPromptFactory;
    this.materialsMenu = materialsMenu;
    this.kitEditorMenu = kitEditorMenu;
  }

  @SuppressWarnings("unchecked")
  public void open(Player player,
                   Kit kit) {
    Map<String, String> session;

    if (player.hasMetadata(METADATA_KEY)) {
      session = MetadataExtractor.extract(
        player,
        METADATA_KEY,
        value -> (Map<String, String>) value
      );
    } else {
      session = new HashMap<>();

      player.setMetadata(
        METADATA_KEY,
        new FixedMetadataValue(
          plugin,
          session
        )
      );
    }

    MenuInventoryBuilder builder = MenuInventory
      .newStringLayoutBuilder(getTitle(player))
      .layoutLines(
        "----K----",
        "ddddddddd",
        "---TIE---",
        "--P-D-R--",
        "ddddddddd",
        "c---r---s"
      )
      .layoutItem('K', ItemClickable
        .builder(-1)
        .item(ItemBuilder
          .builder(kit.getIcon())
          .name(messageHandler.get(player, kit.getName()))
          .lore(messageHandler.replacingMany(
            player,
            kit.getDescription(),
            "%id%", kit.getId(),
            "%name%", "%path_" + kit.getName() + "%",
            "%icon%", PrettyEnums.prettify(kit.getIcon())
          ))
          .build()
        )
        .action(inventory -> {
          SoundPlayer.playNormal(player, XSound.UI_BUTTON_CLICK);

          return true;
        })
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
      .layoutItem('T', ItemClickable
        .builder(-1)
        .item(ItemBuilder
          .builder(Material.NAME_TAG)
          .name(getItemName(
            player,
            "change-translation-key",
            "%kit%", "%path_" + kit.getName() + "%"
          ))
          .lore(getItemLore(
            player,
            "change-translation-key",
            "%kit%", "%path_" + kit.getName() + "%"
          ))
          .build()
        )
        .action(inventory -> {
          SoundPlayer.playNormal(player, XSound.UI_BUTTON_CLICK);

          kitSettingPromptFactory
            .create(
              player,
              kit,
              "translation-key",
              session,
              context -> messageHandler.replacing(
                player,
                "kit.edit-session.translation-key.prompt",
                "%kit%", "%path_" + kit.getName() + "%"
              ),
              (context, input) -> messageHandler.sendReplacingIn(
                context,
                MessageMode.SUCCESS,
                "kit.edit-session.translation-key.set",
                "%kit%",
                "%translation-key%", input
              ),
              (context, event) -> {
                if (!event.gracefulExit()) {
                  messageHandler.sendReplacingIn(
                    player,
                    MessageMode.SUCCESS,
                    "kit.edit-session.translation-key.cancelled",
                    "%kit%", "%path_" + kit.getName() + "%"
                  );

                  session.remove("translation-key");
                }
              }
            )
            .begin();

          return true;
        })
        .build()
      )
      .layoutItem('I', ItemClickable
        .builder(-1)
        .item(ItemBuilder
          .builder(kit.getIcon())
          .name(getItemName(
            player,
            "change-icon",
            "%icon%", PrettyEnums.prettify(kit.getIcon())
          ))
          .lore(getItemLore(
            player,
            "change-icon",
            "%icon%", PrettyEnums.prettify(kit.getIcon())
          ))
          .build()
        )
        .action(inventory -> {
          SoundPlayer.playNormal(player, XSound.UI_BUTTON_CLICK);

          materialsMenu.open(
            player,
            material -> session.put("icon", material.name()),
            $inventory -> {
              open(player, kit);

              return false;
            }
          );

          return true;
        })
        .build()
      )
      .layoutItem('E', ItemClickable
        .builder(-1)
        .item(ItemBuilder
          .builder(Material.BOOK_AND_QUILL)
          .name(getItemName(player, "edit"))
          .lore(getItemLore(player, "edit"))
          .build()
        )
        .action(inventory -> {
          kitEditorMenu.open(
            player,
            kit.getId(),
            null,
            unused -> {
              open(player, kit);

              return false;
            }
          );

          return true;
        })
        .build()
      );
  }
}