package me.kingtux.tuxorm.jda;

import me.kingtux.tuxjsql.core.Column;
import me.kingtux.tuxjsql.core.CommonDataTypes;
import me.kingtux.tuxorm.serializers.SingleSecondarySerializer;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.TextChannel;

public class TextChannelSerializer implements SingleSecondarySerializer<TextChannel, Long> {
    private JDA jda;

    public TextChannelSerializer(JDA jda) {
        this.jda = jda;
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
    public Column createColumn(String s) {
        return Column.create().name(s).type(CommonDataTypes.BIGINT).build();
    }
}
