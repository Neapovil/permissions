package com.github.neapovil.permissions.command;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.json.JsonParser;
import com.github.neapovil.permissions.Permissions;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import net.md_5.bungee.api.ChatColor;

public final class GroupsCommand
{
    private static Permissions plugin = Permissions.getInstance();

    public static void register()
    {
        new CommandAPICommand("permissions")
                .withPermission("permissions.command")
                .withArguments(new LiteralArgument("groups"))
                .withArguments(new LiteralArgument("create"))
                .withArguments(new StringArgument("groupName"))
                .executes((sender, args) -> {
                    final String groupname = (String) args[0];

                    if (plugin.getGroups().get("groups." + groupname) != null)
                    {
                        CommandAPI.fail("Group name already used!");
                    }

                    plugin.getGroups().set("groups." + groupname + ".prefix", "");
                    plugin.getGroups().set("groups." + groupname + ".permissions", new JsonParser().parse("{}"));

                    sender.sendMessage("Group %s created.".formatted(groupname));
                })
                .register();

        new CommandAPICommand("permissions")
                .withPermission("permissions.command")
                .withArguments(new LiteralArgument("groups"))
                .withArguments(new LiteralArgument("delete"))
                .withArguments(new StringArgument("groupName").replaceSuggestions(info -> {
                    if (plugin.getGroups().get("groups") == null)
                    {
                        return new String[] {};
                    }

                    return ((Config) plugin.getGroups().get("groups")).entrySet()
                            .stream()
                            .map(e -> e.getKey())
                            .toArray(String[]::new);
                }))
                .executes((sender, args) -> {
                    final String groupname = (String) args[0];

                    if (plugin.getGroups().get("groups." + groupname) == null)
                    {
                        CommandAPI.fail("Group not found!");
                    }

                    plugin.getGroups().remove("groups." + groupname);

                    sender.sendMessage("Group %s deleted.".formatted(groupname));
                })
                .register();

        modifyCommand()
                .withArguments(new LiteralArgument("name"))
                .withArguments(new StringArgument("newName"))
                .executes((sender, args) -> {
                    final String groupname = (String) args[0];
                    final String newgroupname = (String) args[1];

                    if (plugin.getGroups().get("groups." + groupname) == null)
                    {
                        CommandAPI.fail("Group not found!");
                    }

                    if (plugin.getGroups().get("groups." + newgroupname) != null)
                    {
                        CommandAPI.fail("Group name already used!");
                    }

                    final Config config = plugin.getGroups().remove("groups." + groupname);
                    plugin.getGroups().set("groups." + newgroupname, config);

                    sender.sendMessage("Group renamed to %s.".formatted(newgroupname));
                })
                .register();

        modifyCommand()
                .withArguments(new LiteralArgument("permissions"))
                .withArguments(new MultiLiteralArgument("add", "remove"))
                .withArguments(new StringArgument("permission"))
                .executes((sender, args) -> {
                    final String groupname = (String) args[0];
                    final String operation = (String) args[1];
                    final String permission = (String) args[2];

                    final String permissionreplaced = permission.replace(".", "-");

                    if (plugin.getGroups().get("groups." + groupname) == null)
                    {
                        CommandAPI.fail("Group not found!");
                    }

                    if (operation.equals("add"))
                    {
                        if (plugin.getGroups().get("groups." + groupname + ".permissions." + permissionreplaced) != null)
                        {
                            CommandAPI.fail("Permission already added!");
                        }

                        plugin.getGroups().set("groups." + groupname + ".permissions." + permissionreplaced, true);
                        sender.sendMessage("Permission %s added.".formatted(permission));
                    }
                    else
                    {
                        if (plugin.getGroups().get("groups." + groupname + ".permissions." + permissionreplaced) == null)
                        {
                            CommandAPI.fail("Permission not found!");
                        }

                        plugin.getGroups().remove("groups." + groupname + ".permissions." + permissionreplaced);
                        sender.sendMessage("Permission %s removed.".formatted(permission));
                    }
                })
                .register();

        modifyCommand()
                .withArguments(new LiteralArgument("prefix"))
                .withArguments(new GreedyStringArgument("newPrefix"))
                .executes((sender, args) -> {
                    final String groupname = (String) args[0];
                    final String newprefix = (String) args[1];

                    if (plugin.getGroups().get("groups." + groupname) == null)
                    {
                        CommandAPI.fail("Group not found!");
                    }

                    plugin.getGroups().set("groups." + groupname + ".prefix", newprefix);
                    sender.sendMessage("Prefix changed to %s.".formatted(ChatColor.translateAlternateColorCodes('&', newprefix)));
                })
                .register();
    }

    private static CommandAPICommand modifyCommand()
    {
        return new CommandAPICommand("permissions")
                .withPermission("permissions.command")
                .withArguments(new LiteralArgument("groups"))
                .withArguments(new LiteralArgument("modify"))
                .withArguments(new StringArgument("groupName").replaceSuggestions(info -> {
                    if (plugin.getGroups().get("groups") == null)
                    {
                        return new String[] {};
                    }

                    return ((Config) plugin.getGroups().get("groups")).entrySet()
                            .stream()
                            .map(e -> e.getKey())
                            .toArray(String[]::new);
                }));
    }
}
