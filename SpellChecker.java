import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;


public class SpellChecker {

    public SpellChecker() {
      // TODO: You can modify the body of this constructor,
    }


    // Global Variable
    private static String outputFileName;


    /* ////////////////////////////////////////////// */
    /* ////////////////////////////////////////////// */
    /* MAIN METHOD: JUST FOR ME TO CHECK AND RUN CODE */
    public static void main(String[] args) {
        SpellChecker.start();
    }
    /* ////////////////////////////////////////////// */
    /* ////////////////////////////////////////////// */
    /* ////////////////////////////////////////////// */


  
    public static void start() {
      // TODO: You can modify the body of this method

        Scanner userInput = new Scanner(System.in);                                 // Initializes Scanner and passes it as an argument to other methods
        FileInputStream dictionary = openDictionary(userInput);                     // Prompts user for Dictionary input
        FileInputStream file = openSpellCheckFile(userInput);                       // Prompts user for File input

        ArrayList<String> fileWords = new ArrayList<>();
        ArrayList<String> misspelledWords = spellCheckingFile(dictionary, file, fileWords);    // calls method that will store the misspelled words into ArrayList


        userCorrections(userInput, fileWords, misspelledWords);                     // Calls Method that prompts user to make correction selection

        saveCorrectedFile(fileWords, outputFileName);                               // Save corrections and stores into new file

        userInput.close();
    }



    // Method to open user input Dictionary
    private static FileInputStream openDictionary(Scanner scanner) {
        String userDictName;                                                        // Local Variable that stores user Input

        while (true) {                                                              // While Loop continues until User enters valid input Dictionary
            System.out.printf(Util.DICTIONARY_PROMPT);                              // Prompts user to input Dictionary file
            userDictName = scanner.nextLine();                                      // Stores User input in 'userDictName'


            try {                                                                   // try/catch to see if Dictionary file is valid and can be opened
                FileInputStream input = new FileInputStream(userDictName);
                System.out.printf(Util.DICTIONARY_SUCCESS_NOTIFICATION, userDictName);
                return input;
            } catch (IOException e) {                                               // Throws IO Exception if File is not valid and prompts user to try again
                System.out.printf(Util.FILE_OPENING_ERROR);
            }
        }
    }


    // Method to open user input File to SpellCheck
    private static FileInputStream openSpellCheckFile(Scanner scanner) {
        String userFileName;                                                        // Local Variable that stores user Input


        while (true) {                                                              // While Loop continues until User enters valid input File
            System.out.printf(Util.FILENAME_PROMPT);                                // Prompts user to input SpellCheck file
            userFileName = scanner.nextLine();                                      // Stores User input in 'userFileName'

            try {                                                                   // try/catch to see if Dictionary file is valid and can be opened
                FileInputStream input = new FileInputStream(userFileName);
                outputFileName = userFileName.substring(0, userFileName.length() - 4) + "_chf.txt";
                System.out.printf(Util.FILE_SUCCESS_NOTIFICATION, userFileName, outputFileName);
                return input;
            } catch (IOException e) {                                               // Throws IO Exception if File is not valid and prompts user to try again
                System.out.printf(Util.FILE_OPENING_ERROR);
            }
        }
    }


    private static ArrayList<String> spellCheckingFile(FileInputStream dictionary, FileInputStream file, ArrayList<String> fileWords) {
        ArrayList<String> dictionaryWords = new ArrayList<String>();      // stores all words from Dictionary in ArrayList
        ArrayList<String> misspelledWords = new ArrayList<String>();

        // Read dictionary file word-by-word using Scanner and stores in array
        Scanner dictionaryScanner = new Scanner(dictionary);
        while (dictionaryScanner.hasNext()) {
            dictionaryWords.add(dictionaryScanner.next().trim());
        }
        dictionaryScanner.close();


        // Read user file word-by-word using Scanner and stores in array
        // Initializes the Scanner to read the file line-by-line to keep line breaks of file
        Scanner fileScanner = new Scanner(file);
        while (fileScanner.hasNextLine()) {                               // Reads the next line from the file as a whole string (including spaces)
            String line = fileScanner.nextLine();
            fileWords.add(line);                                          // Adds Line to fileWords Array

            String[] words = line.split(" ");                      // Splits every individual word in Line

            for (int i = 0; i < words.length; i++) {                     // Iterates over each word in each line
                String word = words[i];
                if (!dictionaryWords.contains(word)) {                   // Checks whether the word is in Dictionary
                    misspelledWords.add(word);                           // Add word to misspelledWords if not in dictionary
                }
            }
        }
        fileScanner.close();
        return misspelledWords;

    }




