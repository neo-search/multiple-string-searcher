package org.neosearch.stringsearcher;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;
import org.neosearch.stringsearcher.trie.Emit;
import org.neosearch.stringsearcher.trie.handler.AbstractStatefulEmitHandler;
import org.neosearch.stringsearcher.trie.handler.StatefulEmitHandler;

public class StringSearcherTest {

    private final static String[] ALPHABET = new String[] { "abc", "bcd", "cde" };
    private final static String[] ALPHABET_PAYLOAD = new String[] { "alpha:abc", "alpha:bcd", "alpha:cde" };
    private final static String[] PRONOUNS = new String[] { "hers", "his", "she", "he" };
    private final static int[] PRONOUNS_PAYLOAD_ID = new int[] { 9, 12, 4, 20 };
    private final static String[] FOOD = new String[] { "veal", "cauliflower", "broccoli", "tomatoes" };
    private final static Food[] FOOD_PAYLOAD = new Food[] { new Food("veal"), new Food("cauliflower"), new Food("broccoli"),
            new Food("tomatoes") };

    private final static String[] GREEK_LETTERS = new String[] { "Alpha", "Beta", "Gamma" };
    private final static String[] GREEK_LETTERS_PAYLOAD = new String[] { "greek:Alpha", "greek:Beta", "greek:Gamma" };

    private final static String[] UNICODE = new String[] { "turning", "once", "again", "börkü" };
    private final static String[] UNICODE_PAYLOAD = new String[] { "uni:turning", "uni:once", "uni:again", "uni:börkü" };

    public static class Food {
        private final String name;

        public Food(String name) {
            this.name = name;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Food other = (Food) obj;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            return true;
        }
    }

    private StringSearcherBuilder<Integer> pronounsStringSearchBuilder() {
        return StringSearcher.<Integer>builderWithPayload()//
                .addSearchString(PRONOUNS[0], PRONOUNS_PAYLOAD_ID[0])//
                .addSearchString(PRONOUNS[1], PRONOUNS_PAYLOAD_ID[1])//
                .addSearchString(PRONOUNS[2], PRONOUNS_PAYLOAD_ID[2])//
                .addSearchString(PRONOUNS[3], PRONOUNS_PAYLOAD_ID[3]);
    }

    private StringSearcherBuilder<Food> foodStringSearchBuilder() {
        return StringSearcher.<Food>builderWithPayload()//
                .addSearchString(FOOD[0], FOOD_PAYLOAD[0])//
                .addSearchString(FOOD[1], FOOD_PAYLOAD[1])//
                .addSearchString(FOOD[2], FOOD_PAYLOAD[2])//
                .addSearchString(FOOD[3], FOOD_PAYLOAD[3]);
    }

    private StringSearcherBuilder<String> unicodeStringSearcherBuilder() {
        return StringSearcher.<String>builderWithPayload().ignoreCase().onlyWholeWords()
                .addSearchString(UNICODE[0], UNICODE_PAYLOAD[0])//
                .addSearchString(UNICODE[1], UNICODE_PAYLOAD[1])//
                .addSearchString(UNICODE[2], UNICODE_PAYLOAD[2])//
                .addSearchString(UNICODE[3], UNICODE_PAYLOAD[3]);
    }

    private StringSearcherBuilder<String> greekLettersStringSearcherBuilder() {
        return StringSearcher.<String>builderWithPayload()//
                .addSearchString(GREEK_LETTERS[0], GREEK_LETTERS_PAYLOAD[0])//
                .addSearchString(GREEK_LETTERS[1], GREEK_LETTERS_PAYLOAD[1])//
                .addSearchString(GREEK_LETTERS[2], GREEK_LETTERS_PAYLOAD[2]);
    }

    private StringSearcherBuilder<String> alphabetStringSearcherBuilder() {
        return StringSearcher.<String>builderWithPayload()//
                .addSearchString(ALPHABET[0], ALPHABET_PAYLOAD[0])//
                .addSearchString(ALPHABET[1], ALPHABET_PAYLOAD[1])//
                .addSearchString(ALPHABET[2], ALPHABET_PAYLOAD[2]);
    }

