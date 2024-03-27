package com.github.neapovil.permissions.command;

import com.github.neapovil.permissions.Permissions;

public abstract class AbstractCommand
{
    protected Permissions plugin = Permissions.instance();

    public abstract void register();
}