    // Displays Corrections to User and subsequently handles user input to replace word
    private void userCorrections(Scanner userInput, ArrayList<String> fileWords, ArrayList<String> misspelledWords) {
        int tolerance = 2;                                   // Tolerance
        double commonPercent = 0.5;                          // Minimum similarity threshold
        int topN = 4;                                        // Number of top suggestions to display


        // Looping through misspelled words and prints them to user
        for (int i = 0; i < misspelledWords.size(); i++) {
            String misspelled = misspelledWords.get(i);
            System.out.printf(Util.MISSPELL_NOTIFICATION, misspelled);

            // Passes Misspelled word to WordRecommender Class to get replacement suggestions
            ArrayList<String> suggestions = WordRecommender.getWordSuggestions(misspelled, tolerance, commonPercent, topN);

            // Display suggestions to User
            if (suggestions.isEmpty()) {
                System.out.printf(Util.NO_SUGGESTIONS);
                System.out.printf(Util.TWO_OPTION_PROMPT);
            } else {
                System.out.printf(Util.FOLLOWING_SUGGESTIONS);
                for (int j = 0; j < suggestions.size(); j++) {
                    System.out.printf(Util.SUGGESTION_ENTRY, j + 1, suggestions.get(j));
                }
                System.out.printf(Util.THREE_OPTION_PROMPT);
            }

            // Handling User Input
            String choice = userInput.nextLine().trim().toLowerCase();


            if (choice.equals("a")) {                                           // a = accept misspelled word
                continue ;


            } else if (choice.equals("r")) {                                    // r = replace misspelled word
                System.out.printf(Util.AUTOMATIC_REPLACEMENT_PROMPT);
                int selection = Integer.parseInt(userInput.nextLine()) - 1;     // Stores User Selection (1, 2, 3, or 4)
                if (selection >= 0 && selection < suggestions.size()) {         // If valid user input, calls 'replaceWord' Method
                    replaceWord(fileWords, misspelled, suggestions.get(selection));
                } else {
                    System.out.printf(Util.INVALID_RESPONSE);                   // If user input invalid, prints error message
                }


            } else if (choice.equals("t")) {                                    // t = replace word manually
                System.out.printf(Util.MANUAL_REPLACEMENT_PROMPT);
                String correction = userInput.nextLine();                       // Stores User input
                replaceWord(fileWords, misspelled, correction);                 // Calls 'replaceWord' Method


            } else {
                System.out.printf(Util.INVALID_RESPONSE);                       // If user input anything else, prints error message
            }

        }

    }


    // Replaces the misspelled word and stores in fileWords Array
    private static void replaceWord(ArrayList<String> fileWords, String oldWord, String newWord) {
        for (int i = 0; i < fileWords.size(); i++) {
            String line = fileWords.get(i);

            // Split the line into words based on spaces
            String[] words = line.split(" ");

            // Rebuild the line with replacements where needed
            StringBuilder replacedLine = new StringBuilder();
            for (int j = 0; j < words.length; j++) {
                // Replace the word only if it matches oldWord exactly
                if (words[j].equals(oldWord)) {
                    replacedLine.append(newWord);
                } else {
                    replacedLine.append(words[j]);
                }

                // Add a space after each word except the last one
                if (j < words.length - 1) {
                    replacedLine.append(" ");
                }
            }

            // Update the line in fileWords with the modified line
            fileWords.set(i, replacedLine.toString());
        }
    }


    // Saves the ArrayList with corrected words into new file
    // Maintains the same structure by passing each line into new text file
    private void saveCorrectedFile(ArrayList<String> fileWords, String outputFileName) {
        try (PrintStream outputFile = new PrintStream(new FileOutputStream(outputFileName))) {
            for (int i = 0; i < fileWords.size(); i++) {
                outputFile.println(fileWords.get(i));
            }
            System.out.println("Corrected file saved as " + outputFileName);
        } catch (IOException e) {
            System.out.println("Error saving corrected file.");
        }
    }

  }