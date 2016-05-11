package com.Heather;

import java.sql.*;
import java.util.*;

public class Main {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/MusicBooks";

    static final String USER = "Heather";
    static final String PASSWORD = "ashlynn8";

    static Connection connect=null;
    static Statement statement=null;

    static SongDataModel songDataModel;
    static BookDataModel bookDataModel;
    static ArrayList<String> tableNames=new ArrayList<>();
    static ArrayList<ArrayList> testDataSong;
    static ArrayList<ArrayList> testDataBook;
    static PreparedStatement searcherSong=null;
    static PreparedStatement searcherSongMulti;
    static PreparedStatement searcherBook=null;
    static PreparedStatement newSong=null;
    static PreparedStatement newBook=null;
    static PreparedStatement changeSong=null;
    static PreparedStatement changeBook=null;
    static PreparedStatement delSong=null;
    static PreparedStatement delBook=null;
    static PreparedStatement delBook2=null;
    static ResultSet rsSong=null;
    static ResultSet rsBook=null;

    public static void main(String[] args) throws SQLException {



        try{
            setup();

            //set prepared statements
            String sSolver = "SELECT * FROM Songs WHERE ? = ?";
            searcherSong = connect.prepareStatement(sSolver);
            String smSolver = "SELECT * FROM Songs WHERE ? = ? AND ? = ?";
            searcherSongMulti=connect.prepareStatement(smSolver);
            String bSolver = "SELECT * FROM Books WHERE ? = ?";
            searcherBook = connect.prepareStatement (bSolver);
            //new Song
            //Title, Composer, BookID, Genre, Style, TimeSignature, KeySignature, FirstPage, TotalPages, Lyrics, LowestNote, HighestNote, Format, Instrument
            //String, String, int, String, String, String, String, int, int, Boolean, String, String, String, String
            String nSong= "INSERT INTO Songs (Title, Composer, BookID, Genre, Style, TimeSignature, KeySignature, FirstPage, TotalPages, Lyrics, LowestNote, HighestNote, Format, Instrument) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            newSong=connect.prepareStatement(nSong);
            //new Book
            String nBook= "INSERT INTO Books (Title, Location) VALUES(?,?)";//title, Location
            newBook=connect.prepareStatement(nBook);
            //change Song
            String cSong="UPDATE Songs SET ? = ? WHERE SongID=?";
            changeSong=connect.prepareStatement(cSong);
            //change Book
            String cBook="UPDATE Books SET ? = ? WHERE BookID=?";
            changeBook=connect.prepareStatement(cBook);
            //delete Song
            String dSong="DELETE FROM Songs WHERE SongID=?";
            delSong=connect.prepareStatement(dSong);
            //deleteBook
            String dBook="DELETE FROM Books WHERE BookID=?";
            delBook=connect.prepareStatement(dBook);
            String dBook2="DELETE FROM Songs WHERE BookID=?";
            delBook2=connect.prepareStatement(dBook2);


            //loadAllData(); TODO write function
            loadAllSongData();
            loadAllBookData();
            addTestData();
            //make TableModel
            //MusicBookDataModel mB=new MusicBookDataModel(rs);
            //make Gui
            MusicGui gui=new MusicGui(songDataModel, bookDataModel);



        }catch(SQLException se){
            se.printStackTrace();
        }

    }
    public static void addSong(String title, String composer, int bookID, String genre, String style, String time, String key, Integer firstPage, Integer totalPage, Boolean lyrics, String lowestNote, String highestNote, String format, String instrument){
        try {
            searcherSongMulti.setString(1, "Title");
            searcherSongMulti.setString(2, title);
            searcherSongMulti.setString(3, "BookID");
            searcherSongMulti.setInt(4, bookID);

            ResultSet searchR = searcherSongMulti.executeQuery();//look for an entry with this name in this book
            int resultsCounter = 0;
            while (searchR.next()) {
                resultsCounter++;
            }
            //Title, Composer, BookID, Genre, Style, TimeSignature, KeySignature, FirstPage, TotalPages, Lyrics, LowestNote, HighestNote, Format, Instrument
            ////String, String, int, String, String, String, String, int, int, Boolean, String, String, String, String
            if (resultsCounter == 0) {//if there is no entry, add it
                newSong.setString(1, title);
                newSong.setString(2, composer);
                newSong.setInt(3, bookID);
                newSong.setString(4, genre);
                newSong.setString(5, style);
                newSong.setString(6, time);
                newSong.setString(7, key);
                newSong.setInt(8, firstPage);
                newSong.setInt(9, totalPage);
                newSong.setBoolean(10, lyrics);
                newSong.setString(11, lowestNote);
                newSong.setString(12, highestNote);
                newSong.setString(13, format);
                newSong.setString(14, instrument);

                newSong.executeUpdate();
            } else {
                System.out.println("That Song is already in the database.");//
            }

        }catch(SQLException se){
            System.out.println("error adding entry "+se);
            se.printStackTrace();
        }
    }

