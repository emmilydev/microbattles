package online.nasgar.microbattles.api.model.match;

import online.nasgar.microbattles.api.model.Model;

import java.beans.ConstructorProperties;

public class RejoinQueueEntry implements Model {
  private final String id;
  private final String match;

  @ConstructorProperties({
    "id",
    "match"
  })
  public RejoinQueueEntry(String id,
                          String match) {
    this.id = id;
    this.match = match;
  }

  @Override
  public String getId() {
    return id;
  }

  public String getMatch() {
    return match;
  }
}
