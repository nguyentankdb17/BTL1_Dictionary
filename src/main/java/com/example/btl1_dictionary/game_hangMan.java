package com.example.btl1_dictionary;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// do something

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class game_hangMan extends Games_Controller {

    @FXML
    private ImageView hangmanImageView;

    @FXML
    private Label wordLabel;

    @FXML
    private Label guessLabel;

//
//    @FXML
//    private GridPane virtualKeyboardGridPane;

    @FXML private Button buttonA;
    @FXML private Button buttonB;
    @FXML private Button buttonC;
    @FXML private Button buttonD;
    @FXML private Button buttonE;
    @FXML private Button buttonF;
    @FXML private Button buttonG;
    @FXML private Button buttonH;
    @FXML private Button buttonI;
    @FXML private Button buttonJ;
    @FXML private Button buttonK;
    @FXML private Button buttonL;
    @FXML private Button buttonM;
    @FXML private Button buttonN;
    @FXML private Button buttonO;
    @FXML private Button buttonP;
    @FXML private Button buttonQ;
    @FXML private Button buttonR;
    @FXML private Button buttonS;
    @FXML private Button buttonT;
    @FXML private Button buttonU;
    @FXML private Button buttonV;
    @FXML private Button buttonW;
    @FXML private Button buttonX;
    @FXML private Button buttonY;
    @FXML private Button buttonZ;



    private String[] words;
    private String selectedWord;
    private StringBuilder currentWord;
    private int incorrectGuessCount;
    private static final int MAX_INCORRECT_GUESSES = 6; // Set the maximum incorrect guesses allowed

    public void initialize() {
        loadWordsFromFile(Path.of("src/main/resources/com/example/btl1_dictionary/Text File/Saved.txt"));
        resetGame();

        // init keyboard element
        initializeVirtualKeyboardButtons();

    }

    private void initializeVirtualKeyboardButtons() {
        // Add an action handler for each button
        System.out.println("Sai Gon nhu 1 giac mo dep goi moi");

        // Add similar lines for other buttons
    }

    private void loadWordsFromFile(Path filePath) {
        List<String> wordList = new ArrayList<>();
        try (InputStream inputStream = Files.newInputStream(filePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = reader.readLine()) != null) {
                wordList.add(line.toUpperCase());
            }

            if (wordList.isEmpty()) {
                System.err.println("No words found in the file: " + filePath.toString());
            }

        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath.toString());
            e.printStackTrace();
        }

        words = wordList.toArray(new String[0]);
    }

    private void selectRandomWord() {
        Random random = new Random();
        selectedWord = words[random.nextInt(words.length)].toUpperCase();
        currentWord = new StringBuilder("_".repeat(selectedWord.length()));
    }

    private void updateWordLabel() {
        wordLabel.setText(currentWord.toString());
    }

    private void resetLabels() {
        guessLabel.setText("Guess a letter:");
    }

    @FXML
    private void resetGame() {
        selectRandomWord();
        updateWordLabel();
        resetHangmanImage();
        resetLabels();
        incorrectGuessCount = 0;
        enableAllVirtualKeyboardButtons();
    }

    private void enableAllVirtualKeyboardButtons() {
        buttonA.setDisable(false);
        buttonB.setDisable(false);
        buttonC.setDisable(false);
        buttonD.setDisable(false);
        buttonE.setDisable(false);
        buttonF.setDisable(false);
        buttonG.setDisable(false);
        buttonH.setDisable(false);
        buttonI.setDisable(false);
        buttonJ.setDisable(false);
        buttonK.setDisable(false);
        buttonL.setDisable(false);
        buttonM.setDisable(false);
        buttonN.setDisable(false);
        buttonO.setDisable(false);
        buttonP.setDisable(false);
        buttonQ.setDisable(false);
        buttonR.setDisable(false);
        buttonS.setDisable(false);
        buttonT.setDisable(false);
        buttonU.setDisable(false);
        buttonV.setDisable(false);
        buttonW.setDisable(false);
        buttonX.setDisable(false);
        buttonY.setDisable(false);
        buttonZ.setDisable(false);
    }
    private void disableAllVirtualKeyboardButtons() {
        buttonA.setDisable(true);
        buttonB.setDisable(true);
        buttonC.setDisable(true);
        buttonD.setDisable(true);
        buttonE.setDisable(true);
        buttonF.setDisable(true);
        buttonG.setDisable(true);
        buttonH.setDisable(true);
        buttonI.setDisable(true);
        buttonJ.setDisable(true);
        buttonK.setDisable(true);
        buttonL.setDisable(true);
        buttonM.setDisable(true);
        buttonN.setDisable(true);
        buttonO.setDisable(true);
        buttonP.setDisable(true);
        buttonQ.setDisable(true);
        buttonR.setDisable(true);
        buttonS.setDisable(true);
        buttonT.setDisable(true);
        buttonU.setDisable(true);
        buttonV.setDisable(true);
        buttonW.setDisable(true);
        buttonX.setDisable(true);
        buttonY.setDisable(true);
        buttonZ.setDisable(true);
    }


    private void resetHangmanImage() {
        hangmanImageView.setImage(new Image(getClass().getResource("/com/example/btl1_dictionary/Image/hangman/hangman0.png").toExternalForm()));
    }


    @FXML
    private void handleGuess(javafx.event.ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button button = (Button) event.getSource();
            String letter = button.getText().toUpperCase();
            processGuess(letter);
        }
    }

    @FXML
    private void handleVirtualKeyboardClick(javafx.event.ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (!clickedButton.isDisable()) {
                String letter = clickedButton.getText().toUpperCase();
                processGuess(letter);
                clickedButton.setDisable(true);
            }
        }
    }

    private void processGuess(String letter) {
        if (!currentWord.toString().contains(letter)) {
            boolean correctGuess = false;
            for (int i = 0; i < selectedWord.length(); i++) {
                if (selectedWord.charAt(i) == letter.charAt(0)) {
                    currentWord.setCharAt(i, letter.charAt(0));
                    correctGuess = true;
                }
            }

            if (!correctGuess) {
                handleIncorrectGuess();
            }

            updateWordLabel();

            if (currentWord.toString().equals(selectedWord)) {
                guessLabel.setText("Congratulations! You guessed the word.");
                disableAllVirtualKeyboardButtons();
            }
        }
    }

    private void handleIncorrectGuess() {
        incorrectGuessCount++;
        updateHangmanImage();

        if (incorrectGuessCount >= MAX_INCORRECT_GUESSES) {
            // Handle game over (e.g., display a message)
            disableAllVirtualKeyboardButtons();
            guessLabel.setText("Game Over! The word was: " + selectedWord);
//            disableLetterButtons(); // Disable letter buttons after the game is over
        }
    }

    private void updateHangmanImage() {
        String imagePath = "/com/example/btl1_dictionary/Image/hangman/" + "hangman" + incorrectGuessCount + ".png";
        hangmanImageView.setImage(new Image(getClass().getResource(imagePath).toExternalForm()));
    }

//    private void disableLetterButtons() {
//        // Disable all letter buttons after the game is over
//        // You can iterate through the buttons and disable them
//        // Example: button.setDisable(true);
//    }
}