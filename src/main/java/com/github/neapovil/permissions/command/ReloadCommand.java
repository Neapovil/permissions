package com.github.neapovil.permissions.command;

import java.io.IOException;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LiteralArgument;

public final class ReloadCommand extends AbstractCommand
{
    @Override
    public void register()
    {
        new CommandAPICommand("permissions")
                .withPermission("permissions.command")
                .withArguments(new LiteralArgument("reload"))
                .executes((sender, args) -> {
                    try
                    {
                        plugin.load();
                        plugin.groups().groups.forEach(group -> {
                            plugin.getServer().getPluginManager().removePermission(group.idAsString());
                            plugin.getServer().getPluginManager().addPermission(group.permission());
                            group.players.forEach(i -> {
                                i.player().ifPresent(player -> {
                                    if (player.isOnline())
                                    {
                                        plugin.syncPermissions(player);
                                    }
                                });
                            });
                        });
                        sender.sendMessage("Groups reloaded");
                    }
                    catch (IOException e)
                    {
                        sender.sendRichMessage("<red>Unable to reload groups");
                        plugin.getLogger().severe(e.getMessage());
                    }
                });
    }
}
