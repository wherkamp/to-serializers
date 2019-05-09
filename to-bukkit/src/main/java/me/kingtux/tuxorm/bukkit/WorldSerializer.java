package me.kingtux.tuxorm.bukkit;

import me.kingtux.tuxjsql.core.Column;
import me.kingtux.tuxjsql.core.CommonDataTypes;
import me.kingtux.tuxorm.TOConnection;
import me.kingtux.tuxorm.serializers.SingleSecondarySerializer;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.UUID;

public class WorldSerializer implements SingleSecondarySerializer<World, String> {
    private TOConnection connection;

    public WorldSerializer(TOConnection connection) {
        this.connection = connection;
    }


    @Override
    public String getSimplifiedValue(World o) {
        return o.getUID().toString();
    }

    @Override
    public World buildFromSimplifiedValue(String value) {
        return Bukkit.getWorld(UUID.fromString(value));
    }

    @Override
    public Column createColumn(String name) {
        return connection.getBuilder().createColumn(name, CommonDataTypes.TEXT);
    }
}
