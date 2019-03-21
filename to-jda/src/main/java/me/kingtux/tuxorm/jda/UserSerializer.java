package me.kingtux.tuxorm.jda;

import me.kingtux.tuxjsql.core.Column;
import me.kingtux.tuxjsql.core.CommonDataTypes;
import me.kingtux.tuxorm.serializers.SingleSecondarySerializer;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.User;

public class UserSerializer implements SingleSecondarySerializer<User, Long> {
    private JDA jda;

    public UserSerializer(JDA jda) {
        this.jda = jda;
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
    public Column createColumn(String s) {
        return Column.create().name(s).type(CommonDataTypes.BIGINT).build();
    }
}
