package com.kodilla.seabattle_javafx.data;

import javafx.stage.Stage;

import java.util.*;
import java.util.stream.Collectors;

public abstract class Player {
    public String name;
    private List<Ship> ships;
    public int score;
    private Set<String> shots = new HashSet<>();
    private Set<String> availableFieldsOnBoard = new HashSet<>();
    private Ship currentShip;
    private boolean allShipsSet;

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null) return false;
//
//        Player player = (Player) o;
//
//        return name.equals(player.name);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(name);
//    }

    public boolean isAllShipsSet() {
        return allShipsSet;
    }

    public void setAllShipsSet(boolean allShipsSet) {
        this.allShipsSet = allShipsSet;
    }

    private Map<Integer,Integer> shipsToSet;

    public Map<Integer, Integer> getShipsToSet() {
        return shipsToSet;
    }

    public Set<String> getAvailableFieldsOnBoard() {
        return availableFieldsOnBoard;
    }

    public Set<String> getAvailableFieldsForShipSetUp(Ship ship) {
        Board board = new Board();

        int size = ship.getSize();
        Map<String, String> shipStatus = ship.getStatusOnBoard();
        Set<String> shipFieldsSet = shipStatus.keySet().stream().collect(Collectors.toSet());
        Set<String> availableFieldsForShipSetUp = new HashSet<>();
        if (shipFieldsSet.isEmpty()) {
            return getAvailableFieldsOnBoard();
        } else if ((shipFieldsSet.size() < size) && (shipFieldsSet.size() == 1)) {
            String field = shipFieldsSet.stream().toList().get(0);
            String column = field.substring(0, 1);
            String row;
            if (field.length() > 2) {
                row = field.substring(1, 3);
            } else {
                row = field.substring(1, 2);
            }
            if (board.nextColumnIfAvailable(field) != null)
                availableFieldsForShipSetUp.add(board.nextColumnIfAvailable(field) + row);
            if (board.previousColumnIfAvailable(field) != null)
                availableFieldsForShipSetUp.add(board.previousColumnIfAvailable(field) + row);
            if (board.nextRowIfAvailable(field) != null)
                availableFieldsForShipSetUp.add(column + board.nextRowIfAvailable(field));
            if (board.previousRowIfAvailable(field) != null)
                availableFieldsForShipSetUp.add(column + board.previousRowIfAvailable(field));
            return availableFieldsForShipSetUp.stream()
                    .filter(c->getAvailableFieldsOnBoard().contains(c))
                    .collect(Collectors.toSet());
        } else if (shipFieldsSet.size() < size) {
            List<String> shipFieldsList = shipFieldsSet.stream().toList();
            if (shipFieldsList.get(0).charAt(0) == shipFieldsList.get(shipFieldsList.size() - 1).charAt(0)) {
                for (String field : shipFieldsList) {
                    String column = field.substring(0, 1);
                    String row;
                    if (field.length() > 2) {
                        row = field.substring(1, 3);
                    } else {
                        row = field.substring(1, 2);
                    }
                    if (board.nextRowIfAvailable(field) != null)
                        availableFieldsForShipSetUp.add(column + board.nextRowIfAvailable(field));
                    if (board.previousRowIfAvailable(field) != null)
                        availableFieldsForShipSetUp.add(column + board.previousRowIfAvailable(field));
                }
                availableFieldsForShipSetUp.removeAll(shipFieldsSet);
                return  availableFieldsForShipSetUp.stream()
                        .filter(c->getAvailableFieldsOnBoard().contains(c))
                        .collect(Collectors.toSet());
            } else {
                for (String field : shipFieldsList) {
                    String column = field.substring(0, 1);
                    String row;
                    if (field.length() > 2) {
                        row = field.substring(1, 3);
                    } else {
                        row = field.substring(1, 2);
                    }
                    if (board.nextColumnIfAvailable(field) != null) availableFieldsForShipSetUp.add(board.nextColumnIfAvailable(field) + row);
                    if (board.previousColumnIfAvailable(field) != null) availableFieldsForShipSetUp.add(board.previousColumnIfAvailable(field) + row);
                }
                availableFieldsForShipSetUp.removeAll(shipFieldsSet);
                return availableFieldsForShipSetUp.stream()
                        .filter(c->getAvailableFieldsOnBoard().contains(c))
                        .collect(Collectors.toSet());
            }
        } else if (shipFieldsSet.size() == size) {
            return null;
        } else return null;
    }

    public Set<String> checkAvailableFieldsOnPlayerBoard() {
        Board board = new Board();
        Set<String> fields = board.getAllFieldsOnBoard();

        for (Ship ship : getShips()) {
            for (Map.Entry<String,String> entry : ship.getStatusOnBoard().entrySet()) {
                fields.remove(entry.getKey());
            }
            for (String field : ship.getBufferZone()) {
                fields.remove(field);
            }
        }
        return fields;
    }

    public String getName() {
        return name;
    }

    public void addShot(String shot) {
        this.shots.add(shot);
    }

    public Set<String> getShots() {
        return shots;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public void setShips(List<Ship> ships) {
        this.ships = ships;
    }

    public String selectTarget() {
        //temporarily
        return null;
    }

    public void addShip(Ship ship) {
        this.ships.add(ship);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void shipsSetUp() {
    }
    public void shipsSetUpFx(Stage primaryStage) {

    }
    public boolean tryFieldForShipSetUp(String field, Ship ship) {
        return true;
    }

    public boolean tryFieldForShipsSetUp(String temporaryField) {
        return true;
    }

    public boolean askForAllShipsSetUp(Stage primaryStage) {
        return allShipsSet;

    }

    public void setCurrentShip(int currentShipSize) {

    }

    public Ship getCurrentShip() {
        return currentShip;
    }

}
