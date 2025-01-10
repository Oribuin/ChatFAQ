package dev.oribuin.chatfaq.model;

import dev.oribuin.chatfaq.ChatFAQPlugin;
import dev.oribuin.chatfaq.manager.LocaleManager;
import dev.rosewood.rosegarden.config.CommentedConfigurationSection;
import dev.rosewood.rosegarden.utils.StringPlaceholders;

import java.util.Arrays;
import java.util.List;

/**
 * Construct a faq question that will check if a specified input closely matches a question a player has
 *
 * @param query    The question to check
 * @param answer   The accuracy of the question
 * @param accuracy How precise should the checking be, higher accuracy = more precise
 */
public record Question(String query, List<String> answer, double accuracy) {

    /**
     * Construct a question from a configuration section
     *
     * @param section The section to load a question from
     * @return The question if loaded, otherwise null
     */
    public static Question from(CommentedConfigurationSection section) {
        if (section == null) return null;

        String input = section.getString("question");
        List<String> answer = LocaleManager.asStringList(section.get("answer"));
        double accuracy = section.getDouble("accuracy", 70);

        // Check if input is null
        if (input == null) {
            ChatFAQPlugin.get().getLogger().info("Config Error: Could not find 'question' for a question");
            return null;
        }

        // Check if answer is null
        if (answer.isEmpty()) {
            ChatFAQPlugin.get().getLogger().info("Config error: Answer for question \"" + input + "\" does not exist.");
            return null;
        }

        return new Question(input, answer, accuracy);
    }

    /**
     * Check if the input matches this question within the set accuracy %
     *
     * @param input What the player question matches
     * @return true if the strings match
     */
    public boolean matches(String input) {
        // if the accuracy is 100% don't bother with extra checks
        if (this.accuracy >= 100) return input.equalsIgnoreCase(this.query);

        // check the accuracy of the text to see if they somewhat match
        String primary = input.toLowerCase().replace("'", "");
        String secondary = this.query.toLowerCase().replace("'", "");
        int max = Math.max(primary.length(), secondary.length());
        if (max == 0) return false;

        int distance = calculate(primary, secondary);
        return ((max - (double) distance) / max) > this.accuracy / 100;
    }

    public StringPlaceholders placeholders() {
        return StringPlaceholders.of(
                "question", this.query,
                "answer", String.join("\n", this.answer),
                "accuracy", this.accuracy
        );
    }

    /**
     * Levenshtein distance between two strings
     *
     * @param primary   The first string
     * @param secondary The secondary string
     * @return the difference
     */
    private static int calculate(String primary, String secondary) {
        int[][] dp = new int[primary.length() + 1][secondary.length() + 1];

        for (int i = 0; i <= primary.length(); i++) {
            for (int j = 0; j <= secondary.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = minimumNumber(
                            dp[i - 1][j - 1] + cost(primary.charAt(i - 1), secondary.charAt(j - 1)),
                            dp[i - 1][j] + 1,
                            dp[i][j - 1] + 1
                    );
                }
            }
        }

        return dp[primary.length()][secondary.length()];
    }

    /**
     * Gets the minimum number in the array or max number
     *
     * @param numbers The numbers to check
     * @return the minimum number
     */
    private static int minimumNumber(int... numbers) {
        return Arrays.stream(numbers).min().orElse(Integer.MAX_VALUE);
    }

    private static int cost(char primary, char secondary) {
        return primary == secondary ? 0 : 1;
    }

}
