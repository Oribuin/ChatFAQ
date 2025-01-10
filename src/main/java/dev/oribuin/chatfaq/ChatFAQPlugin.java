package dev.oribuin.chatfaq;

import dev.oribuin.chatfaq.config.Settings;
import dev.oribuin.chatfaq.listener.ChatListener;
import dev.oribuin.chatfaq.manager.CommandManager;
import dev.oribuin.chatfaq.manager.LocaleManager;
import dev.oribuin.chatfaq.manager.QuestionManager;
import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.RoseSetting;
import dev.rosewood.rosegarden.manager.Manager;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChatFAQPlugin extends RosePlugin {

    private static ChatFAQPlugin instance;

    public static ChatFAQPlugin get() {
        return instance;
    }

    public ChatFAQPlugin() {
        super(-1, -1,
                null,
                LocaleManager.class,
                CommandManager.class
        );

        instance = this;
    }

    @Override
    public void enable() {
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new ChatListener(this), this);
    }

    @Override
    public void disable() {
    }

    @Override
    protected @NotNull List<Class<? extends Manager>> getManagerLoadPriority() {
        return List.of(QuestionManager.class);
    }

    @Override
    protected @NotNull List<RoseSetting<?>> getRoseConfigSettings() {
        return Settings.getKeys();
    }

    @Override
    protected @NotNull String[] getRoseConfigHeader() {
        return new String[]{
                "   _____ _           _     ______      ____      ",
                "  / ____| |         | |   |  ____/\\   / __ \\     ",
                " | |    | |__   __ _| |_  | |__ /  \\ | |  | |___ ",
                " | |    | '_ \\ / _` | __| |  __/ /\\ \\| |  | / __|",
                " | |____| | | | (_| | |_  | | / ____ \\ |__| \\__ \\",
                "  \\_____|_| |_|\\__,_|\\__| |_|/_/    \\_\\___\\_\\___/"
        };
    }
}
