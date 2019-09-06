package org.neosearch.stringsearcher;

/**
 * Configures options for matching strings.
 * 
 * @author Daniel Beck
 */
public class StringSearcherConfig {

    private boolean caseInsensitive = false;

    private boolean allowOverlaps = true;

    private boolean onlyWholeWords = false;

    private boolean onlyWholeWordsWhiteSpaceSeparated = false;

    private boolean stopOnHit = false;

    /**
     * Returns true if the matching should be case insensitive.
     */
    public boolean isCaseInsensitive() {
        return caseInsensitive;
    }

    /**
     * Configures if the StringSearcher should respect case sensitivity or not.
     * 
     * @param caseInsensitive Case sensitivty
     */
    public void setCaseInsensitive(boolean caseInsensitive) {
        this.caseInsensitive = caseInsensitive;
    }

    /**
     * @return true if the algorithm should stop on hit.
     */
    public boolean isStopOnHit() {
        return stopOnHit;
    }

    /**
     * Configures it he StringSearcher should stop on hit.
     * @param stopOnHit true, if the StringSearch should stop on hit. False 
     */
    public void setStopOnHit(boolean stopOnHit) {
        this.stopOnHit = stopOnHit;
    }

    public boolean isAllowOverlaps() {
        return allowOverlaps;
    }

    public void setAllowOverlaps(boolean allowOverlaps) {
        this.allowOverlaps = allowOverlaps;
    }

    public boolean isOnlyWholeWords() {
        return onlyWholeWords;
    }

    public void setOnlyWholeWords(boolean onlyWholeWords) {
        this.onlyWholeWords = onlyWholeWords;
    }

    public boolean isOnlyWholeWordsWhiteSpaceSeparated() {
        return onlyWholeWordsWhiteSpaceSeparated;
    }

    public void setOnlyWholeWordsWhiteSpaceSeparated(boolean onlyWholeWordsWhiteSpaceSeparated) {
        this.onlyWholeWordsWhiteSpaceSeparated = onlyWholeWordsWhiteSpaceSeparated;
    }

}
