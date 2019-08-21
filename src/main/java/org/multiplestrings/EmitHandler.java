package org.multiplestrings;

import org.multiplestrings.trie.Emit;

public interface EmitHandler<T> {
    boolean emit(Emit<T> emit);
}
