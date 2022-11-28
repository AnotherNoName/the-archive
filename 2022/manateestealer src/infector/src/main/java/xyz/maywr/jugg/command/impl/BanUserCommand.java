package xyz.maywr.jugg.command.impl;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import xyz.maywr.jugg.command.Command;
import xyz.maywr.jugg.key.KeyStorage;
import xyz.maywr.jugg.util.MessageUtil;

/**
 * @author maywr
 * 17.07.2022 15:47
 */
public class BanUserCommand extends Command
{
	public BanUserCommand()
	{
		super( "ban", "bans a user", true );
	}

	@Override
	public void exec( String[] args, MessageReceivedEvent event, boolean admin )
	{
		if ( KeyStorage.getInstance().banUser( args[ 0 ] ) )
			MessageUtil.success( "banned " + args[ 0 ], event );
		else
			MessageUtil.error( "no such user", event.getMessage().getChannel() );
	}
}
