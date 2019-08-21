package org.multiplestrings.trie;

import org.junit.Test;
import org.multiplestrings.trie.Emit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;

public class EmitTest {

    @Test
    public void equals() {
        Emit one = new Emit(13, 42, null);
        Emit two = new Emit(13, 42, null);
        assertEquals(one, two);
    }

    @Test
    public void notEquals() {
        Emit one = new Emit(13, 42, null);
        Emit two = new Emit(13, 43, null);
        assertNotSame(one, two);
    }

}
