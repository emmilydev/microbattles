package online.nasgar.microbattles.api.error;

import org.bukkit.Bukkit;

public class ErrorNotifier {

  public static void notify(Throwable error,
                            String message) {
    Bukkit.getLogger().warning(String.format(
      "[MicroBattles] %s: %s",
      message,
      error
    ));
  }

  public static void notify(Throwable error) {
    notify(error, "An unexpected error has occurred while performing an async operation.");
  }

}
