package online.nasgar.microbattles.api.message.server;

import java.beans.ConstructorProperties;

public class ServerStateMessage {
  private final String server;
  private final State state;

  @ConstructorProperties({
    "server",
    "state"
  })
  public ServerStateMessage(String server,
                            State state) {
    this.server = server;
    this.state = state;
  }

  public String getServer() {
    return server;
  }

  public State getState() {
    return state;
  }

  public enum State {
    INVALID_ARENA,
    READY,
    FREE,
    NO_MATCH,
    NOT_AUTHENTICATED;
  }

}