    @Test
    public void keywordAndTextAreTheSame() {
        StringSearcher<String> trie = StringSearcher.<String>builderWithPayload()
                .addSearchString(ALPHABET[0], ALPHABET_PAYLOAD[0]).build();
        Collection<Emit<String>> emits = trie.parseText(ALPHABET[0]);
        Iterator<Emit<String>> iterator = emits.iterator();
        checkEmit(iterator.next(), 0, 2, ALPHABET[0], ALPHABET_PAYLOAD[0]);
    }

    @Test
    public void keywordAndTextAreTheSameFirstMatch() {
        StringSearcher<String> trie = StringSearcher.<String>builderWithPayload()
                .addSearchString(ALPHABET[0], ALPHABET_PAYLOAD[0]).build();
        Emit<String> firstMatch = trie.firstMatch(ALPHABET[0]);
        checkEmit(firstMatch, 0, 2, ALPHABET[0], ALPHABET_PAYLOAD[0]);
    }

    @Test
    public void textIsLongerThanKeyword() {
        StringSearcher<String> trie = StringSearcher.<String>builderWithPayload()
                .addSearchString(ALPHABET[0], ALPHABET_PAYLOAD[0]).build();
        Collection<Emit<String>> emits = trie.parseText(" " + ALPHABET[0]);
        Iterator<Emit<String>> iterator = emits.iterator();
        checkEmit(iterator.next(), 1, 3, ALPHABET[0], ALPHABET_PAYLOAD[0]);
    }

    @Test
    public void textIsLongerThanKeywordFirstMatch() {

        StringSearcher<String> trie = StringSearcher.<String>builderWithPayload()
                .addSearchString(ALPHABET[0], ALPHABET_PAYLOAD[0]).build();
        Emit<String> firstMatch = trie.firstMatch(" " + ALPHABET[0]);
        checkEmit(firstMatch, 1, 3, ALPHABET[0], ALPHABET_PAYLOAD[0]);
    }

    @Test
    public void variousKeywordsOneMatch() {
        StringSearcher<String> trie = alphabetStringSearcherBuilder().build();
        Collection<Emit<String>> emits = trie.parseText("bcd");
        Iterator<Emit<String>> iterator = emits.iterator();
        checkEmit(iterator.next(), 0, 2, "bcd", "alpha:bcd");
    }

    @Test
    public void variousKeywordsFirstMatch() {
        StringSearcher<String> trie = alphabetStringSearcherBuilder().build();
        Emit<String> firstMatch = trie.firstMatch("bcd");
        checkEmit(firstMatch, 0, 2, "bcd", "alpha:bcd");
    }

    @Test
    public void ushersTestAndStopOnHit() {
        StringSearcher<Integer> searcher = pronounsStringSearchBuilder().stopOnHit().build();
        Collection<Emit<Integer>> emits = searcher.parseText("ushers");
        assertEquals(1, emits.size()); // she @ 3, he @ 3, hers @ 5
        Iterator<Emit<Integer>> iterator = emits.iterator();
        checkEmit(iterator.next(), 2, 3, "he", 20);
    }

    @Test
    public void ushersTestStopOnHitSkipOne() {
        StringSearcher<Integer> searcher = pronounsStringSearchBuilder().stopOnHit().build();

        StatefulEmitHandler<Integer> testEmitHandler = new AbstractStatefulEmitHandler<Integer>() {
            boolean first = true;

            @Override
            public boolean emit(final Emit<Integer> emit) {
                if (first) {
                    // return false for the first element
                    first = false;
                    return false;
                }
                addEmit(emit);
                return true;
            }

        };

        searcher.parseText("ushers", testEmitHandler);
        Collection<Emit<Integer>> emits = testEmitHandler.getEmits();
        assertEquals(1, emits.size()); // she @ 3, he @ 3, hers @ 5
        Iterator<Emit<Integer>> iterator = emits.iterator();
        checkEmit(iterator.next(), 1, 3, "she", 4);
    }

