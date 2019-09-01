package org.neosearch.stringsearcher;

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
public class MatchToken<T> extends Token<T> {

    private final Emit<T> emit;

    /**
     * Constructs a token with the specified text and the specified payload.
     * 
     * @param fragment Text to use to construct this token.
     * @param emit     Payload to use to construct this token.
     */
    public MatchToken(final String fragment, final Emit<T> emit) {
        super(fragment);
        this.emit = emit;
    }

    @Override
    public boolean isMatch() {
        return true;
    }

    @Override
    public Emit<T> getEmit() {
        return this.emit;
    }
}
