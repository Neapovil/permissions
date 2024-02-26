package com.github.neapovil.permissions.config;

import java.io.IOException;
import java.nio.file.Files;

import com.github.neapovil.permissions.Permissions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class Config<T>
{
    protected final Permissions plugin = Permissions.instance();
    protected final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public T data = null;

    abstract String file();

    abstract Class<T> gson();

    public void init() throws IOException
    {
        plugin.saveResource(this.file(), false);
        this.load();
    }

    public void load() throws IOException
    {
        final String string = Files.readString(plugin.getDataFolder().toPath().resolve(this.file()));
        this.data = this.gson.fromJson(string, this.gson());
    }

    public void save() throws IOException
    {
        final String string = this.gson.toJson(this.data);
        Files.write(plugin.getDataFolder().toPath().resolve(this.file()), string.getBytes());
    }
}
