package xyz.maywr.jugg.command.impl;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import xyz.maywr.jugg.command.Command;
import xyz.maywr.jugg.key.KeyStorage;
import xyz.maywr.jugg.util.MessageUtil;

/**
 * @author maywr
 * 14.07.2022 22:58
 */
public class BanCommand extends Command
{
	public BanCommand()
	{
		super( "bankey", "ban a license key", true );
	}

	@Override
	public void exec( String[] args, MessageReceivedEvent event, boolean admin )
	{
		if ( !KeyStorage.getInstance().isKeyValid( args[ 0 ] ) )
		{
			MessageUtil.error( "key is not valid", event.getMessage().getChannel() );
			return;
		}
		KeyStorage.getInstance().removeKey( args[ 0 ] );
		answer( "successfully banned the key", event );
	}
}
