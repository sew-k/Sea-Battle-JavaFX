package com.kodilla.seabattle_javafx.logic;

import com.kodilla.seabattle_javafx.data.*;
import com.kodilla.seabattle_javafx.presentation.Drawer;
import com.kodilla.seabattle_javafx.presentation.Keyboard;
import com.kodilla.seabattle_javafx.presentation.Printer;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class GameProcessorTestSuite {
    @Test
    void testSetCurrentPlayerFx() {
        //Given
        GameProcessor gameProcessor = new GameProcessor();
        Player playerOne = new HumanPlayer();
        playerOne.setName("aaa");
        GameProcessor.playerOne = playerOne;
        Player playerTwo = new HumanPlayer();
        playerTwo.setName("bbb");
        GameProcessor.playerTwo = playerTwo;

        //When
        gameProcessor.setCurrentPlayerFx(playerOne);

        //Then
        Assertions.assertEquals(GameProcessor.currentPlayer, playerOne);
        Assertions.assertEquals(GameProcessor.waitingPlayer, playerTwo);
    }
    @Test
    void testExitGameFx() {
        //Given
        GameProcessor gameProcessor = new GameProcessor();
        Stage stage = mock(Stage.class);

        //When
        gameProcessor.exitGameFx(stage);

        //Then
        verify(stage, atLeast(1)).close();
    }
    @Test
    void testPlayerWinGame() {
        //Given
        Player player = new HumanPlayer();
        player.setScore(1);
        player.setName("Aaaa");
        Drawer drawer = mock(Drawer.class);
        GameProcessor gameProcessor = new GameProcessor(drawer);

        ScoreBoard scoreBoard = new ScoreBoard();

        //When
        gameProcessor.playerWinGameFx(player);

        //Then
        Assertions.assertTrue(ScoreBoard.getScoreMap().containsKey("Aaaa"));
    }
    @Test
    void testWinnerOfBattleCheckIsWinner() {
        //Given
        Player winner = new HumanPlayer();
        Player loser = new HumanPlayer();
        Ship ship = new Ship();
        Map<String, String> status = new HashMap<>();
        status.put("a1", "sink");
        ship.setStatusOnBoard(status);
        loser.addShip(ship);
        GameProcessor gameProcessor = new GameProcessor();

        //When
        Player result = gameProcessor.winnerOfBattleCheck(winner, loser);

        //Then
        Assertions.assertEquals(winner, result);
    }
    @Test
    void testWinnerOfBattleCheckNoOneWinsYet() {
        //Given
        Player winner = new HumanPlayer();
        Player loser = new HumanPlayer();
        Ship ship = new Ship();
        Map<String, String> status = new HashMap<>();
        status.put("a1", "good");
        ship.setStatusOnBoard(status);
        loser.addShip(ship);
        GameProcessor gameProcessor = new GameProcessor();

        //When
        Player result = gameProcessor.winnerOfBattleCheck(winner, loser);

        //Then
        Assertions.assertEquals(null, result);
    }
    @Test
    void testEndPlayerTurnFxNoWinner() {
        //Given
        Player plOne = new HumanPlayer();
        plOne.setName("One");
        Player plTwo = new HumanPlayer();
        plTwo.setName("Two");
        Ship ship = new Ship();
        Map<String, String> status = new HashMap<>();
        status.put("a1", "good");
        ship.setStatusOnBoard(status);
        plTwo.addShip(ship);
        GameProcessor gameProcessor = new GameProcessor();
        gameProcessor.setPlayerOne(plOne);
        gameProcessor.setPlayerTwo(plTwo);
        gameProcessor.setCurrentPlayerFx(plOne);

        //When
        gameProcessor.endPlayerTurnFx(plOne);

        //Then
        Assertions.assertEquals(GameProcessor.currentPlayer, plTwo);
    }
    @Test
    void testEndPlayerTurnFxPlayerOneIsWinner() {
        //Given
        Player plOne = new HumanPlayer();
        plOne.setName("One");
        Player plTwo = new HumanPlayer();
        plTwo.setName("Two");
        Ship ship = new Ship();
        Map<String, String> status = new HashMap<>();
        status.put("a1", "sink");
        ship.setStatusOnBoard(status);
        plTwo.addShip(ship);
        Drawer drawer = mock(Drawer.class);

        GameProcessor gameProcessor = spy(new GameProcessor(drawer));
        gameProcessor.setPlayerOne(plOne);
        gameProcessor.setPlayerTwo(plTwo);
        gameProcessor.setCurrentPlayerFx(plOne);

        //When
        gameProcessor.endPlayerTurnFx(plOne);

        //Then
        verify(gameProcessor, atLeast(1)).playerWinGameFx(plOne);
        Assertions.assertEquals(GameProcessor.currentPlayer, plOne);
    }
    @Test
    void testSingleShotProcessorFx() {
        //Given
        Player plOne = new HumanPlayer();
        plOne.setName("One");
        Player plTwo = new HumanPlayer();
        plTwo.setName("Two");
        Ship ship = new Ship();
        Map<String, String> status = new HashMap<>();
        status.put("a1", "good");
        status.put("a2", "good");
        ship.setStatusOnBoard(status);
        plTwo.addShip(ship);
        GameProcessor gameProcessor = new GameProcessor();
        gameProcessor.setPlayerOne(plOne);
        gameProcessor.setPlayerTwo(plTwo);
        gameProcessor.setCurrentPlayerFx(plOne);

        //When
        gameProcessor.singleShotProcessorFx(plOne,plTwo,"a1");
        List<Ship> plTwoShips = plTwo.getShips();
        Ship plTwoShip = plTwoShips.get(0);
        String result = plTwoShip.getStatusOnBoard().get("a1");

        //Then
        Assertions.assertEquals("hit", result);
    }
    @Test
    void testSingleShotProcessor() {
        //Given
        Player plOne = new ComputerPlayer();
        Player plTwo = new HumanPlayer();
        plTwo.setName("Two");
        GameProcessor gameProcessor = new GameProcessor();

        //When
        gameProcessor.singleShotProcessor(plOne,plTwo);
        int result = plOne.getShots().size();

        //Then
        Assertions.assertEquals(1, result);
    }
    @Test
    void testSingleTurnProcessorIfGameExit() {
        //Given
        Drawer drawer = mock(Drawer.class);
        GameProcessor gameProcessor = spy(new GameProcessor(drawer));
        Player player1 = new HumanPlayer();
        player1.setName("aaa");
        Player player2 = new ComputerPlayer();
        Stage stage = mock(Stage.class);

        PlayerSettings.setCurrentPlayerSettings(2);
        GameProcessor.continueGame = false;

        //When
        boolean result = gameProcessor.singleTurnProcessorFx(stage, player1,player2);

        //Then
        Assertions.assertFalse(result);
    }
    @Test
    void testSingleTurnProcessorFxIfContinueGame() {
        //Given
        GameProcessor.continueGame = true;
        Drawer drawer = mock(Drawer.class);
        GameProcessor gameProcessor = spy(new GameProcessor(drawer));
        Player player1 = new HumanPlayer();
        player1.setName("aaa");
        Player player2 = new ComputerPlayer();
        Stage stage = mock(Stage.class);

        PlayerSettings.setCurrentPlayerSettings(2);

        //When
        boolean result = gameProcessor.singleTurnProcessorFx(stage, player1, player2);

        //Then
        Assertions.assertTrue(result);
    }

}
