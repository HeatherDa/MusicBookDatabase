package com.Heather;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * Created by cryst on 5/4/2016.
 */
public class MusicGui extends JFrame {
    private JPanel rootPanel;
    private JTabbedPane containerTabbedPane;

    //new song components
    private JTabbedPane newSongTabbedPane;
    private JTextField songTitleTextBox;
    private JTextField composerTextField;
    private JComboBox styleComboBox;
    private JComboBox genreComboBox;
    private JTextField instrumentTextField;
    private JComboBox keyComboBox;
    private JTextField timeSignatureTextField;
    private JTextField firstPageTextField;
    private JTextField totalPagesTextField;
    private JButton addSongButton;
    private JComboBox formatComboBox;
    private JComboBox lowestComboBox;
    private JComboBox highestComboBox;
    private JComboBox bookTitleComboBox;
    private JCheckBox lyricsCheckBox;

    //new Book components
    private JTabbedPane newBookTabbedPane;
    private JTextField bookTitleTextField;
    private JComboBox bookLocationComboBox;
    private JButton addNewBookButton;

    //search components
    private JTabbedPane searcherTabbedPane;//search pane
    private JComboBox searchSongTitleComboBox;//optional query parameter
    private JComboBox searchBookTitleComboBox;//optional query parameter
    private JComboBox searchGenreComboBox;//optional query parameter
    private JComboBox searchComposerComboBox;//optional query parameter
    private JTable searchResultsTable;//shows results of query
    private JComboBox searchKeyComboBox;
    private JButton searchButton;
    private JButton deleteEntryButton;
    private JButton exitButton;
    private JButton editEntryButton;
    private JComboBox searchStyleComboBox;
    private JComboBox searchInstrumentComboBox;



    private DefaultListModel<String> listModel;
    private Boolean editEntry;

    private ArrayList<String> searchText;

