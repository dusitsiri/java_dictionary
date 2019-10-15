package sqlite_database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import models.Vocabulary;

import java.sql.*;
import java.util.ArrayList;

public class SqliteConnection {
    private static Connection connection;
    private static final String URL = "jdbc:sqlite:vocabulary.db";

    public Connection connect(){
        try{
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(URL);
            if (!connection.isClosed()){
                System.out.println("Connected to sqlite");
            } else{
                System.out.println("Connect failed");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public int getMaxId(){
        int maxId = 0;
        try{
            Statement statement = connection.createStatement();
            String query = "select max(id) from vocabulary";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                maxId = resultSet.getInt(1);
                return maxId+1;
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return 1;
    }

    public boolean insert(Vocabulary vocabulary){
        try{
            String query = "insert into vocabulary (id, word, type, meaning) values " +
                    "(\'"+vocabulary.getId()+"\', \'"+vocabulary.getWord()+"\', \'"+vocabulary.getType()+"\', \'"+vocabulary.getMeaning()+"\')";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();
            System.out.println("Insertion success");
            return true;
        } catch (SQLException e){
            System.err.println("Duplicated word");
        }
        return false;
    }

    public void delete(int id){
        try{
            String query = "delete from vocabulary where id == \'"+ id + "\' ";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();
            System.out.println("Deletion success");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void update(Vocabulary vocabulary){
        try{
            String query = "update vocabulary set " +
                    "meaning=\'"+vocabulary.getMeaning()+"\'" +
                    "type=\'"+vocabulary.getType()+"\' where id == \'"+vocabulary.getId()+"\'";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();
            System.out.println("Updation success");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public ObservableList<Vocabulary> getData(){
        ObservableList<Vocabulary> dictionaries = FXCollections.observableArrayList();
        try{
            Statement statement = connection.createStatement();
            String query = "select * from vocabulary";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String word = resultSet.getString("word");
                String type = resultSet.getString("type");
                String meaning = resultSet.getString("meaning");
                dictionaries.add(new Vocabulary(id, word, type, meaning));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return dictionaries;
    }
}
