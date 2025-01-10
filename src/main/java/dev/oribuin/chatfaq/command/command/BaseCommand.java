package dev.oribuin.chatfaq.command.command;

import dev.oribuin.chatfaq.command.command.impl.TestCommand;
import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.HelpCommand;
import dev.rosewood.rosegarden.command.ReloadCommand;
import dev.rosewood.rosegarden.command.framework.ArgumentsDefinition;
import dev.rosewood.rosegarden.command.framework.BaseRoseCommand;
import dev.rosewood.rosegarden.command.framework.CommandInfo;

public class BaseCommand extends BaseRoseCommand {

    public BaseCommand(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    protected CommandInfo createCommandInfo() {
        return CommandInfo.builder("questions")
                .permission("chatfaq.basecommand")
                .aliases("faqs", "chatfaqs")
                .playerOnly(false)
                .arguments(this.createArguments())
                .build();
    }

    private ArgumentsDefinition createArguments() {
        return ArgumentsDefinition.builder().requiredSub(
                new TestCommand(this.rosePlugin),
                new ReloadCommand(this.rosePlugin),
                new HelpCommand(this.rosePlugin, this)
        );
    }


}
