package com.github.neapovil.permissions;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.neapovil.permissions.command.GroupsCommand;
import com.github.neapovil.permissions.command.PlayersCommand;
import com.github.neapovil.permissions.command.ReloadCommand;
import com.github.neapovil.permissions.event.PlayerPermissionsChangeEvent;
import com.github.neapovil.permissions.listener.PermissionsListener;
import com.github.neapovil.permissions.object.PermissionsObject;
import com.github.neapovil.permissions.persistence.PermissionsDataType;
import com.github.neapovil.permissions.resource.GroupsResource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class Permissions extends JavaPlugin
{
    private static Permissions instance;
    private GroupsResource groupsResource;
    private final Map<UUID, PermissionAttachment> attachments = new HashMap<>();
    public final NamespacedKey permissionsKey = new NamespacedKey(this, "permissions");
    public final PermissionsDataType permissionsDataType = new PermissionsDataType();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void onEnable()
    {
        instance = this;

        this.saveResource("groups.json", false);

        try
        {
            this.load();
            this.groupsResource.groups.forEach(i -> this.getServer().getPluginManager().addPermission(i.permission()));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        this.getServer().getPluginManager().registerEvents(new PermissionsListener(), this);

        new GroupsCommand().register();
        new PlayersCommand().register();
        new ReloadCommand().register();
    }

    @Override
    public void onDisable()
    {
    }

    public static Permissions instance()
    {
        return instance;
    }

    public GroupsResource groups()
    {
        return this.groupsResource;
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

        this.groupsResource.groups.forEach(group -> {
            group.findPlayer(player).ifPresentOrElse(player1 -> {
                if (!permissionsobject.groups.contains(group.id))
                {
                    permissionsobject.groups.add(group.id);
                }

                permissionattachment.setPermission(group.idAsString(), true);
            }, () -> {
                permissionsobject.groups.removeIf(i -> i == group.id);
                permissionattachment.unsetPermission(group.idAsString());
            });
        });

        permissionsobject.groups.forEach(i -> {
            if (this.groupsResource.find(i).isEmpty())
            {
                permissionattachment.unsetPermission(i.toString());
            }
        });

        player.getPersistentDataContainer().set(this.permissionsKey, this.permissionsDataType, permissionsobject);

        player.updateCommands();

        final PlayerPermissionsChangeEvent event = new PlayerPermissionsChangeEvent(player);
        this.getServer().getPluginManager().callEvent(event);
    }

    public void syncPermissions(GroupsResource.Group group)
    {
        group.players.forEach(i -> {
            i.player().ifPresent(player -> {
                if (player.isOnline())
                {
                    this.syncPermissions(player);
                }
            });
        });
    }

    public void syncPermissions()
    {
        for (Player i : this.getServer().getOnlinePlayers().toArray(Player[]::new))
        {
            if (i.isOnline())
            {
                this.syncPermissions(i);
            }
        }
    }

    public void load() throws IOException
    {
        final String string = Files.readString(this.getDataFolder().toPath().resolve("groups.json"));
        this.groupsResource = this.gson.fromJson(string, GroupsResource.class);
    }

    public void save() throws IOException
    {
        final String string = this.gson.toJson(this.groupsResource);
        Files.write(this.getDataFolder().toPath().resolve("groups.json"), string.getBytes());
    }
}
