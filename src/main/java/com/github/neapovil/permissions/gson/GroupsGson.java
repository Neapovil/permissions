package com.github.neapovil.permissions.gson;

import java.util.ArrayList;
import java.util.List;

public final class GroupsGson
{
    public List<Group> groups = new ArrayList<>();

    public class Group
    {
        public String name;
        public List<String> permissions = new ArrayList<>();
    }
}
