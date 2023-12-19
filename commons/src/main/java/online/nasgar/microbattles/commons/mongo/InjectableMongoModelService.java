package online.nasgar.microbattles.commons.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import me.yushust.inject.key.TypeReference;
import net.cosmogrp.storage.ModelService;
import net.cosmogrp.storage.dist.RemoteModelService;
import net.cosmogrp.storage.mongo.MongoModelService;
import net.cosmogrp.storage.resolve.ResolverRegistry;
import online.nasgar.microbattles.api.model.Model;
import online.nasgar.microbattles.api.model.engine.MicroBattlesEngine;
import org.bson.UuidRepresentation;
import org.mongojack.JacksonMongoCollection;

import javax.inject.Inject;
import java.util.concurrent.Executor;

public class InjectableMongoModelService<T extends Model> extends MongoModelService<T> {
  @Inject
  public InjectableMongoModelService(Executor executor,
                                     ModelService<T> cacheModelService,
                                     TypeReference<T> type,
                                     MongoClient mongoClient,
                                     ObjectMapper objectMapper,
                                     MicroBattlesEngine engine) {
    super(
      executor,
      cacheModelService,
      ResolverRegistry.empty(),
      JacksonMongoCollection
        .builder()
        .withObjectMapper(objectMapper)
        .build(
          mongoClient,
          (String) engine.getMongoCredentials().getOrDefault("database", "MicroBattles"),
          type.getRawType().getSimpleName(),
          type.getRawType(),
          UuidRepresentation.STANDARD
        )
    );
  }

}
