package online.nasgar.microbattles.lobby.bind.bukkit;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.part.Module;
import me.fixeddev.commandflow.annotated.part.defaults.DefaultsModule;
import me.fixeddev.commandflow.bukkit.factory.BukkitModule;
import me.yushust.inject.AbstractModule;
import online.nasgar.microbattles.commons.command.internal.CommandFlowModule;
import online.nasgar.microbattles.lobby.command.LobbyCommand;
import online.nasgar.microbattles.lobby.command.match.MatchCommand;
import online.nasgar.microbattles.lobby.command.user.ProfileCommand;

public class CommandModule extends AbstractModule {
  @Override
  protected void configure() {
    multibind(Module.class)
      .asSet()
      .toInstance(new DefaultsModule())
      .toInstance(new BukkitModule())
      .to(CommandFlowModule.class);
    multibind(CommandClass.class)
      .asSet()
      .to(LobbyCommand.class)
      .to(MatchCommand.class)
      .to(ProfileCommand.class);
  }
}
