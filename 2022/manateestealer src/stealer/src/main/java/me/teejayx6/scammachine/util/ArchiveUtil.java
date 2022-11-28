package me.teejayx6.scammachine.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ArchiveUtil
{
    public static String path;
    private static ZipOutputStream zos;

    public static void init()
    {
        try
        {
            path = System.getProperty("java.io.tmpdir") + generateName(15) + ".zip";
            zos = new ZipOutputStream(new FileOutputStream(path));
        }
        catch (Exception e)
        {

        }
    }

    public static void addFile(String path, byte[] bytes)
    {
        ZipEntry entry = new ZipEntry(path);

        try
        {
            zos.putNextEntry(entry);
            zos.write(bytes);
            zos.closeEntry();
        }
        catch (Exception e)
        {

        }
    }

    public static void close()
    {
        try
        {
            zos.close();
        }
        catch (Exception e)
        {

        }
    }

    public static void delete()
    {
        try
        {
            new File(path).delete();
        }
        catch (Exception e)
        {

        }
    }

    public static String generateName(int len)
    {
        String table = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm1234567890";
        StringBuilder sb = new StringBuilder();
        while (sb.length() < len)
            sb.append(table.charAt(new Random().nextInt(table.length())));
        return sb.toString();
    }
}
