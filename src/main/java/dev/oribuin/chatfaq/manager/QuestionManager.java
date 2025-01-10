package dev.oribuin.chatfaq.manager;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import dev.oribuin.chatfaq.ChatFAQPlugin;
import dev.oribuin.chatfaq.model.Question;
import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.CommentedConfigurationSection;
import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import dev.rosewood.rosegarden.manager.Manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class QuestionManager extends Manager {

    private static final File QUESTION_FILE = new File(ChatFAQPlugin.get().getDataFolder(), "questions.yml");
    private final List<Question> questions = new ArrayList<>();
    private final Cache<UUID, Question> recentlyAsked = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    public QuestionManager(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    public void reload() {
        this.questions.clear();

        if (!QUESTION_FILE.exists()) {
            this.rosePlugin.saveResource("questions.yml", false);
        }

        CommentedFileConfiguration config = CommentedFileConfiguration.loadConfiguration(QUESTION_FILE);
        CommentedConfigurationSection section = config.getConfigurationSection("questions");
        if (section == null || section.getKeys(false).isEmpty()) {
            this.rosePlugin.getLogger().warning("There are no questions to be loaded in with the plugin. Check your questions.yml file");
            return;
        }

        // Load all the questions into the plugin
        section.getKeys(false).forEach(s -> {
            Question question = Question.from(section.getConfigurationSection(s));
            if (question == null) return;

            this.questions.add(question);
        });

        this.rosePlugin.getLogger().info("Loaded " + this.questions.size() + " questions into the plugin.");
    }

    /**
     * Check if a message matches any of the questions in the config
     *
     * @param input The question to check
     * @return The question that was matched if found, null otherwise
     */
    public Question getMatch(String input) {
        if (this.questions.isEmpty()) return null;

        return this.questions.stream()
                .filter(x -> x.matches(input))
                .findFirst()
                .orElse(null);
    }

    /**
     * Check if a player has asked the same question recently.
     *
     * @param player   The player who asked the question
     * @param question The question to ask
     * @return if the player asked it already
     */
    public boolean hasAskedRecently(UUID player, Question question) {
        Question recent = this.recentlyAsked.getIfPresent(player);
        if (recent == null) return false;

        return recent.equals(question);
    }

    /**
     * Check if a player has asked any question recently.
     *
     * @param player The player who asked the question
     * @return true if they have asked a question in the past 5 minutes
     */
    public boolean hasAskedRecently(UUID player) {
        return this.recentlyAsked.getIfPresent(player) != null;
    }

    /**
     * Mark a question for the player as having been asked recently
     *
     * @param player   The player to check
     * @param question The question to ask
     */
    public void markAsRecent(UUID player, Question question) {
        this.recentlyAsked.put(player, question);
    }

    @Override
    public void disable() {
        this.questions.clear();
    }

}
