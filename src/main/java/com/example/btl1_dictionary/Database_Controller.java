package com.example.btl1_dictionary;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.naming.spi.DirStateFactory.Result;
import javax.swing.text.html.parser.Entity;

class Word {
    public int id;
    public String word;
    public String meaning;
    public String pronunciation;

    public Word() {
        id = 0;
        word = "";
        meaning = "";
        pronunciation = "";
    }

    public Word(int id, String word, String meaning, String pronunciation) {
        this.id = id;
        this.word = word;
        this.meaning = meaning;
        this.pronunciation = pronunciation;
    }
}

public class Database_Controller {

    static Connection connection = null;

    public static String GetWordFromDatabase(String input) throws Exception {
        StringBuilder res = new StringBuilder();
        String hmtlans = "";
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        List<Word> words = new ArrayList<>();

        try (Connection connection1 = DriverManager.getConnection(
                "jdbc:sqlite:D:\\Intell\\BTL1_Dictionary\\src\\main\\resources\\com\\example\\btl1_dictionary\\Database\\dict_hh.db");) {
            if (connection1 != null) {
                connection = connection1;
            }
            System.out.println(connection1);
            String querry = String.format("SELECT * FROM av WHERE word LIKE '%s'", input);
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(querry);
            ResultSet resultSet = preparedStatement.executeQuery();
            int i = 0;

            while (resultSet.next() && i < 10) {
                int id = resultSet.getInt(1);
                String word = resultSet.getString(2);
                String meaning = resultSet.getString(3);
                String pro = resultSet.getString(5);
                hmtlans = resultSet.getString(3);
                i++;
                Word w = new Word(id, word, meaning, pro);
                words.add(w);
            }
            boolean first = true;
            for (Word w : words) {
                if (first) {
                    res.append(w.meaning);
                    first = false;
                    continue;
                }
                res.append(w.meaning.substring(16 + w.word.length()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res.toString();
    }


}

