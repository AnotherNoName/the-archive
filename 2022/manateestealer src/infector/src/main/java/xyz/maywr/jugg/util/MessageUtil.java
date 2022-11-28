package xyz.maywr.jugg.util;

import lombok.experimental.UtilityClass;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.io.File;

/**
 * @author maywr
 * 15.07.2022 0:35
 */
@UtilityClass
public class MessageUtil
{
    public void plain( String msg, MessageReceivedEvent event )
    {
        withColor( msg, null, event.getMessage().getChannel(), 2500135 );
    }

    public void plain( String msg, MessageChannel channel )
    {
        withColor( msg, null, channel, 2500135 );
    }

    public void error( String msg, MessageChannel channel )
    {
        withColor( msg, null, channel, 16192781 );
    }

    public void success( String msg, MessageReceivedEvent event )
    {
        success( msg, null, event.getMessage().getChannel() );
    }

    public void success( String msg, String secondMsg, MessageChannel channel )
    {
        withColor( msg, secondMsg, channel, 3208973 );
    }

    public void file( File file, MessageChannel channel )
    {
        channel.sendFile( file ).submit();
    }

    private void withColor( String msg, String secondMsg, MessageChannel channel, int color )
    {
        channel.sendMessage( new MessageBuilder()
                .setEmbeds( new EmbedBuilder().setColor( new Color( color ) ).setTitle( msg )
                        .setDescription( secondMsg )
                        .setAuthor( "China Town $$", "https://discord.gg/E4SkdxsPw5", "https://cdn.discordapp.com/icons/998933572348018688/a959aa943197ec20a1f7e4c39ca1bbcb.webp?size=128" )
                        .build() ).build() ).submit();
    }

    public static void error( String s, String s1, MessageChannel channel )
    {
        withColor( s, s1, channel, 16192781 );
    }
}
