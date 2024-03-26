package com.github.neapovil.permissions.object;

import java.util.ArrayList;
import java.util.List;

import com.github.neapovil.permissions.Permissions;
import com.github.neapovil.permissions.resource.GroupsResource;

public final class PermissionsObject
{
    public final List<Integer> groups = new ArrayList<>();

    public List<GroupsResource.Group> groups()
    {
        final Permissions plugin = Permissions.instance();
        return plugin.groups().data.groups.stream().filter(i -> this.groups.contains(i.id)).toList();
    }
}
