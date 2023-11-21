package com.example.btl1_dictionary;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    @FXML
    private Button buttonA;

    @FXML
    private Button buttonB;

    @FXML
    private Button buttonC;

    @FXML
    private Button buttonD;

    @FXML
    private Button buttonE;

    @FXML
    private Button buttonF;

    @FXML
    private Button buttonG;

    @FXML
    private Button buttonH;

    @FXML
    private Button buttonI;

    @FXML
    private Button buttonJ;

    @FXML
    private Button buttonK;

    @FXML
    private Button buttonL;

    @FXML
    private Button buttonM;

    @FXML
    private Button buttonN;

    @FXML
    private Button buttonO;

    @FXML
    private Button buttonP;

    @FXML
    private Button buttonQ;

    @FXML
    private Button buttonR;

    @FXML
    private Button buttonS;

    @FXML
    private Button buttonT;

    @FXML
    private Button buttonU;

    @FXML
    private Button buttonV;

    @FXML
    private Button buttonW;

    @FXML
    private Button buttonX;

    @FXML
    private Button buttonY;

    @FXML
    private Button buttonZ;

    @FXML
    private TextField Number;

    @FXML
    private TextField Score;




    private List<String> words = new ArrayList<>();
    private List<Button> buttons = new ArrayList<>();
    private String selectedWord;
    private StringBuilder currentWord;
    private int incorrectGuessCount;
    private static final int MAX_INCORRECT_GUESSES = 6;

    int index = 0;
    int score = 0;
    int numberWord = 0;

    int wordLeft = 0;
    int number = 1;

    public void initialize() throws IOException {
        readFile("src/main/resources/com/example/btl1_dictionary/Text File/Saved.txt", words);
        numberWord = words.size();
        wordLeft = numberWord;
        selectRandomWord();
        updateWordLabel();
        resetHangmanImage();
        resetLabels();
        incorrectGuessCount = 0;

        buttons.add(buttonA); buttons.add(buttonB); buttons.add(buttonC);
        buttons.add(buttonD); buttons.add(buttonE); buttons.add(buttonF);
        buttons.add(buttonG); buttons.add(buttonH); buttons.add(buttonI);
        buttons.add(buttonJ); buttons.add(buttonK); buttons.add(buttonL);
        buttons.add(buttonM); buttons.add(buttonN); buttons.add(buttonO);
        buttons.add(buttonP); buttons.add(buttonQ); buttons.add(buttonR);
        buttons.add(buttonS); buttons.add(buttonT); buttons.add(buttonU);
        buttons.add(buttonV); buttons.add(buttonW); buttons.add(buttonX);
        buttons.add(buttonY); buttons.add(buttonZ);
    }

    private void selectRandomWord() {
        Random random = new Random();
        index = random.nextInt(wordLeft);
        selectedWord = words.get(index).toUpperCase();
        currentWord = new StringBuilder("_".repeat(selectedWord.length()));
    }

    private void updateWordLabel() {
        wordLabel.setText(currentWord.toString());
    }

    private void resetLabels() {
        guessLabel.setText("Guess a letter:");
        for (Button button : buttons) {
            button.setVisible(true);
        }

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
            Button button = (Button) event.getSource();
            String letter = button.getText().toUpperCase();
            processGuess(letter);
            button.setVisible(false);
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
            }
        }
    }

    private void handleIncorrectGuess() {
        incorrectGuessCount++;
        updateHangmanImage();

        if (incorrectGuessCount >= MAX_INCORRECT_GUESSES) {
            // Handle game over (e.g., display a message)
            guessLabel.setText("Game Over! The word was: " + selectedWord);
//            disableLetterButtons(); // Disable letter buttons after the game is over
        }
    }

    private void updateHangmanImage() {
        String imagePath = "/com/example/btl1_dictionary/Image/hangman/" + "hangman" + incorrectGuessCount + ".png";
        hangmanImageView.setImage(new Image(getClass().getResource(imagePath).toExternalForm()));
    }


    @Override
    void Next(MouseEvent event) {
        words.remove(index);
        selectRandomWord();
        updateWordLabel();
        resetHangmanImage();
        resetLabels();
        incorrectGuessCount = 0;
        number++;
        Number.setText(number + "/" + numberWord);
    }
}