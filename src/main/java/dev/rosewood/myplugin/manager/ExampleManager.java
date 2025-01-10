package dev.rosewood.myplugin.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.manager.Manager;

import java.util.List;
import java.util.UUID;

public class ExampleManager extends Manager {

    public ExampleManager(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    public void reload() {
        // startup function :D
    }

    @Override
    public void disable() {
        // goodbye function :(
    }

}
