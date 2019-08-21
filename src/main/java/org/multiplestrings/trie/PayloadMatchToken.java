package org.multiplestrings.trie;

/**
 * PayloadMatchToken holds a text ("the fragment") an emits some output.
 * <p>
 * It matches a search term - so its <code>isMatch</code>-method always returns
 * true.
 * 
 * @author Daniel Beck
 *
 * @param <T> The Type of the emitted payloads.
 */
public class PayloadMatchToken<T> extends PayloadToken<T> {

    private final PayloadEmit<T> emit;

    /**
     * Constructs a token with the specified text and the specified payload.
     * 
     * @param fragment Text to use to construct this token.
     * @param emit     Payload to use to construct this token.
     */
    public PayloadMatchToken(final String fragment, final PayloadEmit<T> emit) {
        super(fragment);
        this.emit = emit;
    }

    @Override
    public boolean isMatch() {
        return true;
    }

    @Override
    public PayloadEmit<T> getEmit() {
        return this.emit;
    }
}
