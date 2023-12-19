package online.nasgar.microbattles.lobby.channel;

import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import net.cosmogrp.storage.redis.channel.Channel;
import net.cosmogrp.storage.redis.channel.ChannelListener;
import online.nasgar.microbattles.api.model.user.teleport.UserTeleportMessage;
import online.nasgar.microbattles.lobby.MicroBattlesLobbyPlugin;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.util.UUID;

public class UserTeleportChannelListener implements ChannelListener<UserTeleportMessage> {
  private final IPlayerManager playerManager;

  @Inject
  public UserTeleportChannelListener(IPlayerManager playerManager) {
    this.playerManager = playerManager;
  }

  @Override
  public void listen(Channel<UserTeleportMessage> channel,
                     String server,
                     UserTeleportMessage object) {
    try {
      playerManager
        .getPlayerExecutor(UUID.fromString(object.getId()))
        .connect(object.getServer());
    } catch (Exception e) {
      Bukkit.getScheduler().runTask(MicroBattlesLobbyPlugin.getPlugin(MicroBattlesLobbyPlugin.class), e::printStackTrace);
    }
  }
}
