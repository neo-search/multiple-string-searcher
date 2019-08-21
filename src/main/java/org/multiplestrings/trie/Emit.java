package org.multiplestrings.trie;

import org.multiplestrings.trie.interval.Interval;
import org.multiplestrings.trie.interval.Intervalable;

/**
 * PayloadEmit contains a matched term and its associated payload data.
 * 
 * @param <T> Type of the wrapped payload-data.
 * 
 * @author Daniel Beck
 */
public class Emit<T> extends Interval implements Intervalable {

    private final String keyword;

    private final T payload;

    /**
     * Created a PayloadEmit
     * 
     * @param start   Start of the matched search term.
     * @param end     End of the matched search term.
     * @param keyword Keyword that matched.
     * @param payload Emitted payload data.
     */
    public Emit(final int start, final int end, String keyword, T payload) {
        super(start, end);
        this.keyword = keyword;
        this.payload = payload;
    }

    public String getKeyword() {
        return this.keyword;
    }

    /**
     * Returns the payload associated to this emit.
     * 
     * @return the associated payload
     */
    public T getPayload() {
        return this.payload;
    }

    @Override
    public String toString() {
        return super.toString() + "=" + this.keyword + (this.payload != null ? "->" + this.payload : "");
    }
}
