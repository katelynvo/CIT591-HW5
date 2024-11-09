import java.io.FileInputStream;     // Import FileInputStream to read file
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;           // Import the Scanner class


public class SpellChecker {

    public SpellChecker() {
      // TODO: You can modify the body of this constructor,
      // or you can leave it blank. You must keep the signature, however.
    }


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

        Scanner userInput = new Scanner(System.in);                         // Initializes Scanner and passes it as an argument to other methods
        FileInputStream dictionary = openDictionary(userInput);             // Prompts user for Dictionary input
        FileInputStream file = openSpellCheckFile(userInput);               // Prompts user for File input


        //  3.) Iterate Over Words: Read each word from the input file and check against words in dictionary
        //  4.) For Each misspelled word, prompt the user with options to accept, replace, or enter a correction
        //  5.) After correction, write the corrected words to a new output file, as specified

        userInput.close();                                                  // Closes Scanner
    }



    // Method to open user input Dictionary
    private static FileInputStream openDictionary(Scanner scanner) {
        String userDictName;                                                // Local Variable that stores user Input

        while (true) {                                                      // While Loop continues until User enters valid input Dictionary
            System.out.printf(Util.DICTIONARY_PROMPT);                      // Prompts user to input Dictionary file
            userDictName = scanner.nextLine();                              // Stores User input in 'userDictName'


            try {                                                           // try/catch to see if Dictionary file is valid and can be opened
                FileInputStream input = new FileInputStream(userDictName);
                System.out.printf(Util.DICTIONARY_SUCCESS_NOTIFICATION, userDictName);
                return input;
            } catch (IOException e) {                                       // Throws IO Exception if File is not valid and prompts user to try again
                System.out.printf(Util.FILE_OPENING_ERROR);
            }

        }

    }


    // Method to open user input File to SpellCheck
    private static FileInputStream openSpellCheckFile(Scanner scanner) {
        String userFileName;                                                // Local Variable that stores user Input

        while (true) {                                                      // While Loop continues until User enters valid input File
            System.out.printf(Util.FILENAME_PROMPT);                        // Prompts user to input SpellCheck file
            userFileName = scanner.nextLine();                              // Stores User input in 'userFileName'

            try {                                                           // try/catch to see if Dictionary file is valid and can be opened
                FileInputStream input = new FileInputStream(userFileName);
                System.out.printf(Util.FILE_SUCCESS_NOTIFICATION, userFileName);
                return input;
            } catch (IOException e) {                                       // Throws IO Exception if File is not valid and prompts user to try again
                System.out.printf(Util.FILE_OPENING_ERROR);
            }
        }
    }







  }