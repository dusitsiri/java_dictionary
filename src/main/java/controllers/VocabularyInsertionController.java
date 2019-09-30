package controllers;

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
    private Button addButton, deleteButton, cancelButton, myDictionaryButton;

    private String date = LocalDateTime.now().toLocalDate().toString();
    private Stage stage;
    private boolean switchRow = false;

    public void initialize() {
        clearTextFields();
        setDate();

//        dictionaryTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                if (event.getClickCount() == 2) {
//                    Stage popupwindow = new Stage();
//                    popupwindow.initModality(Modality.APPLICATION_MODAL);
//
//                    popupwindow.setTitle("Edit Vocabulary");
//                    Label label0 = new Label("id: " + dictionaryTableView.getSelectionModel().getSelectedItem().getId());
//                    Label label1 = new Label("Word: " + dictionaryTableView.getSelectionModel().getSelectedItem().getWord());
//                    Label label2 = new Label("Meaning");
//                    TextField textField2 = new TextField(dictionaryTableView.getSelectionModel().getSelectedItem().getMeaning());
//                    Button button1 = new Button("Ok");
//
//
//                    button1.setOnAction(e -> popupwindow.close());
//                    VBox layout = new VBox(10);
//                    layout.getChildren().addAll(label0, label1, label2, textField2, button1);
//                    layout.setAlignment(Pos.CENTER);
//                    Scene scene1 = new Scene(layout, 500, 400);
//                    popupwindow.setScene(scene1);
//                    popupwindow.showAndWait();
//
//                    button1.setOnMouseClicked(new EventHandler<MouseEvent>() {
//                        @Override
//                        public void handle(MouseEvent event) {
//                            if (event.getClickCount() == 1) {
//                                System.out.println("yes");
//                                System.out.println(label0.getText().substring(4));
//                                Vocabulary vocabulary = new Vocabulary(Integer.parseInt(label0.getText().substring(4)), label1.getText().substring(6), textField2.getText());
//                                sqliteConnection.update(vocabulary);
//                            }
//                        }
//                    });
//                }
//            }
//        });

        meaningTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER && (meaningTextField.getText() != null && wordTextField.getText() != null)) {
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
            Vocabulary vocabulary = new Vocabulary(sqliteConnection.getMaxId(), wordTextField.getText(), meaningTextField.getText());
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
        } else if (wordTextField.getText().length() > 0 && meaningTextField.getText().length() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill meaning of word", ButtonType.OK);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                meaningTextField.requestFocus();
            }
        } else if (wordTextField.getText().length() == 0 && meaningTextField.getText().length() > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill your word", ButtonType.OK);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                wordTextField.requestFocus();
            }
        } else if (wordTextField.getText().length() == 0 && meaningTextField.getText().length() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill word and meaning");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                clearTextFields();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Wrong word form");
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
