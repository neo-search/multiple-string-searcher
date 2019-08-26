package org.neosearch.stringsearcher;

import org.neosearch.stringsearcher.trie.Emit;

public interface EmitHandler<T> {
    boolean emit(Emit<T> emit);
}
