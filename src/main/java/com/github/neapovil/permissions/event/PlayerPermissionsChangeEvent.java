package com.github.neapovil.permissions.event;

import java.util.Optional;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerPermissionsChangeEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();
    @Nullable
    private final Player player;

    public PlayerPermissionsChangeEvent(@Nullable Player player)
    {
        this.player = player;
    }

    public Optional<Player> player()
    {
        return Optional.ofNullable(this.player);
    }

    @NotNull
    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList()
    {
        return handlers;
    }
}