    public static void addBook(String title, String location){
        try {
            searcherBook.setString(1, "Title");
            searcherBook.setString(2, title);

            ResultSet searchR = searcherBook.executeQuery();//look for this book to avoid entering twice
            int resultsCounter = 0;
            while (searchR.next()) {
                resultsCounter++;
            }
            if (resultsCounter == 0) {//if there is no entry, add it
                newBook.setString(1, title);
                newBook.setString(2, location);

                newBook.executeUpdate();
            } else {
                System.out.println("That book is already in the database.");//
            }

        }catch(SQLException se){
            System.out.println("error adding entry "+se);
            se.printStackTrace();
        }
    }

    public static void editSong(ArrayList<String>columnsChanged, ArrayList newValues, int songID){
        try {
            int counter=0;
            for(String column:columnsChanged) {
                String changeMe="UPDATE Songs SET"+column+" = "+ newValues.get(counter)+" WHERE songID = "+songID;
                statement.executeUpdate(changeMe);
                //couldn't use a prepared statement, because couldn't predict what type changed and also couldn't get it to parse (maybe for that reason?)
                counter++;
            }
        }catch(SQLException se){
            System.out.println("there was an error updating this file "+ se);
            se.printStackTrace();
        }
    }
    public static void editBook(ArrayList<String>columnsChanged, ArrayList<String> values, int bookID){
        try {
            int counter=0;
            for(String column:columnsChanged) {
                String changeMe="UPDATE Songs SET"+column+" = "+ values.get(counter)+" WHERE songID = "+bookID;
                statement.executeUpdate(changeMe);
            }

        }catch(SQLException se){
            System.out.println("there was an error updating this file "+ se);
            se.printStackTrace();
        }
    }

    public static void deleteSong(int songID){
        try{
            delSong.setInt(1, songID);
            delSong.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Can't delete Song.");
            e.printStackTrace();
        }
    }