    @Test
    public void ushersTest() {
        StringSearcher<Integer> searcher = pronounsStringSearchBuilder().build();
        Collection<Emit<Integer>> emits = searcher.parseText("ushers");
        assertEquals(3, emits.size()); // she @ 3, he @ 3, hers @ 5
        Iterator<Emit<Integer>> iterator = emits.iterator();

        checkEmit(iterator.next(), 2, 3, "he", 20);
        checkEmit(iterator.next(), 1, 3, "she", 4);
        checkEmit(iterator.next(), 2, 5, "hers", 9);
    }

    @Test
    public void ushersTestWithCapitalKeywords() {
        StringSearcher<String> trie = StringSearcher.<String>builderWithPayload().ignoreCase()//
                .addSearchString("HERS", "hers")//
                .addSearchString("HIS", "his")//
                .addSearchString("SHE", "she")//
                .addSearchString("HE", "he")//
                .build();
        Collection<Emit<String>> emits = trie.parseText("ushers");
        assertEquals(3, emits.size()); // she @ 3, he @ 3, hers @ 5
        Iterator<Emit<String>> iterator = emits.iterator();
        checkEmit(iterator.next(), 2, 3, "he", "he");
        checkEmit(iterator.next(), 1, 3, "she", "she");
        checkEmit(iterator.next(), 2, 5, "hers", "hers");
    }

    @Test
    public void ushersTestFirstMatch() {
        StringSearcher<Integer> stringSearcher = pronounsStringSearchBuilder().build();
        Emit<Integer> firstMatch = stringSearcher.firstMatch("ushers");
        checkEmit(firstMatch, 2, 3, "he", 20);
    }

    @Test
    public void ushersTestByCallback() {
        StringSearcher<Integer> stringSearcher = pronounsStringSearchBuilder().build();
        final List<Emit<Integer>> emits = new ArrayList<>();
        EmitHandler<Integer> emitHandler = new EmitHandler<Integer>() {

            @Override
            public boolean emit(Emit<Integer> emit) {
                emits.add(emit);
                return true;
            }
        };
        stringSearcher.parseText("ushers", emitHandler);
        assertEquals(3, emits.size()); // she @ 3, he @ 3, hers @ 5
        Iterator<Emit<Integer>> iterator = emits.iterator();

        checkEmit(iterator.next(), 2, 3, "he", 20);
        checkEmit(iterator.next(), 1, 3, "she", 4);
        checkEmit(iterator.next(), 2, 5, "hers", 9);
    }

    @Test
    public void misleadingTest() {
        StringSearcher<String> trie = StringSearcher.<String>builderWithPayload().addSearchString("hers", "pronon:hers").build();
        Collection<Emit<String>> emits = trie.parseText("h he her hers");
        Iterator<Emit<String>> iterator = emits.iterator();
        checkEmit(iterator.next(), 9, 12, "hers", "pronon:hers");
    }

    @Test
    public void misleadingTestFirstMatch() {
        StringSearcher<String> trie = StringSearcher.<String>builderWithPayload().addSearchString("hers", "pronon:hers").build();
        Emit<String> firstMatch = trie.firstMatch("h he her hers");
        checkEmit(firstMatch, 9, 12, "hers", "pronon:hers");
    }

    @Test
    public void recipes() {
        StringSearcher<Food> trie = foodStringSearchBuilder().build();
        Collection<Emit<Food>> emits = trie.parseText("2 cauliflowers, 3 tomatoes, 4 slices of veal, 100g broccoli");
        Iterator<Emit<Food>> iterator = emits.iterator();
        checkEmit(iterator.next(), 2, 12, "cauliflower", new Food("cauliflower"));
        checkEmit(iterator.next(), 18, 25, "tomatoes", new Food("tomatoes"));
        checkEmit(iterator.next(), 40, 43, "veal", new Food("veal"));
        checkEmit(iterator.next(), 51, 58, "broccoli", new Food("broccoli"));
    }

    @Test
    public void recipesFirstMatch() {
        StringSearcher<Food> trie = foodStringSearchBuilder().build();
        Emit<Food> firstMatch = trie.firstMatch("2 cauliflowers, 3 tomatoes, 4 slices of veal, 100g broccoli");
        checkEmit(firstMatch, 2, 12, "cauliflower", new Food("cauliflower"));
    }

