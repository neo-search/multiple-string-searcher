package org.neosearch.stringsearcher;

import java.util.Collection;

import org.neosearch.stringsearcher.trie.Emit;
import org.neosearch.stringsearcher.trie.handler.StatefulEmitHandler;

public interface StringSearcher<T> {
    public Collection<Token<T>> tokenize(final String text);

    public Collection<Emit<T>> parseText(final CharSequence text);

    public Collection<Emit<T>> parseText(final CharSequence text, final StatefulEmitHandler<T> emitHandler);

    public boolean containsMatch(final CharSequence text);

    public void parseText(final CharSequence text, final EmitHandler<T> emitHandler);

    public Emit<T> firstMatch(final CharSequence text);

    public static <T> StringSearcherBuilder<T> builderWithPayload() {
        return new StringSearcherBuilder<>();
    }

    public static SimpleStringSearcherBuilder builder() {
        return new SimpleStringSearcherBuilder();
    }

}