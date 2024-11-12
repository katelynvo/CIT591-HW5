import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class WordRecommenderTest {
    private WordRecommender recommender;

    @Before
    public void setUp() {
        // Initialize WordRecommender with dictionary file
        recommender = new WordRecommender("engDictionary.txt");
    }

    @Test
    public void testGetSimilarity_PerfectMatch() {
        // Test similarity between identical words
        assertEquals(9.0, recommender.getSimilarity("abandoned", "abandoned"), 0.001);
    }

    @Test
    public void testGetSimilarity_NoCommonality() {
        // Test similarity between completely different words
        assertEquals(0.0, recommender.getSimilarity("yes", "no"), 0.001);
    }

    @Test
    public void testGetSimilarity_PartialMatch() {
        // Test partial similarity between two words
        assertEquals(2.5, recommender.getSimilarity("oblige", "oblivion"), 0.001);
    }

    @Test
    public void testLeftAndRightSimilarity() {
        // Test left and right similarity calculations for words in HW example
        assertEquals(4, recommender.calculateLeftSimilarity("oblige", "oblivion"));
        assertEquals(1, recommender.calculateRightSimilarity("oblige", "oblivion"));
    }

    @Test
    public void testCalculateLeftSimilarity_EmptyStrings() {
        // Test left similarity for empty strings
        assertEquals(0, recommender.calculateLeftSimilarity("", ""));
        assertEquals(0, recommender.calculateLeftSimilarity("test", ""));
        assertEquals(0, recommender.calculateLeftSimilarity("", "test"));
    }

    @Test
    public void testCalculateRightSimilarity_EmptyStrings() {
        // Test right similarity for empty strings
        assertEquals(0, recommender.calculateRightSimilarity("", ""));
        assertEquals(0, recommender.calculateRightSimilarity("test", ""));
        assertEquals(0, recommender.calculateRightSimilarity("", "test"));
    }

    @Test
    public void testGetWordSuggestions_TopMatches() {
        // Test that the top 4 suggestions for misspelled word in HW example are correct
        ArrayList<String> suggestions = recommender.getWordSuggestions("morbit", 2, 0.5, 4);
        assertEquals(4, suggestions.size());
        assertTrue(suggestions.contains("morbid"));
        assertTrue(suggestions.contains("hobbit"));
        assertTrue(suggestions.contains("sorbet"));
        assertTrue(suggestions.contains("forbid"));
    }

    @Test
    public void testGetWordSuggestions_LimitedTopN() {
        // Test that the number of suggestions is limited to top N
        ArrayList<String> suggestions = recommender.getWordSuggestions("morbit", 2, 0.5, 2);
        assertEquals(2, suggestions.size());
    }

    @Test
    public void testGetWordSuggestions_NoSuggestions() {
        // Test that no suggestions are provided for an unknown word
        ArrayList<String> suggestions = recommender.getWordSuggestions("sleepyyyyyyyyyyyyyyyy", 2, 0.5, 4);
        assertTrue(suggestions.isEmpty());
    }

    @Test
    public void testCalculateCommonPercent_Full() {
        // Test common percent between identical words
        assertEquals(1.0, recommender.calculateCommonPercent("abandon", "abandon"), 0.001);
    }

    @Test
    public void testCalculateCommonPercent_Partial() {
        // Test partial character overlap between two words
        assertEquals(0.6, recommender.calculateCommonPercent("abandon", "abandonment"), 0.001);
    }

    @Test
    public void testCalculateCommonPercent_None() {
        // Test common percent between completely different words
        assertEquals(0.0, recommender.calculateCommonPercent("yes", "no"), 0.001);
    }
}

