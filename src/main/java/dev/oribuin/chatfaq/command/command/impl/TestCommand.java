package dev.oribuin.chatfaq.command.command.impl;

import dev.oribuin.chatfaq.manager.LocaleManager;
import dev.oribuin.chatfaq.manager.QuestionManager;
import dev.oribuin.chatfaq.model.Question;
import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.argument.ArgumentHandlers;
import dev.rosewood.rosegarden.command.framework.ArgumentsDefinition;
import dev.rosewood.rosegarden.command.framework.BaseRoseCommand;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.CommandInfo;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import org.bukkit.command.CommandSender;

public class TestCommand extends BaseRoseCommand {

    public TestCommand(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @RoseExecutable
    public void execute(CommandContext context, String content) {
        LocaleManager locale = this.rosePlugin.getManager(LocaleManager.class);
        CommandSender sender = context.getSender();

        Question question = this.rosePlugin.getManager(QuestionManager.class).getMatch(content);
        if (question == null) {
            locale.sendMessage(sender, "command-test-invalid");
            return;
        }

        locale.sendMessages(sender, "command-test-found", question.placeholders());
    }

    @Override
    protected CommandInfo createCommandInfo() {
        return CommandInfo.builder("test")
                .descriptionKey("command-test-description")
                .permission("chatfaq.test")
                .arguments(ArgumentsDefinition.of("content", ArgumentHandlers.GREEDY_STRING))
                .playerOnly(false)
                .build();
    }
}
