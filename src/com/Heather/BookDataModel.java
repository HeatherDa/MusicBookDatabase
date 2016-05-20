package com.Heather;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by cryst on 5/9/2016.
 */
public class BookDataModel extends AbstractTableModel{
    private static int rowCount=0;
    private static int colCount=0;
    static ResultSet resultSet;

    public BookDataModel (ResultSet results){
        resultSet=results;
        setup();
        //Main.addTestData();
        //Main.loadAllBookData();
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
        fireTableDataChanged();
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
        return colCount;
    }
    @Override
    public Object getValueAt(int row, int col){
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
