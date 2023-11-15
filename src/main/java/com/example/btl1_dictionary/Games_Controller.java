package com.example.btl1_dictionary;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Games_Controller extends General_Controller {
    private static final String wordsListPath = "src/main/resources/com/example/btl1_dictionary/hangmanWordsList.txt";

    @FXML
    private Text hangmanTextArea;

    @FXML
    private TextField guess;

    @FXML
    private Text textForWord;

    @FXML
    private Text endOfGameText;

    private String word;

    private StringBuilder secretWord = new StringBuilder();

    private int livesPos = 0;

    ArrayList<String> hangManLives = new ArrayList<>(Arrays.asList(
            """
            +---+
            |   |
                |
                |
                |
                |
          ========""",
            """
            +---+
            |   |
            O   |
                |
                |
                |
          ========""",
            """
            +---+
            |   |
            O   |
            |   |
                |
                |
          ========""",
            """
            +---+
            |   |
            O   |
           /|   |
                |
                |
          ========""",
            """
            +---+
            |   |
            O   |
           /|\\  |
                |
                |
          ========""",
            """
            +---+
            |   |
            O   |
           /|\\  |
           /    |
                |
          ========""",
            """
            +---+
            |   |
            O   |
           /|\\  |
           / \\  |
                |
          ========"""
    ));

    private static String randomWord(String filePath) {
        List<String> wordsList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                wordsList.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (wordsList.isEmpty()) {
            System.out.println("Words List is null");
            return null;
        } else {
            Random random = new Random();
            int randomIndex = random.nextInt(wordsList.size());
            return wordsList.get(randomIndex);
        }
    }

    public void initialize() {
        word = randomWord(wordsListPath);
        livesPos = 0;
        setupWord();
    }

    @FXML
    void getTextInput(ActionEvent event) {
        playTurn();
    }

    public void setupWord(){
        secretWord = new StringBuilder();
        hangmanTextArea.setText(hangManLives.get(livesPos));
        int wordLength = word.length();
        secretWord.append("-".repeat(wordLength));
        textForWord.setText(String.valueOf(secretWord));
    }

    private boolean checking_status() {
        if (word.contentEquals(secretWord)) {
            return true;
        } else if (livesPos == 6) {
            return true;
        }
        return false;
    }

    public void playTurn(){
        String guess = this.guess.getText();
        ArrayList<Integer> positions = new ArrayList<>();
        char[] wordChars = word.toCharArray();
        char letterGuess = guess.charAt(0);

        if(checking_status()){
            System.out.println("you won");
            endOfGameText.setText("you won");
            return;
        }

        if(word.contains(guess)){
            for (int i = 0; i < word.length(); i++) {
                if(wordChars[i] == letterGuess){
                    positions.add(i);
                }
            }
            positions.forEach(pos ->{
                secretWord.setCharAt(pos,letterGuess);
            });

            textForWord.setText(String.valueOf(secretWord));
        } else {
            hangmanTextArea.setText(hangManLives.get(++livesPos));
            if(checking_status()){
                System.out.println("you lost");
                endOfGameText.setText("you lost. the correct word is: " + this.word);
            }
        }
    }

    @FXML
    void reset(ActionEvent event) {
        word = randomWord(wordsListPath);
        setupWord();
        livesPos = 0;
        hangmanTextArea.setText(hangManLives.get(livesPos));
        endOfGameText.setText("");
    }


    // đừng nói về hôm nay, đừng nói về sau này
    // khi lang thang ở góc công viên anh thấy bàn tay em là chất thôi miên
    // và anh có, 14 nghìn trong tay, là có đủ không đây

    @Override
    public void Entered(MouseEvent event) {
        ImageView enteredImageView = (ImageView) event.getSource();

        if (enteredImageView == getSearch_Button()) {
            setSearch_Button(Search_Image);
            setGame_Button(null);
        } else if (enteredImageView == getHistory_Button()) {
            setHistory_Button(History_Image);
            setGame_Button(null);
        } else if (enteredImageView == getEdit_Button()) {
            setEdit_Button(Edit_Image);
            setGame_Button(null);
        } else if (enteredImageView == getGoogle_Button()) {
            setGoogle_Button(Google_Image);
            setGame_Button(null);
        } else if (enteredImageView == getSaved_Button()) {
            setSaved_Button(Saved_Image);
            setGame_Button(null);
        }
    }

    @Override
    public void Exited(MouseEvent event) {
        ImageView exitedImageView = (ImageView) event.getSource();

        if (exitedImageView == getSearch_Button()) {
            setSearch_Button(null);
            setGame_Button(Game_Image);
        } else if (exitedImageView == getHistory_Button()) {
            setHistory_Button(null);
            setGame_Button(Game_Image);
        } else if (exitedImageView == getEdit_Button()) {
            setEdit_Button(null);
            setGame_Button(Game_Image);
        } else if (exitedImageView == getGoogle_Button()) {
            setGoogle_Button(null);
            setGame_Button(Game_Image);
        } else if (exitedImageView == getSaved_Button()) {
            setSaved_Button(null);
            setGame_Button(Game_Image);
        }
    }
}
