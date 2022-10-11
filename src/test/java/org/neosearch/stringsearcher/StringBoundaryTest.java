package org.neosearch.stringsearcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import java.util.Iterator;
import java.util.function.Predicate;
import org.junit.Test;

public class StringBoundaryTest {

    private final static Predicate<Character> IN_WORD_CHARACTERS =
            ch -> Character.isAlphabetic(ch) || Character.isDigit(ch) || ch == '-' || ch == '_';

    @Test
    public void testWordBoundariesForNumbers() {
        final String text = "Plida C2 / TELC C2 C3 and the programming language C.";

        StringSearcher searcher = StringSearcher.builder().addSearchString("C")
                .addSearchString("C2").setIsInWordCharacter(IN_WORD_CHARACTERS).build();
        Iterator<Emit> resultIterator = searcher.parseText(text).iterator();
        checkEmit(resultIterator.next(), 6, 7, "C2");
        checkEmit(resultIterator.next(), 16, 17, "C2");
        checkEmit(resultIterator.next(), 51, 51, "C");
        assertFalse("The iterator shouldn't have found more elements", resultIterator.hasNext());
    }

    @Test
    public void testWordBoundariesWithPunctuation() {
        StringSearcher searcher =
                StringSearcher.builder().addSearchString("MySQL").addSearchString("MariaDB")
                        .addSearchString("Database").addSearchString("Database Systems")
                        .ignoreOverlaps().setIsInWordCharacter(IN_WORD_CHARACTERS).build();
        Iterator<Emit> resultIterator =
                searcher.parseText("Database Systems: MariaDB;MySQL").iterator();
        checkEmit(resultIterator.next(), 0, 15, "Database Systems");
        checkEmit(resultIterator.next(), 18, 24, "MariaDB");
        checkEmit(resultIterator.next(), 26, 30, "MySQL");
        assertFalse("The iterator shouldn't have found more elements", resultIterator.hasNext());
    }

    @Test
    public void testWordsWithSpacesAndHyphens() {
        StringSearcher searcher = StringSearcher.builder().addSearchString("ER-Models")
                .addSearchString("Database").addSearchString("Database Systems").ignoreOverlaps()
                .setIsInWordCharacter(IN_WORD_CHARACTERS).build();
        Iterator<Emit> resultIterator =
                searcher.parseText("Knowledge of ER-Models and Database Systems:-)").iterator();
        checkEmit(resultIterator.next(), 13, 21, "ER-Models");
        checkEmit(resultIterator.next(), 27, 42, "Database Systems");
        assertFalse("The iterator shouldn't have found more elements", resultIterator.hasNext());
    }

    private void checkEmit(Emit next, int expectedStart, int expectedEnd, String expectedKeyword) {
        assertEquals("Start of emit should have been " + expectedStart, expectedStart,
                next.getStart());
        assertEquals("End of emit should have been " + expectedEnd, expectedEnd, next.getEnd());
        assertEquals("Keyword of emit shoud be " + expectedKeyword, expectedKeyword,
                next.getSearchString());
    }

}
