package com.example.btl1_dictionary;

import javafx.scene.image.Image;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * word structure.
 */
class Word {
    public int id;
    public String word;
    public String meaning;

    public String html;
    public String pronunciation;

    public Word() {
        id = 0;
        word = "";
        meaning = "";
        html = "";
        pronunciation = "";
    }

    public Word(int id, String word, String html, String meaning, String pronunciation) {
        this.id = id;
        this.word = word;
        this.meaning = meaning;
        this.html = html;
        this.pronunciation = pronunciation;
    }
}

/**
 * trie structure.
 */
class Trie {

    private static final ArrayList<String> searchedWords = new ArrayList<>();
    private static final TrieNode root = new TrieNode();

    public static ArrayList<String> getSearchedWords() {
        return searchedWords;
    }

    /**
     * Insert word `target` into Trie DS.
     *
     * @param target the word to insert
     */
    public static void insert(String target) {
        int length = target.length();

        TrieNode pCrawl = root;

        for (int i = 0; i < length; i++) {
            char index = target.charAt(i);

            if (pCrawl.children.get(index) == null) {
                pCrawl.children.put(index, new TrieNode());
            }

            pCrawl = pCrawl.children.get(index);
        }

        // Set `target` word ends at pCrawl
        pCrawl.isEndOfWord = true;
    }

    private static void GetWordsSubtree(TrieNode pCrawl, String target) {
        if (pCrawl.isEndOfWord) {
            searchedWords.add(target);
        }
        for (char index : pCrawl.children.keySet()) {
            if (pCrawl.children.get(index) != null) {
                GetWordsSubtree(pCrawl.children.get(index), target + index);
            }
        }
    }

    public static ArrayList<String> search(String prefix) {
        if (prefix.isEmpty()) {
            return new ArrayList<>();
        }
        searchedWords.clear();
        int length = prefix.length();
        TrieNode pC = root;

        for (int i = 0; i < length; i++) {
            char index = prefix.charAt(i);

            if (pC.children.get(index) == null) {
                return getSearchedWords();
            }

            pC = pC.children.get(index);
        }
        GetWordsSubtree(pC, prefix);
        return getSearchedWords();
    }

    public static void delete(String target) {
        int length = target.length();

        TrieNode pC = root;

        for (int i = 0; i < length; i++) {
            char index = target.charAt(i);
            if (pC.children.get(index) == null) {
                System.out.println("This word has not been inserted");
                return;
            }
            pC = pC.children.get(index);
        }
        if (!pC.isEndOfWord) {
            System.out.println("This word has not been inserted");
            return;
        }

        pC.isEndOfWord = false;
    }
    public static class TrieNode {
        Map<Character, TrieNode> children = new TreeMap<>();
        /* isEndOfWord is true if the node represents the end of a word */
        boolean isEndOfWord;

        TrieNode() {
            isEndOfWord = false;
        }
    }
}
class TrieV {

    private static final ArrayList<String> searchedWords = new ArrayList<>();
    private static final Trie.TrieNode root = new Trie.TrieNode();

    public static ArrayList<String> getSearchedWords() {
        return searchedWords;
    }

    /**
     * Insert word `target` into Trie DS.
     *
     * @param target the word to insert
     */
    public static void insert(String target) {
        int length = target.length();

        Trie.TrieNode pCrawl = root;

        for (int i = 0; i < length; i++) {
            char index = target.charAt(i);

            if (pCrawl.children.get(index) == null) {
                pCrawl.children.put(index, new Trie.TrieNode());
            }

            pCrawl = pCrawl.children.get(index);
        }

        // Set `target` word ends at pCrawl
        pCrawl.isEndOfWord = true;
    }

    private static void GetWordsSubtree(Trie.TrieNode pCrawl, String target) {
        if (pCrawl.isEndOfWord) {
            searchedWords.add(target);
        }
        for (char index : pCrawl.children.keySet()) {
            if (pCrawl.children.get(index) != null) {
                GetWordsSubtree(pCrawl.children.get(index), target + index);
            }
        }
    }

    public static ArrayList<String> search(String prefix) {
        if (prefix.isEmpty()) {
            return new ArrayList<>();
        }
        searchedWords.clear();
        int length = prefix.length();
        Trie.TrieNode pC = root;

        for (int i = 0; i < length; i++) {
            char index = prefix.charAt(i);

            if (pC.children.get(index) == null) {
                return getSearchedWords();
            }

            pC = pC.children.get(index);
        }
        GetWordsSubtree(pC, prefix);
        return getSearchedWords();
    }

    public static void delete(String target) {
        int length = target.length();

        Trie.TrieNode pC = root;

        for (int i = 0; i < length; i++) {
            char index = target.charAt(i);
            if (pC.children.get(index) == null) {
                System.out.println("This word has not been inserted");
                return;
            }
            pC = pC.children.get(index);
        }
        if (!pC.isEndOfWord) {
            System.out.println("This word has not been inserted");
            return;
        }

        pC.isEndOfWord = false;
    }
    public static class TrieNodeV {
        Map<Character, TrieV.TrieNodeV> children = new TreeMap<>();
        /* isEndOfWord is true if the node represents the end of a word */
        boolean isEndOfWord;

        TrieNodeV() {
            isEndOfWord = false;
        }
    }
}
public class Database_Connect {

    static Connection connection = null;

    static String word;

