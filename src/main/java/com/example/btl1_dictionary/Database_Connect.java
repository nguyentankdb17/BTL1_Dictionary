package com.example.btl1_dictionary;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class Word extends Database_Connect {
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


class Trie extends Database_Connect {

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

        for (int level = 0; level < length; level++) {
            char index = target.charAt(level);

            if (pCrawl.children.get(index) == null) {
                pCrawl.children.put(index, new TrieNode());
            }

            pCrawl = pCrawl.children.get(index);
        }

        // Set `target` word ends at pCrawl
        pCrawl.isEndOfWord = true;
    }

    /**
     * Get all words ended in the subtree of node `pCrawl`
     *
     * @param pCrawl the current node
     * @param target the current word that `pCrawl` represents
     */
    private static void dfsGetWordsSubtree(TrieNode pCrawl, String target) {
        if (pCrawl.isEndOfWord) {
            searchedWords.add(target);
        }
        for (char index : pCrawl.children.keySet()) {
            if (pCrawl.children.get(index) != null) {
                dfsGetWordsSubtree(pCrawl.children.get(index), target + index);
            }
        }
    }

    /**
     * Search all words start with `prefix` in the Trie.
     *
     * @param prefix the prefix to search
     * @return an ArrayList of String contains the words start with `prefix`
     */
    public static ArrayList<String> search(String prefix) {
        if (prefix.isEmpty()) {
            return new ArrayList<>();
        }
        searchedWords.clear();
        int length = prefix.length();
        TrieNode pCrawl = root;

        for (int level = 0; level < length; level++) {
            char index = prefix.charAt(level);

            if (pCrawl.children.get(index) == null) {
                return getSearchedWords();
            }

            pCrawl = pCrawl.children.get(index);
        }
        dfsGetWordsSubtree(pCrawl, prefix);
        return getSearchedWords();
    }

    /**
     * Delete the word `target` from Trie DS.
     *
     * @param target the word to delete
     */
    public static void delete(String target) {
        int length = target.length();

        TrieNode pCrawl = root;

        for (int level = 0; level < length; level++) {
            char index = target.charAt(level);
            if (pCrawl.children.get(index) == null) {
                System.out.println("This word has not been inserted");
                return;
            }
            pCrawl = pCrawl.children.get(index);
        }
        if (!pCrawl.isEndOfWord) {
            System.out.println("This word has not been inserted");
            return;
        }

        pCrawl.isEndOfWord = false;
    }

    /** a Node on the Trie DS. */
    public static class TrieNode {
        Map<Character, TrieNode> children = new TreeMap<>();
        /* isEndOfWord is true if the node represents the end of a word */
        boolean isEndOfWord;

        TrieNode() {
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

    public static void lookUpDatabase(String input) throws Exception {
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
            String querry = String.format("SELECT * FROM av WHERE word LIKE '%s'", input);
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(querry);
            ResultSet resultSet = preparedStatement.executeQuery();
            int i = 0;

            while (resultSet.next() && i < 10) {
                int id = resultSet.getInt(1);
                String word = resultSet.getString(2);
                String html = resultSet.getString(3);
                String meaning = resultSet.getString(3);
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
            for (Word w : words) {
                if (first) {
                    res1.append(w.html, 0, 18 + w.word.length() + w.pronunciation.length());
                    res2.append(w.html.substring(18 + w.word.length() + w.pronunciation.length()));
                    first = false;
                    continue;
                }
                res2.append(w.html.substring(16 + w.word.length()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        word = res1.toString();
        meaning = res2.toString();
        both = word.concat(meaning);
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
            String querry = "SELECT word FROM av";

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
}

