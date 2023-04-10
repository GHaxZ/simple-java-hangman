import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Game {
    private String wordToGuess; // Stores the word that is supposed to be guessed
    private String censoredWord; // Stores a censored version of the word that is supposed to be guessed
    private int currentLives; //Stores the current amount of lives
    // Stores all the available word that can get picked for guessing
    private ArrayList<String> words = new ArrayList<>(Arrays.asList("family", "school", "friend", "mother", "father", "doctor", "police", "market", "garden", "library", "camera", "street", "nature", "bicycle", "dinner", "country", "kitchen", "airport", "bedroom", "computer", "hospital", "business", "internet", "vacation", "sandwich", "newspaper", "football", "magazine", "fireplace", "basketball", "vegetable", "furniture", "breakfast", "chocolate", "adventure", "landscape", "passenger", "building", "umbrella", "telephone", "playlist", "education", "furniture", "playlist", "software", "painting", "butterfly", "lipstick", "calendar", "staircase", "perfume", "elevator", "jewelry", "gasoline", "airplane", "medicine", "vacation", "airplane", "architect", "umbrella", "fireworks", "furniture", "playground", "chocolate", "cathedral", "waterfall", "skylight", "magazine", "dinosaur", "classroom", "butterfly", "landscape", "breakfast", "furniture", "champagne", "vegetable", "accessory", "basketball", "masterpiece", "pollution", "furniture", "billboard", "fireplace", "satellite", "orchestra", "spotlight", "telephone", "furniture", "skyscraper", "adventure", "portfolio", "skyscraper", "landscape", "chocolate", "warehouse", "furniture", "helicopter", "medication", "furniture"));
    // Stores the already guessed words and letters
    private ArrayList<String> alreadyGuessed = new ArrayList<>();

    public static void main(String[] args) {
        Path path = Paths.get("customWords.txt"); // Creates the Path Object for the "customWords.txt" file
        Game game = new Game(path); // Initializes new Game Object
        game.runGame(); // Runs the main game loop
    }

    public Game(Path path){ // Creates a new game Instance.
        createFile(path);
        addCustomWords(path);
        pickRandomWord();
        currentLives = 6;
    }

    private void runGame(){ // Main Game loop that takes inputs, shows progress, etc.
        System.out.println("\nWelcome to Hangman!\nA random word has been picked.");
        boolean wordGuessed = false;
        while(!wordGuessed) {
            if(currentLives == 0){ // Checks if the current lives are zero and if so ends the game
                System.out.println("\n--- You lost! The word was " + wordToGuess + ".");
                break;
            }

            System.out.println("\nAmount of lifes left: " + currentLives);
            System.out.println("\nWord to guess: " + censoredWord);

            // Prints the words or characters already guessed
            if(alreadyGuessed.size() != 0){
                System.out.print("Already guessed: ");

                for(int x = 0; x < alreadyGuessed.size(); x++){
                    System.out.print((x == 0) ? alreadyGuessed.get(x) : ", " + alreadyGuessed.get(x));
                }
                System.out.println();
            }

            System.out.print("\nEnter a letter or a word to guess: ");

            String guessedString = stringInput().trim(); // Takes the user input and stores it in the guessedString variable

            if(!guessedString.isEmpty()){ // If the user input is not empty
                // If the user input is either a single letter or a word with the length of the word to guess
                if(guessedString.length() == 1 || guessedString.length() == wordToGuess.length()){
                    int guessResult = guess(guessedString);

                    // Handles the different cases (word guessed, letter correct, wrong guess, guess already made)
                    if(guessResult == 1){
                        System.out.println("\n--- You guessed the word! It was " + wordToGuess + " ---");
                        wordGuessed = true;
                    } else if(guessResult == 0){
                        System.out.println("\n--- You guessed correctly! The character \"" + guessedString + "\" is in the word. ---");
                    } else if (guessResult == 2){
                        System.out.println("\n--- You already guessed \"" + guessedString + "\". Try something different. ---");
                    } else {
                        System.out.println("\n--- Oops! You guessed wrong. One life less! ---");
                        currentLives--;
                    }
                } else {
                    System.err.println("\n[!] Your guess must be a single letter or the length of the word to guess (=" + wordToGuess.length() + ").");
                }
            }
        }

        // Lets the player play again if they want to
        while(true){
            System.out.print("\n--- Do you want to play again? (Yes/No): ");
            String userInput = stringInput().toLowerCase();

            if(userInput.equals("yes")){ // If yes, the main() method gets called so the game resets and runs again
                main(null); // "null" because there are no runtime arguments
            } else if(userInput.equals("no")){
                System.exit(0);
            } else {
                System.out.println("[!] \"" + userInput + "\" is not valid answer.");
            }
        }
    }

    // Creates the customWords.txt file if not already created.
    private void createFile(Path path){
        try {
            Files.createFile(path);
        } catch(IOException e){}
    }

    // Adds all the custom words to the ArrayList.
    private void addCustomWords(Path path){
        try {
            List<String> customWords = Files.readAllLines(path);

            for(String word : customWords){
                word = word.trim();
                if(word != ""){
                    words.add(word);
                }
            }
        } catch(IOException e){}
    }

    private void pickRandomWord(){
        wordToGuess = words.get(ranNum(0, words.size())); // Chooses a random word from the words ArrayList as the word to guess

        censoredWord = "";
        for(int x = 0; x < wordToGuess.length(); x++){ // Creates the censored word by adding one "-" for each letter of the word to guess
            censoredWord += "-";
        }
    }

    // Handles the guess-taking
    private int guess(String guessedString){
        // If the letter or word was already guessed returns 2 (already guessed)
        if(alreadyGuessed.contains(guessedString.toLowerCase())){
            return 2;
        }

        alreadyGuessed.add(guessedString); // Adds the word to the alreadyGuessed ArrayList

        // Checks if the guessedString is a word (longer than 1)
        if(guessedString.length() > 1){
            if(guessedString.equalsIgnoreCase(wordToGuess)){ // If guessedString is the word to guess returns 1 (word guessed).
                return 1;
            } else { // Else return -1 (wrong guess).
                return -1;
            }
        } else { // If the guessedString is not a word (1 character long).
            boolean successfulGuess = false;
            for(int x = 0; x < wordToGuess.length(); x++){ // Loops through every letter in the word to guess
                if(guessedString.equalsIgnoreCase(Character.toString(wordToGuess.charAt(x)))){ // If the letter at index x is the guessedString
                    successfulGuess = true; // Guess was successful
                    //Replaces the "-" symbol in the censoredWord String at the index x with the letter of the wordToGuess String at the index x
                    //to be honest I don't really know why this shit works, I asked ChatGPT
                    censoredWord = censoredWord.substring(0, x) + guessedString + censoredWord.substring(x + 1);
                }
            }

            // Returns the correct number based on the result of the guess
            if(censoredWord.equals(wordToGuess)){ // If the censored word is equal to the word to guess return 1 (word guessed)
                return 1;
            } else if(successfulGuess) { // If the successfulGuess boolean has been set to true returns 0 (successful guess)
                return 0;
            } else { // Else return -1 (guess was wrong)
                return -1;
            }
        }
    }

    // Generates a random number between the given min and max value
    private int ranNum(int min, int max){
        return (int)(Math.random() * ((max - min) + 1) + min);
    }

    // Handles the String Input
    private String stringInput(){
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}