//package controllers;
//
//import javafx.collections.ObservableList;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.TableView;
//import javafx.stage.Stage;
//import models.Vocabulary;
//import sqlite_database.SqliteConnection;
//
//import java.io.IOException;
//import java.util.ArrayList;
//
//public class DictionariesController {
//   ;
//   @FXML
//   private TableView<Vocabulary> dictionayTableView;
//
//    public void setTableview(ObservableList<Vocabulary> vocabularyInDatabase){
//
//    }
//
//    public void handle_Back_Button(ActionEvent event){
//        Button button = (Button) event.getSource();
//        Stage stage = (Stage) button.getScene().getWindow();
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("../vocabulary.fxml"));
//        try{
//            stage.setScene(new Scene((Parent) loader.load(), 307, 403));
//            stage.show();
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//    }
//}
