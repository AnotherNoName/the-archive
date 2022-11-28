package xyz.maywr.jugg.command.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import xyz.maywr.jugg.command.Command;
import xyz.maywr.jugg.key.KeyStorage;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.Map;

/**
 * @author maywr
 * 17.07.2022 15:31
 */
public class KeyListCommand extends Command
{
	public KeyListCommand()
	{
		super( "keylist", "tells you about the keys", true );
	}

	@Override
	public void exec( String[] args, MessageReceivedEvent event, boolean admin )
	{
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor( new Color( 2500135 ) ).setTimestamp( OffsetDateTime.now() )
		       .setFooter( "juggenbande ent. 2022", null )
		       .setAuthor( "JUGGSTORE $$", "https://discord.gg/bEHjGT39", "https://i.imgur.com/XnFtSfM.jpeg" );
		builder.addField( "unused keys", String.join( "\n", KeyStorage.getInstance().getUnused() ), true );
		for ( Map.Entry< String, String > entry : KeyStorage.getInstance().getLinked().entrySet() )
		{
			builder.addField( entry.getKey(), entry.getValue(), false );
		}
		event.getMessage().getChannel().sendMessage( new MessageBuilder().setEmbeds( builder.build() ).build() )
		     .submit();
	}
}
