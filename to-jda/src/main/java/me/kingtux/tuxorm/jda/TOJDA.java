package me.kingtux.tuxorm.jda;

import me.kingtux.tuxorm.TOConnection;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class TOJDA {
    public static void registerSerializers(JDA jda, TOConnection connection) {
        connection.registerSecondarySerializer(User.class, new UserSerializer(jda));
        connection.registerSecondarySerializer(TextChannel.class, new TextChannelSerializer(jda));
        connection.registerSecondarySerializer(Guild.class, new GuildSerializer(jda));
    }
}
