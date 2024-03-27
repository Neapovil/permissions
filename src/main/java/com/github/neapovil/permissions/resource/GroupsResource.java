package com.github.neapovil.permissions.resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;

import com.github.neapovil.permissions.Permissions;

public final class GroupsResource
{
    public final List<Group> groups = new ArrayList<>();

    public List<String> commandSuggestions()
    {
        return this.groups.stream().map(i -> i.name).toList();
    }

    public Optional<Group> find(String name)
    {
        return this.groups.stream().filter(i -> i.name.equalsIgnoreCase(name)).findFirst();
    }

    public class Group
    {
        public int id;
        public String name;
        public List<String> permissions = new ArrayList<>();
        public final List<Player> players = new ArrayList<>();

        public Optional<org.bukkit.entity.Player> findPlayer(org.bukkit.entity.Player player)
        {
            return this.players.stream().filter(i -> i.uuid.equals(player.getUniqueId())).map(i -> i.player()).findFirst();
        }

        public void addPlayer(org.bukkit.entity.Player player) throws IOException
        {
            this.players.add(new Player(player.getUniqueId()));

            final Permissions plugin = Permissions.instance();
            plugin.groups().save();
            plugin.syncPermissions(player);
        }

        public void removePlayer(org.bukkit.entity.Player player) throws IOException
        {
            this.players.removeIf(i -> i.uuid.equals(player.getUniqueId()));

            final Permissions plugin = Permissions.instance();
            plugin.groups().save();
            plugin.syncPermissions(player);
        }

        public class Player
        {
            public UUID uuid;

            public Player(UUID uuid)
            {
                this.uuid = uuid;
            }

            public org.bukkit.entity.Player player()
            {
                return Bukkit.getPlayer(this.uuid);
            }
        }
    }
}
