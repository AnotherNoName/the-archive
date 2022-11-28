package xyz.maywr.jugg.util;

import lombok.experimental.UtilityClass;

/**
 * @author maywr
 * 14.07.2022 22:49
 */
@UtilityClass
public class HexUtil
{
	private final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

	public String bytesToHex( byte[] bytes )
	{
		char[] hexChars = new char[ bytes.length * 2 ];
		for ( int j = 0 ; j < bytes.length ; j++ )
		{
			int v = bytes[ j ] & 0xFF;
			hexChars[ j * 2 ] = HEX_ARRAY[ v >>> 4 ];
			hexChars[ j * 2 + 1 ] = HEX_ARRAY[ v & 0x0F ];
		}
		return new String( hexChars );
	}
}
