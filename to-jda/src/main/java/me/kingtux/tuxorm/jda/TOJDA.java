package me.kingtux.tuxorm.jda;

import me.kingtux.tuxorm.TOConnection;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class TOJDA {
    public static void registerSerializers(JDA jda, TOConnection connection) {
        connection.registerSecondarySerializer(User.class, new UserSerializer(jda, connection));
        connection.registerSecondarySerializer(TextChannel.class, new TextChannelSerializer(jda, connection));
        connection.registerSecondarySerializer(Guild.class, new GuildSerializer(jda, connection));
    }
}