    @Test
    public void longAndShortOverlappingMatch() {
        StringSearcher<String> trie = StringSearcher.<String>builderWithPayload().addSearchString("he", "pronon:he")
                .addSearchString("hehehehe", "garbage").build();
        Collection<Emit<String>> emits = trie.parseText("hehehehehe");
        Iterator<Emit<String>> iterator = emits.iterator();
        checkEmit(iterator.next(), 0, 1, "he", "pronon:he");
        checkEmit(iterator.next(), 2, 3, "he", "pronon:he");
        checkEmit(iterator.next(), 4, 5, "he", "pronon:he");
        checkEmit(iterator.next(), 6, 7, "he", "pronon:he");
        checkEmit(iterator.next(), 0, 7, "hehehehe", "garbage");
        checkEmit(iterator.next(), 8, 9, "he", "pronon:he");
        checkEmit(iterator.next(), 2, 9, "hehehehe", "garbage");
    }

    @Test
    public void nonOverlapping() {
        StringSearcher<String> trie = StringSearcher.<String>builderWithPayload().ignoreOverlaps()
                .addSearchString("ab", "alpha:ab").addSearchString("cba", "alpha:cba").addSearchString("ababc", "alpha:ababc")
                .build();
        Collection<Emit<String>> emits = trie.parseText("ababcbab");
        assertEquals(2, emits.size());
        Iterator<Emit<String>> iterator = emits.iterator();
        // With overlaps: ab@1, ab@3, ababc@4, cba@6, ab@7
        checkEmit(iterator.next(), 0, 4, "ababc", "alpha:ababc");
        checkEmit(iterator.next(), 6, 7, "ab", "alpha:ab");
    }

    @Test
    public void nonOverlappingFirstMatch() {
        StringSearcher<String> trie = StringSearcher.<String>builderWithPayload().ignoreOverlaps()
                .addSearchString("ab", "alpha:ab").addSearchString("cba", "alpha:cba").addSearchString("ababc", "alpha:ababc")
                .build();
        Emit<String> firstMatch = trie.firstMatch("ababcbab");

        checkEmit(firstMatch, 0, 4, "ababc", "alpha:ababc");
    }

    @Test
    public void containsMatch() {
        StringSearcher<String> trie = StringSearcher.<String>builderWithPayload().ignoreOverlaps()
                .addSearchString("ab", "alpha:ab").addSearchString("cba", "alpha:cba").addSearchString("ababc", "alpha:ababc")
                .build();
        assertTrue(trie.containsMatch("ababcbab"));
    }

    @Test
    public void startOfChurchillSpeech() {
        StringSearcher<String> trie = StringSearcher.<String>builderWithPayload().ignoreOverlaps().addSearchString("T")
                .addSearchString("u").addSearchString("ur").addSearchString("r").addSearchString("urn").addSearchString("ni")
                .addSearchString("i").addSearchString("in").addSearchString("n").addSearchString("urning").build();
        Collection<Emit<String>> emits = trie.parseText("Turning");
        assertEquals(2, emits.size());
    }

    @Test
    public void partialMatch() {
        StringSearcher<String> trie = StringSearcher.<String>builderWithPayload().onlyWholeWords()
                .addSearchString("sugar", "food:sugar").build();
        Collection<Emit<String>> emits = trie.parseText("sugarcane sugarcane sugar canesugar"); // left, middle, right test
        assertEquals(1, emits.size()); // Match must not be made
        checkEmit(emits.iterator().next(), 20, 24, "sugar", "food:sugar");
    }

    @Test
    public void partialMatchFirstMatch() {
        StringSearcher<String> trie = StringSearcher.<String>builderWithPayload().onlyWholeWords()
                .addSearchString("sugar", "food:sugar").build();
        Emit<String> firstMatch = trie.firstMatch("sugarcane sugarcane sugar canesugar"); // left, middle, right test

        checkEmit(firstMatch, 20, 24, "sugar", "food:sugar");
    }

