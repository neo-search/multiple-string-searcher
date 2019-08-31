package org.neosearch.stringsearcher;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import org.neosearch.stringsearcher.trie.Trie;

/**
 * Builder class to create a StringMatcher instance. Several algorithms can be
 * chosen from:
 * <ul>
 * <li>AHO_COHARICK
 * <li>AHO_COHARICK_IMPROVED
 * <li>COMPRESSED_TRIE
 * </ul>
 * 
 * The <code>build()</code>-method creates a concret instance of the chosen
 * StringMatcher.
 * 
 * @param <T> The type of the emitted payload.
 */

public class StringSearcherBuilder<T> {

    private final StringSearcherConfig config = new StringSearcherConfig();

    private StringSearcherPrepare<T> stringMatcher;

    private Algorithm algorithm;
    private Queue<SimpleEntry<String, T>> keyPayloads = new LinkedList<>();

    public StringSearcherBuilder() {
        this.algorithm = Algorithm.AHO_COHARICK;
    }

    public StringSearcherBuilder<T> algorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    /**
     * Configure the Trie to ignore case when searching for keywords in the text.
     * This must be called before calling addKeyword because the algorithm converts
     * keywords to lowercase as they are added, depending on this case sensitivity
     * setting.
     *
     * @return This builder.
     */
    public StringSearcherBuilder<T> ignoreCase() {
        this.config.setCaseInsensitive(true);
        return this;
    }

    /**
     * Configure the Trie to ignore overlapping keywords.
     *
     * @return This builder.
     */
    public StringSearcherBuilder<T> ignoreOverlaps() {
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
    public StringSearcherBuilder<T> addSearchString(final String keyword) {
        this.keyPayloads.add(new SimpleEntry<>(keyword, null));
        return this;
    }

    public StringSearcherBuilder<T> addSearchStrings(final String... keywords) {
        for (String string : keywords) {
            this.keyPayloads.add(new SimpleEntry<>(string, null));
        }
        return this;
    }

    public StringSearcherBuilder<T> addSearchStrings(final Collection<String> keywords) {
        for (String string : keywords) {
            this.keyPayloads.add(new SimpleEntry<>(string, null));
        }
        return this;
    }

    /**
     * Adds a keyword and a payload to the Trie's list of text search keywords.
     *
     * @param keyword The keyword to add to the list.
     * @return This builder.
     * @throws NullPointerException if the keyword is null.
     */
    public StringSearcherBuilder<T> addSearchString(final String keyword, final T payload) {
        this.keyPayloads.add(new SimpleEntry<>(keyword, payload));
        return this;
    }

    /**
     * Configure the Trie to match whole keywords in the text.
     *
     * @return This builder.
     */
    public StringSearcherBuilder<T> onlyWholeWords() {
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
    public StringSearcherBuilder<T> onlyWholeWordsWhiteSpaceSeparated() {
        this.config.setOnlyWholeWordsWhiteSpaceSeparated(true);
        return this;
    }

    /**
     * Configure the Trie to stop after the first keyword is found in the text.
     *
     * @return This builder.
     */
    public StringSearcherBuilder<T> stopOnHit() {
        this.config.setStopOnHit(true);
        return this;
    }

    /**
     * Configure the PayloadTrie based on the builder settings.
     *
     * @return The configured PayloadTrie.
     */
    public StringSearcher<T> build() {
        if (this.algorithm == Algorithm.AHO_COHARICK) {
            this.stringMatcher = new Trie<T>(this.config);
            SimpleEntry<String, T> simpleEntry = null;
            while ((simpleEntry = keyPayloads.poll()) != null)
                stringMatcher.addSearchString(simpleEntry.getKey(), simpleEntry.getValue());

            return this.stringMatcher.build();
        }
        return null;
    }
}