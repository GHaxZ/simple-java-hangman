import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Game {
    private String wordToGuess;
    private String censoredWord;
    private int currentLifes;
    private ArrayList<String> words = new ArrayList<>(Arrays.asList("family", "school", "friend", "mother", "father", "doctor", "police", "market", "garden", "library", "camera", "street", "nature", "bicycle", "dinner", "country", "kitchen", "airport", "bedroom", "computer", "hospital", "business", "internet", "vacation", "sandwich", "newspaper", "football", "magazine", "fireplace", "basketball", "vegetable", "furniture", "breakfast", "chocolate", "adventure", "landscape", "passenger", "building", "umbrella", "telephone", "playlist", "education", "furniture", "playlist", "software", "painting", "butterfly", "lipstick", "calendar", "staircase", "perfume", "elevator", "jewelry", "gasoline", "airplane", "medicine", "vacation", "airplane", "architect", "umbrella", "fireworks", "furniture", "playground", "chocolate", "cathedral", "waterfall", "skylight", "magazine", "dinosaur", "classroom", "butterfly", "landscape", "breakfast", "furniture", "champagne", "vegetable", "accessory", "basketball", "masterpiece", "pollution", "furniture", "billboard", "fireplace", "satellite", "orchestra", "spotlight", "telephone", "furniture", "skyscraper", "adventure", "portfolio", "skyscraper", "landscape", "chocolate", "warehouse", "furniture", "helicopter", "medication", "furniture"));
    private ArrayList<String> alreadyGuessed = new ArrayList<>();

    public static void main(String[] args) {
        Path path = Paths.get("customWords.txt");
        Game game = new Game(path);
        game.runGame();
    }

    public Game(Path path){ // Creates a new Game Instance.
        createFile(path);
        addCustomWords(path);
        pickRandomWord();
        currentLifes = 6;
    }

    private void runGame(){
        for(String item : words){
            System.out.println(item);
        }

        System.out.println(words.contains("testword"));
        System.out.println("\nWelcome to Hangman!\nA random word has been picked.");
        boolean wordGuessed = false;
        while(!wordGuessed) {
            if(currentLifes == 0){
                System.out.println("\n--- You lost! The word was " + wordToGuess + ".");
                break;
            }



            System.out.println("\nAmount of lifes left: " + currentLifes);
            System.out.println("\nWord to guess: " + censoredWord);

            if(alreadyGuessed.size() != 0){
                System.out.print("Already guessed: ");

                for(int x = 0; x < alreadyGuessed.size(); x++){
                    System.out.print((x == 0) ? alreadyGuessed.get(x) : ", " + alreadyGuessed.get(x));
                }
                System.out.println();
            }

            System.out.print("\nEnter a letter or a word to guess: ");

            String guessedString = stringInput().trim();

            if(guessedString != ""){
                if(guessedString.length() > 0 && guessedString.length() <= wordToGuess.length()){
                    int guessResult = guess(guessedString);

                    if(guessResult == 1){
                        System.out.println("\n--- You guessed the word! It was " + wordToGuess + " ---");
                        wordGuessed = true;
                    } else if(guessResult == 0){
                        System.out.println("\n--- You guessed correctly! The character \"" + guessedString + "\" is in the word. ---");
                    } else if (guessResult == 2){
                        System.out.println("\n--- You already guessed \"" + guessedString + "\". Try something different. ---");
                    } else {
                        System.out.println("\n--- Oops! You guessed wrong. One life less! ---");
                        currentLifes--;
                    }
                } else {
                    System.err.println("\n[!] Your guess must be longer than 0 and shorter than the words length (=" + wordToGuess.length() + ").");
                }
            }
        }

        while(true){
            System.out.print("\n--- Do you want to play again? (Yes/No): ");
            String userInput = stringInput().toLowerCase();

            if(userInput.equals("yes")){
                main(null);
            } else if(userInput.equals("no")){
                System.exit(0);
            } else {
                System.out.println("[!] \"" + userInput + "\" is not valid answer.");
            }
        }
    }

    private void createFile(Path path){ // Creates the customWords.txt file if not already created.
        try {
            Files.createFile(path);
        } catch(IOException e){}
    }

    private void addCustomWords(Path path){ // Adds all the custom words to the ArrayList.
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
        wordToGuess = words.get(ranNum(0, words.size()));

        censoredWord = "";
        for(int x = 0; x < wordToGuess.length(); x++){
            censoredWord += "-";
        }
    }

    private int guess(String guessedString){
        if(alreadyGuessed.contains(guessedString)){
            return 2;
        }

        alreadyGuessed.add(guessedString);

        if(guessedString.length() > 1){
            if(guessedString.equalsIgnoreCase(wordToGuess)){
                return 1;
            } else {
                return -1;
            }
        } else {
            boolean successfulGuess = false;
            for(int x = 0; x < wordToGuess.length(); x++){
                if(guessedString.equalsIgnoreCase(Character.toString(wordToGuess.charAt(x)))){
                    successfulGuess = true;
                    censoredWord = censoredWord.substring(0, x) + guessedString + censoredWord.substring(x + 1);
                }
            }

            if(censoredWord.equals(wordToGuess)){
                return 1;
            } else if(successfulGuess) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    private int ranNum(int min, int max){
        return (int)(Math.random() * ((max - min) + 1) + min);
    }

    private String stringInput(){
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}