    @Test
    public void tokenizeFullSentence() {
        StringSearcher<String> trie = greekLettersStringSearcherBuilder().build();
        Collection<Token<String>> tokens = trie.tokenize("Hear: Alpha team first, Beta from the rear, Gamma in reserve");
        assertEquals(7, tokens.size());
        Iterator<Token<String>> tokensIt = tokens.iterator();
        assertEquals("Hear: ", tokensIt.next().getFragment());
        assertEquals("Alpha", tokensIt.next().getFragment());
        assertEquals(" team first, ", tokensIt.next().getFragment());
        assertEquals("Beta", tokensIt.next().getFragment());
        assertEquals(" from the rear, ", tokensIt.next().getFragment());
        assertEquals("Gamma", tokensIt.next().getFragment());
        assertEquals(" in reserve", tokensIt.next().getFragment());
    }

    // @see https://github.com/robert-bor/aho-corasick/issues/5
    @Test
    public void testStringIndexOutOfBoundsException() {
        StringSearcher<String> trie = unicodeStringSearcherBuilder().build();
        Collection<Emit<String>> emits = trie.parseText("TurninG OnCe AgAiN BÖRKÜ");
        assertEquals(4, emits.size()); // Match must not be made
        Iterator<Emit<String>> it = emits.iterator();

        checkEmit(it.next(), 0, 6, "turning", "uni:turning");
        checkEmit(it.next(), 8, 11, "once", "uni:once");
        checkEmit(it.next(), 13, 17, "again", "uni:again");
        checkEmit(it.next(), 19, 23, "börkü", "uni:börkü");
    }

    @Test
    public void testIgnoreCase() {
        StringSearcher<String> trie = unicodeStringSearcherBuilder().ignoreCase().build();
        Collection<Emit<String>> emits = trie.parseText("TurninG OnCe AgAiN BÖRKÜ");
        assertEquals(4, emits.size()); // Match must not be made
        Iterator<Emit<String>> it = emits.iterator();

        checkEmit(it.next(), 0, 6, "turning", "uni:turning");
        checkEmit(it.next(), 8, 11, "once", "uni:once");
        checkEmit(it.next(), 13, 17, "again", "uni:again");
        checkEmit(it.next(), 19, 23, "börkü", "uni:börkü");
    }

    @Test
    public void testIgnoreCaseFirstMatch() {
        StringSearcher<String> trie = unicodeStringSearcherBuilder().ignoreCase().build();
        Emit<String> firstMatch = trie.firstMatch("TurninG OnCe AgAiN BÖRKÜ");

        checkEmit(firstMatch, 0, 6, "turning", "uni:turning");
    }

    @Test
    public void tokenizeTokensInSequence() {
        StringSearcher<String> trie = greekLettersStringSearcherBuilder().build();
        Collection<Token<String>> tokens = trie.tokenize("Alpha Beta Gamma");
        assertEquals(5, tokens.size());
    }

    // @see https://github.com/robert-bor/aho-corasick/issues/7
    @Test
    public void testZeroLength() {
        StringSearcher<String> trie = StringSearcher.<String>builderWithPayload().ignoreOverlaps().onlyWholeWords().ignoreCase()
                .addSearchString("").build();
        trie.tokenize(
                "Try a natural lip and subtle bronzer to keep all the focus on those big bright eyes with NARS Eyeshadow Duo in Rated R And the winner is... Boots No7 Advanced Renewal Anti-ageing Glycolic Peel Kit ($25 amazon.com) won most-appealing peel.");
    }

    // @see https://github.com/robert-bor/aho-corasick/issues/8
    @Test
    public void testUnicode1() {
        String target = "LİKE THIS"; // The second character ('İ') is Unicode, which was read by AC as a 2-byte char
        assertEquals("THIS", target.substring(5, 9)); // Java does it the right way
        StringSearcher<String> trie = StringSearcher.<String>builderWithPayload().ignoreCase().onlyWholeWords()
                .addSearchString("this", "pronon:this").build();
        Collection<Emit<String>> emits = trie.parseText(target);
        assertEquals(1, emits.size());
        Iterator<Emit<String>> it = emits.iterator();
        checkEmit(it.next(), 5, 8, "this", "pronon:this");
    }

