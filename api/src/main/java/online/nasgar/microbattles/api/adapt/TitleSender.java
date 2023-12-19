package online.nasgar.microbattles.api.adapt;

import org.bukkit.entity.Player;

/**
 * Version-nonspecific interface for
 * sending titles to players.
 */
public interface TitleSender {

  /**
   * Sends the given {@code title} to
   * the given {@code player} with the
   * specified parameters.
   *
   * @param player  The player who will
   *                receive the title.
   * @param title   The title to be sent.
   * @param fadeIn  The time (in seconds)
   *                that the title will
   *                fade while appearing.
   * @param stay    The time (in seconds)
   *                that the title will be
   *                displayed.
   * @param fadeOut The time (in seconds)
   *                that the title will
   *                fade while disappearing.
   */
  void sendTitle(Player player,
                 String title,
                 int fadeIn,
                 int stay,
                 int fadeOut);

  /**
   * Sends the given {@code subtitle} to
   * the given {@code player} with the
   * specified parameters.
   *
   * @param player   The player who will
   *                 receive the title.
   * @param subtitle The subtitle to be sent.
   * @param fadeIn   The time (in seconds)
   *                 that the subtitle will
   *                 fade while appearing.
   * @param stay     The time (in seconds)
   *                 that the subtitle will be
   *                 displayed.
   * @param fadeOut  The time (in seconds)
   *                 that the subtitle will
   *                 fade while disappearing.
   */
  void sendSubtitle(Player player,
                    String subtitle,
                    int fadeIn,
                    int stay,
                    int fadeOut);

  /**
   * Sends the given {@code title}
   * and {@code subtitle} to the
   * given {@code player} with the
   * specified parameters.
   *
   * @see #sendTitle(Player, String, int, int, int)
   * @see #sendSubtitle(Player, String, int, int, int)
   */
  void sendBoth(Player player,
                String title,
                String subtitle,
                int fadeIn,
                int stay,
                int fadeOut);

  /**
   * Sends an action bar to the given
   * {@code player} with the given
   * {@code text}.
   *
   * @param player   The player who will
   *                 receive the action bar.
   * @param value    The text for the action bar.
   */
  void sendActionbar(Player player,
                     String value);
}
