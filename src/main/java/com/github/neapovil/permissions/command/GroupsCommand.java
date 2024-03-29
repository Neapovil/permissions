package com.github.neapovil.permissions.command;

import java.io.IOException;

import com.github.neapovil.permissions.resource.GroupsResource;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;

public final class GroupsCommand extends AbstractCommand
{
    @Override
    public void register()
    {
        new CommandAPICommand("permissions")
                .withPermission("permissions.command")
                .withArguments(new LiteralArgument("groups"))
                .withArguments(new LiteralArgument("create"))
                .withArguments(new StringArgument("name"))
                .executes((sender, args) -> {
                    final String name = (String) args.get("group");

                    if (plugin.groups().find(name).isPresent())
                    {
                        throw CommandAPI.failWithString("A group with this name already exists");
                    }

                    final GroupsResource.Group group = new GroupsResource.Group();
                    group.id = plugin.groups().groups.size() + 1;
                    group.name = name;

                    try
                    {
                        plugin.groups().groups.add(group);
                        plugin.save();
                        plugin.getServer().getPluginManager().removePermission(name);
                        plugin.getServer().getPluginManager().addPermission(group.permission());
                        sender.sendMessage("Group %s created".formatted(name));
                    }
                    catch (IOException e)
                    {
                        sender.sendRichMessage("Unable to create group");
                        plugin.getLogger().severe(e.getMessage());
                    }
                })
                .register();

        new CommandAPICommand("permissions")
                .withPermission("permissions.command")
                .withArguments(new LiteralArgument("groups"))
                .withArguments(new LiteralArgument("remove"))
                .withArguments(new StringArgument("name").replaceSuggestions(ArgumentSuggestions.strings(plugin.groups().commandSuggestions())))
                .executes((sender, args) -> {
                    final String name = (String) args.get("name");

                    plugin.groups().find(name).ifPresentOrElse(group -> {
                        try
                        {
                            plugin.groups().groups.removeIf(i -> i.name.equalsIgnoreCase(name));
                            plugin.save();
                            plugin.getServer().getPluginManager().removePermission(name);
                            plugin.syncPermissions(group);
                            sender.sendMessage("Group %s removed".formatted(name));
                        }
                        catch (IOException e)
                        {
                            sender.sendRichMessage("Unable to create group");
                            plugin.getLogger().severe(e.getMessage());
                        }
                    }, () -> sender.sendRichMessage("<red>Group not found"));
                })
                .register();

        new CommandAPICommand("permissions")
                .withPermission("permissions.command")
                .withArguments(new LiteralArgument("groups"))
                .withArguments(new LiteralArgument("edit"))
                .withArguments(new StringArgument("groupName").replaceSuggestions(ArgumentSuggestions.strings(plugin.groups().commandSuggestions())))
                .withArguments(new LiteralArgument("permissions"))
                .withArguments(new LiteralArgument("add"))
                .withArguments(new StringArgument("perm"))
                .executes((sender, args) -> {
                    final String groupname = (String) args.get("groupName");
                    final String perm = (String) args.get("perm");

                    plugin.groups().find(groupname).ifPresentOrElse(group -> {
                        if (group.permissions.contains(perm))
                        {
                            sender.sendRichMessage("<red>Group already has this permission");
                        }
                        else
                        {
                            try
                            {
                                group.permissions.add(perm);
                                plugin.save();
                                plugin.syncPermissions(group);
                                sender.sendMessage("Permission added");
                            }
                            catch (IOException e)
                            {
                                sender.sendRichMessage("<red>Unable to add permission");
                                plugin.getLogger().severe(e.getMessage());
                            }
                        }
                    }, () -> sender.sendRichMessage("<red>Group not found"));
                })
                .register();

        new CommandAPICommand("permissions")
                .withPermission("permissions.command")
                .withArguments(new LiteralArgument("groups"))
                .withArguments(new LiteralArgument("edit"))
                .withArguments(new StringArgument("groupName").replaceSuggestions(ArgumentSuggestions.strings(plugin.groups().commandSuggestions())))
                .withArguments(new LiteralArgument("permissions"))
                .withArguments(new LiteralArgument("remove"))
                .withArguments(new StringArgument("perm"))
                .executes((sender, args) -> {
                    final String groupname = (String) args.get("groupName");
                    final String perm = (String) args.get("perm");

                    plugin.groups().find(groupname).ifPresentOrElse(group -> {
                        if (group.permissions.contains(perm))
                        {
                            try
                            {
                                group.permissions.removeIf(i -> i.equalsIgnoreCase(perm));
                                plugin.save();
                                plugin.syncPermissions(group);
                                sender.sendMessage("Permission removed");
                            }
                            catch (IOException e)
                            {
                                sender.sendRichMessage("<red>Unable to remove permission");
                                plugin.getLogger().severe(e.getMessage());
                            }
                        }
                        else
                        {
                            sender.sendRichMessage("<red>Group doesn't have this permission");
                        }
                    }, () -> sender.sendRichMessage("<red>Group not found"));
                })
                .register();
    }
}
