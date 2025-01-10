package dev.oribuin.chatfaq.config;

import dev.oribuin.chatfaq.ChatFAQPlugin;
import dev.rosewood.rosegarden.config.CommentedConfigurationSection;
import dev.rosewood.rosegarden.config.RoseSetting;
import dev.rosewood.rosegarden.config.RoseSettingSerializer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static dev.rosewood.rosegarden.config.RoseSettingSerializers.BOOLEAN;

public class Settings {

    private static final List<RoseSetting<?>> KEYS = new ArrayList<>();
    public static RoseSetting<Boolean> USE_COOLDOWN = create("use-cooldown", BOOLEAN, true, "Should the plugin not intervene with a question if they have already asked it within a short amount of time?");
    public static RoseSetting<Boolean> PREVENT_DUPLICATE = create("prevent-duplicate", BOOLEAN, false, "Should the plugin stop a player from spamming the same question while it is on cooldown?");

    private static <T> RoseSetting<T> create(String key, RoseSettingSerializer<T> serializer, T defaultValue, String... comments) {
        RoseSetting<T> setting = RoseSetting.backed(ChatFAQPlugin.get(), key, serializer, defaultValue, comments);
        KEYS.add(setting);
        return setting;
    }

    private static RoseSetting<CommentedConfigurationSection> create(String key, String... comments) {
        RoseSetting<CommentedConfigurationSection> setting = RoseSetting.backedSection(ChatFAQPlugin.get(), key, comments);
        KEYS.add(setting);
        return setting;
    }

    public static List<RoseSetting<?>> getKeys() {
        return Collections.unmodifiableList(KEYS);
    }

    private Settings() {
    }

}
