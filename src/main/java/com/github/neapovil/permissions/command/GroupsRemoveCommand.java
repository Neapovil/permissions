package com.github.neapovil.permissions.command;

import org.bukkit.entity.Player;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;

public final class GroupsRemoveCommand extends AbstractCommand
{
    @Override
    public void register()
    {
        new CommandAPICommand("permissions")
                .withPermission("permissions.command")
                .withArguments(new EntitySelectorArgument.OnePlayer("player"))
                .withArguments(new LiteralArgument("groups"))
                .withArguments(new LiteralArgument("remove"))
                .withArguments(new StringArgument("group").replaceSuggestions(ArgumentSuggestions.strings(plugin.groups().data.commandSuggestions())))
                .executes((sender, args) -> {
                    final Player player = (Player) args.get("player");
                    final String groupname = (String) args.get("group");

                    plugin.groups().data.find(groupname).ifPresentOrElse(group -> {
                        group.findPlayer(player).ifPresentOrElse((i) -> {
                            try
                            {
                                group.removePlayer(player);
                                sender.sendRichMessage("Removed %s from group %s".formatted(player.getName(), groupname));
                            }
                            catch (Exception e)
                            {
                                sender.sendRichMessage("<red>PERMISSIONS ! <white>Unable to remove group from player");
                            }
                        }, () -> {
                            sender.sendRichMessage("<red>PERMISSIONS ! <white>Player not in this group");
                        });
                    }, () -> sender.sendRichMessage("<red>PERMISSIONS ! <white>Group not found"));
                })
                .register();
    }
}
