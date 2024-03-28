package com.github.neapovil.permissions.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerCommandEvent;

import com.github.neapovil.permissions.Permissions;
import com.github.neapovil.permissions.event.PlayerPermissionsChangeEvent;

public final class PermissionsListener implements Listener
{
    private final Permissions plugin = Permissions.instance();

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event)
    {
        plugin.syncPermissions(event.getPlayer());
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event)
    {
        plugin.removePlayerAttachment(event.getPlayer());
    }

    @EventHandler
    private void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        if (event.getMessage().startsWith("/op "))
        {
            final PlayerPermissionsChangeEvent event1 = new PlayerPermissionsChangeEvent(event.getPlayer());
            plugin.getServer().getPluginManager().callEvent(event1);
        }

        if (event.getMessage().startsWith("/deop "))
        {
            final PlayerPermissionsChangeEvent event1 = new PlayerPermissionsChangeEvent(event.getPlayer());
            plugin.getServer().getPluginManager().callEvent(event1);
        }
    }

    @EventHandler
    private void onServerCommand(ServerCommandEvent event)
    {
        if (event.getCommand().startsWith("op "))
        {
            final Player player = Bukkit.getPlayer(event.getCommand().replace("op ", ""));
            final PlayerPermissionsChangeEvent event1 = new PlayerPermissionsChangeEvent(player);
            plugin.getServer().getPluginManager().callEvent(event1);
        }

        if (event.getCommand().startsWith("deop "))
        {
            final Player player = Bukkit.getPlayer(event.getCommand().replace("deop ", ""));
            final PlayerPermissionsChangeEvent event1 = new PlayerPermissionsChangeEvent(player);
            plugin.getServer().getPluginManager().callEvent(event1);
        }
    }
}