    protected MusicGui() {
        setContentPane(rootPanel);
        setPreferredSize(new Dimension(800, 600));
        pack();
        setTitle("Sheet Music Inventory");
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        fillBookTitleComboBox();
        fillGenreComboBox();
        fillStyleComboBox();
        fillKeyComboBox();
        fillFormatComboBox();
        fillLowestComboBox();
        fillHighestComboBox();
        fillBookLocationComboBox();
        setSearchComboBoxes();

        //assigning table models
        SongDataModel songDataModel = new SongDataModel();
        searchResultsTable.setGridColor(Color.black);
        searchResultsTable.setModel(songDataModel);

        genreComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Genre = genreComboBox.getSelectedItem().toString();
                if (!Genre.equalsIgnoreCase("Blues")) {
                    styleComboBox.removeAllItems();
                    fillStyleComboBox();
                }
            }
        });

        addSongButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String songName = songTitleTextBox.getText().trim();
                String songComp = composerTextField.getText().trim();
                String songGenre = genreComboBox.getSelectedItem().toString();
                String songStyle = styleComboBox.getSelectedItem().toString();
                boolean songL = lyricsCheckBox.isSelected();
                String songLyrics;
                if (songL) {
                    songLyrics = "Lyrics";
                }//convert boolean to string so easier to understand in table than 1 or 0
                else {
                    songLyrics = "No Lyrics";
                }
                String songKey = keyComboBox.getSelectedItem().toString();
                String songTime = timeSignatureTextField.getText().trim();
                String songInst = instrumentTextField.getText().trim();
                int firstPage = Integer.parseInt(firstPageTextField.getText());
                int totalPage = Integer.parseInt(totalPagesTextField.getText());
                String format = formatComboBox.getSelectedItem().toString();
                String low = lowestComboBox.getSelectedItem().toString();
                String high = highestComboBox.getSelectedItem().toString();

                String bookTitle = bookTitleComboBox.getSelectedItem().toString();
                int bookID=Main.getBookID(bookTitle);

                Main.addSong(songName, songComp, bookID, songGenre, songStyle, songTime, songKey, firstPage, totalPage, songLyrics, low, high, format, songInst);
                songTitleTextBox.setText("");
                composerTextField.setText("");

            }
        });

        addNewBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bookName = bookTitleTextField.getText().trim();
                String location = bookLocationComboBox.getSelectedItem().toString();

                Main.addBook(bookName, location);
                bookTitleComboBox.removeAllItems();//clear current contents
                fillBookTitleComboBox();//refresh contents to include new book
                JOptionPane.showMessageDialog(rootPane, "New book added.");
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String songTitle = searchSongTitleComboBox.getSelectedItem().toString();
                String bookTitle = searchBookTitleComboBox.getSelectedItem().toString();
                String songComposer = searchComposerComboBox.getSelectedItem().toString();
                String songGenre = searchGenreComboBox.getSelectedItem().toString();
                String songStyle = searchStyleComboBox.getSelectedItem().toString();
                String songKey = searchKeyComboBox.getSelectedItem().toString();
                String songInstrument = searchInstrumentComboBox.getSelectedItem().toString();
                searchText = new ArrayList<>();
                if (songTitle != null) {
                    searchText.add("songs.Title=" + songTitle);
                }
                if (bookTitle != null) {
                    int bookID=Main.getBookID(bookTitle);
                    searchText.add("songs.BookID="+bookID);//Search by bookID in songs table
                }
                if (songComposer != null) {
                    searchText.add("songs.Composer=" + songComposer);
                }
                if (songGenre != null) {
                    searchText.add("songs.Genre=" + songGenre);
                }
                if (songStyle != null) {
                    searchText.add("songs.Style=" + songStyle);
                }
                if (songKey != null) {
                    searchText.add("songs.Key=" + songKey);
                }
                if (songInstrument != null) {
                    searchText.add("songs.Instrument=" + songInstrument);
                }
                for (int parameter=0;parameter<searchText.size()-1;parameter++){//each entry represents the table/field combo as seen above


                }

            }
        });

        editEntryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        deleteEntryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!editEntry) {//nothing selected, can't delete anything
                    JOptionPane.showMessageDialog(rootPane, "Select a record to delete it.");
                } else {//something is selected, so proceed with deletion
                    String name = songTitleTextBox.getText();

                    // Main.deleteSong(String.valueOf(name));//need songID//TODO fix it
                }//move selected row to absolute row and delete that row to delete a selected row
                songDataModel.updateResultSet(Main.getSongRs());
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.shutDown();
                System.exit(0);
            }
        });
    }

    private void setSearchComboBoxes() {
        ArrayList<String> allBookTitles = Main.allBookTitles();
        for (String n:allBookTitles){
            searchBookTitleComboBox.addItem(n);
        }
        ArrayList<String> allSongTitles = Main.allSongTitles();
        for (String n:allSongTitles){
            searchSongTitleComboBox.addItem(n);
        }
        ArrayList<String> allSongComp = Main.allSongComposers();//not adding?
        for (String n:allSongComp){
            searchComposerComboBox.addItem(n);
        }
        ArrayList<String> allSongGenres = Main.allSongGenres();
        for (String n:allSongGenres){
            searchGenreComboBox.addItem(n);
        }
        ArrayList<String> allSongStyles = Main.allSongStyles();
        for (String n:allSongStyles){
            searchStyleComboBox.addItem(n);
        }
        ArrayList<String> allSongKeys = Main.allSongKeys();
        for (String n:allSongKeys){
            searchKeyComboBox.addItem(n);
        }
        ArrayList<String> allSongInstruments = Main.allSongInstruments();
        for (String n:allSongInstruments){
            searchInstrumentComboBox.addItem(n);
        }
    }

    private void fillBookTitleComboBox(){
        ArrayList<String> allBookTitles = Main.allBookTitles();
        for (String n:allBookTitles){
            bookTitleComboBox.addItem(n);
        }
    }

    private void fillKeyComboBox(){
        keyComboBox.addItem("C Major, A Minor, 0");
        keyComboBox.addItem("G Major, E Minor, #");
        keyComboBox.addItem("D Major, B Minor, ##");
        keyComboBox.addItem("A Major, F# Minor, ###");
        keyComboBox.addItem("E Major, C# Minor, ####");
        keyComboBox.addItem("B Major, G# Minor, #####");
        keyComboBox.addItem("F# Major, D# Minor, ######");
        keyComboBox.addItem("Gb Major, Eb Minor, bbbbbb");
        keyComboBox.addItem("Db Major, Bb Minor, bbbbb");
        keyComboBox.addItem("Ab Major, F Minor, bbbb");
        keyComboBox.addItem("Eb Major, C Minor, bbb");
        keyComboBox.addItem("Bb Major, G Minor, bb");
        keyComboBox.addItem("F Major, D Minor, b");
    }
    private void fillFormatComboBox(){
        formatComboBox.addItem("Hardcover Book");
        formatComboBox.addItem("Softcover Book");
        formatComboBox.addItem("Binder");
        formatComboBox.addItem("Filebox Folder");
        formatComboBox.addItem("Loose Papers");
    }
    private void fillLowestComboBox(){
        lowestComboBox.addItem("C");
        lowestComboBox.addItem("C#/Db");
        lowestComboBox.addItem("D");
        lowestComboBox.addItem("D#/Eb");
        lowestComboBox.addItem("E");
        lowestComboBox.addItem("F");
        lowestComboBox.addItem("F#/Gb");
        lowestComboBox.addItem("G");
        lowestComboBox.addItem("G#/Ab");
        lowestComboBox.addItem("A");
        lowestComboBox.addItem("A#/Bb");
        lowestComboBox.addItem("B");
    }
    private void fillHighestComboBox(){
        highestComboBox.addItem("C");
        highestComboBox.addItem("C#/Db");
        highestComboBox.addItem("D");
        highestComboBox.addItem("D#/Eb");
        highestComboBox.addItem("E");
        highestComboBox.addItem("F");
        highestComboBox.addItem("F#/Gb");
        highestComboBox.addItem("G");
        highestComboBox.addItem("G#/Ab");
        highestComboBox.addItem("A");
        highestComboBox.addItem("A#/Bb");
        highestComboBox.addItem("B");
    }
    private void fillBookLocationComboBox(){
        bookLocationComboBox.addItem("Livingroom Shelf 1a");
        bookLocationComboBox.addItem("Livingroom Shelf 1b");
        bookLocationComboBox.addItem("Livingroom Shelf 1c");
        bookLocationComboBox.addItem("Livingroom Shelf 1d");
        bookLocationComboBox.addItem("Livingroom Shelf 1e");
        bookLocationComboBox.addItem("Livingroom Shelf 1f");
        bookLocationComboBox.addItem("Livingroom Shelf 1g");
    }
    private void fillGenreComboBox(){
        genreComboBox.addItem("Blues");
        genreComboBox.addItem("Children's Music");
        genreComboBox.addItem("Classical");
        genreComboBox.addItem("Country");
        genreComboBox.addItem("Disney");
        genreComboBox.addItem("Folk");
        genreComboBox.addItem("Holiday");
        genreComboBox.addItem("Other");
        genreComboBox.addItem("Religious");
        genreComboBox.addItem("Jazz");
        genreComboBox.addItem("Pop");
        genreComboBox.addItem("Rock");
        genreComboBox.addItem("Singer/Songwriter");
        genreComboBox.addItem("Vocal");
    }
    private void fillStyleComboBox(){
        String genre= (String) genreComboBox.getSelectedItem();
        if (genre.equalsIgnoreCase("Blues")) {
            styleComboBox.addItem("Classic Blues");
        }else if (genre.equalsIgnoreCase("Children's Music")){
            styleComboBox.addItem("Lullabies");
            styleComboBox.addItem("Sing-Along");
        }else if (genre.equalsIgnoreCase("Classical")){
            styleComboBox.addItem("Baroque");
            styleComboBox.addItem("Chamber Music");
            styleComboBox.addItem("Plain Chant");
            styleComboBox.addItem("Choral");
            styleComboBox.addItem("Contemporary Classical");
            styleComboBox.addItem("Early Music");
            styleComboBox.addItem("High Classical");
            styleComboBox.addItem("Medieval");
            styleComboBox.addItem("Opera");
            styleComboBox.addItem("Orchestral");
            styleComboBox.addItem("Renaissance");
            styleComboBox.addItem("Early Romantic");
            styleComboBox.addItem("Late Romantic");
            styleComboBox.addItem("Wedding Music");
        }else if (genre.equalsIgnoreCase("Country")){
            styleComboBox.addItem("Bluegrass");
            styleComboBox.addItem("Contemporary Country");
            styleComboBox.addItem("Traditional Country");
            styleComboBox.addItem("Traditional Bluegrass");
        }else if (genre.equalsIgnoreCase("Disney")){
            styleComboBox.addItem("Fake Book");
            styleComboBox.addItem("Accompaniment Included");
        }else if (genre.equalsIgnoreCase("Folk")){
            styleComboBox.addItem("African");
            styleComboBox.addItem("African-American");
            styleComboBox.addItem("American");
            styleComboBox.addItem("Australian");
            styleComboBox.addItem("Austrian");
            styleComboBox.addItem("Canadian");
            styleComboBox.addItem("Caribbean");
            styleComboBox.addItem("Central European");
            styleComboBox.addItem("Chinese");
            styleComboBox.addItem("Drinking Songs");
            styleComboBox.addItem("English");
            styleComboBox.addItem("Finnish");
            styleComboBox.addItem("French");
            styleComboBox.addItem("German");
            styleComboBox.addItem("Indian");
            styleComboBox.addItem("Irish");
            styleComboBox.addItem("Israeli");
            styleComboBox.addItem("Italian");
            styleComboBox.addItem("Japanese");
            styleComboBox.addItem("Jewish");
            styleComboBox.addItem("Latin-American");
            styleComboBox.addItem("Dutch");
            styleComboBox.addItem("Norwegian");
            styleComboBox.addItem("Russian");
            styleComboBox.addItem("Scottish");
            styleComboBox.addItem("Sea Shanties");
            styleComboBox.addItem("South American");
            styleComboBox.addItem("Spanish");
            styleComboBox.addItem("Swedish");
            styleComboBox.addItem("Syrian");
            styleComboBox.addItem("Turkish");
            styleComboBox.addItem("Welsh");
        }else if (genre.equalsIgnoreCase("Holiday")){
            styleComboBox.addItem("Chanukah");
            styleComboBox.addItem("Christmas");
            styleComboBox.addItem("Easter");
            styleComboBox.addItem("Halloween");
            styleComboBox.addItem("Other Holidays");
            styleComboBox.addItem("Thanksgiving");
        }else if (genre.equalsIgnoreCase("Other")){
            styleComboBox.addItem("Other");
        }else if (genre.equalsIgnoreCase("Religious")){
            styleComboBox.addItem("Christian");
            styleComboBox.addItem("Jewish");
            styleComboBox.addItem("Meditation");
            styleComboBox.addItem("New Age");
        }else if (genre.equalsIgnoreCase("Jazz")){
            styleComboBox.addItem("Big Band");
            styleComboBox.addItem("Contemporary Jazz");
            styleComboBox.addItem("Dixieland");
            styleComboBox.addItem("Mainstream Jazz");
            styleComboBox.addItem("Ragtime");
            styleComboBox.addItem("Traditional Jazz");
        }else if (genre.equalsIgnoreCase("Pop")){
            styleComboBox.addItem("Various Pop");
        }else if (genre.equalsIgnoreCase("Rock")){
            styleComboBox.addItem("Metal");
            styleComboBox.addItem("Rock and Roll");
            styleComboBox.addItem("Various others");
        }else if (genre.equalsIgnoreCase("Singer/Songwriter")){
            styleComboBox.addItem("Contemporary Folk");
            styleComboBox.addItem("Contemporary Singer/Songwriter");
        }else if (genre.equalsIgnoreCase("Vocal")){
            styleComboBox.addItem("A Cappella");
            styleComboBox.addItem("Barbershop");
            styleComboBox.addItem("Doo-wop");
            styleComboBox.addItem("Music Hall");
            styleComboBox.addItem("Show Tunes");
        }
    }
}