    // @see https://github.com/robert-bor/aho-corasick/issues/8
    @Test
    public void testUnicode2() {
        String target = "LİKE THIS"; // The second character ('İ') is Unicode, which was read by AC as a 2-byte char
        StringSearcher<String> trie = StringSearcher.<String>builderWithPayload().ignoreCase().onlyWholeWords()
                .addSearchString("this", "pronon:this").build();
        assertEquals("THIS", target.substring(5, 9)); // Java does it the right way
        Emit<String> firstMatch = trie.firstMatch(target);
        checkEmit(firstMatch, 5, 8, "this", "pronon:this");
    }

    @Test
    public void testPartialMatchWhiteSpaces() {
        StringSearcher<String> trie = StringSearcher.<String>builderWithPayload().onlyWholeWordsWhiteSpaceSeparated()
                .addSearchString("#sugar-123", "sugar").build();
        Collection<Emit<String>> emits = trie.parseText("#sugar-123 #sugar-1234"); // left, middle, right test
        assertEquals(1, emits.size()); // Match must not be made
        checkEmit(emits.iterator().next(), 0, 9, "#sugar-123", "sugar");
    }

    @Test
    public void testLargeString() {
        final int interval = 100;
        final int textSize = 1000000;
        final String keyword = FOOD[1];
        final Food payload = FOOD_PAYLOAD[1];
        final StringBuilder text = randomNumbers(textSize);

        injectKeyword(text, keyword, interval);

        StringSearcher<Food> trie = StringSearcher.<Food>builderWithPayload().onlyWholeWords().addSearchString(keyword, payload)
                .build();

        final Collection<Emit<Food>> emits = trie.parseText(text);

        assertEquals(textSize / interval, emits.size());
    }

    /**
     * Generates a random sequence of ASCII numbers.
     *
     * @param count The number of numbers to generate.
     * @return A character sequence filled with random digits.
     */
    private StringBuilder randomNumbers(int count) {
        final StringBuilder sb = new StringBuilder(count);

        while (--count > 0) {
            sb.append(randomInt(0, 10));
        }

        return sb;
    }

    /**
     * Injects keywords into a string builder.
     *
     * @param source   Should contain a bunch of random data that cannot match any
     *                 keyword.
     * @param keyword  A keyword to inject repeatedly in the text.
     * @param interval How often to inject the keyword.
     */
    private void injectKeyword(final StringBuilder source, final String keyword, final int interval) {
        final int length = source.length();
        for (int i = 0; i < length; i += interval) {
            source.replace(i, i + keyword.length(), keyword);
        }
    }

    private int randomInt(final int min, final int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    private void checkEmit(Emit<Food> next, int expectedStart, int expectedEnd, String expectedKeyword, Food expectedPayload) {
        assertEquals("Start of emit should have been " + expectedStart, expectedStart, next.getStart());
        assertEquals("End of emit should have been " + expectedEnd, expectedEnd, next.getEnd());
        assertEquals("Keyword of emit shoud be " + expectedKeyword, expectedKeyword, next.getKeyword());
        assertEquals("Payload of emit shoud be " + expectedPayload, expectedPayload, next.getPayload());
    }

    private void checkEmit(Emit<Integer> next, int expectedStart, int expectedEnd, String expectedKeyword,
            Integer expectedPayload) {
        assertEquals("Start of emit should have been " + expectedStart, expectedStart, next.getStart());
        assertEquals("End of emit should have been " + expectedEnd, expectedEnd, next.getEnd());
        assertEquals("Keyword of emit shoud be " + expectedKeyword, expectedKeyword, next.getKeyword());
        assertEquals("Payload of emit shoud be " + expectedPayload, expectedPayload, next.getPayload());
    }

    private void checkEmit(Emit<String> next, int expectedStart, int expectedEnd, String expectedKeyword,
            String expectedPayload) {
        assertEquals("Start of emit should have been " + expectedStart, expectedStart, next.getStart());
        assertEquals("End of emit should have been " + expectedEnd, expectedEnd, next.getEnd());
        assertEquals("Keyword of emit shoud be " + expectedKeyword, expectedKeyword, next.getKeyword());
        assertEquals("Payload of emit shoud be " + expectedPayload, expectedPayload, next.getPayload());
    }
}
