package org.neosearch.stringsearcher;

import org.neosearch.stringsearcher.trie.interval.Interval;
import org.neosearch.stringsearcher.trie.interval.Intervalable;

/**
 * PayloadEmit contains a matched term and its associated payload data.
 * 
 * @param <T> Type of the wrapped payload-data.
 * 
 * @author Daniel Beck
 */
public class Emit<T> extends Interval implements Intervalable {

    private final String searchString;

    private final T payload;

    /**
     * Created a PayloadEmit
     * 
     * @param start        Start of the matched search term.
     * @param end          End of the matched search term.
     * @param searchString Keyword that matched.
     * @param payload      Emitted payload data.
     */
    public Emit(final int start, final int end, String searchString, T payload) {
        super(start, end);
        this.searchString = searchString;
        this.payload = payload;
    }

    public String getSearchString() {
        return this.searchString;
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
        return super.toString() + "=" + this.searchString + (this.payload != null ? "->" + this.payload : "");
    }
}
