package online.nasgar.microbattles.lobby.menu.match;

import me.yushust.message.MessageHandler;
import net.cosmogrp.storage.dist.RemoteModelService;
import online.nasgar.microbattles.api.error.ErrorNotifier;
import online.nasgar.microbattles.api.match.MatchManager;
import online.nasgar.microbattles.api.message.MessageMode;
import online.nasgar.microbattles.api.model.match.Match;
import online.nasgar.microbattles.commons.enums.PrettyEnums;
import online.nasgar.microbattles.commons.menu.AbstractMenu;
import online.nasgar.microbattles.lobby.MicroBattlesLobbyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import team.unnamed.gui.item.ItemBuilder;
import team.unnamed.gui.item.util.DecorateItemUtils;
import team.unnamed.gui.menu.item.ItemClickable;
import team.unnamed.gui.menu.type.MenuInventory;

import javax.inject.Inject;

public class MatchesMenu extends AbstractMenu {
  private final RemoteModelService<Match> matchService;
  private final MessageHandler messageHandler;
  private final MicroBattlesLobbyPlugin plugin;
  private final MatchManager matchManager;

  @Inject
  public MatchesMenu(RemoteModelService<Match> matchService,
                     MessageHandler messageHandler,
                     MicroBattlesLobbyPlugin plugin,
                     MatchManager matchManager) {
    super("matches");
    this.matchService = matchService;
    this.messageHandler = messageHandler;
    this.plugin = plugin;
    this.matchManager = matchManager;
  }

  public void open(Player player) {
    messageHandler.sendIn(
      player,
      MessageMode.INFO,
      "match.opening-menu"
    );

    matchService
      .findAll()
      .whenComplete((matches, error) -> {
        if (error != null) {
          ErrorNotifier.notify(error);

          messageHandler.sendReplacingIn(
            player,
            MessageMode.ERROR,
            "match.error-opening-menu"
          );

          return;
        }

        Bukkit.getScheduler().runTask(
          plugin,
          () -> player.openInventory(MenuInventory
            .newPaginatedBuilder(Match.class, getTitle(player))
            .itemsPerRow(7)
            .entityParser(match -> {
              DyeColor glassColor = DyeColor.GRAY;

              switch (match.getPhase()) {
                case PLAYING_WITH_WALLS:
                case PLAYING: {
                  glassColor = DyeColor.RED;

                  break;
                }
                case WAITING: {
                  glassColor = DyeColor.GREEN;

                  break;
                }
                case STARTING: {
                  glassColor = DyeColor.YELLOW;

                  break;
                }
              }

              return ItemClickable
                .builder(-1)
                .item(DecorateItemUtils
                  .stainedPaneBuilder(glassColor)
                  .name(getItemName(
                    player,
                    "match-format",
                    "%arena%", match.getArena(),
                    "%current-players%", matchManager.countPlayers(match),
                    "%max-players%", 0,
                    "%phase%", "%path_" + PrettyEnums.prettify(match.getPhase()) + "%"
                  ))
                  .lore(getItemLore(
                    player,
                    "match-format",
                    "%arena%", match.getArena(),
                    "%current-players%", matchManager.countPlayers(match),
                    "%max-players%", 0,
                    "%phase%", "%path_" + PrettyEnums.prettify(match.getPhase()) + "%"
                  ))
                  .build()
                )
                .action(inventory -> {
                  player.performCommand("match join " + match.getId());

                  return true;
                })
                .build();
            })
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
            .entities(matches)
            .build()
          )
        );
      });
  }
}
