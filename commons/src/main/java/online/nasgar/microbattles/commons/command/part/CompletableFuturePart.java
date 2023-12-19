package online.nasgar.microbattles.commons.command.part;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.annotated.part.PartFactory;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
import net.cosmogrp.storage.ModelService;
import net.cosmogrp.storage.dist.CachedRemoteModelService;
import net.cosmogrp.storage.dist.RemoteModelService;
import net.cosmogrp.storage.model.Model;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CompletableFuturePart<T extends Model> implements PartFactory {
  private final CachedRemoteModelService<T> modelService;
  private final ModelService<T> cacheModelService;

  public CompletableFuturePart(CachedRemoteModelService<T> modelService,
                               ModelService<T> cacheModelService) {
    this.modelService = modelService;
    this.cacheModelService = cacheModelService;
  }

  @Override
  public CommandPart createPart(String name,
                                List<? extends Annotation> list) {
    return new ArgumentPart() {

      @Override
      public List<CompletableFuture<T>> parseValue(CommandContext commandContext,
                                                   ArgumentStack argumentStack,
                                                   CommandPart commandPart) throws ArgumentParseException {
        String next = argumentStack.hasNext() ? argumentStack.next() : "";
        CompletableFuture<T> value;

        if (modelService != null) {
          value = modelService.getOrFind(next);
        } else {
          if (cacheModelService instanceof RemoteModelService) {
            value = ((RemoteModelService<T>) cacheModelService).find(next);
          } else {
            value = CompletableFuture.completedFuture(cacheModelService.findSync(next));
          }
        }

        return Collections.singletonList(value);
      }

      @Override
      public String getName() {
        return name;
      }

      @Override
      public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        List<String> ids = new ArrayList<>();

        for (T model : cacheModelService.findAllSync()) {
          ids.add(model.getId());
        }

        return ids;
      }
    };
  }
}
