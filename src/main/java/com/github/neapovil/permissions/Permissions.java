package com.github.neapovil.permissions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.neapovil.permissions.command.GroupsAddCommand;
import com.github.neapovil.permissions.command.GroupsRemoveCommand;
import com.github.neapovil.permissions.config.GroupsConfig;
import com.github.neapovil.permissions.listener.PermissionsListener;
import com.github.neapovil.permissions.object.PermissionsObject;
import com.github.neapovil.permissions.persistence.PermissionsDataType;

public final class Permissions extends JavaPlugin
{
    private static Permissions instance;
    private GroupsConfig groups;
    private final Map<UUID, PermissionAttachment> attachments = new HashMap<>();
    public final NamespacedKey permissionsKey = new NamespacedKey(this, "permissions");
    public final PermissionsDataType permissionsDataType = new PermissionsDataType();

    @Override
    public void onEnable()
    {
        instance = this;

        this.getServer().getPluginManager().registerEvents(new PermissionsListener(), this);

        this.groups = new GroupsConfig();

        try
        {
            this.groups.init();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        new GroupsAddCommand().register();
        new GroupsRemoveCommand().register();
    }

    @Override
    public void onDisable()
    {
    }

    public static Permissions instance()
    {
        return instance;
    }

    public GroupsConfig groups()
    {
        return this.groups;
    }

    public PermissionAttachment playerAttachment(Player player)
    {
        return this.attachments.computeIfAbsent(player.getUniqueId(), k -> player.addAttachment(this));
    }

    public void removePlayerAttachment(Player player)
    {
        final PermissionAttachment permissionattachment = this.attachments.remove(player.getUniqueId());

        if (permissionattachment != null)
        {
            player.removeAttachment(permissionattachment);
        }
    }

    public void syncPermissions(Player player)
    {
        final PermissionAttachment permissionattachment = this.playerAttachment(player);
        final PermissionsObject permissionsobject = player.getPersistentDataContainer().getOrDefault(this.permissionsKey,
                this.permissionsDataType, new PermissionsObject());

        this.groups.data.groups.forEach(group -> {
            group.findPlayer(player).ifPresentOrElse(player1 -> {
                if (!permissionsobject.groups.contains(group.id))
                {
                    permissionsobject.groups.add(group.id);
                }

                group.permissions.forEach(i -> {
                    permissionattachment.setPermission(i, true);
                });
            }, () -> {
                permissionsobject.groups.removeIf(i -> i == group.id);
                group.permissions.forEach(i -> {
                    permissionattachment.unsetPermission(i);
                });
            });
        });

        player.getPersistentDataContainer().set(this.permissionsKey, this.permissionsDataType, permissionsobject);

        player.updateCommands();
    }
}
