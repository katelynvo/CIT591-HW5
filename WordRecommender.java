import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class WordRecommender {

    private Set<String> dictionary;

    public WordRecommender(String dictionaryFile) {
        dictionary = new HashSet<>();
        try (Scanner scanner = new Scanner(new File(dictionaryFile))) {
            while (scanner.hasNextLine()) {
                dictionary.add(scanner.nextLine().trim().toLowerCase());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Dictionary file not found.");
            e.printStackTrace();
        }
    }
  
    public double getSimilarity(String word1, String word2) {
        int left = 0;
        int right = 0;
        int minLength = Math.min(word1.length(), word2.length());

        // Calculate left similarity
        for (int i = 0; i < minLength; i++) {
            if (word1.charAt(i) == word2.charAt(i)) {
                left++;
            }
        }

        // Calculate right similarity
        for (int i = 1; i <= minLength; i++) {
            if (word1.charAt(word1.length() - i) == word2.charAt(word2.length() - i)) {
                right++;
            }
        }

        // Return average of left and right similarity
        return (left + right) / 2.0;
    }

    public ArrayList<String> getWordSuggestions(String word, int tolerance, double commonPercent, int topN) {
        PriorityQueue<String> topSuggestions = new PriorityQueue<>(
                Comparator.comparingDouble(candidate -> -getSimilarity(word, candidate))
        );

        for (String candidate : dictionary) {
            // Check length tolerance
            if (Math.abs(candidate.length() - word.length()) > tolerance) {
                continue;
            }

            // Check common character percentage
            double commonCharPercent = getCommonCharPercent(word, candidate);
            if (commonCharPercent < commonPercent) {
                continue;
            }

            // Add valid candidate to the priority queue
            topSuggestions.offer(candidate);
        }

        // Retrieve top N suggestions
        ArrayList<String> suggestions = new ArrayList<>();
        for (int i = 0; i < topN && !topSuggestions.isEmpty(); i++) {
            suggestions.add(topSuggestions.poll());
        }

        return suggestions;
    }

    private static double getCommonCharPercent(String word, String candidate) {
        Set<Character> wordSet = new HashSet<>();
        Set<Character> candidateSet = new HashSet<>();
        for (char c : word.toCharArray()) wordSet.add(c);
        for (char c : candidate.toCharArray()) candidateSet.add(c);

        Set<Character> intersection = new HashSet<>(wordSet);
        intersection.retainAll(candidateSet);

        Set<Character> union = new HashSet<>(wordSet);
        union.addAll(candidateSet);

        double commonCharPercent = (double) intersection.size() / union.size();
        return commonCharPercent;
    }


}