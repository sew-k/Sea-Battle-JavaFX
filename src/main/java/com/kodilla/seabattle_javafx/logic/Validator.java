package com.kodilla.seabattle_javafx.logic;

import com.kodilla.seabattle_javafx.data.Board;
import com.kodilla.seabattle_javafx.data.Player;
import com.kodilla.seabattle_javafx.data.Ship;

import java.util.Map;

public class Validator {
    public boolean validateForOptions(String keys, Options options) {
        boolean result = false;
        if ("0123456789".contains(keys)) {
            int input = Integer.parseInt(keys);
            if ((input >= 0) && (input <= (options.getOptions().size()-1))) {
                result = true;
            }
        }
        return result;
    }
    public boolean validateForChangeSettings(String keys, Options options) {
        Settings settings = new Settings();
        if ((keys.equals(settings.getKeyForChangeSettings())) || (keys.equals(settings.getKeyForLeaveSettings()))) {
            return true;
            } else {
            return false;
        }
    }
    public boolean validateIsTargetOnBoard(String target, Board board) {
        if ((target.length() == 2) &&
                (board.getColumns().contains(Character.toString(target.charAt(0)))) &&
                (board.getRows().contains(Character.toString(target.charAt(1))))) {
            return true;
        } else if ((target.length() == 3) &&
                (board.getColumns().contains(Character.toString(target.charAt(0)))) &&
                (board.getRows().contains(Character.toString(target.charAt(1)) +
                        Character.toString(target.charAt(2))))) {
            return true;
        } else {
            return false;
        }
    }
    public boolean validateIfFieldAlreadyChosen(String newField, Map<String,String> shipStatus, Player player) {
        boolean isFieldInThisShipAlready = shipStatus.entrySet().stream()
                .anyMatch(en -> en.getKey().equals(newField));
        boolean isFieldInOtherShipAlready = player.getShips().stream()
                .flatMap(s -> s.getStatusOnBoard().entrySet().stream())
                .anyMatch(e -> e.getKey().equals(newField));
        if (isFieldInThisShipAlready || isFieldInOtherShipAlready) {
            return false;
        } else {
            return true;
        }
    }
    public boolean isFieldAvailable(String newField, Player player) {
        if (player.getAvailableFieldsOnBoard().equals(newField)) {
            return true;
        } else {
            return false;
        }
    }
    public boolean validateFieldToSetUpShip(String newField, Ship ship, Player player) {
        boolean checkIfFieldAlreadyChosen = player.getShips().stream()
                .flatMap(ship1 -> ship1.getStatusOnBoard().entrySet().stream())
                .anyMatch(e -> e.getKey() == newField);
        return false;
    }
}
