package com.github.neapovil.permissions.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;

import com.github.neapovil.permissions.Permissions;
import com.github.neapovil.permissions.object.PermissionsObject;

public final class PermissionsListener implements Listener
{
    private final Permissions plugin = Permissions.instance();

    @EventHandler
    private void playerJoin(PlayerJoinEvent event)
    {
        final PermissionAttachment permissionattachment = plugin.playerAttachment(event.getPlayer());
        final PermissionsObject permissionsobject = event.getPlayer().getPersistentDataContainer().getOrDefault(plugin.permissionsKey,
                plugin.permissionsDataType, new PermissionsObject());

        plugin.groups().data.groups.forEach(group -> {
            group.findPlayer(event.getPlayer()).ifPresentOrElse(player -> {
                if (!permissionsobject.groups.contains(group.id))
                {
                    permissionsobject.groups.add(group.id);
                }
            }, () -> permissionsobject.groups.removeIf(i -> i == group.id));
        });

        event.getPlayer().getPersistentDataContainer().set(plugin.permissionsKey, plugin.permissionsDataType, permissionsobject);

        permissionsobject.groups().forEach(group -> {
            group.permissions.forEach(i -> {
                permissionattachment.setPermission(i, true);
            });
        });

        event.getPlayer().updateCommands();
    }

    @EventHandler
    private void playerQuit(PlayerQuitEvent event)
    {
        plugin.removePlayerAttachment(event.getPlayer());
    }
}
