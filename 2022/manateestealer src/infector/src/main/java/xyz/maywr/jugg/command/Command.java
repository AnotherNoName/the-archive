package xyz.maywr.jugg.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import xyz.maywr.jugg.util.MessageUtil;

/**
 * @author maywr
 * 14.07.2022 21:58
 */
public @Getter
@AllArgsConstructor
abstract class Command
{
	private String name, destination;
	private boolean requireAdmin;

	public abstract void exec( String[] args, MessageReceivedEvent event, boolean admin );

	protected void answer( String msg, MessageReceivedEvent event )
	{
		MessageUtil.plain( msg, event );
	}

}