package org.neosearch.stringsearcher;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;

import org.neosearch.stringsearcher.trie.Trie;

/**
 * Builder class to create a StringMatcher instance. The builder is can provide
 * a stringsearcher implementation for serval algorithm. For the moment, only
 * "naive aho coharick" is available.
 * <ul>
 * <li>AHO_COHARICK
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

    private Queue<Entry<String, T>> stringsearchPayloads = new LinkedList<>();

    private Algorithm algorithm;

    /**
     * Creates a string searcher builder. It defaults to the AHO_COHARICK string
     * matching algorithm. The defaut algorithm can be overriden with
     * <code>algorithm()</code>.
     */
    public StringSearcherBuilder() {
        this.algorithm = Algorithm.AHO_COHARICK;
    }

    /**
     * Sets the string searching implementation to be used.
     * 
     * @param algorithm Algorithm to use.
     * @return This builder.
     */
    public StringSearcherBuilder<T> algorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    /**
     * Configure the StringSearcher to ignore case when searching for keywords in
     * the text. This must be called before calling addSearchString because the
     * algorithm converts keywords to lowercase as they are added, depending on this
     * case sensitivity setting.
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
     * Adds a search string to the StringSearchers list of text search keywords. No
     * payload is supplied.
     *
     * @param searchString The search string to add to the list.
     * @return This builder.
     * @throws NullPointerException if the keyword is null.
     */
    public StringSearcherBuilder<T> addSearchString(final String searchString) {
        addSearchStringImpl(searchString, null);
        return this;
    }

    /**
     * Adds an array of search strings without associated payloads to the
     * StringSearcher.
     * 
     * @param searchStrings Search String to add to the StringSearcher.
     * @return This builder.
     */
    public StringSearcherBuilder<T> addSearchStrings(final String... searchStrings) {
        for (String string : searchStrings)
            addSearchStringImpl(string, null);

        return this;
    }

    /**
     * Adds a collections of search strings without associated payloads to the
     * StringSearcher.
     * 
     * @param searchStrings Search String to add to the StringSearcher.
     * @return This builder.
     */
    public StringSearcherBuilder<T> addSearchStrings(final Collection<String> searchStrings) {
        for (String string : searchStrings)
            addSearchStringImpl(string, null);

        return this;
    }

    /**
     * Adds a keyword and a payload to the StringSeacher' list of text search
     * keywords.
     *
     * @param searchString The keyword to add to the list.
     * @return This builder.
     * @throws NullPointerException if the keyword is null.
     */
    public StringSearcherBuilder<T> addSearchString(final String searchString, final T payload) {
        addSearchStringImpl(searchString, payload);
        return this;
    }

    private void addSearchStringImpl(final String searchString, T payload) {
        this.stringsearchPayloads.add(new SimpleEntry<>(searchString, payload));
    }

    /**
     * Adds a keyword and a payload to the Trie's list of text search keywords.
     *
     * @param entry The keyword and its payload to add to the list.
     * @return This builder.
     * @throws NullPointerException if the keyword is null.
     */
    public StringSearcherBuilder<T> addSearchString(final Entry<String, T> entry) {
        this.stringsearchPayloads.add(entry);
        return this;
    }

    /**
     * Configure the StringSearcher to match whole keywords in the text.
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
     * Constructs a StringSearcher based on the builder settings.
     *
     * @return The configured StringSearcher.
     */
    public StringSearcher<T> build() {
        if (this.algorithm == Algorithm.AHO_COHARICK) {
            this.stringMatcher = new Trie<T>(this.config);
            Entry<String, T> simpleEntry = null;
            while ((simpleEntry = stringsearchPayloads.poll()) != null)
                stringMatcher.addSearchString(simpleEntry.getKey(), simpleEntry.getValue());

            return this.stringMatcher.build();
        }
        return null;
    }
}