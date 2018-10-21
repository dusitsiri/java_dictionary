import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Vocabulary;
import sqlite_database.SqliteConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("vocabulary.fxml"));
        primaryStage.setTitle("My Dictionary");
        primaryStage.setScene(new Scene(root, 1007, 437));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }



    //use when you don't use fxml
//    public static void submit_Add_Vocabulary(Vocabulary vocabulary) {
////        if (sqliteConnection.insert(vocabulary.getWord(), vocabulary.getMeaning())){
////            System.out.println("Insertion success");
////        }
////    }

//    public static void main(String[] args) {
//        receive_Input();
//        Scanner sc = new Scanner(System.in);
//        System.out.print("Add word: ");
//        int again = sc.nextInt();
//        if (again == 0){
//            System.out.println("end program");
//        } else {
//            receive_Input();
//        }
//    }
//
//    public static void receive_Input() {
//        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
//        try {
//            System.out.print("Enter your word: ");
//            String word = bf.readLine();
//            System.out.print("Enter your meaning: ");
//            String meaning = bf.readLine();
//            System.out.print("Are you sure?: ");
//            String submit = bf.readLine();
//
//            String REGEX = "[Yy]es|[Yy]ES|1|[oO][kK]";
//            while (true) {
//                if (submit.matches(REGEX)) {
//                    submit_Add_Vocabulary(new Vocabulary(word, meaning));
//                    break;
//                } else {
//                    System.out.print("Are you sure?: ");
//                    submit = bf.readLine();
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
