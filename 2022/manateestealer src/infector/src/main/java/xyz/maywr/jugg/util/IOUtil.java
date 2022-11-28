package xyz.maywr.jugg.util;

import lombok.experimental.UtilityClass;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.function.BiConsumer;

/**
 * @author maywr
 * 15.07.2022 0:57
 */
@UtilityClass
public class IOUtil
{
	public void downloadAsync( Message.Attachment attachment, MessageChannel channel, File parent, BiConsumer< File, MessageChannel > callback )
	{
		new Thread( () ->
		{
			if ( !parent.isDirectory() )
				return;
			File f = new File( parent, attachment.getFileName() );
			MessageUtil.plain( "infecting " + f.getName(), channel );
			try

			{
				f.createNewFile();
			} catch ( IOException e )

			{
				e.printStackTrace();
			}
			try

			{
				URLConnection hc = new URL( attachment.getUrl() ).openConnection();
				hc.setRequestProperty( "User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2" );
				BufferedInputStream in = new BufferedInputStream( hc.getInputStream() );
				FileOutputStream fileOutputStream = new FileOutputStream( f );
				byte[] dataBuffer = new byte[ 1024 ];
				int bytesRead;
				while ( ( bytesRead = in.read( dataBuffer, 0, 1024 ) ) != -1 )
				{
					fileOutputStream.write( dataBuffer, 0, bytesRead );
				}
			} catch ( IOException e )

			{
				e.printStackTrace();
			}
			callback.accept( f, channel );
		} ).start();
	}

	public byte[] toByteArray( InputStream is )
	{
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[ 16384 ];

		try
		{
			while ( ( nRead = is.read( data, 0, data.length ) ) != -1 )
			{
				buffer.write( data, 0, nRead );
			}
		} catch ( Exception e )
		{
			return new byte[ 0 ];
		}

		return buffer.toByteArray();
	}

	public void copyFile( File first, File second )
	{
		try ( InputStream in = new BufferedInputStream( new FileInputStream( first ) ) ; OutputStream out = new BufferedOutputStream( new FileOutputStream( second ) ) )
		{

			byte[] buffer = new byte[ 1024 ];
			int lengthRead;
			while ( ( lengthRead = in.read( buffer ) ) > 0 )
			{
				out.write( buffer, 0, lengthRead );
				out.flush();
			}
		} catch ( IOException e )
		{
			e.printStackTrace();
		}
	}
}
