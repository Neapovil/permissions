package com.github.neapovil.permissions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.neapovil.permissions.config.GroupsConfig;
import com.github.neapovil.permissions.listener.PermissionsListener;
import com.github.neapovil.permissions.object.PermissionsObject;
import com.github.neapovil.permissions.persistence.PermissionsDataType;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;

public final class Permissions extends JavaPlugin
{
    private static Permissions instance;
    private GroupsConfig groups;
    public Map<UUID, PermissionAttachment> players = new HashMap<>();
    public NamespacedKey permissionsKey = new NamespacedKey(this, "permissions");

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

        // /permissions player <name> groups add <group>
        new CommandAPICommand("permissions")
                .withPermission("permissions.command")
                .withArguments(new LiteralArgument("player"))
                .withArguments(new EntitySelectorArgument.OnePlayer("player"))
                .withArguments(new LiteralArgument("groups"))
                .withArguments(new LiteralArgument("add"))
                .withArguments(new StringArgument("group").replaceSuggestions(ArgumentSuggestions.strings(info -> {
                    final Player player = (Player) info.previousArgs().get("player");
                    final PermissionsObject permissions = player.getPersistentDataContainer().get(this.permissionsKey, new PermissionsDataType());
                    final List<String> groups = permissions.groups.stream().map(i -> i.name).toList();
                    return this.groups.data.groups.stream().map(i -> i.name).filter(i -> !groups.contains(i)).toArray(String[]::new);
                })))
                .executes((sender, args) -> {
                    final Player player = (Player) args.get("player");
                    final String group = (String) args.get("group");
                })
                .register();
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
}
