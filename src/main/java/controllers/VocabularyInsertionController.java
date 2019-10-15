package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Vocabulary;
import sqlite_database.SqliteConnection;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Optional;

public class VocabularyInsertionController {
    private static SqliteConnection sqliteConnection = new SqliteConnection();
    private static Connection connection = sqliteConnection.connect();

    @FXML
    private Label dateTimeLabel;
    @FXML
    private TableView<Vocabulary> dictionaryTableView;
    @FXML
    private TextField wordTextField, meaningTextField;
    @FXML
    private ComboBox typeWordBox;
    @FXML
    private Button addButton, deleteButton, cancelButton, myDictionaryButton;

    private String date = LocalDateTime.now().toLocalDate().toString();

    private boolean typeCheck = false;

    public void initialize() {
        clearTextFields();
        setDate();
        ObservableList<String> list = FXCollections.observableArrayList("noun", "pronoun", "adjective", "adverb", "verb", "conjunction", "interjection");
        typeWordBox.setItems(list);

        typeWordBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (typeWordBox.getSelectionModel().getSelectedItem() != "select type"){
                    typeCheck = true;
                }
            }
        });
        meaningTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER && wordTextField.getText() != null && typeCheck && meaningTextField.getText() != null) {
                    handle_AddButton();
                }
            }
        });
    }

    public void setDate() {
        String day = LocalDateTime.now().getDayOfWeek().name();
        String day1 = day.substring(0, 1);
        String day2 = day.substring(1, day.length()).toLowerCase();
        day = day1 + day2;
        dateTimeLabel.setText("Today is " + day + " " + date);
    }

    public void handle_AddButton() {
        if (wordTextField.getText().matches("[a-zA-Z]+") && meaningTextField.getText().length() > 0) {
            Vocabulary vocabulary = new Vocabulary(sqliteConnection.getMaxId(), wordTextField.getText(), typeWordBox.getSelectionModel().getSelectedItem().toString(), meaningTextField.getText());
            if (sqliteConnection.insert(vocabulary)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Success", ButtonType.CLOSE);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get().equals(ButtonType.CLOSE)) {
                    clearTextFields();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Your word is duplicate", ButtonType.CLOSE);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get().equals(ButtonType.CLOSE)) {
                    clearTextFields();
                }
            }
        } else if (wordTextField.getText().length() > 0 && !typeCheck && meaningTextField.getText().length() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select type and fill meaning word", ButtonType.OK);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                meaningTextField.requestFocus();
            }
        } else if (wordTextField.getText().length() > 0 && typeCheck && meaningTextField.getText().length() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill meaning of word", ButtonType.OK);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                wordTextField.requestFocus();
            }
        } else if (wordTextField.getText().length() == 0 && typeCheck && meaningTextField.getText().length() > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill your word");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                clearTextFields();
            }
        } else if (wordTextField.getText().length() == 0 && !typeCheck && meaningTextField.getText().length() > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill word and select type");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                clearTextFields();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill word");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                wordTextField.clear();
                wordTextField.requestFocus();
            }
        }
    }

    public void handle_DeleteButton(ActionEvent event) {
        if (dictionaryTableView.getSelectionModel().getSelectedItem() != null) {
            int id = dictionaryTableView.getSelectionModel().getSelectedItem().getId();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like you delete?", ButtonType.OK, ButtonType.CANCEL);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                sqliteConnection.delete(id);
                clearTextFields();
            }
        } else if (dictionaryTableView.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select your word before delete.", ButtonType.OK);
            alert.showAndWait();
        }

    }

    public void handle_EditButton() {
        if (dictionaryTableView.getSelectionModel().getSelectedItem() != null) {
            wordTextField.setText(dictionaryTableView.getSelectionModel().getSelectedItem().getWord());
            meaningTextField.setText(dictionaryTableView.getSelectionModel().getSelectedItem().getMeaning());
        } else if (dictionaryTableView.getSelectionModel().getSelectedItem() == null) {
            Alert aler = new Alert(Alert.AlertType.WARNING, "Please select your word before edit");
            aler.showAndWait();
        }
    }

    public void clearTextFields() {
        dictionaryTableView.setItems(sqliteConnection.getData());
        wordTextField.clear();
        meaningTextField.clear();
        dictionaryTableView.getSelectionModel().clearSelection();
        wordTextField.requestFocus();
    }

//    public void show_My_Dictionary(ActionEvent event){
//        Button button = (Button) event.getSource();
//        Stage stage = (Stage) button.getScene().getWindow();
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("../dictionary.fxml"));
//        try{
//            stage.setScene(new Scene((Parent) loader.load(), 740, 483));
//            DictionariesController dictionariesController = (DictionariesController) loader.getController();
//            dictionariesController.setTableview(sqliteConnection.getData());
//            stage.show();
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//    }

//    public void handle_CancelButton(ActionEvent event){
//        try{
//            Thread.sleep(1000);
//            System.exit(0);
//        } catch (InterruptedException e){
//            e.printStackTrace();
//        }
//    }

}
