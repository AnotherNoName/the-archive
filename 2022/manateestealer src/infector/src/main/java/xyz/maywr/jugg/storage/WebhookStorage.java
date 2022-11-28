package xyz.maywr.jugg.storage;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import lombok.Cleanup;
import lombok.Getter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * @author maywr
 * 20.07.2022 23:58
 */
public class WebhookStorage implements Jsonable
{
	@Getter( lazy = true )
	private static final WebhookStorage instance = new WebhookStorage();

	private final File webhooksFile;

	private WebhookStorage()
	{
		webhooksFile = new File( "webhooks.json" );
		if ( webhooksFile.exists() )
			return;
		try
		{
			if ( !webhooksFile.createNewFile() )
			{
				System.err.println( "failed to create keys file" );
				System.exit( 1 );
			}
			@Cleanup FileWriter writer = new FileWriter( webhooksFile );
			JsonObject object = new JsonObject();
			writer.write( new GsonBuilder().setPrettyPrinting().create().toJson( object ) );
		} catch ( IOException e )
		{
			System.err.println( "failed to create keys file" );
			e.printStackTrace();
			System.exit( 1 );
		}
	}

	public Map< String, String > getAsMap()
	{
		try
		{
			JsonObject object = ( JsonObject ) JsonParser.parseString( new String( Files.readAllBytes( webhooksFile.toPath() ), StandardCharsets.UTF_8 ) );
			return GSON.fromJson( object.toString(), new TypeToken< HashMap< String, String > >()
			{
			}.getType() );
		} catch ( Exception e )
		{
			e.printStackTrace();
		}
		return new HashMap<>();
	}

	public void put( String key, String webhook )
	{
		try
		{
			JsonObject object = ( JsonObject ) JsonParser.parseString( new String( Files.readAllBytes( webhooksFile.toPath() ), StandardCharsets.UTF_8 ) );
			@Cleanup FileWriter writer = new FileWriter( webhooksFile );
			object.addProperty( key, webhook );
			writer.write( GSON_PRETTY.toJson( object ) );
			writer.close();
		} catch ( Exception e )
		{
			e.printStackTrace();
		}
	}

	public String getWebhook( String key )
	{
		try
		{
			JsonObject object = ( JsonObject ) JsonParser.parseString( new String( Files.readAllBytes( webhooksFile.toPath() ), StandardCharsets.UTF_8 ) );
			if ( object.has( key ) )
				return object.get( key ).getAsString();
		} catch ( Exception e )
		{
			e.printStackTrace();
		}
		return null;
	}

	public boolean hasWebhook( String key )
	{
		return this.getWebhook( key ) != null;
	}
}
