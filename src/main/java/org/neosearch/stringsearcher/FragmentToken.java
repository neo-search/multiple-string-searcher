package org.neosearch.stringsearcher;

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
public class FragmentToken<T> extends Token<T> {

    /**
     * Constructs a token with the specified text.
     * 
     * @param fragment Text to use to construct this token.
     */
    public FragmentToken(String fragment) {
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
    public Emit<T> getEmit() {
        return null;
    }
}
