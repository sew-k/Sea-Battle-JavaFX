package com.kodilla.seabattle_javafx.data;

import java.util.HashMap;
import java.util.Map;

public class ScoreBoard {
    private static Map<String,Integer> scoreMap;
    static {

        //temporarily values
        scoreMap = new HashMap<>();
        scoreMap.put("Aaaa", 2);
        scoreMap.put("Bbbb", 1);
        scoreMap.put("Cccc", 10);
        scoreMap.put("Dddd", 1);
    }


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
