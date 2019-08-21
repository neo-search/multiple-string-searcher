package org.multiplestrings.trie;

import org.multiplestrings.trie.interval.Interval;
import org.multiplestrings.trie.interval.Intervalable;

public class Emit extends Interval implements Intervalable {
    private final String keyword;

    public Emit(final int start, final int end, String keyword) {
        super(start, end);
        this.keyword = keyword;
    }

    public String getKeyword() {
        return this.keyword;
    }

    @Override
    public String toString() {
        return super.toString() + "=" + this.keyword;
    }

}
