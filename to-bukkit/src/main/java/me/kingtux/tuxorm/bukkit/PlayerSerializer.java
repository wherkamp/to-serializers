package me.kingtux.tuxorm.bukkit;

import dev.tuxjsql.basic.sql.BasicDataTypes;
import dev.tuxjsql.core.builders.ColumnBuilder;

import me.kingtux.tuxorm.TOConnection;
import me.kingtux.tuxorm.serializers.SingleSecondarySerializer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class PlayerSerializer implements SingleSecondarySerializer<OfflinePlayer, String> {
    private TOConnection connection;

    public PlayerSerializer(TOConnection connection) {
        this.connection = connection;
    }

    @Override
    public String getSimplifiedValue(OfflinePlayer o) {
        return o.getUniqueId().toString();
    }

    @Override
    public OfflinePlayer buildFromSimplifiedValue(String value) {
        return Bukkit.getOfflinePlayer(UUID.fromString(value));
    }

    @Override
    public ColumnBuilder createColumn(String name) {
        return connection.getBuilder().createColumn().name(name).setDataType(BasicDataTypes.TEXT);
    }
}
