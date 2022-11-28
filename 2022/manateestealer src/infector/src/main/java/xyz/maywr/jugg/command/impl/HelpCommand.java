package xyz.maywr.jugg.command.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import xyz.maywr.jugg.command.Command;
import xyz.maywr.jugg.command.CommandManager;

import java.awt.*;
import java.time.OffsetDateTime;

/**
 * @author maywr
 * 14.07.2022 22:32
 */
public class HelpCommand extends Command
{
	public HelpCommand()
	{
		super( "help", "see all commands available", false );
	}

	@Override
	public void exec( String[] args, MessageReceivedEvent event, boolean admin )
	{
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor( new Color( 2500135 ) ).setTimestamp( OffsetDateTime.now() )
				.setAuthor( "China Town $$", "https://discord.gg/E4SkdxsPw5", "https://cdn.discordapp" +
						".com/icons/998933572348018688/a959aa943197ec20a1f7e4c39ca1bbcb.webp?size=128" );
		for ( Command c : CommandManager.INSTANCE.getCommands() )
		{
			if ( c.isRequireAdmin() )
			{
				if ( admin )
				{
					builder.addField( "$" + c.getName(), c.getDestination(), true );
				}
			} else
			{
				builder.addField( "$" + c.getName(), c.getDestination(), true );
			}
		}
		event.getMessage().getChannel().sendMessage( new MessageBuilder().setEmbeds( builder.build() ).build() )
		     .submit();

	}
}
