package dev.oribuin.chatfaq.listener;

import dev.oribuin.chatfaq.ChatFAQPlugin;
import dev.oribuin.chatfaq.config.Settings;
import dev.oribuin.chatfaq.manager.LocaleManager;
import dev.oribuin.chatfaq.manager.QuestionManager;
import dev.oribuin.chatfaq.model.Question;
import dev.rosewood.rosegarden.utils.NMSUtil;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@SuppressWarnings("deprecation")
public class ChatListener implements Listener {

    private final ChatFAQPlugin plugin;

    public ChatListener(ChatFAQPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onChat(AsyncPlayerChatEvent event) {
        String content = ChatColor.stripColor(event.getMessage());

        // don't do anything if the person can just bypass it
        if (event.getPlayer().hasPermission("chatfaq.bypass")) return;

        QuestionManager manager = this.plugin.getManager(QuestionManager.class);
        LocaleManager locale = this.plugin.getManager(LocaleManager.class);
        Question question = manager.getMatch(content);
        if (question == null) return;

        if (Settings.USE_COOLDOWN.get()) {
            // Don't intercept the message if the player has already asked it before
            if (manager.hasAskedRecently(event.getPlayer().getUniqueId(), question)) {
                if (Settings.PREVENT_DUPLICATE.get()) {
                    event.setCancelled(true);
                    locale.sendMessages(event.getPlayer(), "question-answer-duplicate", question.placeholders());
                    return;
                }
                
                return; // don't do anything if they've asked recently and it wasn't the right answer
            }

            // mark the player as asking it recently
            manager.markAsRecent(event.getPlayer().getUniqueId(), question);
        }

        // tell the player the answer to the question
        event.setCancelled(true);
        locale.sendMessages(event.getPlayer(), "question-answer-found", question.placeholders());
    }

}
