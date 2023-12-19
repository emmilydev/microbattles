package online.nasgar.microbattles.api.model.user.kdr;

import online.nasgar.microbattles.api.model.user.User;

public interface KdrCalculator {
  static float getKdr(User user) {
    int deaths = user.getDeaths();

    if (deaths == 0) {
      return Float.NaN;
    }

    return (float) user.getKills() / deaths;
  }
}
