package online.nasgar.microbattles.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface Model extends net.cosmogrp.storage.model.Model {
  @Override
  @JsonProperty("_id")
  String getId();
}
