package org.neosearch.stringsearcher;

import java.util.Collection;

/***
 * To implement an StringSearcher-Algorithm, the developer must implement
 * StringSearcherPrepare and StringSearcher.
 * <p>
 * StringSearcherPrepare contains three methods to holds a text ("the fragment")
 * an emits some output. If <code>isMatch</code> returns true, the token matched
 * a search term.
 * 
 * @author Daniel Beck
 *
 * @param <T> The type of the emitted payloads.
 */
public interface StringSearcherPrepare<T> {
    public void addSearchString(String keyword, T emit);

    public void addSearchString(String keyword);

    public void addSearchStrings(final Collection<String> keywords);

    public void addSearchStrings(String... keywords);

    public StringSearcher<T> build();

}
