package com.github.neapovil.permissions.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.neapovil.permissions.Permissions;

public final class PermissionsListener implements Listener
{
    private final Permissions plugin = Permissions.instance();

    @EventHandler
    private void playerJoin(PlayerJoinEvent event)
    {
        plugin.syncPermissions(event.getPlayer());
    }

    @EventHandler
    private void playerQuit(PlayerQuitEvent event)
    {
        plugin.removePlayerAttachment(event.getPlayer());
    }
}
