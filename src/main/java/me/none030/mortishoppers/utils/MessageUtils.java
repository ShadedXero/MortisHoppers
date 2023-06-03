package me.none030.mortishoppers.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class MessageUtils {

    private String message;

    public MessageUtils(String message) {
        if (message != null) {
            this.message = message;
        }else {
            this.message = " ";
        }
    }

    public static Component color(String message) {
        if (message == null) {
            return Component.text(" ");
        }
        return LegacyComponentSerializer.legacyAmpersand().deserialize(message);
    }

    public Component color() {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(this.message);
    }

    public String replace(String value, String replacement) {
        String message = this.message.replace(value, replacement);
        setMessage(message);
        return message;
    }

    public Long getSeconds() {
        try {
            if (message.contains("s")) {
                return Long.parseLong(message.replace("s", ""));
            }
            if (message.contains("m")) {
                long time = Long.parseLong(message.replace("m", ""));
                return time * 60;
            }
            if (message.contains("h")) {
                long time = Long.parseLong(message.replace("h", ""));
                return time * 60 * 60;
            }
        } catch (NumberFormatException exp) {
            return null;
        }
        return null;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
