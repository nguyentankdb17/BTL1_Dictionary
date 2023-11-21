package com.example.btl1_dictionary;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;

public abstract class General_Controller {

    @FXML
    private VBox Main_Box;

    @FXML
    private ImageView Background;

    @FXML
    private ImageView Game_Button;

    public Stage stage;
    public  Scene scene;

    private SingleSelectionModel<Scene> sceneModel;

    public ImageView getGame_Button() {
        return Game_Button;
    }

    public void setGame_Button(Image image) {
        Game_Button.setImage(image);
    }

    @FXML
    public final Image Game_Image = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/Games_button.png").toExternalForm());


    @FXML
    private ImageView Google_Button;

    public ImageView getGoogle_Button() {
        return Google_Button;
    }

    public void setGoogle_Button(Image image) {
        Google_Button.setImage(image);
    }

    @FXML
    public final Image Google_Image = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/Google_button.png").toExternalForm());

    @FXML
    private ImageView History_Button;

    public ImageView getHistory_Button() {
        return History_Button;
    }

    public void setHistory_Button(Image image) {
        History_Button.setImage(image);
    }

    @FXML
    public final Image History_Image = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/History_button.png").toExternalForm());

    @FXML
    private ImageView Saved_Button;

    public ImageView getSaved_Button() {
        return Saved_Button;
    }

    public void setSaved_Button(Image image) {
        Saved_Button.setImage(image);
    }

    @FXML
    public final Image Saved_Image = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/Saved_button.png").toExternalForm());

    @FXML
    private ImageView Search_Button;

    public ImageView getSearch_Button() {
        return Search_Button;
    }

    public void setSearch_Button(Image image) {
        Search_Button.setImage(image);
    }

    @FXML
    public final Image Search_Image = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/Search_button.png").toExternalForm());

    @FXML
    private ImageView Edit_Button;

    public ImageView getEdit_Button() {
        return Edit_Button;
    }

    public void setEdit_Button(Image image) {
        Edit_Button.setImage(image);
    }

    @FXML
    public final Image Edit_Image = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/Edit_button.png").toExternalForm());

    @FXML
    public final Image Micro_Image = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/Micro_Button.png").toExternalForm());

    @FXML
    public final Image Micro_On_Image = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/Micro_Button2.png").toExternalForm());


    String historyPath = "src/main/resources/com/example/btl1_dictionary/Text File/History.txt";
    String frequencyPath = "src/main/resources/com/example/btl1_dictionary/Text File/Frequency.txt";
    String savedPath = "src/main/resources/com/example/btl1_dictionary/Text File/Saved.txt";
    String dictionaryPath = "src/main/resources/com/example/btl1_dictionary/Database/dict_hh.db";

    public void writeToFile(String path, List<String> list) throws IOException {
        FileWriter fw = new FileWriter(path);
        for (String lineToWrite : list) {
            fw.write(lineToWrite);
            fw.write("\n");
        }
        fw.close();
    }

    public void readFile(String path, List<String> list) throws IOException {
        String line ="";
        BufferedReader br = new BufferedReader(new FileReader(path));
        while ((line = br.readLine()) != null) {
            if (!line.isEmpty()) {
                if (!line.equals(" ")) {
                    list.add(line.trim());
                }
            }
        }
        br.close();
    }

    void playSound(String in, boolean check) {
        try {
            String tmp = in.replace(" ", "%20");
            tmp = tmp.replace("\n", "%20");
            String apiUrl = (check) ? "https://api.voicerss.org/?key=331802f6088c4348b53f5cb3f553e3f3&hl=en-us&v=Chi&src="
                    : "https://api.voicerss.org/?key=331802f6088c4348b53f5cb3f553e3f3&hl=vi-vn&v=Odai&src=";
            apiUrl += tmp;
            URI uri = new URI(apiUrl);
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            byte[] data = inputStream.readAllBytes();

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(byteArrayInputStream);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            clip.start();

            Thread.sleep(clip.getMicrosecondLength() / 1000);

            clip.close();
            audioInputStream.close();
            byteArrayInputStream.close();
            inputStream.close();
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleFontsize(TextField textField) {
        int textLength = textField.getText().length();
        double textFieldWidth = textField.getWidth();
        Font font = textField.getFont();
        System.out.println(font.toString());
    }

    @FXML
    public void switchScene(String fxmlPath, MouseEvent event) {
        try {
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent fxmlLoader = FXMLLoader.load(getClass().getResource(fxmlPath));
            scene = new Scene(fxmlLoader, 875, 650);
            stage.setTitle("DICTIONARY");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleSceneSwitch(MouseEvent event) {
        ImageView clickedImageView = (ImageView) event.getSource();

        if (clickedImageView == Search_Button) {
            switchScene("FXML File/search.fxml", event);
        } else if (clickedImageView == Google_Button) {
            switchScene("FXML File/google.fxml", event);
        } else if (clickedImageView == Game_Button) {
            switchScene("FXML File/games.fxml", event);
        } else if (clickedImageView == History_Button) {
            switchScene("FXML File/history.fxml", event);
        } else if (clickedImageView == Saved_Button) {
            switchScene("FXML File/saved.fxml", event);
        } else if (clickedImageView == Edit_Button) {
            switchScene("FXML File/edit.fxml", event);
        }
    }

    @FXML
    public abstract void Entered(MouseEvent event);

    @FXML
    public abstract void Exited(MouseEvent event);

}
