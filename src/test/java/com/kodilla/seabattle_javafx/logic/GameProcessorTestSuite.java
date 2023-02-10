package com.kodilla.seabattle_javafx.logic;

import com.kodilla.seabattle_javafx.data.HumanPlayer;
import com.kodilla.seabattle_javafx.data.Player;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void testStartGame() {
        //Given

        //When

        //Then

    }
}
