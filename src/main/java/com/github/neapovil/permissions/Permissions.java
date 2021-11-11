package com.github.neapovil.permissions;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import com.electronwill.nightconfig.core.file.FileConfig;
import com.github.neapovil.permissions.command.GroupsCommand;
import com.github.neapovil.permissions.command.UsersCommand;
import com.github.neapovil.permissions.listener.Listener;

public final class Permissions extends JavaPlugin
{
    private static Permissions instance;
    private FileConfig groups;
    private FileConfig config;
    private FileConfig players;

    @Override
    public void onEnable()
    {
        instance = this;

        this.groups = this.initConfig("groups.json");
        this.config = this.initConfig("config.toml");
        this.players = this.initConfig("players.json");

        this.getServer().getPluginManager().registerEvents(new Listener(), this);

        GroupsCommand.register();
        UsersCommand.register();
    }

    @Override
    public void onDisable()
    {
    }

    public static Permissions getInstance()
    {
        return instance;
    }

    public FileConfig getGroups()
    {
        return this.groups;
    }

    public FileConfig getPluginConfig()
    {
        return this.config;
    }

    public FileConfig getPlayers()
    {
        return this.players;
    }

    private FileConfig initConfig(String fileName)
    {
        this.saveResource(fileName, false);

        final FileConfig config = FileConfig.builder(new File(this.getDataFolder(), fileName))
                .autoreload()
                .autosave()
                .sync()
                .build();
        config.load();

        return config;
    }
}
