package org.multiplestrings;

import java.util.Collection;

import org.multiplestrings.trie.Emit;
import org.multiplestrings.trie.Payload;

/**
 * Matches multiple strings from a text.
 * 
 * @author Daniel Beck
 */
public class StringsMatcher<T> {

    public StringsMatcher() {
    }

    public StringsMatcher(StringsMatcherConfig trieConfig) {
    }

    public void parseText(final CharSequence text, final EmitHandler<T> emitHandler) {
    }

    /**
     * Tokenizes a specified text and returns the emitted outputs.
     * 
     * @param text The character sequence to tokenize.
     * @return A collection of emits.
     */
    public Collection<Emit<T>> parseText(final CharSequence text) {
        return null;
    }

    /**
     * Returns true if the text contains contains one of the search terms. Else,
     * returns false.
     * 
     * @param Text Specified text.
     * @return true if the text contains one of the search terms. Else, returns
     *         false.
     */
    public boolean containsMatch(final CharSequence text) {
        return false;
    }

    /**
     * Tokenizes the specified text and returns the emitted outputs.
     * 
     * @param text The text to tokenize.
     */
    public Collection<Token<T>> tokenize(final String text) {
        return null;
    }

    private void addKeyword(String keyword, T payload) {
        // TODO Auto-generated method stub

    }

    private void addKeyword(String keyword) {
        // TODO Auto-generated method stub

    }

    private void compile() {
        // TODO Auto-generated method stub

    }

    /**
     * Builder class to create a StringMatcher instance.
     * 
     * @param <T> The type of the emitted payload.
     */

    public static class MultipleStringsMatcherBuilder<T> {
        private final StringsMatcherConfig config = new StringsMatcherConfig();

        private final StringsMatcher<T> trie = new StringsMatcher<>(config);

        private MultipleStringsMatcherBuilder() {

        }

        /**
         * Configure the Trie to ignore case when searching for keywords in the text.
         * This must be called befor public void parseText(final CharSequence text,
         * final PayloadEmitHandler<T> emitHandler) { e calling addKeyword because the
         * algorithm converts keywords to lowercase as they are added, depending on this
         * case sensitivity setting.
         *
         * @return This builder.
         */
        public MultipleStringsMatcherBuilder<T> ignoreCase() {
            this.config.setCaseInsensitive(true);
            return this;
        }

        /**
         * Configure the Trie to ignore overlapping keywords.
         *
         * @return This builder.
         */
        public MultipleStringsMatcherBuilder<T> ignoreOverlaps() {
            this.config.setAllowOverlaps(false);
            return this;
        }

        /**
         * Adds a keyword to the Trie's list of text search keywords. No Payload is
         * supplied.
         *
         * @param keyword The keyword to add to the list.
         * @return This builder.
         * @throws NullPointerException if the keyword is null.
         */
        public MultipleStringsMatcherBuilder<T> addKeyword(final String keyword) {
            this.trie.addKeyword(keyword);
            return this;
        }

        /**
         * Adds a keyword and a payload to the Trie's list of text search keywords.
         *
         * @param keyword The keyword to add to the list.
         * @return This builder.
         * @throws NullPointerException if the keyword is null.
         */
        public MultipleStringsMatcherBuilder<T> addKeyword(final String keyword, final T payload) {
            this.trie.addKeyword(keyword, payload);
            return this;
        }

        /**
         * Adds a list of keywords and payloads to the Trie's list of text search
         * keywords.
         *
         * @param keywords The keywords to add to the list.
         * @return This builder.
         */
        public MultipleStringsMatcherBuilder<T> addKeywords(final Collection<Payload<T>> keywords) {
            for (Payload<T> payload : keywords) {
                this.trie.addKeyword(payload.getKeyword(), payload.getData());
            }
            return this;
        }

        /**
         * Configure the Trie to match whole keywords in the text.
         *
         * @return This builder.
         */
        public MultipleStringsMatcherBuilder<T> onlyWholeWords() {
            this.config.setOnlyWholeWords(true);
            return this;
        }

        /**
         * Configure the Trie to match whole keywords that are separated by whitespace
         * in the text. For example, "this keyword thatkeyword" would only match the
         * first occurrence of "keyword".
         *
         * @return This builder.
         */
        public MultipleStringsMatcherBuilder<T> onlyWholeWordsWhiteSpaceSeparated() {
            this.config.setOnlyWholeWordsWhiteSpaceSeparated(true);
            return this;
        }

        /**
         * Configure the Trie to stop after the first keyword is found in the text.
         *
         * @return This builder.
         */
        public MultipleStringsMatcherBuilder<T> stopOnHit() {
            this.config.setStopOnHit(true);
            return this;
        }

        /**
         * Configure the PayloadTrie based on the builder settings.
         *
         * @return The configured PayloadTrie.
         */
        public StringsMatcher<T> build() {
            this.trie.compile();
            return this.trie;
        }

        /**
         * Configure the PayloadTrie based on the builder settings.
         *
         * @return The configured PayloadTrie.
         */
        public StringsMatcher<T> buildPayloadMatcher() {
            this.trie.compile();
            return this.trie;
        }

        /**
         * @return This builder.
         * @deprecated Use ignoreCase()
         */
        public MultipleStringsMatcherBuilder<T> caseInsensitive() {
            return ignoreCase();
        }
    }

}
