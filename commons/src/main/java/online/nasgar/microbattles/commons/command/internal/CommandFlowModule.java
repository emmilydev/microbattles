package online.nasgar.microbattles.commons.command.internal;

import com.google.common.reflect.TypeToken;
import me.fixeddev.commandflow.annotated.part.AbstractModule;
import me.fixeddev.commandflow.annotated.part.defaults.factory.EnumPartFactory;
import net.cosmogrp.storage.ModelService;
import net.cosmogrp.storage.dist.CachedRemoteModelService;
import net.cosmogrp.storage.dist.RemoteModelService;
import online.nasgar.microbattles.api.model.arena.Arena;
import online.nasgar.microbattles.api.model.kit.Kit;
import online.nasgar.microbattles.api.model.match.Match;
import online.nasgar.microbattles.api.model.user.User;
import online.nasgar.microbattles.commons.command.types.BoundType;
import online.nasgar.microbattles.commons.command.types.CapacityType;
import online.nasgar.microbattles.commons.command.part.CompletableFuturePart;
import online.nasgar.microbattles.commons.command.types.SpawnpointType;
import org.bukkit.DyeColor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@Singleton
public class CommandFlowModule extends AbstractModule {
  private final CachedRemoteModelService<Arena> arenaService;
  private final ModelService<Arena> arenaCacheService;
  private final CachedRemoteModelService<Kit> kitService;
  private final ModelService<Kit> kitCacheService;
  private final RemoteModelService<Match> matchService;
  private final CachedRemoteModelService<User> userService;
  private final ModelService<User> userCacheService;

  @Inject
  public CommandFlowModule(CachedRemoteModelService<Arena> arenaService,
                           ModelService<Arena> arenaCacheService,
                           CachedRemoteModelService<Kit> kitService,
                           ModelService<Kit> kitCacheService,
                           RemoteModelService<Match> matchService,
                           CachedRemoteModelService<User> userService,
                           ModelService<User> userCacheService) {
    this.arenaService = arenaService;
    this.arenaCacheService = arenaCacheService;
    this.kitService = kitService;
    this.kitCacheService = kitCacheService;
    this.matchService = matchService;
    this.userService = userService;
    this.userCacheService = userCacheService;
  }

  @Override
  public void configure() {
    bindFactory(
      new TypeToken<CompletableFuture<Arena>>() {
      }.getType(),
      new CompletableFuturePart<>(arenaService, arenaCacheService)
    );
    bindFactory(
      new TypeToken<CompletableFuture<Kit>>() {
      }.getType(),
      new CompletableFuturePart<>(kitService, kitCacheService)
    );
    bindFactory(
      new TypeToken<CompletableFuture<Match>>() {
      }.getType(),
      new CompletableFuturePart<>(null, matchService)
    );
    bindFactory(
      new TypeToken<CompletableFuture<User>>() {

      }.getType(),
      new CompletableFuturePart<>(userService, userCacheService)
    );
    bindEnums(Arrays.asList(
      Arena.MatchType.class,
      BoundType.class,
      CapacityType.class,
      SpawnpointType.class,
      DyeColor.class
    ));
  }


  private void bindEnums(Collection<Class<? extends Enum<?>>> enumTypes) {
    for (Class<? extends Enum<?>> enumType : enumTypes) {
      bindFactory(enumType, new EnumPartFactory(enumType));
    }
  }

}
