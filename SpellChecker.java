import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;


public class SpellChecker {

    public SpellChecker() {
    }

    private static String outputFileName;                                       // Global Variable


    public static void start() {

        Scanner userInput = new Scanner(System.in);                             // Initializes Scanner and passes it as an argument to other methods

        String dictionaryFilePath = openDictionary(userInput);                  // Prompts user for Dictionary input

        FileInputStream file = openSpellCheckFile(userInput);                   // Prompts user for File input

        WordRecommender recommender = new WordRecommender(dictionaryFilePath);  // Passes the file path as a String to the WordRecommender constructor

        ArrayList<String> fileWords = new ArrayList<>();                        // Initialises variable that will store the words from input file

        ArrayList<String> misspelledWords = spellCheckingFile(file, fileWords, recommender);    // calls method that will store the misspelled words into ArrayList

        userCorrections(userInput, fileWords, misspelledWords, recommender);    // Calls Method that prompts user to make correction selection

        saveCorrectedFile(fileWords, outputFileName);                           // Save corrections and stores into new file

        userInput.close();
    }



    // Method to open user input Dictionary
    private static String openDictionary(Scanner scanner) {
        String userDictName;                                                        // Local Variable that stores user Input

        while (true) {                                                              // While Loop continues until User enters valid input Dictionary
            System.out.printf(Util.DICTIONARY_PROMPT);                              // Prompts user to input Dictionary file
            userDictName = scanner.nextLine();                                      // Stores User input in 'userDictName'

            try {                                                                   // try/catch to see if Dictionary file is valid and can be opened
                FileInputStream input = new FileInputStream(userDictName);
                input.close(); // CLOSE IMMEDIATELY #CHECk1
                System.out.printf(Util.DICTIONARY_SUCCESS_NOTIFICATION, userDictName);
                return userDictName;
            } catch (IOException e) {                                               // Throws IO Exception if File is not valid and prompts user to try again
                System.out.printf(Util.FILE_OPENING_ERROR);
            }
        }
    }


    // Method to open user input File
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


    // Spellchecks every word in user input file against the provided dictionary
    private static ArrayList<String> spellCheckingFile(FileInputStream file, ArrayList<String> fileWords, WordRecommender recommender) {

        // Getting dictionary words from WordRecommender
        ArrayList<String> dictionaryWords = recommender.getDictionary();
        ArrayList<String> misspelledWords = new ArrayList<>();                      // Initialises Array that will hold the misspelled Words from file

        // Reading file line by line and then word-by-word using Scanner to maintain the structure when generating the output file
        Scanner fileScanner = new Scanner(file);
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            fileWords.add(line);                                                    // Adding entire line to fileWords Array

            String[] words = line.split(" ");                                // Splitting the line into individual words

            for (String word : words) {
                if (!dictionaryWords.contains(word.trim().toLowerCase())) {         // Checking if word is in dictionary
                    misspelledWords.add(word);                                      // Adding the word to misspelledWords if not in dictionary
                }
            }
        }
        fileScanner.close();
        return misspelledWords;
    }


    // Displays Corrections to User and subsequently handles user input to replace word
    private static void userCorrections(Scanner userInput, ArrayList<String> fileWords, ArrayList<String> misspelledWords, WordRecommender recommender) {

        int tolerance = 2;                                                      // Tolerance
        double commonPercent = 0.5;                                             // Minimum similarity threshold
        int topN = 4;                                                           // Number of top suggestions to display

        // Looping through misspelled words and print them to the user
        for (int i = 0; i < misspelledWords.size(); i++) {
            String misspelled = misspelledWords.get(i);
            System.out.printf(Util.MISSPELL_NOTIFICATION, misspelled);

            // Pass the misspelled word to WordRecommender to get replacement suggestions
            ArrayList<String> suggestions = recommender.getWordSuggestions(misspelled, tolerance, commonPercent, topN);

            // Display suggestions to the user
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

            // Prompting user input for instructions on whether to replace or accept the word
            boolean validChoice = false;                        // boolean initialised to false, to continue asking for valid user prompt when input is invalid
            while (!validChoice) {

                // Handling User Input
                String choice = userInput.nextLine().trim().toLowerCase();

                // Choice 1: Accept
                if (choice.equals("a")) {                       // Accept misspelled word
                    validChoice = true;                         // validChoice = true exits the while loop
                }

                // Choice 2: Replace misspelled word with suggestion
                else if (choice.equals("r")) {
                    System.out.printf(Util.AUTOMATIC_REPLACEMENT_PROMPT);   // prints replacement possibilities
                    boolean validChoice2 = false;               // Inner boolean initialised to false, to continue asking for valid user input (1-4) to replace the word with suggestion
                    while (!validChoice2) {
                        try {
                            int selection = Integer.parseInt(userInput.nextLine()) - 1;
                            if (selection >= 0 && selection < suggestions.size()) {
                                replaceWord(fileWords, misspelled, suggestions.get(selection));
                                validChoice = true;         // exits outer while loop
                                validChoice2 = true;        // exits inner while loop
                            } else {
                                System.out.printf(Util.INVALID_RESPONSE);
                            }

                        } catch (NumberFormatException e) {
                            System.out.printf(Util.INVALID_RESPONSE);
                        }
                    }
                }

                // Choice 3: Replace word manually
                else if (choice.equals("t")) {
                    System.out.printf(Util.MANUAL_REPLACEMENT_PROMPT);
                    String correction = userInput.nextLine();
                    replaceWord(fileWords, misspelled, correction);
                    validChoice = true;
                }

                // Invalid Response: Keep prompting for user input! (bool = false)
                else {
                    System.out.printf(Util.INVALID_RESPONSE);
                }
            }
        }
    }


    // Replaces the misspelled word and stores in fileWords Array
    private static void replaceWord(ArrayList<String> fileWords, String oldWord, String newWord) {

        // Iterating over all the words in the fileWords Array
        for (int i = 0; i < fileWords.size(); i++) {
            String line = fileWords.get(i);

            // Splits the line into words based on spaces
            String[] words = line.split(" ");

            // Rebuilds the line with replacements where needed using StringBuilder
            StringBuilder replacedLine = new StringBuilder();
            for (int j = 0; j < words.length; j++) {
                // Replace the word only if it matches oldWord exactly
                if (words[j].equals(oldWord)) {
                    replacedLine.append(newWord);
                } else {
                    replacedLine.append(words[j]);
                }

                // Adding spaces after each word except the last one
                if (j < words.length - 1) {
                    replacedLine.append(" ");
                }
            }

            // Updates the line in fileWords with the modified line
            fileWords.set(i, replacedLine.toString());
        }
    }


    // Saves the ArrayList with corrected words into new file
    // Maintains the same structure by passing each line into new text file
    private static void saveCorrectedFile(ArrayList<String> fileWords, String outputFileName) {
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