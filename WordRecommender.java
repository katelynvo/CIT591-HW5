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
      // TODO: change this!
      return 0.0;
    }
  
    public ArrayList<String> getWordSuggestions(String word, int tolerance, double commonPercent, int topN) {
      // TODO: change this!
      return null;
    }
  
    
  }