package com.Heather;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by cryst on 5/8/2016.
 */
public class SongDataModel extends AbstractTableModel {
    private static int rowCount=0;
    private static int colCount=0;
    static ResultSet resultSet;

    public SongDataModel(ResultSet results){
        resultSet=results;
        setup();
        //Main.addTestData();
        //Main.loadAllSongData();
    }

    private void setup(){
        countRows();
        try{
            colCount=resultSet.getMetaData().getColumnCount();
        }catch(SQLException se){
            System.out.println("Couldn't count Columns "+ se);
            se.printStackTrace();
        }
    }

    public void updateResultSet(ResultSet newRS){
        resultSet=newRS;
        setup();
        fireTableDataChanged(); //this would make the table refresh, but it won't work because it's not static
    }

    private void countRows() {
        rowCount = 0;
        try {
            //Move cursor to the start...
            resultSet.beforeFirst();
            // next() method moves the cursor forward one row and returns true if there is another row ahead
            while (resultSet.next()) {
                rowCount++;

            }
            resultSet.beforeFirst();

        } catch (SQLException se) {
            System.out.println("Error counting rows " + se);
        }

    }
    @Override
    public int getRowCount(){
        countRows();
        return rowCount;
    }
    @Override
    public int getColumnCount(){
        //something, don't really need this
        return colCount;
    }
    @Override
    public Object getValueAt(int row, int col){//wanted to use this instead of getItemAt, but making it static made it not override.
        try{
            resultSet.absolute(row+1);
            Object o=resultSet.getObject(col+1);
            return o.toString();
        }catch(SQLException se){
            se.printStackTrace();
            return se.toString();

        }
    }
}
