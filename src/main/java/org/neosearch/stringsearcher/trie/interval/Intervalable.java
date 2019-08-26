package org.neosearch.stringsearcher.trie.interval;

public interface Intervalable extends Comparable {

    public int getStart();

    public int getEnd();

    public int size();

}
