package online.nasgar.microbattles.plugin.task;

import com.cryptomorin.xseries.XSound;
import me.yushust.message.MessageHandler;
import online.nasgar.microbattles.api.event.match.MatchStartEvent;
import online.nasgar.microbattles.api.match.MatchManager;
import online.nasgar.microbattles.api.message.MessageMode;
import online.nasgar.microbattles.api.model.match.Match;
import online.nasgar.microbattles.api.sound.SoundPlayer;
import online.nasgar.microbattles.api.model.engine.MicroBattlesEngine;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class MatchCountdownTask implements Runnable {
  private final Match match;
  private final MessageHandler messageHandler;
  private final MatchManager matchManager;
  private final MicroBattlesEngine engine;

  public MatchCountdownTask(Match match,
                            MessageHandler messageHandler,
                            MatchManager matchManager,
                            MicroBattlesEngine engine) {
    this.match = match;
    this.messageHandler = messageHandler;
    this.matchManager = matchManager;
    this.engine = engine;
  }

  @Override
  public void run() {
    if (match.getPhase() != Match.Phase.STARTING) {
      return;
    }

    long elapsedTime = System.currentTimeMillis() - match.getCountdownStartDate();

    if (elapsedTime >= engine.getStartCountdown()) {
      for (Player player : matchManager.getPlayers(match)) {
        SoundPlayer.playFast(player, XSound.UI_BUTTON_CLICK);
      }

      Bukkit.getPluginManager().callEvent(new MatchStartEvent(match));
      Bukkit.getScheduler().cancelTask(match.getCountdownId());
    } else {
      for (Player player : matchManager.getPlayers(match)) {
        messageHandler.sendReplacingIn(
          player,
          MessageMode.INFO,
          "match.countdown",
          "%countdown%", TimeUnit.MILLISECONDS.toSeconds(elapsedTime)
        );
      }
    }
  }
}
