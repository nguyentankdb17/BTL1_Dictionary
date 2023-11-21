package com.example.btl1_dictionary;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class Search_Controller extends General_Controller {

    @FXML
    public TextField searchBar;

    @FXML
    private ImageView voice;

    @FXML
    private ImageView en_vi;

    @FXML
    private ImageView vi_en;

    @FXML
    private ImageView saved;

    @FXML
    private ImageView edit;

    @FXML
    private ImageView delete;

    @FXML
    private final Image Saved_Image_Off = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/star_black.png").toExternalForm());

    @FXML
    private final Image Saved_Image_On = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/star.png").toExternalForm());

    @FXML
    private final Image en_vi_on = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/en-vi-on.png").toExternalForm());

    @FXML
    private final Image en_vi_off = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/en-vi-off.png").toExternalForm());

    @FXML
    private final Image vi_en_on = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/vi-en-on.png").toExternalForm());

    @FXML
    private final Image vi_en_off = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/vi-en-off.png").toExternalForm());


    @FXML
    private WebView word;

    @FXML
    private WebView meaning;

    @FXML
    private ListView<String> suggestion;

    private List<String> suggestionEng = new ArrayList<String>();

    private List<String> suggestionVie = new ArrayList<String>();

    @FXML
    private ImageView searchButton;

    private boolean isSaved = false;

    private boolean isEnglish = true;

    private List<String> savedList = new ArrayList<>();
    private List<String> historyList = new ArrayList<>();

    public TextField getSearchBar() {
        return searchBar;
    }

    /**
     * initialize.
     */
    public void initialize() {
        Database_Connect.loadSuggestions();
        Database_Connect.loadSuggestionsViet();
    }

    /**
     * load suggestions.
     * @param input the input string
     */
    private void showSuggestions(String input, boolean isEnglish) {
        ObservableList<String> filteredSuggestions = FXCollections.observableArrayList();
        List<String> matchingPrefixSuggestions = new ArrayList<>();
        List<String> nonMatchingSuggestions = new ArrayList();

        if (isEnglish) {
            for (String suggestion : suggestionEng) {
                if (suggestion.toLowerCase().startsWith(input.toLowerCase())) {
                    matchingPrefixSuggestions.add(suggestion);
                } else if (suggestion.toLowerCase().contains(input.toLowerCase())) {
                    nonMatchingSuggestions.add(suggestion);
                }
            }
        } else {
            for (String suggestion : suggestionVie) {
                if (suggestion.toLowerCase().startsWith(input.toLowerCase())) {
                    matchingPrefixSuggestions.add(suggestion);
                } else if (suggestion.toLowerCase().contains(input.toLowerCase())) {
                    nonMatchingSuggestions.add(suggestion);
                }
            }
        }

        matchingPrefixSuggestions.sort((s1, s2) -> {
            String prefix = input.toLowerCase();
            return Integer.compare(s1.toLowerCase().indexOf(prefix), s2.toLowerCase().indexOf(prefix));
        });

        filteredSuggestions.addAll(matchingPrefixSuggestions);
        filteredSuggestions.addAll(nonMatchingSuggestions);

        suggestion.setItems(filteredSuggestions);
    }

    /**
     * custom font and size of listview.
     */
    public static class CustomListCellFactory implements Callback<ListView<String>, ListCell<String>> {
        @Override
        public ListCell<String> call(ListView<String> param) {
            return new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item);
                        setFont(Font.font("Segoe UI", FontWeight.EXTRA_LIGHT, 15));
                    }
                }
            };
        }
    }


    /**
     * when on key typed.
     * @param event the key event
     */
    @FXML
    public void onKeyTyped(KeyEvent event) {
        String input = searchBar.getText().toLowerCase();
        suggestion.setCellFactory(new CustomListCellFactory());

        Database_Connect.createSuggestions(input);

        suggestionEng = Database_Connect.suggestions;
        suggestionVie = Database_Connect.suggestionsVie;
        if (isEnglish) {
            suggestion.setItems(FXCollections.observableArrayList(suggestionEng));
        } else {
            suggestion.setItems(FXCollections.observableArrayList(suggestionVie));
        }

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            showSuggestions(newValue, isEnglish);
        });

        if (event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.DELETE) {
            String currentValue = searchBar.getText();
            showSuggestions(currentValue, isEnglish);
        }
    }

    /**
     * search the word.
     * @param event the mouse event
     */
    @FXML
    void search(MouseEvent event) throws Exception {
        isSaved = false;
        String input = searchBar.getText().toLowerCase();

        System.out.println(isEnglish);
        Database_Connect.lookUpDatabase(input,isEnglish);

        word.getEngine().loadContent(Database_Connect.word, "text/html");
        meaning.getEngine().loadContent(Database_Connect.meaning, "text/html");

        if (!Database_Connect.found) {
            voice.setVisible(false);
            saved.setVisible(false);
            edit.setVisible(false);
            delete.setVisible(false);
        } else {
            voice.setVisible(true);
            saved.setVisible(true);
            edit.setVisible(true);
            delete.setVisible(true);

            handleHistory(input);
            handleFrequency(input);
            handleSaved(input);
        }
    }

    /**
     * handle history when searching.
     * @param input the string input
     */
    void handleHistory(String input) throws IOException {
        String line = "";
        readFile(historyPath,historyList);

        int size = historyList.size();
        int MAX_LENGTH = 300;
        if (size > MAX_LENGTH) {
            while (size >= MAX_LENGTH) {
                historyList.remove(size - 1);
                size--;
            }
        }

        historyList.removeIf(e -> e.equals(input));
        historyList.add(0, input);

        writeToFile(historyPath,historyList);
    }

    /**
     * handle saved when searching.
     * @param input the string input
     */
    void handleSaved(String input) throws IOException {
        String line = "";
        BufferedReader br = new BufferedReader(new FileReader(savedPath));
        while ((line = br.readLine()) != null) {
            if (line.equals(input)) {
                isSaved = true;
            } else {
                if (!line.isEmpty()) {
                    savedList.add(line.trim());
                }
            }

        }
        br.close();

        if (isSaved) {
            saved.setImage(Saved_Image_On);
        } else {
            saved.setImage(Saved_Image_Off);
        }
    }

    /**
     * handle frequency when searching.
     * @param input the string input
     */
    void handleFrequency(String input) throws IOException {
        long currentTimeMillis = System.currentTimeMillis();
        Date currentDate = new Date(currentTimeMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateString = dateFormat.format(currentDate);

        List<Integer>frequencyList = new ArrayList<>();
        boolean getDate = true;
        String lastDate ="";
        String line = "";
        BufferedReader br = new BufferedReader(new FileReader(frequencyPath));
        while ((line = br.readLine()) != null) {
            if (!line.isEmpty()) {
                if (getDate) {
                    lastDate = line;
                    getDate = false;
                    continue;
                }
                frequencyList.add(Integer.parseInt(line.trim()));
            }
        }
        br.close();

        if (currentDateString.equals(lastDate)) {
            frequencyList.set(0,frequencyList.get(0)+1);
        } else {
            frequencyList.add(0,1);
            frequencyList.remove(frequencyList.size() - 1);
        }

        FileWriter fw = new FileWriter(frequencyPath);
        fw.write(currentDateString);
        fw.write("\n");
        for (Integer lineToWrite : frequencyList) {
            fw.write(lineToWrite.toString());
            fw.write("\n");
        }
        fw.close();
    }

    /**
     * Play the sound of the words.
     * @param event the mouse event
     */
    @FXML
    void Voice(MouseEvent event) {
        if (isEnglish) {
            System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
            Voice voice = VoiceManager.getInstance().getVoice("kevin16");
            if (voice != null) {
                voice.allocate();
                voice.speak(searchBar.getText());
                voice.deallocate();
            }
        } else {
            playSound(searchBar.getText(),isEnglish);
        }
    }


    /**
     * Save the word into saved list.
     * @param event the mouse event
     */
    @FXML
    void Save(MouseEvent event) throws IOException {
        String input = searchBar.getText().toLowerCase();
        if (isSaved) {
            isSaved = false;
            saved.setImage(Saved_Image_Off);
            savedList.removeIf(e -> e.equals(input));
        } else {
            isSaved = true;
            saved.setImage(Saved_Image_On);
            savedList.add(input);
        }
        writeToFile(savedPath,savedList);
    }

    @FXML
    void Fill(MouseEvent event) {
        int selectedIndex = suggestion.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0) {
            String selectedItem = suggestion.getItems().get(selectedIndex);
            searchBar.setText(selectedItem);
        }
    }


    /**
     * Edit the word.
     * @param event the mouse event
     */
    @FXML
    void Edit(MouseEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML File/edit.fxml"));
        Parent fxmlLoader = loader.load();
        ((Edit_Controller) loader.getController()).switchToModify(event);
        ((Edit_Controller) loader.getController()).searchBar.setText(searchBar.getText());
        ((Edit_Controller) loader.getController()).search_button.setOnMouseClicked(e -> {
            try {
                ((Edit_Controller) loader.getController()).Search(e);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader, 875, 650);
        stage.setTitle("DICTIONARY");
        stage.setScene(scene);
        stage.show();
    }


    /**
     * Delete the word.
     * @param event the mouse event
     */
    @FXML
    void Delete(MouseEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML File/edit.fxml"));
        Parent fxmlLoader = loader.load();
        ((Edit_Controller) loader.getController()).switchToDelete(event);
        ((Edit_Controller) loader.getController()).searchBar1.setText(searchBar.getText());
        ((Edit_Controller) loader.getController()).Search(event);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader, 875, 650);
        stage.setTitle("DICTIONARY");
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Switch language.
     * @param event the mouse event
     */
    @FXML
    void switchLanguage(MouseEvent event) {
        ImageView clickedImage = (ImageView) event.getSource();
        if (clickedImage == en_vi) {
            isEnglish = true;
            en_vi.setImage(en_vi_on);
            vi_en.setImage(vi_en_off);
        } else if (clickedImage == vi_en) {
            isEnglish = false;
            en_vi.setImage(en_vi_off);
            vi_en.setImage(vi_en_on);
        }
    }


    @Override
    public void Entered(MouseEvent event) {
        ImageView enteredImageView = (ImageView) event.getSource();

        if (enteredImageView == getHistory_Button()) {
            setHistory_Button(History_Image);
            setSearch_Button(null);
        } else if (enteredImageView == getSaved_Button()) {
            setSaved_Button(Saved_Image);
            setSearch_Button(null);
        } else if (enteredImageView == getEdit_Button()) {
            setEdit_Button(Edit_Image);
            setSearch_Button(null);
        } else if (enteredImageView == getGoogle_Button()) {
            setGoogle_Button(Google_Image);
            setSearch_Button(null);
        } else if (enteredImageView == getGame_Button()) {
            setGame_Button(Game_Image);
            setSearch_Button(null);
        }
    }

    @Override
    public void Exited(MouseEvent event) {
        ImageView exitedImageView = (ImageView) event.getSource();

        if (exitedImageView == getHistory_Button()) {
            setHistory_Button(null);
            setSearch_Button(Search_Image);
        } else if (exitedImageView == getSaved_Button()) {
            setSaved_Button(null);
            setSearch_Button(Search_Image);
        } else if (exitedImageView == getEdit_Button()) {
            setEdit_Button(null);
            setSearch_Button(Search_Image);
        } else if (exitedImageView == getGoogle_Button()) {
            setGoogle_Button(null);
            setSearch_Button(Search_Image);
        } else if (exitedImageView == getGame_Button()) {
            setGame_Button(null);
            setSearch_Button(Search_Image);
        }
    }
}
