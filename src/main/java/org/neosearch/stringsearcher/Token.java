package org.neosearch.stringsearcher;

import org.neosearch.stringsearcher.trie.Emit;

/***
 * PayloadToken holds a text ("the fragment") an emits some output. If
 * <code>isMatch</code> returns true, the token matched a search term.
 * 
 * @author Daniel Beck
 *
 * @param <T> The type of the emitted payloads.
 */
public abstract class Token<T> {
    private String fragment;

    public Token(String fragment) {
        this.fragment = fragment;
    }

    public String getFragment() {
        return this.fragment;
    }

    /**
     * Return true if a search term matched.
     */
    public abstract boolean isMatch();

    public abstract Emit<T> getEmit();
}
