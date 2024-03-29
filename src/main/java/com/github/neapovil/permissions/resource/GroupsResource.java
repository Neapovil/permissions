package com.github.neapovil.permissions.resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;

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

    public Optional<Group> find(int id)
    {
        return this.groups.stream().filter(i -> i.id == id).findFirst();
    }

    public static class Group
    {
        public int id;
        public String name;
        public List<String> permissions = new ArrayList<>();
        public final List<Player> players = new ArrayList<>();

        public String idAsString()
        {
            return "" + this.id;
        }

        public Optional<org.bukkit.entity.Player> findPlayer(org.bukkit.entity.Player player)
        {
            return this.players.stream()
                    .filter(i -> i.uuid.equals(player.getUniqueId()))
                    .filter(i -> i.player().isPresent())
                    .map(i -> i.player().get())
                    .findFirst();
        }

        public void addPlayer(org.bukkit.entity.Player player) throws IOException
        {
            this.players.add(new Player(player.getUniqueId()));

            final Permissions plugin = Permissions.instance();
            plugin.save();
            plugin.syncPermissions(player);
        }

        public void removePlayer(org.bukkit.entity.Player player) throws IOException
        {
            this.players.removeIf(i -> i.uuid.equals(player.getUniqueId()));

            final Permissions plugin = Permissions.instance();
            plugin.save();
            plugin.syncPermissions(player);
        }

        public Permission permission()
        {
            final Map<String, Boolean> children = this.permissions.stream().collect(Collectors.toMap(k -> k, v -> true));
            return new Permission(this.idAsString(), children);
        }

        public class Player
        {
            public UUID uuid;

            public Player(UUID uuid)
            {
                this.uuid = uuid;
            }

            public Optional<org.bukkit.entity.Player> player()
            {
                return Optional.ofNullable(Bukkit.getPlayer(this.uuid));
            }
        }
    }
}