    static String meaning;

    static String both;

    static boolean found = true;

    static List<String> suggestions = new ArrayList<>();

    static List<String> questions = new ArrayList<>();
    static List<String> answers = new ArrayList<>();
    static List<String> explanations = new ArrayList<>();

    public static void lookUpDatabase(String input, boolean isEnglish) throws Exception {
        StringBuilder res1 = new StringBuilder();
        StringBuilder res2 = new StringBuilder();
        String noMatchingResult = "<h1></h1><h3><i>Xin lỗi , Không có kết quả phù hợp nội dung bạn tìm kiếm !";

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Word> words = new ArrayList<>();

        try (Connection connection1 = DriverManager.getConnection(
                "jdbc:sqlite:src/main/resources/com/example/btl1_dictionary/Database/dict_hh.db");) {
            if (connection1 != null) {
                connection = connection1;
            }
            String querry = "";
            if (isEnglish == true) {
                querry = String.format("SELECT * FROM av WHERE word = '%s'", input);
            } else {
                querry = String.format("SELECT * FROM va WHERE word = '%s'", input);
            }
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(querry);
            ResultSet resultSet = preparedStatement.executeQuery();
            int i = 0;

            while (resultSet.next() && i < 10) {
                int id = resultSet.getInt(1);
                String word = resultSet.getString(2);
                String html = resultSet.getString(3);
                String meaning = resultSet.getString(4);
                String pro = resultSet.getString(5);
                i++;
                Word w = new Word(id, word, html, meaning, pro);
                words.add(w);
            }
            if (words.isEmpty()) {
                found = false;
                return;
            }
            found = true;
            boolean first = true;
            if (isEnglish) {
                for (Word w : words) {
                    if (first) {
                        res1.append(w.html, 0, 18 + w.word.length() + w.pronunciation.length());
                        res2.append(w.html.substring(18 + w.word.length() + w.pronunciation.length()));
                        first = false;
                        continue;
                    }
                    res2.append(w.html.substring(16 + w.word.length()));
                }
            } else {
                for (Word w : words) {
                    if (first) {
                        res1.append(w.html, 0, 9 + w.word.length());
                        res2.append(w.html.substring(9 + w.word.length()));
                    }
                    res2.append(w.html.substring(9 + w.word.length()));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        word = res1.toString();
        meaning = res2.toString();
        both = word.concat(meaning);
    }
    public static void loadSuggestionsViet() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Connection connection1 = DriverManager.getConnection(
                "jdbc:sqlite:src/main/resources/com/example/btl1_dictionary/Database/dict_hh.db");) {
            if (connection1 != null) {
                connection = connection1;
            }
            String querry = "SELECT word FROM va";

            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(querry);
            ResultSet resultSet = preparedStatement.executeQuery();
            long i = 0;
            while (resultSet.next() == true) {
                String word = resultSet.getString(1);
                TrieV.insert(word);
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void loadSuggestions() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Connection connection1 = DriverManager.getConnection(
                "jdbc:sqlite:src/main/resources/com/example/btl1_dictionary/Database/dict_hh.db");) {
            if (connection1 != null) {
                connection = connection1;
            }
            String querry = "SELECT word FROM av UNION SELECT word FROM va";

            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(querry);
            ResultSet resultSet = preparedStatement.executeQuery();
            long i = 0;
            while (resultSet.next() == true) {
                String word = resultSet.getString(1);
                Trie.insert(word);
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createSuggestions(String input) {
        suggestions = Trie.search(input);
    }

    public static void addWord(String word, String pronounce, String html) throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Connection connection1 = DriverManager.getConnection(
                "jdbc:sqlite:src/main/resources/com/example/btl1_dictionary/Database/dict_hh.db");) {
            if (connection1 != null) {
                connection = connection1;
            }
            String query = "INSERT INTO av(word, pronounce, html) VALUES (?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, word);
                preparedStatement.setString(2, pronounce);
                preparedStatement.setString(3, html);

                preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void makeModify(String word, String html) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Connection connection1 = DriverManager.getConnection(
                "jdbc:sqlite:src/main/resources/com/example/btl1_dictionary/Database/dict_hh.db");) {
            if (connection1 != null) {
                connection = connection1;
            }
            String querry = String.format("UPDATE av SET html = '%s' where word = '%s';", html,word);

            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(querry);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteWord(String word) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Connection connection1 = DriverManager.getConnection(
                "jdbc:sqlite:src/main/resources/com/example/btl1_dictionary/Database/dict_hh.db");) {
            if (connection1 != null) {
                connection = connection1;
            }
            String querry = String.format("DELETE FROM av WHERE word = '%s'", word);

            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(querry);
            try (preparedStatement) {
                connection.setAutoCommit(false);
                preparedStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void loadQuiz() throws SQLException {
        questions.clear();
        answers.clear();
        explanations.clear();

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Connection connection1 = DriverManager.getConnection(
                "jdbc:sqlite:src/main/resources/com/example/btl1_dictionary/Database/funQuiz.db");) {
            if (connection1 != null) {
                connection = connection1;
            }
            String querry = "SELECT * FROM funQuiz";
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(querry);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next() == true) {
                String question = resultSet.getString(2);
                questions.add(question);
                String answer = resultSet.getString(3);
                answers.add(answer);
                String explaination = resultSet.getString(4);
                explanations.add(explaination);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

