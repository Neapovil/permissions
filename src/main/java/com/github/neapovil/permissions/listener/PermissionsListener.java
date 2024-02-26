package com.github.neapovil.permissions.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;

import com.github.neapovil.permissions.Permissions;
import com.github.neapovil.permissions.object.PermissionsObject;
import com.github.neapovil.permissions.persistence.PermissionsDataType;

public final class PermissionsListener implements Listener
{
    private final Permissions plugin = Permissions.instance();

    @EventHandler
    private void playerJoin(PlayerJoinEvent event)
    {
        plugin.players.put(event.getPlayer().getUniqueId(), event.getPlayer().addAttachment(plugin));

        final PermissionsObject permissions = event.getPlayer().getPersistentDataContainer().get(plugin.permissionsKey, new PermissionsDataType());

        if (permissions == null)
        {
            event.getPlayer().getPersistentDataContainer().set(plugin.permissionsKey, new PermissionsDataType(), new PermissionsObject());
        }
    }

    @EventHandler
    private void playerQuit(PlayerQuitEvent event)
    {
        final PermissionAttachment permissionattachment = plugin.players.remove(event.getPlayer().getUniqueId());
        event.getPlayer().removeAttachment(permissionattachment);
    }
}
