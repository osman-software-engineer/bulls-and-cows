package bullscows;

import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Create a scanner object for user input
        Scanner scanner = new Scanner(System.in);

        // Prompt user for the length of the secret code
        System.out.print("Input the length of the secret code: ");
        String lengthInput = scanner.nextLine();

        // Validate the input is numeric
        if (!isNumeric(lengthInput)) {
            System.out.println("Error: \"" + lengthInput + "\" isn't a valid number.");
            scanner.close();
            return;
        }

        int length = Integer.parseInt(lengthInput);
        // Validate the length is a positive integer
        if (length <= 0) {
            System.out.println("Error: the length of the secret code must be a positive integer.");
            scanner.close();
            return;
        }

        // Prompt user for the number of possible symbols in the code
        System.out.print("Input the number of possible symbols in the code: ");
        String rangeInput = scanner.nextLine();

        // Validate the input is numeric
        if (!isNumeric(rangeInput)) {
            System.out.println("Error: \"" + rangeInput + "\" isn't a valid number.");
            scanner.close();
            return;
        }

        int range = Integer.parseInt(rangeInput);

        // Validate the provided range and length
        if (length > range || range > 36) {
            System.out.println("Error: it's not possible to generate a code with a length of " + length + " with " + range + " unique symbols.");
            scanner.close();
            return;
        }

        // Generate the secret code
        String secretCode = generateSecretCode(length, range);
        System.out.println("The secret is prepared: " + "*".repeat(length) + " (" + getRangeString(range) + ").\nOkay, let's start a game!");

        // Game loop
        int turn = 1;
        boolean guessed = false;
        while (!guessed) {
            System.out.print("Turn " + turn + ":\n> ");
            String guess = scanner.nextLine();

            // Validate the length of the guess
            if (guess.length() != length) {
                System.out.println("Error: the length of the guess doesn't match the length of the secret code.");
                continue;  // Skip to next iteration
            }

            // Grade the guess
            String grade = gradeGuess(secretCode, guess);
            System.out.println("Grade: " + grade);

            // Check if the guess is correct
            if (grade.contains(length + " bull")) {
                guessed = true;
                System.out.println("Congratulations! You guessed the secret code.");
            }
            turn++;
        }

        // Close the scanner
        scanner.close();
    }

    /**
     * Checks if a string is numeric
     * @param str the string to check
     * @return true if the string is numeric, false otherwise
     */
    public static boolean isNumeric(String str) {
        return str.chars().allMatch(Character::isDigit);
    }

    /**
     * Generates a secret code with the specified length and range
     * @param length the length of the secret code
     * @param range the range of possible symbols
     * @return the generated secret code
     */
    public static String generateSecretCode(int length, int range) {
        StringBuilder secretCode = new StringBuilder();
        Random random = new Random();
        String symbols = "0123456789abcdefghijklmnopqrstuvwxyz".substring(0, range);
        for (int i = 0; i < length; i++) {
            int index;
            do {
                index = random.nextInt(range);
            } while (secretCode.indexOf(String.valueOf(symbols.charAt(index))) != -1);
            secretCode.append(symbols.charAt(index));
        }
        return secretCode.toString();
    }

    /**
     * Gets the range string based on the specified range
     * @param range the range of possible symbols
     * @return a string representing the range of symbols
     */
    public static String getRangeString(int range) {
        char startChar = '0';
        char endChar = range <= 10 ? (char) ('0' + range - 1) : (char) ('a' + range - 11);
        return startChar + "-" + endChar;
    }

    /**
     * Grades a guess against the secret code
     * @param secretCode the secret code
     * @param guess the user's guess
     * @return a string describing the grade of the guess
     */
    public static String gradeGuess(String secretCode, String guess) {
        int bulls = 0;
        int cows = 0;

        for (int i = 0; i < secretCode.length(); i++) {
            char guessDigit = guess.charAt(i);
            char secretDigit = secretCode.charAt(i);

            if (guessDigit == secretDigit) {
                bulls++;
            } else if (secretCode.indexOf(guessDigit) != -1) {
                cows++;
            }
        }

        return formatGrade(bulls, cows);
    }

    /**
     * Formats the grade string based on the number of bulls and cows
     * @param bulls the number of bulls
     * @param cows the number of cows
     * @return a formatted string describing the grade
     */
    public static String formatGrade(int bulls, int cows) {
        if (bulls == 0 && cows == 0) {
            return "None";
        } else {
            return (bulls > 0 ? bulls + " bull(s)" : "") + (bulls > 0 && cows > 0 ? " and " : "") + (cows > 0 ? cows + " cow(s)" : "");
        }
    }
}
