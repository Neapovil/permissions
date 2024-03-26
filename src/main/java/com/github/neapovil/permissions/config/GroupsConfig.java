package com.github.neapovil.permissions.config;

import com.github.neapovil.permissions.resource.GroupsResource;

public final class GroupsConfig extends Config<GroupsResource>
{
    String file()
    {
        return "groups.json";
    }

    @Override
    Class<GroupsResource> gson()
    {
        return GroupsResource.class;
    }
}
