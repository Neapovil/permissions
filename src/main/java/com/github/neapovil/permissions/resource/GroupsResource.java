package com.github.neapovil.permissions.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;

public final class GroupsResource
{
    public final List<Group> groups = new ArrayList<>();

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

        public class Player
        {
            public UUID uuid;

            public org.bukkit.entity.Player player()
            {
                return Bukkit.getPlayer(this.uuid);
            }
        }
    }
}
