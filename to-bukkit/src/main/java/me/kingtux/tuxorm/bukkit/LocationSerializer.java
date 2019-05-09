package me.kingtux.tuxorm.bukkit;

import me.kingtux.tuxjsql.core.Column;
import me.kingtux.tuxjsql.core.CommonDataTypes;
import me.kingtux.tuxjsql.core.DataType;
import me.kingtux.tuxjsql.core.Table;
import me.kingtux.tuxjsql.core.builders.SQLBuilder;
import me.kingtux.tuxjsql.core.result.DBResult;
import me.kingtux.tuxjsql.core.result.DBRow;
import me.kingtux.tuxorm.TOConnection;
import me.kingtux.tuxorm.TOUtils;
import me.kingtux.tuxorm.serializers.MultiSecondarySerializer;
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationSerializer implements MultiSecondarySerializer<Location> {
    private static final String WORLD = "world";
    private static final String X = "x";
    private static final String Y = "y";
    private static final String Z = "z";
    private static final String YAW = "yaw";
    private static final String PITCH = "pitch";
    private TOConnection connection;

    public LocationSerializer(TOConnection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Location location, Table table, Object parentID, Field field) {
        Map<Column, Object> o = getValues(location, table);
        o.put(table.getColumnByName(TOUtils.PARENT_ID_NAME), TOUtils.simplifyObject(parentID));
        table.insert(o);
    }

    @Override
    public Location build(DBResult dbResult, Field field) {
        return minorBuild(dbResult.get(0));
    }

    @Override
    public Table createTable(String name, Field field, DataType parentDataType) {
        List<Column> baseColumns = getColumns("");
        baseColumns.add(connection.getBuilder().createColumn(TOUtils.PARENT_ID_NAME, parentDataType));
        baseColumns.add(connection.getBuilder().createColumn().type(CommonDataTypes.BIGINT).name("id").autoIncrement(true).primary(true).build());
        return connection.getBuilder().createTable(name, baseColumns);
    }

    @Override
    public List<Column> getColumns(String after) {
        SQLBuilder builder = connection.getBuilder();
        List<Column> columns = new ArrayList<>();
        columns.add(builder.createColumn(X + after, CommonDataTypes.DOUBLE));
        columns.add(builder.createColumn(Y + after, CommonDataTypes.DOUBLE));
        columns.add(builder.createColumn(Z + after, CommonDataTypes.DOUBLE));
        columns.add(builder.createColumn(PITCH + after, CommonDataTypes.DOUBLE));
        columns.add(builder.createColumn(YAW + after, CommonDataTypes.DOUBLE));
        columns.add(((WorldSerializer) connection.getSecondarySerializer(World.class)).createColumn(WORLD + after));

        return columns;
    }

    @Override
    public Map<Column, Object> getValues(Location location, Table table, String s) {
        Map<Column, Object> map = new HashMap<>();
        map.put(table.getColumnByName(X + s), location.getX());
        map.put(table.getColumnByName(Y + s), location.getY());
        map.put(table.getColumnByName(Z + s), location.getZ());
        map.put(table.getColumnByName(PITCH + s), location.getPitch());
        map.put(table.getColumnByName(YAW + s), location.getYaw());
        map.put(table.getColumnByName(WORLD + s), ((WorldSerializer) connection.getSecondarySerializer(World.class)).getSimplifiedValue(location.getWorld()));

        return map;
    }

    @Override
    public Location minorBuild(DBRow row, String after) {
        World world = ((WorldSerializer) connection.getSecondarySerializer(World.class)).buildFromSimplifiedValue(row.getRowItem("world").getAsString());

        double x = ((Double)row.getRowItem(X + after).getAsObject());
        double y = ((Double)row.getRowItem(Y + after).getAsObject());
        double z = ((Double)row.getRowItem(Z + after).getAsObject());

        float pitch = Float.parseFloat(((Double)row.getRowItem(PITCH + after).getAsObject()).toString());
        float yaw = Float.parseFloat(((Double)row.getRowItem(YAW + after).getAsObject()).toString());
        return new Location(world, x, y, z, yaw, pitch);
    }

    @Override
    public TOConnection getConnection() {
        return connection;
    }
}
