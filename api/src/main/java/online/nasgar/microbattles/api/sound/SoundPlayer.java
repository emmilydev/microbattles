package online.nasgar.microbattles.api.sound;

import com.cryptomorin.xseries.XSound;
import org.bukkit.entity.Player;

public interface SoundPlayer {
  static void play(Player player,
                   XSound sound,
                   float speed) {
    player.playSound(player.getLocation(), sound.parseSound(), 1, speed);
  }

  static void playNormal(Player player,
                         XSound sound) {
    play(player, sound, 1);
  }

  static void playFast(Player player,
                       XSound sound) {
    play(player, sound, 2);
  }

  static void playSlow(Player player,
                       XSound sound) {
    play(player, sound, 0.5f);
  }

}
