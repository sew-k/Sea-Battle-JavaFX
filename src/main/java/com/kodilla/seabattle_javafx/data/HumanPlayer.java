package com.kodilla.seabattle_javafx.data;

import com.kodilla.seabattle_javafx.logic.GameProcessor;
import com.kodilla.seabattle_javafx.logic.Settings;
import com.kodilla.seabattle_javafx.presentation.Drawer;
import com.kodilla.seabattle_javafx.presentation.Keyboard;
import com.kodilla.seabattle_javafx.presentation.Printer;
import javafx.stage.Stage;

import java.util.*;

public class HumanPlayer extends Player {

    //private String name;
    private List<Ship> ships = new ArrayList<>();
    private Set<String> shots = new HashSet<>();
    private Set<String> availableFieldsOnBoard = new HashSet<>();
    private Ship currentShip;
    private Map<Integer,Integer> shipsToSet;
    private boolean allShipsSet;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        Player that = (Player) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean isAllShipsSet() {
        return allShipsSet;
    }
    @Override
    public void setAllShipsSet(boolean allShipsSet) {
        this.allShipsSet = allShipsSet;
    }
    @Override
    public Map<Integer, Integer> getShipsToSet() {
        return shipsToSet;
    }

    public HumanPlayer() {
        shipsToSet = new HashMap<>(Settings.getShipCountSettings());
        allShipsSet = false;
    }
    @Override
    public Set<String> getAvailableFieldsOnBoard() {
        return availableFieldsOnBoard;
    }

    @Override
    public List<Ship> getShips() {
        return ships;
    }
    @Override
    public Ship getCurrentShip() {
        return currentShip;
    }

    @Override
    public void addShot(String shot) {
        this.shots.add(shot);
    }
    @Override
    public String selectTarget() {
        Keyboard keyboard = new Keyboard();
        String target = keyboard.getString();
        return target;
    }
    @Override
    public void setShips(List<Ship> ships) {
        this.ships = ships;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
    @Override
    public void addShip(Ship ship) {
        this.ships.add(ship);
    }
    @Override
    public Set<String> getShots() {
        return shots;
    }
    @Override
    public Set<String> getAvailableFieldsForShipSetUp(Ship ship) {
        return super.getAvailableFieldsForShipSetUp(ship);
    }

    public void setAvailableFieldsOnBoard() {
        this.availableFieldsOnBoard = checkAvailableFieldsOnPlayerBoard();
    }

    public Set<String> checkAvailableFieldsOnPlayerBoard() {
        return super.checkAvailableFieldsOnPlayerBoard();
    }

    @Override
    public void shipsSetUp() {
        Drawer drawer = new Drawer();
        while (!isAllShipsSet()) {
            drawer.drawPlayerBoardForShipsSetUp(new Stage(), GameProcessor.currentPlayer);
        }
    }

    @Override
    public boolean tryFieldForShipSetUp(Stage stage, String temporaryField, Ship ship) {
        System.out.println("trying field: " + temporaryField);

        setAvailableFieldsOnBoard();
        System.out.println("available fields on board: " + getAvailableFieldsOnBoard());
        Printer printer = new Printer();

        List<String> availableFieldsForShipSetUp = getAvailableFieldsForShipSetUp(ship).stream()
                    .filter(c -> getAvailableFieldsOnBoard().contains(c))
                    .toList();

            printer.printAvailableFieldsForShipSetUp(availableFieldsForShipSetUp);

            Map<String,String> currentShipStatus = ship.getStatusOnBoard();

            if (getAvailableFieldsForShipSetUp(ship).contains(temporaryField)) {
                currentShipStatus.put(temporaryField, "good");
                System.out.println("set field for ship: " + temporaryField);
                if (currentShipStatus.size() == ship.getSize()) {
                    addShip(ship);
                    stage.close();
                    ship.setBufferZone();
                    setAvailableFieldsOnBoard();
                    printer.printShipAdded(ship);
                    printer.printPlayerShips(this);
                    currentShip = null;
                    if (shipsToSet.get(ship.getSize()) > 0) {
                        shipsToSet.replace(ship.getSize(), (shipsToSet.get(ship.getSize()) - 1));
                    }

                    if (shipsToSet.values().stream().allMatch(n -> n == 0)) {
                        System.out.println("ALL SHIPS SET");
                        setAllShipsSet(true);
                    } else {

                        System.out.println("------------------");
                        shipsToSet.entrySet().stream()
                                .forEach(System.out::println);
                        System.out.println("------------------");

                    }
                }
                return true;
            } else {
                printer.incorrectSelectionMessage();
                return false;
            }
        }

    @Override
    public void setCurrentShip(int currentShipSize) {
        currentShip = new Ship();
        currentShip.setSize(currentShipSize);
        System.out.println("Chosen current ship size to set: " + currentShip.getSize());
    }

}
