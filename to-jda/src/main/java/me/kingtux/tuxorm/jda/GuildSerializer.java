package me.kingtux.tuxorm.jda;


import dev.tuxjsql.basic.sql.BasicDataTypes;
import dev.tuxjsql.core.builders.ColumnBuilder;
import me.kingtux.tuxorm.TOConnection;
import me.kingtux.tuxorm.serializers.SingleSecondarySerializer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;


public class GuildSerializer implements SingleSecondarySerializer<Guild, Long> {
    private JDA jda;
    private TOConnection connection;

    public GuildSerializer(JDA jda, TOConnection connection) {
        this.jda = jda;
        this.connection = connection;
    }

    @Override
    public Long getSimplifiedValue(Guild guild) {
        return guild.getIdLong();
    }

    @Override
    public Guild buildFromSimplifiedValue(Long aLong) {
        return jda.getGuildById(aLong);
    }

    @Override
    public ColumnBuilder createColumn(String s) {
        return connection.getBuilder().createColumn().name(s).setDataType(BasicDataTypes.INTEGER);
    }
}
