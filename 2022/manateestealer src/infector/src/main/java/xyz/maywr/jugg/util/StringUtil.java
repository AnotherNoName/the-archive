package xyz.maywr.jugg.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Random;

/**
 * @author maywr
 * 17.07.2022 2:56
 */
@UtilityClass
public class StringUtil
{
    public static final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    public static final Random random = new Random();

    public String generateString( int len )
    {
        byte[] b = new byte[ len / 2 ];
        random.nextBytes( b );
        return HexUtil.bytesToHex( b );
    }


    public String generateModId()
    {
        StringBuilder sb = new StringBuilder();
        for ( int i = 0; i < 8; i++ )
        {
            sb.append( ALPHABET[ random.nextInt( ALPHABET.length ) ] );
        }
        return sb.toString();
    }

    @SneakyThrows
    public String sha256( String input )
    {
        MessageDigest md = MessageDigest.getInstance( "SHA-256" );
        return HexUtil.bytesToHex( md.digest( input.getBytes( StandardCharsets.UTF_8 ) ) );
    }
}
