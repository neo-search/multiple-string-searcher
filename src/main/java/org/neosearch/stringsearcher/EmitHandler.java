package org.neosearch.stringsearcher;

public interface EmitHandler<T> {
    boolean emit(Emit<T> emit);
}
