package com.github.neapovil.permissions.persistence;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import com.github.neapovil.permissions.object.PermissionsObject;
import com.google.gson.Gson;

public final class PermissionsDataType implements PersistentDataType<String, PermissionsObject>
{
    private final Gson gson = new Gson();

    @Override
    public @NotNull Class<String> getPrimitiveType()
    {
        return String.class;
    }

    @Override
    public @NotNull Class<PermissionsObject> getComplexType()
    {
        return PermissionsObject.class;
    }

    @Override
    public @NotNull String toPrimitive(@NotNull PermissionsObject complex, @NotNull PersistentDataAdapterContext context)
    {
        return this.gson.toJson(complex);
    }

    @Override
    public @NotNull PermissionsObject fromPrimitive(@NotNull String primitive, @NotNull PersistentDataAdapterContext context)
    {
        return this.gson.fromJson(primitive, PermissionsObject.class);
    }
}
