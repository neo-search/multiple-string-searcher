package org.multiplestrings.trie;

/***
 * PayloadFragmentToken holds a text ("the fragment").
 * <p>
 * It does not match a search term - so its <code>isMatch</code>-method always
 * returns false. <code>getEmits</code> returns not Emits.
 * 
 * @author Daniel Beck
 *
 * @param <T> The Type of the emitted payloads.
 */
public class PayloadFragmentToken<T> extends PayloadToken<T> {

    /**
     * Constructs a token with the specified text.
     * 
     * @param fragment Text to use to construct this token.
     */
    public PayloadFragmentToken(String fragment) {
        super(fragment);
    }

    @Override
    public boolean isMatch() {
        return false;
    }

    /**
     * Returns null.
     */
    @Override
    public PayloadEmit<T> getEmit() {
        return null;
    }
}
