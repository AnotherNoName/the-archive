package xyz.maywr.jugg.command.impl;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import xyz.maywr.jugg.ServerBootstrap;
import xyz.maywr.jugg.command.Command;
import xyz.maywr.jugg.key.KeyStorage;
import xyz.maywr.jugg.util.MessageUtil;

/**
 * @author maywr
 * 14.07.2022 23:14
 */
public class ActivateCommand extends Command
{
	public ActivateCommand()
	{
		super( "activate", "activate a license key", false );
	}

	@Override
	public void exec( String[] args, MessageReceivedEvent event, boolean admin )
	{

		if ( KeyStorage.getInstance().activate( args[ 0 ], event ) )
		{
			MessageUtil.success( "successful activation", "send a minecraft mod file to edit", event.getMessage()
			                                                                                        .getChannel() );
			MessageUtil.success( String.format( "%s activated key", event.getAuthor()
			                                                             .getName() ), String.format( "used key: %s", args[ 0 ] ), ServerBootstrap.getAdminChannel() );
		} else
		{
			MessageUtil.error( "key is banned/not valid", event.getMessage().getChannel() );
			MessageUtil.error( String.format( "%s tried $activate with wrong key", event.getAuthor()
			                                                                            .getName() ), ServerBootstrap.getAdminChannel() );
		}
	}
}
