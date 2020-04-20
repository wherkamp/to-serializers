package me.kingtux.tuxorm.jda;

import dev.tuxjsql.basic.sql.BasicDataTypes;
import dev.tuxjsql.core.builders.ColumnBuilder;

import me.kingtux.tuxorm.TOConnection;
import me.kingtux.tuxorm.serializers.SingleSecondarySerializer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;


public class UserSerializer implements SingleSecondarySerializer<User, Long> {
    private JDA jda;
    private TOConnection connection;

    public UserSerializer(JDA jda, TOConnection connection) {
        this.jda = jda;
        this.connection = connection;
    }

    @Override
    public Long getSimplifiedValue(User user) {
        return user.getIdLong();
    }

    @Override
    public User buildFromSimplifiedValue(Long aLong) {
        return jda.getUserById(aLong);
    }

    @Override
    public ColumnBuilder createColumn(String s) {
        return connection.getBuilder().createColumn().name(s).setDataType(BasicDataTypes.INTEGER);
    }
}