    public static void deleteBook(int bookID){
        try{
            delBook.setInt(1, bookID);
            delBook.executeUpdate();
            delBook2.setInt(1,bookID);//remove all Songs from Song table that have this book ID
            delBook2.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static boolean setup(){//setup the database and connection

        try {
            Class.forName(JDBC_DRIVER);

        }catch(ClassNotFoundException cnfe){
            System.out.println("Can't instantiate driver class; check you have drives and classpath configured correctly?");
            cnfe.printStackTrace();
            System.exit(-1);
        }

        try {
            connect = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD);
            statement = connect.createStatement();
            System.out.println(statement);
            //String makeTable= "CREATE TABLE if NOT EXISTS Songs (first int, second VARCHAR (10), third double)";
            //statement.executeUpdate(makeTable);
            tableNames.add("Songs");
            tableNames.add("Books");
            System.out.println(tableNames.get(0)+" first position, "+tableNames.get(1)+" second position");
            boolean tableExists;
            for (String tableName:tableNames) {
                tableExists=false;
                String checkTablePresentQuery = "SHOW TABLES LIKE '" + tableName + "'";
                System.out.println(checkTablePresentQuery);
                ResultSet tablesRS = statement.executeQuery(checkTablePresentQuery);
                if (tablesRS.next()) {
                    tableExists = true;
                    System.out.println("table exists");
                }


                if (!tableExists) {
                    //make new table
                    if (tableName.equalsIgnoreCase("Songs")){
                        //Title, Composer, BookID, Genre, Style, TimeSignature, KeySignature, FirstPage, TotalPages, Lyrics, LowestNote, HighestNote, Format, Instrument
                        //String, String, int, String, String, String, String, int, int, Boolean, String, String, String, String
                        String newTable = "CREATE TABLE if NOT EXISTS Songs (SongID int NOT NULL AUTO_INCREMENT, Title VARCHAR (100) NOT NULL, Composer VARCHAR (50), BookID int, Genre VARCHAR (50), Style VARCHAR (50), TimeSignature VARCHAR (5), KeySignature VARCHAR (8), FirstPage int, LastPage int, Lyrics BOOLEAN NOT NULL, LowestNote VARCHAR (8), HighestNote VARCHAR (8), Format VARCHAR (10)NOT NULL, Instrument VARCHAR (50), PRIMARY KEY (SongID))";
                        System.out.println(newTable);
                        statement.executeUpdate(newTable);
                        System.out.println("created new song table");
                    }else if (tableName.equalsIgnoreCase("Books")) {
                        String newTable = "CREATE TABLE if NOT EXISTS Books (BookID int NOT NULL AUTO_INCREMENMT, Title VARCHAR (100), Location VARCHAR (15), PRIMARY KEY (BookID))";
                        System.out.println("created new book table");
                        statement.executeUpdate(newTable);
                    }
                }
            }
            return true;

        }catch(SQLException se){
            System.out.println("setup isn't working");
            se.printStackTrace();
            return false;
        }
    }

    //make results list of all data from Song table
    public static boolean loadAllSongData(){

        try{

            if (rsSong!=null) {
                rsSong.close();
            }

            String getAllData = "SELECT * FROM Songs";
            rsSong = statement.executeQuery(getAllData);

           /* if (songDataModel == null) {
                //If no current songDataModel, then make one
                songDataModel = new SongDataModel(rsSong);
            } else {
                //Or, if one already exists, update its ResultSet
                songDataModel.updateResultSet(rsSong);
            }*/

            return true;

        } catch (Exception e) {
            System.out.println("Error loading or reloading Songs Table.");
            e.printStackTrace();
            return false;
        }

    }

    //make results list of all data from Book table
    public static boolean loadAllBookData(){

        try{

            if (rsBook!=null) {
                rsBook.close();
            }

            String getAllData = "SELECT * FROM Books";
            rsBook = statement.executeQuery(getAllData);

            /*if (bookDataModel == null) {
                //If no current bookDataModel, then make one
                bookDataModel = new BookDataModel(rsBook);
            } else {
                //Or, if one already exists, update its ResultSet
                bookDataModel.updateResultSet(rsBook);
            }*/

            return true;

        } catch (Exception e) {
            System.out.println("Error loading or reloading Books Table");
            e.printStackTrace();
            return false;
        }

    }

    public static void addTestData() {
        try {
            //store test data
            testDataSong = new ArrayList<>();
            testDataBook = new ArrayList<>();
            //Title VARCHAR (100) NOT NULL, Composer VARCHAR (50), BookID int, Genre VARCHAR (50), Style VARCHAR (50), TimeSignature VARCHAR (5), KeySignature VARCHAR (8), FirstPage int, LastPage int, Lyrics BOOLEAN NOT NULL, LowestNote VARCHAR (8), HighestNote VARCHAR (8), Format VARCHAR (10)NOT NULL, Instrument VARCHAR (50), PRIMARY KEY (SongID))";
            ArrayList song1 = new ArrayList(Arrays.asList("Amarilli, mia bella", "Giulio Caccini", 1, "Classical", "Romantic", "4/4", "C major", 9, 5, true, "1low B", "1High D","Book", "Piano"));//number is what octave above or below middle C octave
            testDataSong.add(song1);
            ArrayList book1 = new ArrayList(Arrays.asList("26 Italian Songs and Arias", "Livingroom 1D"));
            testDataBook.add(book1);
            ArrayList paper = new ArrayList(Arrays.asList("Loose Papers", "Livingroom 1C"));
            testDataBook.add(paper);

            //add test data to table
            for (ArrayList songs : testDataSong) {//test if this entry is already in the database
                searcherSong.setString(1, "Title");//column name
                searcherSong.setString(2, (String) songs.get(0));//Title of song is in first position after auto number.  TODO check if this is the right index
                ResultSet searchR = searcherSong.executeQuery();//look for an entry with this name
                int count = 0;
                while (searchR.next()) {
                    count++;
                }
                if (count == 0) {//if there is no entry, add it
                    //newSong.setInt(1, (int)songs.get(0));
                    newSong.setString(1, (String) songs.get(0));
                    newSong.setString(2, (String) songs.get(1));
                    newSong.setInt(3, (Integer) songs.get(2));
                    newSong.setString(4, (String) songs.get(3));
                    newSong.setString(5, (String) songs.get(4));
                    newSong.setString(6, (String) songs.get(5));
                    newSong.setString(7, (String) songs.get(6));
                    newSong.setInt(8, (Integer) songs.get(7));
                    newSong.setInt(9, (Integer) songs.get(8));
                    newSong.setBoolean(10, (Boolean) songs.get(9));
                    newSong.setString(11, (String) songs.get(10));
                    newSong.setString(12, (String) songs.get(11));
                    newSong.setString(13, (String) songs.get(12));
                    newSong.setString(14, (String) songs.get(13));
                    newSong.executeUpdate();
                }
            }
            for (ArrayList book : testDataBook) {
                searcherBook.setString(1, "Title");
                searcherBook.setString(2, (String) book.get(0));
                ResultSet searchR = searcherBook.executeQuery();
                int count = 0;
                while (searchR.next()) {
                    count++;
                }
                if (count == 0) {
                    newBook.setString(1, (String) book.get(0));
                    newBook.setString(2, (String) book.get(1));
                }
            }
        } catch (SQLException se) {
            System.out.println("failed to add test data.");
            se.printStackTrace();
        }
    }
    public static void shutDown() {

        try {
            if (newSong != null) {
                newSong.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        try {
            if (newBook != null) {
                newBook.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        try {
            if (delSong != null) {
                delSong.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        try {
            if (delBook != null) {
                delBook.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        try {
            if (delBook2 != null) {
                delBook2.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        try {
            if (changeSong != null) {
                changeSong.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        try {
            if (changeBook != null) {
                changeBook.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        try {
            if (searcherSong != null) {
                searcherSong.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        try {
            if (searcherBook != null) {
                searcherBook.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        try {
            if (searcherSongMulti != null) {
                searcherSongMulti.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        try {
            if (connect != null) {
                connect.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }

            System.out.println("Program finished");
    }
}
