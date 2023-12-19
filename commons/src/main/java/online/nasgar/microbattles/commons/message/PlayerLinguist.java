package online.nasgar.microbattles.commons.message;

import me.yushust.message.language.Linguist;
import online.nasgar.microbattles.api.version.ServerVersionProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressWarnings("all")
public class PlayerLinguist implements Linguist<Player> {
  public static Method NEW_GET_LOCALE;

  static {
    try {
      if (ServerVersionProvider.SERVER_VERSION_INT >= 15) {
        NEW_GET_LOCALE = Player.class.getDeclaredMethod("getLocale");
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }


  @Override
  public String getLanguage(Player player) {
    if (ServerVersionProvider.SERVER_VERSION_INT >= 15) {
      try {
        return ((String) NEW_GET_LOCALE.invoke(player)).split("_")[0];
      } catch (IllegalAccessException | InvocationTargetException e) {
        Bukkit.getLogger().info("Unable to invoke NEW_GET_LOCALE.");
      }
    }

    return player.spigot().getLocale();
  }

}
