package me.kingtux.tuxorm.jda;

import me.kingtux.tuxjsql.core.Column;
import me.kingtux.tuxjsql.core.CommonDataTypes;
import me.kingtux.tuxorm.serializers.SingleSecondarySerializer;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

public class GuildSerializer implements SingleSecondarySerializer<Guild, Long> {
    private JDA jda;

    public GuildSerializer(JDA jda) {
        this.jda = jda;
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
    public Column createColumn(String s) {
        return Column.create().name(s).type(CommonDataTypes.BIGINT).build();
    }
}
