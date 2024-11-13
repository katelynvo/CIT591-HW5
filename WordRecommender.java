import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class WordRecommender {
    private ArrayList<String> dictionary;

    // Loads the dictionary file into a list
    public WordRecommender(String dictionaryFile) {
        dictionary = new ArrayList<>();
        try (Scanner scanner = new Scanner(new FileInputStream(dictionaryFile))) {
            while (scanner.hasNext()) {
                dictionary.add(scanner.next().trim().toLowerCase());
            }
        } catch (IOException e) {
            // Pass the error up if there is an unexpected problem
            throw new RuntimeException("Error reading the dictionary file: " + dictionaryFile, e);
        }
    }

    // Calculates overall similarity as the average of left and right similarities
    public double getSimilarity(String word1, String word2) {
        int left = calculateLeftSimilarity(word1, word2);
        int right = calculateRightSimilarity(word1, word2);
        return (left + right) / 2.0;
    }

    // Calculates left similarity
    public int calculateLeftSimilarity(String word1, String word2) {
        int similarity = 0;
        int minLength = Math.min(word1.length(), word2.length());
        for (int i = 0; i < minLength; i++) {
            if (word1.charAt(i) == word2.charAt(i)) {
                similarity++;
            }
        }
        return similarity;
    }

    // Calculates right similarity
    public int calculateRightSimilarity(String word1, String word2) {
        int similarity = 0;
        int minLength = Math.min(word1.length(), word2.length());
        for (int i = 0; i < minLength; i++) {
            if (word1.charAt(word1.length() - 1 - i) == word2.charAt(word2.length() - 1 - i)) {
                similarity++;
            }
        }
        return similarity;
    }

    // Finds top N word suggestions based on similarity and character overlap
    public ArrayList<String> getWordSuggestions(String word, int tolerance, double commonPercent, int topN) {
        ArrayList<String> suggestions = new ArrayList<>();

        // Filter candidates by length tolerance and common character percentage
        for (String candidate : dictionary) {
            if (Math.abs(candidate.length() - word.length()) <= tolerance
                    && calculateCommonPercent(word, candidate) >= commonPercent) {
                suggestions.add(candidate);
            }
        }

        // Find top N matches based on similarity
        ArrayList<String> topSuggestions = new ArrayList<>();
        for (int i = 0; i < topN && !suggestions.isEmpty(); i++) {
            String bestMatch = findBestMatch(word, suggestions);
            topSuggestions.add(bestMatch);
            suggestions.remove(bestMatch);
        }

        return topSuggestions;
    }

    // Calculates the percentage of common characters between two words
    public double calculateCommonPercent(String word1, String word2) {
        HashSet<Character> set1 = new HashSet<>();
        HashSet<Character> set2 = new HashSet<>();

        // Populate sets with characters from both words
        for (char c : word1.toCharArray()) {
            set1.add(c);
        }
        for (char c : word2.toCharArray()) {
            set2.add(c);
        }

        // Calculate intersection and union of character sets
        HashSet<Character> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        HashSet<Character> union = new HashSet<>(set1);
        union.addAll(set2);

        return (double) intersection.size() / union.size();
    }

    // Finds the best match from a list of candidates based on similarity
    private String findBestMatch(String word, ArrayList<String> candidates) {
        String bestMatch = null;
        double highestSimilarity = -1;

        for (String candidate : candidates) {
            double similarity = getSimilarity(word, candidate);
            if (similarity > highestSimilarity) {
                highestSimilarity = similarity;
                bestMatch = candidate;
            }
        }

        return bestMatch;
    }

    // Dictionary getter
    public ArrayList<String> getDictionary() {
        return dictionary;
    }

}