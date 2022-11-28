package xyz.maywr.jugg.command.impl;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import xyz.maywr.jugg.command.Command;
import xyz.maywr.jugg.storage.KeyStorage;
import xyz.maywr.jugg.storage.WebhookStorage;
import xyz.maywr.jugg.util.MessageUtil;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author maywr
 * 21.07.2022 0:30
 */
public class WebhookCommand extends Command
{
	public WebhookCommand()
	{
		super( "webhook", "changes ur webhook", false );
	}

	@Override
	public void exec( String[] args, MessageReceivedEvent event, boolean admin )
	{
		final String key = KeyStorage.getInstance().getKeyFor( event.getAuthor() );
		final String webhook = args[ 0 ];

		if ( key.equals( "no-key" ) )
		{
			MessageUtil.error( "enter a license key first", event.getMessage().getChannel() );
			return;
		}

		if ( !webhook.contains( "api/webhooks/" ) || !isUrl( webhook ) )
		{
			MessageUtil.error( "the sent url is not a discord webhook", event.getMessage().getChannel() );
			return;
		}

		WebhookStorage.getInstance().put( key, args[ 0 ] );
		MessageUtil.success( "success!", "webhook updated", event.getMessage().getChannel() );
	}

	private boolean isUrl( String link )
	{
		try
		{
			new URL( link );
			return true;
		} catch ( MalformedURLException e )
		{
			return false;
		}
	}
}
