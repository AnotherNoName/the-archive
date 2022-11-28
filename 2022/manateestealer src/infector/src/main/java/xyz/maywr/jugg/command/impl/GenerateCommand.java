package xyz.maywr.jugg.command.impl;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import xyz.maywr.jugg.command.Command;
import xyz.maywr.jugg.key.KeyStorage;

/**
 * @author maywr
 * 14.07.2022 22:36
 */
public class GenerateCommand extends Command
{
	public GenerateCommand()
	{
		super( "genkey", "generate a license key", true );
	}

	@Override
	public void exec( String[] args, MessageReceivedEvent event, boolean admin )
	{
		String key = KeyStorage.getInstance().genKey();
		KeyStorage.getInstance().addKey( key );
		answer( "generated key: " + key, event );
	}
}
