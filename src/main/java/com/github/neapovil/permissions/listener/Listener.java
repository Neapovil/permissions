package com.github.neapovil.permissions.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.neapovil.permissions.Permissions;
import com.github.neapovil.permissions.config.Players;

import net.md_5.bungee.api.ChatColor;

public final class Listener implements org.bukkit.event.Listener
{
    private final Permissions plugin = Permissions.getInstance();

    @EventHandler
    public void playerJoin(PlayerJoinEvent event)
    {
        if (!Players.get(event.getPlayer()).exists())
        {
            plugin.getPlayers().set("players." + event.getPlayer().getUniqueId() + ".group", "");
        }
    }

    @EventHandler
    public void asyncPlayerChat(AsyncPlayerChatEvent event)
    {
        final String nogroupformat = plugin.getPluginConfig().get("no_group_format");
        final String groupformat = plugin.getPluginConfig().get("group_format");

        final String playergroup = Players.get(event.getPlayer()).getGroup();

        if (playergroup.isEmpty())
        {
            event.setFormat(nogroupformat.formatted(event.getPlayer().getDisplayName(), event.getMessage()));
        }
        else
        {
            final String prefix = ChatColor.translateAlternateColorCodes('&', plugin.getGroups().get("groups." + playergroup + ".prefix"));
            event.setFormat(groupformat.formatted(prefix, event.getPlayer().getDisplayName(), event.getMessage()));
        }
    }
}
