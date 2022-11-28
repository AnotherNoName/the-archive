package xyz.maywr.jugg.command;

import lombok.Getter;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import xyz.maywr.jugg.ServerBootstrap;
import xyz.maywr.jugg.command.impl.*;
import xyz.maywr.jugg.util.MessageUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum CommandManager
{
	INSTANCE;

	@Getter
	private final List< Command > commands = new ArrayList<>();

	CommandManager()
	{
		commands.addAll( Arrays.asList( new HelpCommand(), new GenerateCommand(), new BanCommand(),
				new ActivateCommand(), /*new UpdateCommand(),*/ new KeyListCommand(), new BanUserCommand(), new WebhookCommand() ) );
	}

	public void executeCommand( String text, MessageReceivedEvent event )
	{
		String[] split = text.split( " " );
		boolean found = false, admin = event.getMessage().getChannel().getName()
		                                    .equals( ServerBootstrap.MANAGE_CHAT_NAME );

		if ( !admin && !( event.getMessage().getChannelType() == ChannelType.PRIVATE ) )
			return;

		if ( admin )
		{
			ServerBootstrap.setAdminChannel( event.getMessage().getChannel() );
		}

		for ( Command c : commands )
		{
			if ( c.getName().equals( split[ 0 ] ) )
			{
				if ( c.isRequireAdmin() && !admin )
				{
					MessageUtil.error( "this command require admin rights", event.getMessage().getChannel() );
					return;
				}
				c.exec( Arrays.copyOfRange( split, 1, split.length ), event, admin );
				found = true;
			}
		}

		if ( !found )
		{
			MessageUtil.error( String.format( "no **$%s** command found. do **$help**", split[ 0 ] ), event.getMessage()
			                                                                                               .getChannel() );
		}
	}
}
