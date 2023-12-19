package online.nasgar.microbattles.api.event.user;

import online.nasgar.microbattles.api.model.user.User;
import org.bukkit.Achievement;
import org.bukkit.event.HandlerList;

public class UserUpdateAchievementEvent extends UserEvent {
  private static final HandlerList HANDLER_LIST = new HandlerList();

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

  private final Achievement achievement;
  private final int level;

  public UserUpdateAchievementEvent(User user,
                                    Achievement achievement,
                                    int level) {
    super(user);
    this.achievement = achievement;
    this.level = level;
  }

  public Achievement getAchievement() {
    return achievement;
  }

  public int getLevel() {
    return level;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }
}
