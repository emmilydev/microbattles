package online.nasgar.microbattles.commons.loader;

import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilder;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.builder.AnnotatedCommandBuilder;
import me.fixeddev.commandflow.annotated.part.Module;
import me.fixeddev.commandflow.annotated.part.PartInjector;
import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.yushust.inject.Injector;
import online.nasgar.microbattles.api.loader.Loader;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.util.Set;

public class CommandLoader implements Loader {
  private final Set<CommandClass> classes;
  private final Set<Module> modules;
  private final JavaPlugin javaPlugin;
  private final Injector injector;

  @Inject
  public CommandLoader(Set<CommandClass> classes,
                       Set<Module> modules,
                       JavaPlugin javaPlugin,
                       Injector injector) {
    this.classes = classes;
    this.modules = modules;
    this.javaPlugin = javaPlugin;
    this.injector = injector;
  }

  @Override
  public void load() {
    PartInjector partInjector = PartInjector.create();

    for (Module module : modules) {
      partInjector.install(module);
    }

    CommandManager commandManager = new BukkitCommandManager(javaPlugin.getName());

    AnnotatedCommandTreeBuilder treeBuilder = AnnotatedCommandTreeBuilder.create(
      AnnotatedCommandBuilder.create(partInjector),
      (clazz, parent) -> injector.getInstance(clazz)
    );

    for (CommandClass commandClass : classes) {
      commandManager.registerCommands(treeBuilder.fromClass(commandClass));
    }
  }
}
