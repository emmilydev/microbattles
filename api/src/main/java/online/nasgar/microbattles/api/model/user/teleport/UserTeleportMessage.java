package online.nasgar.microbattles.api.model.user.teleport;

import java.beans.ConstructorProperties;

public class UserTeleportMessage {
  private final String id;
  private final String server;

  @ConstructorProperties({
    "id",
    "server"
  })
  public UserTeleportMessage(String id,
                             String server) {
    this.id = id;
    this.server = server;
  }

  public String getId() {
    return id;
  }

  public String getServer() {
    return server;
  }
}
