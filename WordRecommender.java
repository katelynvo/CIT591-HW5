import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
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
      // TODO: change this!
      return null;
    }
  
    
  }