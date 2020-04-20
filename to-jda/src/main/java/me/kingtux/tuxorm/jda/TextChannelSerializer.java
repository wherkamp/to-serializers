package me.kingtux.tuxorm.jda;

import dev.tuxjsql.basic.sql.BasicDataTypes;

import dev.tuxjsql.core.builders.ColumnBuilder;
import dev.tuxjsql.core.sql.SQLColumn;
import me.kingtux.tuxorm.TOConnection;
import me.kingtux.tuxorm.serializers.SingleSecondarySerializer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;


public class TextChannelSerializer implements SingleSecondarySerializer<TextChannel, Long> {
    private JDA jda;
    private TOConnection connection;

    public TextChannelSerializer(JDA jda, TOConnection connection) {
        this.jda = jda;
        this.connection = connection;
    }

    @Override
    public Long getSimplifiedValue(TextChannel channel) {
        return channel.getIdLong();
    }

    @Override
    public TextChannel buildFromSimplifiedValue(Long aLong) {
        return jda.getTextChannelById(aLong);
    }

    @Override
    public ColumnBuilder createColumn(String s) {
        return connection.getBuilder().createColumn().name(s).setDataType(BasicDataTypes.INTEGER);
    }
}
