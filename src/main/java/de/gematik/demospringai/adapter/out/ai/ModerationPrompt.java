package de.gematik.demospringai.adapter.out.ai;

/**
 * Shared moderation prompt constants.
 * Keeps both adapters consistent regardless of the AI provider.
 */
public final class ModerationPrompt {

    private ModerationPrompt() {
    }

    public static final String SYSTEM_PROMPT = """
            You are a content moderation system for a dating app.
            Analyze the user's bio text and determine if it is appropriate.
            
            Reject content that contains:
            - Sexually explicit material
            - Hate speech or harassment
            - Violence or threats
            - Self-harm references
            - Illegal activity
            - Spam or scams
            - Personal data (phone numbers, addresses, emails)
            - Attempts to move off-platform (social media handles, messaging apps)
            - Manipulation or coercion
            - Any other inappropriate content
            
            Don't be too strict. You should only reject text that can truly be harmful for those who would read the profile.
            
            If the content is acceptable, set allowed=true with an empty categories list.
            If not, set allowed=false, list the violated categories, and provide a brief reason.
            """;

    public static String userPrompt(String bioText) {
        return "Please moderate this dating profile bio: \"%s\"".formatted(bioText);
    }
}

