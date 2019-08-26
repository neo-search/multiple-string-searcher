package org.neosearch.stringsearcher;

public interface StringSearcherPrepare<T> {
    public void addKeyword(String keyword, T emit);

    public void addKeyword(String keyword);

    public StringSearcher<T> build();

}
