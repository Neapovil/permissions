package com.github.neapovil.permissions.command;

import org.bukkit.OfflinePlayer;

import com.electronwill.nightconfig.core.Config;
import com.github.neapovil.permissions.Permissions;
import com.github.neapovil.permissions.config.Players;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.OfflinePlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;

public final class UsersCommand
{
    private static Permissions plugin = Permissions.getInstance();

    public static void register()
    {
        new CommandAPICommand("permissions")
                .withPermission("permissions.command")
                .withArguments(new LiteralArgument("users"))
                .withArguments(new LiteralArgument("set"))
                .withArguments(new OfflinePlayerArgument("player"))
                .withArguments(new StringArgument("groupName").replaceSuggestions(info -> {
                    return ((Config) plugin.getGroups().get("groups")).entrySet()
                            .stream()
                            .map(e -> e.getKey())
                            .toArray(String[]::new);
                }))
                .executes((sender, args) -> {
                    final OfflinePlayer player = (OfflinePlayer) args[0];
                    final String groupname = (String) args[1];

                    if (plugin.getGroups().get("groups." + groupname) == null)
                    {
                        CommandAPI.fail("Group not found!");
                    }

                    if (Players.get(player).getGroup().equals(groupname))
                    {
                        CommandAPI.fail("Group already assigned to this player!");
                    }

                    Players.get(player).setGroup(groupname);
                    sender.sendMessage("Changed %s's group to %s.".formatted(player.getName(), groupname));
                })
                .register();

        new CommandAPICommand("permissions")
                .withPermission("permissions.command")
                .withArguments(new LiteralArgument("users"))
                .withArguments(new LiteralArgument("unset"))
                .withArguments(new OfflinePlayerArgument("player"))
                .executes((sender, args) -> {
                    final OfflinePlayer player = (OfflinePlayer) args[0];

                    if (Players.get(player).getGroup().isEmpty())
                    {
                        CommandAPI.fail("The player doesn't have a group!");
                    }

                    plugin.getPlayers().set("players." + player.getUniqueId() + ".group", "");
                    sender.sendMessage("User group unset.");
                })
                .register();
    }
}
