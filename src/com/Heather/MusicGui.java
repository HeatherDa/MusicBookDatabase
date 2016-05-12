package com.Heather;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by cryst on 5/4/2016.
 */
public class MusicGui extends JFrame{
    private JPanel rootPanel;
    private JTabbedPane containerTabbedPane;

    //new song components
    private JTabbedPane newSongTabbedPane;
    private JTextField songTitleTextBox;
    private JTextField composerTextField;
    private JComboBox styleComboBox;
    private JComboBox genreComboBox;
    private JRadioButton lyricsRadioButton;
    private JTextField instrumentTextField;
    private JComboBox keyComboBox;
    private JTextField timeSignatureTextField;
    private JTextField firstPageTextField;
    private JTextField totalPagesTextField;
    private JButton addSongButton;
    private JComboBox formatComboBox;
    private JComboBox lowestComboBox;
    private JComboBox highestComboBox;
    private JTable BookTable;

    //new Book components
    private JTabbedPane newBookTabbedPane;

    private JTextField bookTitleTextField;
    private JTextField ISBNTextField;
    private JComboBox bookLocationComboBox;
    private JButton addNewBookButton;

    //search components
    private JTabbedPane searcherTabbedPane;//search pane
    private JComboBox searchSongTitleComboBox;//optional query parameter
    private JComboBox searchBookTitleComboBox;//optional query parameter
    private JComboBox searchAlbumTitleComboBox;//optional query parameter
    private JComboBox searchComposerComboBox;//optional query parameter
    private JTable searchResultsTable;//shows results of query
    private JComboBox searchKeyComboBox;
    private JButton searchButton;
    private JButton deleteEntryButton;
    private JButton exitButton;
    private JButton editEntryButton;


    private DefaultListModel<String> listModel;


    protected MusicGui(final SongDataModel songDataModel, final BookDataModel bookDataModel){
        setContentPane(rootPanel);
        pack();
        setTitle("Sheet Music Inventory");
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        searchResultsTable.setGridColor (Color.black);
        searchResultsTable.setModel(songDataModel);
        BookTable.setModel(bookDataModel);

        addSongButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String songName = songTitleTextBox.getText().trim();
                String songComp= composerTextField.getText().trim();
                String songGenre=(String)genreComboBox.getSelectedItem();
                String songStyle=(String)styleComboBox.getSelectedItem();
                boolean songL=lyricsRadioButton.isSelected();
                //String songLyrics;
                //if (songL){songLyrics="yes";}
                //else{songLyrics="no";};
                String songKey=(String)keyComboBox.getSelectedItem();
                String songTime=timeSignatureTextField.getText().trim();;
                String songInst=instrumentTextField.getText().trim();;
                int firstPage=Integer.parseInt(firstPageTextField.getText());
                int totalPage=Integer.parseInt(totalPagesTextField.getText());
                String format=(String)formatComboBox.getSelectedItem();;
                String low=(String)lowestComboBox.getSelectedItem();
                String high=(String)highestComboBox.getSelectedItem();

                int currentRow=BookTable.getSelectedRow();

                if(currentRow ==-1){
                    JOptionPane.showMessageDialog(rootPane, "Please select a book.  If the book is not listed, add a new book under add book tab.");
                }
                int bookID= (int) bookDataModel.getValueAt(currentRow, 0);
                System.out.println(bookID);

                Main.addSong(songName, songComp, bookID, songGenre, songStyle, songTime, songKey, firstPage, totalPage, songL, low, high, format, songInst);

            }
        });
    }

    private void addListeners() {

        //Need to listen for button clicked,
        //Read the text in the text box and add this to the list.

        addSongButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String songName = songTitleTextBox.getText().trim();
                String songComp= composerTextField.getText().trim();
                String songGenre=(String)genreComboBox.getSelectedItem();
                String songStyle=(String)styleComboBox.getSelectedItem();
                boolean songL=lyricsRadioButton.isSelected();
                String songLyrics;
                if (songL){songLyrics="yes";}
                else{songLyrics="no";};
                String songKey=(String)keyComboBox.getSelectedItem();
                String songTime=timeSignatureTextField.getText().trim();;
                String songInst=instrumentTextField.getText().trim();;
                int firstPage=Integer.parseInt(firstPageTextField.getText());
                int totalPage=Integer.parseInt(totalPagesTextField.getText());
                String format=(String)formatComboBox.getSelectedItem();;
                String low=(String)lowestComboBox.getSelectedItem();
                String high=(String)highestComboBox.getSelectedItem();




            }
        });


    }
}
