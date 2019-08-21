package org.multiplestrings.trie.handler;

import org.multiplestrings.trie.Emit;
import org.multiplestrings.trie.PayloadEmit;

/**
 * Convenience wrapper class that delegates every method to a EmitHandler.
 */
public class PayloadEmitDelegateHandler implements PayloadEmitHandler<String> {

    private EmitHandler handler;

    public PayloadEmitDelegateHandler(EmitHandler handler) {
        this.handler = handler;

    }

    @Override
    public boolean emit(PayloadEmit<String> emit) {
        Emit newEmit = new Emit(emit.getStart(), emit.getEnd(), emit.getKeyword());
        return handler.emit(newEmit);
    }

}
