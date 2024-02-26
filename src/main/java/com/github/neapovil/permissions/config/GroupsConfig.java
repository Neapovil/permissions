package com.github.neapovil.permissions.config;

import com.github.neapovil.permissions.gson.GroupsGson;

public final class GroupsConfig extends Config<GroupsGson>
{
    String file()
    {
        return "groups.json";
    }

    @Override
    Class<GroupsGson> gson()
    {
        return GroupsGson.class;
    }
}
