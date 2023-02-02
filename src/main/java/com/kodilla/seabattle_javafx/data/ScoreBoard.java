package com.kodilla.seabattle_javafx.data;

import java.util.HashMap;
import java.util.Map;

public class ScoreBoard {
    private static Map<String,Integer> scoreMap = new HashMap<>();

    public static void addScore(String playerName, int wins) {
        if (scoreMap.containsKey(playerName)) {
            int oldScore = scoreMap.get(playerName);
            scoreMap.replace(playerName,oldScore+wins);
        } else {
            scoreMap.put(playerName, wins);
        }
    }
    public static Map<String, Integer> getScoreMap() {
        return scoreMap;
    }
}
