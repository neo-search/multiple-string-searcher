package org.neosearch.stringsearcher;

import java.util.Collection;

import org.neosearch.stringsearcher.trie.handler.StatefulEmitHandler;

/**
 * The StringSearcher algorithm main interface, with builder methods to create a
 * concrete StringSearcher instance.
 * 
 * StringSearcher contains methods to parse the text.
 * 
 * Every StringSearcher Algorithm must implement StringSearcher and
 * StringSearcherPrepare.
 * <p>
 *
 * The payload trie adds the possibility to specify emitted payloads for each
 * added keyword.
 * 
 * @author Daniel Beck
 * @param <T> The type of the supplied of the payload
 */
public interface StringSearcher<T> {

    /**
     * Tokenizes the specified text with this stringsearcher and returns matching
     * and non-matching tokens.
     * 
     * @param text The text to tokenize.
     * @return All tokens.
     */
    public Collection<Token<T>> tokenize(final String text);

    /**
     * Parses the specified text with this stringsearcher and returns the emitted
     * output for the matching strings.
     * 
     * @param text The text to tokenize.
     * @return The emitted outputs.
     */
    public Collection<Emit<T>> parseText(final CharSequence text);

    /**
     * Returns true if the text contains contains one of the search terms. Else,
     * returns false.
     * 
     * @param text Specified text.
     * @return true if the text contains one of the search terms. Else, returns
     *         false.
     */
    public boolean containsMatch(final CharSequence text);

//    /**
//     * Parses the specified text with this stringsearcher with the specified
//     * EmitHandler.
//     * 
//     * @param text        The text to tokenize.
//     * @param emitHandler EmitHandler to call for each matching string.
//     */
//    public Collection<Emit<T>> parseText(final CharSequence text, final StatefulEmitHandler<T> emitHandler);

    /**
     * Parses the specified text with this stringsearcher with the specified
     * EmitHandler.
     * 
     * @param text        The text to tokenize.
     * @param emitHandler EmitHandler to call for each matching string.
     */
    // TODO: parseText.isOnlyWords, ... lacking.
    public void parseText(final CharSequence text, final EmitHandler<T> emitHandler);

    /**
     * The first matching text sequence.
     *
     * @param text The text to search for keywords.
     * @return null if no matches found.
     */
    public Emit<T> firstMatch(final CharSequence text);

    /**
     * Builder to create a StringSearcher instance with payloads.
     * 
     * @return A StringSearcherBuilder with methods to specify payloads of the
     *         specified type.
     */
    public static <T> StringSearcherBuilder<T> builderWithPayload() {
        return new StringSearcherBuilder<>();
    }

    /**
     * Builder to create a StringSearcher instance without payloads.
     * 
     * @return StringSearcherBuilder without payloads.
     */
    public static SimpleStringSearcherBuilder builder() {
        return new SimpleStringSearcherBuilder();
    }

}