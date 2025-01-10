package dev.rosewood.myplugin.command.argument;

import dev.rosewood.rosegarden.command.framework.Argument;
import dev.rosewood.rosegarden.command.framework.ArgumentHandler;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.InputIterator;

import java.util.List;
import java.util.UUID;

public class ExampleArgument extends ArgumentHandler<UUID> {

    public ExampleArgument() {
        super(UUID.class);
    }

    @Override
    public UUID handle(CommandContext commandContext, Argument argument, InputIterator inputIterator) throws HandledArgumentException {
        try {
            return UUID.fromString(inputIterator.next());
        } catch (IllegalArgumentException | NullPointerException ex) {
            throw new HandledArgumentException("argument-handler-uuid");
        }
    }

    @Override
    public List<String> suggest(CommandContext commandContext, Argument argument, String[] strings) {
        return List.of(UUID.randomUUID().toString());
    }

}
