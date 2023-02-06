package com.kodilla.seabattle_javafx.data;

import com.kodilla.seabattle_javafx.logic.GameProcessor;
import com.kodilla.seabattle_javafx.logic.Settings;
import com.kodilla.seabattle_javafx.presentation.Drawer;
import com.kodilla.seabattle_javafx.presentation.Keyboard;
import com.kodilla.seabattle_javafx.presentation.Printer;
import javafx.stage.Stage;

import java.util.*;
import java.util.stream.Collectors;

public class HumanPlayer extends Player {

    private String name;
    private List<Ship> ships = new ArrayList<>();
    private Set<String> shots = new HashSet<>();
    private Set<String> availableFieldsOnBoard = new HashSet<>();
    private Ship currentShip;
    private Map<Integer,Integer> shipsToSet;
    private boolean allShipsSet;

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

    @Override
    public boolean askForAllShipsSetUp(Stage primaryStage) {
        Drawer drawer = new Drawer();

        System.out.println("PLAYER - im asked");
        drawer.drawPlayerBoardForShipsSetUp(primaryStage, this);
        drawer.askPlayerForSelectShipToSetWindow(this);


        return allShipsSet;
    }
    @Override
    public boolean tryFieldForShipSetUp(String temporaryField, Ship ship) {
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
                    ship.setBufferZone();
                    setAvailableFieldsOnBoard();
                    //shipAdded = true;
                    printer.printShipAdded(ship);
                    printer.printPlayerShips(this);
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
                        //askForShipsSetUp();
                    }
                }
                return true;
            } else {
                printer.incorrectSelectionMessage();
                return false;
            }
        }
    @Override
    public boolean tryFieldForShipsSetUp(String temporaryField) {
//        System.out.println("trying field: " + temporaryField);
//        Printer printer = new Printer();
//        Map<Integer,Integer> shipCountSettings = Settings.getShipCountSettings();
//
//        setAvailableFieldsOnBoard();
//        System.out.println(getAvailableFieldsOnBoard());
//
//        for (Map.Entry<Integer,Integer> entry : shipCountSettings.entrySet()) {
//            for (int j=0; j< entry.getValue(); j++) {
//                Ship ship = new Ship();
//                ship.setSize(entry.getKey());
//                Map<String,String> shipStatus = new HashMap<>();
//
//                printer.askForSetUpShip(ship);
//
//                boolean shipAdded = false;
//                while (!shipAdded) {
//                    for (int i = 0; i < entry.getKey(); i++) {    // iterate field of ship to set up
//                        if (i == 0) {
//                            printer.askForField(this);
//                        } else if (i > 0) {
//                            printer.askForNextField(this);
//                        }
//                        boolean correct = false;
//                        while (!correct) {
//                            List<String> availableFieldsForShipSetUp = new ArrayList<>();
//                            availableFieldsForShipSetUp = getAvailableFieldsForShipSetUp(ship).stream()
//                                    .filter(c -> getAvailableFieldsOnBoard().contains(c))
//                                    .toList();
//
//                            printer.printAvailableFieldsForShipSetUp(availableFieldsForShipSetUp);
//
//                            shipStatus = ship.getStatusOnBoard();
//
//                            if (getAvailableFieldsForShipSetUp(ship).contains(temporaryField)) {
//                                shipStatus.put(temporaryField, "good");
//                                correct = true;
//                                System.out.println("FIELD GOOOOD");
//                                ship.setStatusOnBoard(shipStatus);
//                                ship.setBufferZone();
//
//                                if (ship.getStatusOnBoard().size() == entry.getKey()) {
//                                    addShip(ship);
//                                    ship.setBufferZone();
//                                    setAvailableFieldsOnBoard();
//                                    shipAdded = true;
//                                    printer.printShipAdded(ship);
//                                    printer.playersBoardDrawer(this);
//                                }
//
//                            } else if (!getAvailableFieldsForShipSetUp(ship).contains(temporaryField)) {
//                                printer.incorrectSelectionMessage();
//                                printer.askForField(this);
//                                return false;
//                            }
//                        }
//
//                    }
//
//                }
//
//            }
//        }
        return true;
    }
    @Override
    public void setCurrentShip(int currentShipSize) {
        currentShip = new Ship();
        currentShip.setSize(currentShipSize);
        System.out.println("Chosen current ship size to set: " + currentShip.getSize());
    }

}
