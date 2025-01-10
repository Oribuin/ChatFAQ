package dev.rosewood.myplugin;

import dev.rosewood.myplugin.config.SettingKey;
import dev.rosewood.myplugin.manager.CommandManager;
import dev.rosewood.myplugin.manager.DataManager;
import dev.rosewood.myplugin.manager.ExampleManager;
import dev.rosewood.myplugin.manager.LocaleManager;
import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.RoseSetting;
import dev.rosewood.rosegarden.manager.Manager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MyPlugin extends RosePlugin {

    private static MyPlugin instance;

    public static MyPlugin getInstance() {
        return instance;
    }

    public MyPlugin() {
        super(-1, -1,
                DataManager.class,
                LocaleManager.class,
                CommandManager.class
        );

        instance = this;
    }

    @Override
    public void enable() {
    }

    @Override
    public void disable() {
    }

    @Override
    protected @NotNull List<Class<? extends Manager>> getManagerLoadPriority() {
        return List.of(ExampleManager.class);
    }

    @Override
    protected @NotNull List<RoseSetting<?>> getRoseConfigSettings() {
        return SettingKey.getKeys();
    }
}
