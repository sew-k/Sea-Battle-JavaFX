package com.kodilla.seabattle_javafx.data;

import java.util.*;
import java.util.stream.Collectors;

public class Ship {
    private int size;
    private Map<String,String> statusOnBoard = new HashMap<>();
    private Set<String> bufferZone = new HashSet<>();
    public int getSize() {
        return size;
    }
    public Map<String, String> getStatusOnBoard() {
        return statusOnBoard;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ship ship = (Ship) o;

        if (size != ship.size) return false;
        return Objects.equals(statusOnBoard, ship.statusOnBoard);
    }
    @Override
    public String toString() {
        String result = "";
        if (getSize() != getStatusOnBoard().size()) {
            String field = "?";
            String shipSize = "";
            for (int i = 0; i < getSize(); i++) {
                shipSize = shipSize + "[" + field + "]";
            }
            result = shipSize;
        } else if (getSize() == getStatusOnBoard().size()) {
            String shipSimpleString = "";
            String field = "?";
            for (Map.Entry<String,String> entry : getStatusOnBoard().entrySet()) {
                if (entry.getKey() != null) {
                    field = entry.getKey();
                }
            shipSimpleString = shipSimpleString + "[" + field + "]";
            }
            result = shipSimpleString;
        }
        return result;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public void setStatusOnBoard(Map<String, String> statusOnBoard) {
        this.statusOnBoard = statusOnBoard;
    }
    public Set<String> getBufferZone() {
        return bufferZone;
    }
    public void setBufferZone() {
        Set<String> setOfFields = new HashSet<>();
        String row;
        String column;
        Board board = new Board();
        setOfFields = getStatusOnBoard().entrySet().stream()
                .map(e->e.getKey())
                .collect(Collectors.toSet());

        Set<String> fieldSet = new HashSet<>();
        for (String field : setOfFields) {
            fieldSet.addAll(board.allAroundFieldsOfBufferZoneIfAvailable(field).stream().collect(Collectors.toSet()));
        }
        setOfFields.addAll(fieldSet);

        for (Map.Entry<String,String> entry : getStatusOnBoard().entrySet()) {
                    setOfFields.remove(entry.getKey());
        }

        this.bufferZone = setOfFields;
    }
    @Override
    public int hashCode() {
        int result = size;
        result = 31 * result + (statusOnBoard != null ? statusOnBoard.hashCode() : 0);
        return result;
    }
    public void checkIfShipSink() {
        boolean shipSink = true;
        for (Map.Entry<String,String> entry : getStatusOnBoard().entrySet()) {
            if (entry.getValue().equals("good")) {
                shipSink = false;
            }
        }
        if (shipSink) {
            for (Map.Entry<String,String> entry : getStatusOnBoard().entrySet()) {
                entry.setValue("sink");
            }
        }
    }
}
