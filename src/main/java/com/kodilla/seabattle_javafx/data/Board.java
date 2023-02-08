package com.kodilla.seabattle_javafx.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Board {

    private static int rowsCount = 10;
    private static int columnsCount = 10;
    private  List<String> rows = new ArrayList<>();
    private  List<String> columns = new ArrayList<>();

    public Board() {

        for (int i = 0; i< rowsCount; i++) {
            rows.add(Integer.toString(i+1));
        }
        for (int j = 0; j< columnsCount; j++) {
            String alphabet = "abcdefghijklmnopqrstuvwxyz";
            columns.add(Character.toString(alphabet.charAt(j)));
        }
    }

    public static void setRowsCount(int rowsCount) {
        Board.rowsCount = rowsCount;
    }

    public static void setColumnsCount(int columnsCount) {
        Board.columnsCount = columnsCount;
    }

    public String nextRowIfAvailable(String field) {
        String nextRow = null;
        String row;
        if (field.length() > 2) {
            row = field.substring(1,3);
        } else {
            row = field.substring(1,2);
        }
        if (Integer.parseInt(row) < this.rows.size()) {
            nextRow = this.rows.get(Integer.parseInt(row));
        }
        return nextRow;
    }

    public String previousRowIfAvailable(String field) {
        String previousRow = null;
        String row;
        if (field.length() > 2) {
            row = field.substring(1,3);
        } else {
            row = field.substring(1,2);
        }
        if ((Integer.parseInt(row)-1) > 0) {
            previousRow = this.rows.get((Integer.parseInt(row)-1) - 1);
        }
        return previousRow;
    }

    public String nextColumnIfAvailable(String field) {
        String nextColumn = null;
        String column = field.substring(0,1);
        if (this.columns.indexOf(column) < (this.columns.size() - 1)) {
            nextColumn = this.columns.get(this.columns.indexOf(column) + 1);
        }
        return nextColumn;
    }

    public String previousColumnIfAvailable(String field) {
        String previousColumn = null;
        String column = field.substring(0,1);
        if (this.columns.indexOf(column) > 0) {
            previousColumn = this.columns.get(this.columns.indexOf(column) - 1);
        }
        return previousColumn;
    }

    public List<String> allAroundFieldsOfBufferZoneIfAvailable(String field) {
        String column = field.substring(0,1);
        String row;
        if (field.length() > 2) {
            row = field.substring(1,3);
        } else {
            row = field.substring(1,2);
        }
        List<String> resultList = new ArrayList<>();
        if (nextColumnIfAvailable(field) != null) resultList.add(nextColumnIfAvailable(field) + row);
        if (previousColumnIfAvailable(field) != null) resultList.add(previousColumnIfAvailable(field) + row);
        if (nextRowIfAvailable(field) != null) resultList.add(column + nextRowIfAvailable(field));
        if (previousRowIfAvailable(field) != null) resultList.add(column + previousRowIfAvailable(field));
        if (nextColumnIfAvailable(field) != null && previousRowIfAvailable(field) != null) resultList.add(nextColumnIfAvailable(field) + previousRowIfAvailable(field));
        if (nextColumnIfAvailable(field) != null && nextRowIfAvailable(field) != null) resultList.add(nextColumnIfAvailable(field) + nextRowIfAvailable(field));
        if (previousColumnIfAvailable(field) != null && nextRowIfAvailable(field) != null) resultList.add(previousColumnIfAvailable(field) + nextRowIfAvailable(field));
        if (previousColumnIfAvailable(field) != null && previousRowIfAvailable(field) != null) resultList.add(previousColumnIfAvailable(field) + previousRowIfAvailable(field));

        return resultList;
    }

    public static int getRowsCount() {
        return rowsCount;
    }

    public static int getColumnsCount() {
        return columnsCount;
    }

    public  List<String> getRows() {
        return rows;
    }

    public  List<String> getColumns() {
        return columns;
    }

    public Set<String> getAllFieldsOnBoard() {
        Set<String> fields = new HashSet<>();
        List<String> rows = getRows();
        List<String> columns = getColumns();
        for (int i = 0; i < columns.size(); i++) {
            for (int j = 0; j < rows.size(); j++) {
                fields.add(columns.get(i) + rows.get(j));
            }
        }
        return fields;
    }
}
