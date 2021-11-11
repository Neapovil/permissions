package com.github.neapovil.permissions.config;

import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.github.neapovil.permissions.Permissions;

public final class Players
{
    private final Permissions plugin = Permissions.getInstance();
    private final UUID uuid;

    private Players(UUID uuid)
    {
        this.uuid = uuid;
    }

    public static Players get(Player player)
    {
        return new Players(player.getUniqueId());
    }

    public static Players get(OfflinePlayer player)
    {
        return new Players(player.getUniqueId());
    }

    public boolean exists()
    {
        return plugin.getPlayers().get("players." + uuid) != null;
    }

    public String getGroup()
    {
        return plugin.getPlayers().get("players." + uuid + ".group");
    }

    public void setGroup(String groupName)
    {
        plugin.getPlayers().set("players." + uuid + ".group", groupName);
    }
}
