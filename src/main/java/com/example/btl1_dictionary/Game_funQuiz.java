package com.example.btl1_dictionary;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game_funQuiz extends Games_Controller {

    static class Quiz {
        private int id;
        private String question;
        private String answer;
        private String explanation;
        private Image image;

        public Quiz(int id, String question, String answer, String explanation) {
            this.id = id;
            this.question = question;
            this.answer = answer;
            this.explanation = explanation;
        }

        public Image getImage() {
            return image;
        }

        public void setImage(Image image) {
            this.image = image;
        }
    }

    List<Quiz> quizzes = new ArrayList<>();
    List<String> questions = new ArrayList<>();
    List<String> answers = new ArrayList<>();
    List<String> explanations = new ArrayList<>();

    @FXML
    private TextField Number;

    @FXML
    private TextField Score;

    @FXML
    private TextField answer;

    @FXML
    private ImageView check;

    @FXML
    private TextArea explanation;

    @FXML
    private TextField nextStep;

    @FXML
    private TextField question;

    @FXML
    private ImageView next_button;

    @FXML
    private AnchorPane result;

    @FXML
    private ImageView showResult;

    @FXML
    private ImageView status;

    @FXML
    private AnchorPane submit;

    @FXML
    private ImageView submit_button;

    @FXML
    private TextField yourAnswer;

    @FXML
    private final Image thinking = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/funQuiz/thinking.png").toExternalForm());

    @FXML
    private final Image correct_answer = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/funQuiz/correct_answer.png").toExternalForm());

    @FXML
    private final Image congrat = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/funQuiz/congrat.png").toExternalForm());

    @FXML
    private final Image show_answer = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/funQuiz/button_show-answer.png").toExternalForm());

    @FXML
    private final Image show_explanation = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/funQuiz/button_show-explanation.png").toExternalForm());

    @FXML
    private final Image wrong_answer = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/funQuiz/wrong-answer-bro.png").toExternalForm());

    @FXML
    private final Image try_again = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/funQuiz/tryagain.png").toExternalForm());

    private boolean isTrue = false;

    int index = 0;
    int score = 0;
    int numberQuestion = 0;

    int questionLeft = 0;
    int number = 1;

    public void initialize() throws Exception {
        Database_Connect.loadQuiz();
        questions = Database_Connect.questions;
        answers = Database_Connect.answers;
        explanations = Database_Connect.explanations;
        numberQuestion = questions.size();
        questionLeft = numberQuestion;

        for (int i = 0; i < numberQuestion; i++) {
            Quiz quiz = new Quiz(i, questions.get(i), answers.get(i), explanations.get(i) == null ? "" : explanations.get(i));
            quizzes.add(quiz);
            setImage(i);
        }

        Random rand = new Random();
        index = rand.nextInt(numberQuestion);

        question.setText(quizzes.get(index).question.toUpperCase());
        Number.setText(number + "/" + numberQuestion);
    }


    void setImage(int id) {
        String path = "/com/example/btl1_dictionary/Image/funQUiz/Question" + String.valueOf(id+1) +  ".png";
        quizzes.get(id).setImage(new Image(getClass().getResource(path).toExternalForm()));
    }

    @FXML
    void Submit(MouseEvent event) {
        String answer = standardized(yourAnswer.getText());
        if (answer.equals(standardized(quizzes.get(index).answer))) {
            isTrue = true;
            score += 10;
            Score.setText(String.valueOf(score));
            nextStep.setText("NEXT");
            status.setImage(quizzes.get(index).image);
            showResult.setImage(show_explanation);
            check.setImage(congrat);
        } else {
            isTrue = false;
            status.setImage(wrong_answer);
            showResult.setImage(show_answer);
            check.setImage(try_again);
        }
    }

    @Override
    void Next(MouseEvent event) {
        submit.setVisible(true);
        result.setVisible(false);
        status.setImage(thinking);
        showResult.setImage(show_answer);
        check.setImage(null);
        yourAnswer.clear();
        quizzes.remove(index);
        questionLeft--;
        if (questionLeft == 1) {
            next_button.setVisible(false);
            nextStep.setVisible(false);
        }
        if (questionLeft == 0) {
            nextStep.setVisible(false);
        }
        Random rand = new Random();
        index = rand.nextInt(questionLeft);
        question.setText(quizzes.get(index).question.toUpperCase());
        nextStep.setText("SKIP");
        number++;
        Number.setText(number + "/" + numberQuestion);
    }

    @FXML
    void onKeyTyped(KeyEvent event) {
        status.setImage(thinking);
    }

    @FXML
    void showRes(MouseEvent event) {
        submit.setVisible(false);
        result.setVisible(true);
        status.setImage(quizzes.get(index).image);
        answer.setText(quizzes.get(index).answer.toUpperCase());
        explanation.setText(quizzes.get(index).explanation);
    }
}
