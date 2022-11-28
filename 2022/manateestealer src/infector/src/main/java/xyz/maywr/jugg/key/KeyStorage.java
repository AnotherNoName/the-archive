package xyz.maywr.jugg.key;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.Cleanup;
import lombok.Getter;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import xyz.maywr.jugg.util.HexUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author maywr
 * 14.07.2022 22:37
 */
public class KeyStorage
{
	@Getter( lazy = true )
	private static final KeyStorage instance = new KeyStorage();

	private static final Gson GSON = new Gson();
	private static final Gson GSON_PRETTY = new GsonBuilder().setPrettyPrinting().create();

	private final File keysFile;

	private KeyStorage()
	{
		keysFile = new File( "keys.json" );
		if ( keysFile.exists() )
			return;
		try
		{
			if ( !keysFile.createNewFile() )
			{
				System.err.println( "failed to create keys file" );
				System.exit( 1 );
			}
			@Cleanup FileWriter writer = new FileWriter( keysFile );
			JsonObject object = new JsonObject();
			object.add( "unused_keys", new JsonArray() );
			writer.write( new GsonBuilder().setPrettyPrinting().create().toJson( object ) );
		} catch ( IOException e )
		{
			System.err.println( "failed to create keys file" );
			e.printStackTrace();
			System.exit( 1 );
		}
	}

	public boolean isPremium( User user )
	{
		try
		{
			JsonObject object = ( JsonObject ) JsonParser.parseString( new String( Files.readAllBytes( keysFile.toPath() ), StandardCharsets.UTF_8 ) );
			return !( object.get( user.getName() ) == null );
		} catch ( Exception e )
		{
			e.printStackTrace();
		}
		return false;
	}

	public boolean isKeyValid( String key )
	{
		try
		{
			JsonObject object = ( JsonObject ) JsonParser.parseString( new String( Files.readAllBytes( keysFile.toPath() ), StandardCharsets.UTF_8 ) );
			return object.get( "unused_keys" ).getAsJsonArray().contains( GSON.toJsonTree( key ) );
		} catch ( Exception e )
		{
			e.printStackTrace();
		}
		return false;
	}

	public boolean activate( String key, MessageReceivedEvent event )
	{
		if ( !isKeyValid( key ) )
		{
			return false;
		}
		put( event.getAuthor().getName(), key );
		removeKey( key );
		return true;
	}

	private void put( String id, String key )
	{
		try
		{
			JsonObject object = ( JsonObject ) JsonParser.parseString( new String( Files.readAllBytes( keysFile.toPath() ), StandardCharsets.UTF_8 ) );
			@Cleanup FileWriter writer = new FileWriter( keysFile );
			object.addProperty( id, key );
			writer.write( new GsonBuilder().setPrettyPrinting().create().toJson( object ) );
			writer.close();
		} catch ( Exception e )
		{
			e.printStackTrace();
		}
	}

	public void addKey( String key )
	{
		try
		{
			JsonObject object = ( JsonObject ) JsonParser.parseString( new String( Files.readAllBytes( keysFile.toPath() ), StandardCharsets.UTF_8 ) );
			@Cleanup FileWriter writer = new FileWriter( keysFile );
			object.get( "unused_keys" ).getAsJsonArray().add( key );
			writer.write( new GsonBuilder().setPrettyPrinting().create().toJson( object ) );
			writer.close();
		} catch ( Exception e )
		{
			e.printStackTrace();
		}
	}

	public void removeKey( String key )
	{
		try
		{
			JsonObject object = ( JsonObject ) JsonParser.parseString( new String( Files.readAllBytes( keysFile.toPath() ), StandardCharsets.UTF_8 ) );
			@Cleanup FileWriter writer = new FileWriter( keysFile );
			List< String > keys = GSON_PRETTY.fromJson( object.get( "unused_keys" )
			                                                  .getAsJsonArray(), new TypeToken< List< String > >()
			{
			}.getType() );
			keys.remove( key );
			object.remove( "unused_keys" );
			object.add( "unused_keys", GSON.toJsonTree( keys ) );
			writer.write( GSON_PRETTY.toJson( object ) );
			writer.close();
		} catch ( Exception e )
		{
			e.printStackTrace();
		}
	}

	public List< String > getUnused()
	{
		JsonObject object = null;
		try
		{
			object = ( JsonObject ) JsonParser.parseString( new String( Files.readAllBytes( keysFile.toPath() ), StandardCharsets.UTF_8 ) );
		} catch ( IOException e )
		{
			e.printStackTrace();
		}
		return GSON_PRETTY.fromJson( object.get( "unused_keys" ).getAsJsonArray(), new TypeToken< List< String > >()
		{
		}.getType() );
	}

	public Map< String, String > getLinked()
	{
		try
		{
			JsonObject object = ( JsonObject ) JsonParser.parseString( new String( Files.readAllBytes( keysFile.toPath() ), StandardCharsets.UTF_8 ) );
			object.remove( "unused_keys" );
			return GSON.fromJson( object.toString(), new TypeToken< HashMap< String, String > >()
			{
			}.getType() );
		} catch ( Exception e )
		{
			e.printStackTrace();
		}
		return new HashMap<>();
	}

	public String getKeyFor( User user )
	{
		try
		{
			JsonObject object = ( JsonObject ) JsonParser.parseString( new String( Files.readAllBytes( keysFile.toPath() ), StandardCharsets.UTF_8 ) );
			if ( object.has( user.getName() ) )
				return object.get( user.getName() ).getAsString();

		} catch ( Exception e )
		{
			e.printStackTrace();
		}
		return "no-key";
	}

	public boolean banUser( String name )
	{
		try
		{
			JsonObject object = ( JsonObject ) JsonParser.parseString( new String( Files.readAllBytes( keysFile.toPath() ), StandardCharsets.UTF_8 ) );
			if ( !object.has( name ) )
				return false;
			@Cleanup FileWriter writer = new FileWriter( keysFile );
			object.remove( name );
			writer.write( new GsonBuilder().setPrettyPrinting().create().toJson( object ) );
			writer.close();
			return true;
		} catch ( Exception e )
		{
			e.printStackTrace();
		}
		return false;
	}

	public String genKey()
	{
		byte[] keyBytes = new byte[ 15 ];
		new Random().nextBytes( keyBytes );
		return HexUtil.bytesToHex( keyBytes );
	}
}
