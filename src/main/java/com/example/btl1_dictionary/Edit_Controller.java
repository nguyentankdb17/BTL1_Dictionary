package com.example.btl1_dictionary;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.HTMLEditor;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Edit_Controller extends General_Controller {

    @FXML
    private AnchorPane Add;

    @FXML
    private ImageView Add_Button;

    @FXML
    private AnchorPane Delete;

    @FXML
    private ImageView Delete_Button;

    @FXML
    private ImageView Edit_Button;

    @FXML
    private ImageView Game_Button;

    @FXML
    private ImageView Google_Button;

    @FXML
    private ImageView Guide_Button;

    @FXML
    private ImageView Guide_Button1;

    @FXML
    private ImageView Guide_Button2;

    @FXML
    private ImageView History_Button;

    @FXML
    private AnchorPane Modify;

    @FXML
    private ImageView Modify_Button;

    @FXML
    private ImageView Saved_Button;

    @FXML
    private ImageView Search_Button;

    @FXML
    private HTMLEditor addView;

    @FXML
    private HTMLEditor deleteView;

    @FXML
    private ImageView delete_button;

    @FXML
    private HTMLEditor modifyView;

    @FXML
    private ImageView save_button;

    @FXML
    private ImageView add_button;

    @FXML
    private ImageView saved_button;

    @FXML
    public TextField searchBar;

    @FXML
    public TextField searchBar1;

    @FXML
    private ImageView search_button;

    @FXML
    private ImageView search_button1;

    @FXML
    private ImageView reset_button;

    @FXML
    private ImageView reset_button1;

    @FXML
    private final Image add_on = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/add_on.png").toExternalForm());

    @FXML
    private final Image add_off = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/add_off.png").toExternalForm());

    @FXML
    private final Image modify_on = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/modify_on.png").toExternalForm());

    @FXML
    private final Image modify_off = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/modify_off.png").toExternalForm());

    @FXML
    private final Image delete_on = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/delete_on.png").toExternalForm());

    @FXML
    private final Image delete_off = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/delete_off.png").toExternalForm());

    private List<String> historyList = new ArrayList<>();
    private List<String> savedList = new ArrayList<>();
    public void initialize() throws IOException {
        readFile(historyPath,historyList);
        readFile(savedPath,savedList);
    }

    @FXML
    void switchToAdd(MouseEvent event) {
        Add.setVisible(true);
        Add_Button.setImage(add_on);
        Modify.setVisible(false);
        Modify_Button.setImage(modify_off);
        Delete.setVisible(false);
        Delete_Button.setImage(delete_off);
    }

    @FXML
    void switchToDelete(MouseEvent event) {
        Add.setVisible(false);
        Add_Button.setImage(add_off);
        Modify.setVisible(false);
        Modify_Button.setImage(modify_off);
        Delete.setVisible(true);
        Delete_Button.setImage(delete_on);
    }

    @FXML
    void switchToModify(MouseEvent event) {
        Add.setVisible(false);
        Add_Button.setImage(add_off);
        Modify.setVisible(true);
        Modify_Button.setImage(modify_on);
        Delete.setVisible(false);
        Delete_Button.setImage(delete_off);
    }

    @FXML
    void Added(MouseEvent event) throws Exception {
        String htmlText = addView.getHtmlText();

        htmlText = htmlText.replace("<html dir=\"ltr\"><head></head><body contenteditable=\"true\">","");
        htmlText = htmlText.replace("<span style=\"font-family: &quot;&quot;;\">","");
        htmlText = htmlText.replace("</span>", "");
        htmlText = htmlText.replace("</body></html>", "");

        String html = htmlText;

        int i = htmlText.indexOf("</h1>");
        String word = htmlText.substring(4,i);

        htmlText = htmlText.substring(i);
        int j = htmlText.indexOf("</i>");
        String pronounce = htmlText.substring(13,j-1);

        Database_Connect.addWord(word,pronounce,html);

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(" Word Added Successfully");
        alert.setHeaderText(null);
        alert.setContentText("Bạn đã thêm từ thành công");
        alert.showAndWait();
    }

    @FXML
    void Modified(MouseEvent event) {
        String word = searchBar.getText();
        String htmlText = modifyView.getHtmlText();
        htmlText = htmlText.replace("<html dir=\"ltr\"><head></head><body contenteditable=\"true\">","");
        htmlText = htmlText.replace("<span style=\"font-family: &quot;&quot;;\">","");
        htmlText = htmlText.replace("</span>", "");
        htmlText = htmlText.replace("</body></html>", "");

        String html = htmlText;

        Database_Connect.makeModify(word,htmlText);

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(" Word Modified Successfully");
        alert.setHeaderText(null);
        alert.setContentText("Bạn đã chỉnh sửa từ thành công");
        alert.showAndWait();
    }

    @FXML
    void Deleted(MouseEvent event) throws IOException {
        String input = searchBar1.getText();
        Database_Connect.deleteWord(input);

        AtomicBoolean userConfirmed = new AtomicBoolean(false);

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setContentText("Bạn có chắc chắn muốn xóa từ này ?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                userConfirmed.set(true);
            }
        });

        if (userConfirmed.get()) {
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setTitle("Word Deleted Successfully");
            alert2.setHeaderText(null);
            alert2.setContentText("Bạn đã xóa từ thành công");
            alert2.showAndWait();

            Trie.delete(input);

            historyList.removeIf(e -> e.equals(input));
            writeToFile(historyPath,historyList);
            savedList.removeIf(e -> e.equals(input));
            writeToFile(savedPath,savedList);
        }
    }

    @FXML
    void Guide(MouseEvent event) {

    }

    @FXML
    void Reset(MouseEvent event) throws Exception {
        ImageView clicked = (ImageView) event.getSource();
        if (clicked == reset_button) {
            addView.setHtmlText("<h1>#nhập từ</h1><h3><i>/#nhập cách phát âm/</i></h3><h2>#nhập loại từ</h2><ul><li>#nhập nghĩa<ul style=\"list-style-type:circle\"><li>#nhập ví dụ tiếng Anh:<i>&nbsp;#nhập ví dụ tiếng Việt</i></li><li>#nhập ví dụ tiếng Anh:<i>&nbsp;#nhập ví dụ tiếng Việt</i></li><li>#nhập ví dụ tiếng Anh:<i>&nbsp;#nhập ví dụ tiếng Việt</i></li></ul></li><li>#nhập nghĩa<ul><li>#nhập ví dụ tiếng Anh:<i>&nbsp;#nhập ví dụ tiếng Việt</i></li><li>#nhập ví dụ tiếng Anh:<i>&nbsp;#nhập ví dụ tiếng Việt</i></li></ul></li><li>#nhập nghĩa<ul><li>#nhập ví dụ tiếng Anh:<i>&nbsp;#nhập ví dụ tiếng Việt</i></li></ul></li></ul><h2>#nhập loại từ</h2><ul><li>#nhập nghĩa<ul><li>#nhập ví dụ tiếng Anh:<i>&nbsp;#nhập ví dụ tiếng Việt</i></li><li>#nhập ví dụ tiếng Anh:<i>&nbsp;#nhập ví dụ tiếng Việt</i></li></ul></li><li>#nhập nghĩa<ul><li>#nhập ví dụ tiếng Anh:<i>&nbsp;#nhập ví dụ tiếng Việt</i></li></ul></li></ul></body></html>");
        } else if (clicked == reset_button1) {
            Database_Connect.lookUpDatabase(searchBar1.getText(),true);
            modifyView.setHtmlText(Database_Connect.both);
        }
    }

    @FXML
    void Search(MouseEvent event) throws Exception {
        ImageView clicked = (ImageView) event.getSource();
        String input = "";
        if (clicked == search_button) {
            input = searchBar.getText();
            Database_Connect.lookUpDatabase(input,true);
            modifyView.setHtmlText(Database_Connect.both);
        } else if (clicked == search_button1) {
            input = searchBar1.getText();
            Database_Connect.lookUpDatabase(input,true);
            deleteView.setHtmlText(Database_Connect.both);
        }
    }

    @Override
    public void Entered(MouseEvent event) {
        ImageView enteredImageView = (ImageView) event.getSource();

        if (enteredImageView == getSearch_Button()) {
            setSearch_Button(Search_Image);
            setEdit_Button(null);
        } else if (enteredImageView == getHistory_Button()) {
            setHistory_Button(History_Image);
            setEdit_Button(null);
        } else if (enteredImageView == getSaved_Button()) {
            setSaved_Button(Saved_Image);
            setEdit_Button(null);
        } else if (enteredImageView == getGoogle_Button()) {
            setGoogle_Button(Google_Image);
            setEdit_Button(null);
        } else if (enteredImageView == getGame_Button()) {
            setGame_Button(Game_Image);
            setEdit_Button(null);
        }
    }

    @Override
    public void Exited(MouseEvent event) {
        ImageView exitedImageView = (ImageView) event.getSource();

        if (exitedImageView == getSearch_Button()) {
            setSearch_Button(null);
            setEdit_Button(Edit_Image);
        } else if (exitedImageView == getHistory_Button()) {
            setHistory_Button(null);
            setEdit_Button(Edit_Image);
        } else if (exitedImageView == getSaved_Button()) {
            setSaved_Button(null);
            setEdit_Button(Edit_Image);
        } else if (exitedImageView == getGoogle_Button()) {
            setGoogle_Button(null);
            setEdit_Button(Edit_Image);
        } else if (exitedImageView == getGame_Button()) {
            setGame_Button(null);
            setEdit_Button(Edit_Image);
        }
    }
}
