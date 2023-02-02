package com.kodilla.seabattle_javafx.data;

import com.kodilla.seabattle_javafx.logic.Settings;
import com.kodilla.seabattle_javafx.presentation.Keyboard;
import com.kodilla.seabattle_javafx.presentation.Printer;

import java.util.*;

public class HumanPlayer extends Player {

    private String name;
    private List<Ship> ships = new ArrayList<>();
    private Set<String> shots = new HashSet<>();
    private Set<String> availableFieldsOnBoard = new HashSet<>();
    @Override
    public Set<String> getAvailableFieldsOnBoard() {
        return availableFieldsOnBoard;
    }

    @Override
    public List<Ship> getShips() {
        return ships;
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
        Printer printer = new Printer();
        Keyboard keyboard = new Keyboard();
        printer.printShipCountSettings(Settings.getShipCountSettings());
        Map<Integer,Integer> shipCountSettings = Settings.getShipCountSettings();

        setAvailableFieldsOnBoard();

        for (Map.Entry<Integer,Integer> entry : shipCountSettings.entrySet()) {
                for (int j=0; j< entry.getValue(); j++) {
                    Ship ship = new Ship();
                    ship.setSize(entry.getKey());
                    Map<String,String> shipStatus = new HashMap<>();

                    printer.askForSetUpShip(ship);

                    boolean shipAdded = false;
                    while (!shipAdded) {
                        for (int i = 0; i < entry.getKey(); i++) {    // iterate field of ship to set up
                            if (i == 0) {
                                printer.askForField(this);
                            } else if (i > 0) {
                                printer.askForNextField(this);
                            }
                            boolean correct = false;
                            while (!correct) {
                                List<String> availableFieldsForShipSetUp = new ArrayList<>();
                                availableFieldsForShipSetUp = getAvailableFieldsForShipSetUp(ship).stream()
                                        .filter(c -> getAvailableFieldsOnBoard().contains(c))
                                        .toList();

                                printer.printAvailableFieldsForShipSetUp(availableFieldsForShipSetUp);

                                String temporaryField = keyboard.getString();
                                shipStatus = ship.getStatusOnBoard();

                                if (getAvailableFieldsForShipSetUp(ship).contains(temporaryField)) {
                                    shipStatus.put(temporaryField, "good");
                                    correct = true;
                                } else {
                                    printer.incorrectSelectionMessage();
                                    printer.askForField(this);
                                }
                            }
                            ship.setStatusOnBoard(shipStatus);
                            ship.setBufferZone();
                        }
                        addShip(ship);
                        ship.setBufferZone();
                        setAvailableFieldsOnBoard();
                        shipAdded = true;
                    }
                    printer.printShipAdded(ship);
                    printer.playersBoardDrawer(this);
                }
        }
    }
}
