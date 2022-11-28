package xyz.maywr.jugg.anonfiles;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.net.URL;

public class AnonAPI
{

	public static String upload( File file )
	{
		try
		{
			JsonObject response = ( JsonObject ) JsonParser.parseString( HttpRequest
					.get( new URL( "https://api.anonfiles.com/upload" ) )
					.part( "file", file.getName(), "application/text", file ).body() );
			return response.get( "data" ).getAsJsonObject().get( "file" ).getAsJsonObject().get( "url" )
			               .getAsJsonObject().get( "short" ).getAsString();
		} catch ( Exception e )
		{
			e.printStackTrace();
		}
		return null;
	}

}
