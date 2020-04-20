package me.kingtux.tuxorm.bukkit;

import me.kingtux.tuxorm.TOConnection;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;

public class TOBukkit {

    public static void registerSerializers(TOConnection connection) {
        connection.registerSecondarySerializer(OfflinePlayer.class, new PlayerSerializer(connection));
        connection.registerSecondarySerializer(World.class, new WorldSerializer(connection));
        connection.registerSecondarySerializer(Location.class, new LocationSerializer(connection));
    }
}
