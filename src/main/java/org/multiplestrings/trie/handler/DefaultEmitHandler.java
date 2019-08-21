package org.multiplestrings.trie.handler;

import java.util.ArrayList;
import java.util.List;

import org.multiplestrings.trie.Emit;

public class DefaultEmitHandler<T> implements StatefulEmitHandler<T> {

    private final List<Emit<T>> emits = new ArrayList<>();

    @Override
    public boolean emit(final Emit<T> emit) {
        this.emits.add(emit);
        return true;
    }

    @Override
    public List<Emit<T>> getEmits() {
        return this.emits;
    }
}
