package com.example.btl1_dictionary;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.*;
import java.net.*;

public class Google_Controller {

    @FXML
    private VBox Main_Box;

    @FXML
    private ImageView Background;

    @FXML
    private ImageView Game_Button;

    @FXML
    private final Image Game_Image = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/Games_button.png").toExternalForm());

    @FXML
    private ImageView History_Button;

    @FXML
    private final Image History_Image = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/History_button.png").toExternalForm());

    @FXML
    private ImageView Saved_Button;

    @FXML
    private final Image Saved_Image = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/Saved_button.png").toExternalForm());

    @FXML
    private ImageView Search_Button;

    @FXML
    private final Image Search_Image = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/Search_button.png").toExternalForm());

    @FXML
    private ImageView langFrom;

    @FXML
    private final Image Vie = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/Vietnam_Button.png").toExternalForm());

    @FXML
    private ImageView langTo;

    @FXML
    private final Image Eng = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/English_Button.png").toExternalForm());


    @FXML
    private ImageView voice_from;

    @FXML
    private ImageView voice_to;

    @FXML
    private final Image Voice_Image = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/Voice_Button.png").toExternalForm());

    @FXML
    private TextArea input;

    @FXML
    private TextArea output;

    @FXML
    private ImageView Switch_Button;

    @FXML
    private ImageView STT;

    @FXML
    private Button Translate_Button;

    @FXML
    private Button Voice_Button;

    private boolean checked = true;

    private static String ConnectToGGAPI( String input, String languageFrom, String languageTo) throws IOException, IOException {
        String urlSource = "https://script.google.com/macros/s/AKfycby3AOWmhe32TgV9nW-Q0TyGOEyCHQeFiIn7hRgy5m8jHPaXDl2GdToyW_3Ys5MTbK6wjg/exec"
                        + "?q=" + URLEncoder.encode(input, "UTF-8")
                        + "&target=" + languageTo
                        + "&source=" + languageFrom;
        URL url = new URL(urlSource);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        StringBuilder res = new StringBuilder();
        String line;
        while ((line = bf.readLine()) != null) {
            res.append(line);
            res.append("\n");
        }
        bf.close();
        return res.toString();
    }

    @FXML
    void translate(MouseEvent event) throws IOException {
        String in = input.getText();
        voice_from.setImage(Voice_Image);
        voice_to.setImage(Voice_Image);
        String res = (checked) ? ConnectToGGAPI(in, "en", "vi") : ConnectToGGAPI(in, "vi", "en");
        output.setText(res);
    }

    @FXML
    void Voice_gg(MouseEvent event) throws IOException {
        ImageView clickedImageView = (ImageView) event.getSource();

        if (clickedImageView == voice_from) {
            playSound(input.getText(),checked);
        } else if (clickedImageView == voice_to) {
            playSound(output.getText().trim(),!checked);
        }
    }

    @FXML
    void speechToText(MouseEvent event) throws IOException {
        String pythonScriptPath = "D:\\Code_vsc\\Python_Basic\\Speech.py";
        String[] cmd = new String[2];
        cmd[0] = "python";
        cmd[1] = pythonScriptPath;
//        for(int i = 0; i < args.length; i++) {
//            cmd[i+2] = args[i];
//            System.out.println("tham so dong lenh la: " + args[i]);
//        }

// create runtime to execute external command
        System.out.println("ready to speech");
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec(cmd);

// retrieve output from python script
        BufferedReader bfr = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line = "";
        String resu = "";
        while((line = bfr.readLine()) != null) {
// display each output line form python script
            resu+=line;
            System.out.println(line);
        }
        input.setText(resu);
    }

    static void playSound(String in, boolean check) {
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


    @FXML
    void switchLanguage(MouseEvent event) {
        if (checked) {
            checked = false;
            langFrom.setImage(Vie);
            langTo.setImage(Eng);
        }
        else {
            checked = true;
            langFrom.setImage(Eng);
            langTo.setImage(Vie);
        }
    }

    @FXML
    void switchSceneToGame(MouseEvent event) {
        switchScene("FXML File/games.fxml",  event);
    }

    @FXML
    void switchSceneToHistory(MouseEvent event) {
        switchScene("FXML File/history.fxml", event);
    }

    @FXML
    void switchSceneToSearch(MouseEvent event) {
        switchScene("FXML File/search.fxml", event);
    }

    @FXML
    void switchSceneToSaved(MouseEvent event) {
        switchScene("FXML File/saved.fxml", event);
    }

    @FXML
    void handleSceneSwitch(MouseEvent event) {
        ImageView clickedImageView = (ImageView) event.getSource();

        if (clickedImageView == Search_Button) {
            switchSceneToSearch(event);
        } else if (clickedImageView == History_Button) {
            switchSceneToHistory(event);
        } else if (clickedImageView == Game_Button) {
            switchSceneToGame(event);
        } else if (clickedImageView == Saved_Button) {
            switchSceneToSaved(event);
        }
    }

    @FXML
    private void switchScene(String fxmlPath, MouseEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent fxmlLoader = FXMLLoader.load(getClass().getResource(fxmlPath));
            Scene scene = new Scene(fxmlLoader, 900, 620);
            stage.setTitle("3L DICTIONARY");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }

    @FXML
    void Entered(MouseEvent event) {
        ImageView enteredImageView = (ImageView) event.getSource();

        if (enteredImageView == Search_Button) {
            Search_Button.setImage(Search_Image);
        } else if (enteredImageView == History_Button) {
            History_Button.setImage(History_Image);
        } else if (enteredImageView == Game_Button) {
            Game_Button.setImage(Game_Image);
        } else if (enteredImageView == Saved_Button) {
            Saved_Button.setImage(Saved_Image);
        }
    }

    @FXML
    void Exited(MouseEvent event) {
        ImageView exitedImageView = (ImageView) event.getSource();

        if (exitedImageView == Search_Button) {
            Search_Button.setImage(null);
        } else if (exitedImageView == History_Button) {
            History_Button.setImage(null);
        } else if (exitedImageView == Game_Button) {
            Game_Button.setImage(null);
        } else if (exitedImageView == Saved_Button) {
            Saved_Button.setImage(null);
        }
    }


}
