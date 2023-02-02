package com.kodilla.seabattle_javafx.data;

import com.kodilla.seabattle_javafx.logic.Settings;
import com.kodilla.seabattle_javafx.presentation.Printer;

import java.util.*;

public class ComputerPlayer extends Player {

    private final String name = "Computer";
    private List<Ship> ships = new ArrayList<>();
    private Set<String> shots = new HashSet<>();
    private Set<String> availableFieldsOnBoard = new HashSet<>();
    @Override
    public Set<String> getAvailableFieldsOnBoard() {
        return availableFieldsOnBoard;
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
    public List<Ship> getShips() {
        return ships;
    }
    @Override
    public void addShot(String shot) {
        this.shots.add(shot);
    }
    @Override
    public void setShips(List<Ship> ships) {
        this.ships = ships;
    }

    @Override
    public void addShip(Ship ship) {
        this.ships.add(ship);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<String> getShots() {
        return shots;
    }

    @Override
    public String selectTarget() {                           //TODO: selecting target from available fields on board!
        Printer printer = new Printer();
        Board board = new Board();
        Random randomRow = new Random();
        Random randomColumn = new Random();
        String target = "";
        boolean inCorrectTarget = true;
        while (inCorrectTarget) {
            target = (board.getColumns().get(randomColumn.nextInt(board.getColumns().size())) +
                    board.getRows().get(randomRow.nextInt(board.getRows().size())));
            printer.printTarget(target);
            if (!getShots().contains(target)) {
                addShot(target);
                inCorrectTarget = false;
            }
        }
        return target;
    }

    @Override
    public void shipsSetUp() {

        Printer printer = new Printer();
        Random random = new Random();
        printer.printShipCountSettings(Settings.getShipCountSettings());
        Map<Integer,Integer> shipCountSettings = Settings.getShipCountSettings();

        setAvailableFieldsOnBoard();

        for (Map.Entry<Integer,Integer> entry : shipCountSettings.entrySet()) {
            for (int j=0; j< entry.getValue(); j++) {

                boolean shipTrulyAdded = false;
                while (!shipTrulyAdded) {

                    Ship ship = new Ship();
                    ship.setSize(entry.getKey());
                    Map<String, String> shipStatus = new HashMap<>();


                    printer.askForSetUpShip(ship);

                    boolean shipAvailabilityOfAddingCheckedEnd = false;
                    while (!shipAvailabilityOfAddingCheckedEnd) {
                        for (int i = 0; i < entry.getKey(); i++) {    // iterate field of ship to set up

                            List<String> availableFieldsForShipSetUp = new ArrayList<>();
                            availableFieldsForShipSetUp = getAvailableFieldsForShipSetUp(ship).stream()
                                    .filter(c -> getAvailableFieldsOnBoard().contains(c))
                                    .toList();

                            printer.printAvailableFieldsForShipSetUp(availableFieldsForShipSetUp);

                            if (i == 0) {
                                printer.askForField(this);
                            } else if (i > 0) {
                                printer.askForNextField(this);
                            }

                            if (availableFieldsForShipSetUp.size() > 0) {
                                String temporaryField = availableFieldsForShipSetUp.get(random.nextInt(0, availableFieldsForShipSetUp.size()));
                                shipStatus = ship.getStatusOnBoard();
                                shipStatus.put(temporaryField, "good");
                                printer.printTarget(temporaryField);
                                ship.setStatusOnBoard(shipStatus);
                                ship.setBufferZone();
                            } else if (availableFieldsForShipSetUp.size() == 0) {
                                System.out.println("CAN'T ADD THAT SHIP");    //TODO
                                shipAvailabilityOfAddingCheckedEnd = true;
                            }

                            if (ship.getStatusOnBoard().size() == entry.getKey()) {
                                addShip(ship);
                                ship.setBufferZone();
                                setAvailableFieldsOnBoard();

                                printer.printShipAdded(ship);
                                printer.playersBoardDrawer(this);
                                shipTrulyAdded = true;
                                shipAvailabilityOfAddingCheckedEnd = true;
                            }
                        }
                    }
                }
            }
        }
    }
}
