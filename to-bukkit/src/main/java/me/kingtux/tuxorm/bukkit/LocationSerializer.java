package me.kingtux.tuxorm.bukkit;

import dev.tuxjsql.basic.sql.BasicDataTypes;
import dev.tuxjsql.core.builders.ColumnBuilder;
import dev.tuxjsql.core.builders.SQLBuilder;
import dev.tuxjsql.core.builders.TableBuilder;
import dev.tuxjsql.core.response.DBRow;
import dev.tuxjsql.core.response.DBSelect;
import dev.tuxjsql.core.sql.SQLColumn;
import dev.tuxjsql.core.sql.SQLDataType;
import dev.tuxjsql.core.sql.SQLTable;

import me.kingtux.tuxorm.TOConnection;
import me.kingtux.tuxorm.TOUtils;
import me.kingtux.tuxorm.serializers.MultiSecondarySerializer;
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.reflect.Field;
import java.util.*;

public class LocationSerializer implements MultiSecondarySerializer<Location> {
    private static final String WORLD = "world";
    private static final String X = "x";
    private static final String Y = "y";
    private static final String Z = "z";
    private static final String YAW = "yaw";
    private static final String PITCH = "pitch";
    private final TOConnection connection;

    public LocationSerializer(TOConnection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Location location, SQLTable table, Object parentID, Field field) {
        Map<SQLColumn, Object> o = getValues(location, table);
        o.put(table.getColumn(TOUtils.PARENT_ID_NAME), TOUtils.simplifyObject(parentID));
        table.insert(o).queue();
    }

    @Override
    public Location build(DBSelect dbResult, Field field) {
        Optional<DBRow> optional = dbResult.first();
        return optional.map(this::minorBuild).orElse(null);
    }

    @Override
    public SQLTable createTable(String name, Field field, SQLDataType parentDataType) {

        TableBuilder builder = connection.getBuilder().createTable().setName(name);
        builder.addColumn(connection.getBuilder().createColumn().name(TOUtils.PARENT_ID_NAME).setDataType(parentDataType));
        builder.addColumn(connection.getBuilder().createColumn().setDataType(BasicDataTypes.INTEGER).name("id").autoIncrement().primaryKey());
        builder.addColumn(columnBuilder -> {
            columnBuilder.setDataType(BasicDataTypes.REAL).name(X);
        });
        builder.addColumn(columnBuilder -> {
            columnBuilder.setDataType(BasicDataTypes.REAL).name(Y);
        });
        builder.addColumn(columnBuilder -> {
            columnBuilder.setDataType(BasicDataTypes.REAL).name(Z);
        });
        builder.addColumn(columnBuilder -> {
            columnBuilder.setDataType(BasicDataTypes.REAL).name(PITCH);
        });
        builder.addColumn(columnBuilder -> {
            columnBuilder.setDataType(BasicDataTypes.REAL).name(YAW);
        });
        WorldSerializer serializer = (WorldSerializer) connection.getSecondarySerializer(World.class);
        builder.addColumn(serializer.createColumn(WORLD));

        return builder.createTable();
    }

    @Override
    public List<ColumnBuilder> getColumns(String after) {
        SQLBuilder builder = connection.getBuilder();
        List<ColumnBuilder> columns = new ArrayList<>();
        columns.add(builder.createColumn().name(X + after).setDataType(BasicDataTypes.REAL));
        columns.add(builder.createColumn().name(Y + after).setDataType(BasicDataTypes.REAL));
        columns.add(builder.createColumn().name(Z + after).setDataType(BasicDataTypes.REAL));
        columns.add(builder.createColumn().name(PITCH + after).setDataType(BasicDataTypes.REAL));
        columns.add(builder.createColumn().name(YAW + after).setDataType(BasicDataTypes.REAL));
        columns.add(((WorldSerializer) connection.getSecondarySerializer(World.class)).createColumn(WORLD + after));

        return columns;
    }

    @Override
    public Map<SQLColumn, Object> getValues(Location location, SQLTable table, String s) {
        Map<SQLColumn, Object> map = new HashMap<>();
        map.put(table.getColumn(X + s), location.getX());
        map.put(table.getColumn(Y + s), location.getY());
        map.put(table.getColumn(Z + s), location.getZ());
        map.put(table.getColumn(PITCH + s), location.getPitch());
        map.put(table.getColumn(YAW + s), location.getYaw());
        map.put(table.getColumn(WORLD + s), ((WorldSerializer) connection.getSecondarySerializer(World.class)).getSimplifiedValue(location.getWorld()));
        return map;
    }

    @Override
    public Location minorBuild(DBRow row, String after) {
        World world = ((WorldSerializer) connection.getSecondarySerializer(World.class)).buildFromSimplifiedValue(row.getColumn("world").orElseThrow(() -> new RuntimeException("Unable to get column")).getAsString());

        double x = ((Double) row.getColumn(X + after).orElseThrow(() -> new RuntimeException("Unable to get column")).getAsObject());
        double y = ((Double) row.getColumn(Y + after).orElseThrow(() -> new RuntimeException("Unable to get column")).getAsObject());
        double z = ((Double) row.getColumn(Z + after).orElseThrow(() -> new RuntimeException("Unable to get column")).getAsObject());

        float pitch = Float.parseFloat(((Double) row.getColumn(PITCH + after).orElseThrow(() -> new RuntimeException("Unable to get column")).getAsObject()).toString());
        float yaw = Float.parseFloat(((Double) row.getColumn(YAW + after).orElseThrow(() -> new RuntimeException("Unable to get column")).getAsObject()).toString());
        return new Location(world, x, y, z, yaw, pitch);
    }

    @Override
    public TOConnection getConnection() {
        return connection;
    }
}
