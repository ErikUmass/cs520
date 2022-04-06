import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import controller.RowGameController;
import model.RowBlockModel;
import model.RowGameModel;
import javax.swing.JButton;

import view.RowGameGUI;

/**
 * An example test class, which merely shows how to write JUnit tests.
 */
public class TestExample {

    private RowGameController game;
    private int boardSize = 3;

    // get the board so we can manually manipulate it
    JButton[][] blocks;

    @Before
    public void setUp() {

	    game = new RowGameController(boardSize);
        blocks = game.getGameView().getBlocks();
    }

    @After
    public void tearDown() {
	
        game = null;
        blocks = null;
    }

    @Test
    public void testModel() {

        // model should be automatically created with game (in setup)
        RowGameModel model = game.getGameModel();

        assertNotEquals(null, model);

        // model should have the same allocated board space as view
        assertEquals(game.getGameView().getBlocks().length, model.getBlocksData().length);

        // model should have initialized the player to the first player in the player list
        assertEquals(RowGameModel.PLAYER_STRINGS[0], model.getPlayer());

        // moves left should be init to boardsize * boardsize
        assertEquals(model.getBlocksData().length * model.getBlocksData().length, model.getMovesLeft());

        // total moves should be the same as movesLeft
        assertEquals(model.getTotalMoves(), model.getMovesLeft());

        // final result should be null at start
        assertEquals(null, model.getFinalResult());

        // make one move
        game.move(blocks[0][0]);

        // number of moves left should have gone down
        assertEquals(model.getTotalMoves() - 1, model.getMovesLeft());

        // final result should still be null
        assertEquals(null, model.getFinalResult());

        // player 2 should be the current player
        assertEquals(RowGameModel.PLAYER_STRINGS[1], model.getPlayer());

        // make another move
        game.move(blocks[1][0]);

        // number of moves left should have gone down
        assertEquals(model.getTotalMoves() - 2, model.getMovesLeft());

        // final result should still be null
        assertEquals(null, model.getFinalResult());

        // player 2 should be the current player
        assertEquals(RowGameModel.PLAYER_STRINGS[0], model.getPlayer());
    }

    @Test
    public void testView() {

        RowGameGUI gameView = game.getGameView();

        // gameView exists
        assertNotEquals(null, gameView);

        // model should have the same allocated board space as view
        assertEquals(gameView.getBlocks().length, game.getGameModel().getBlocksData().length);

        // turn text check
        assertEquals("Player 1 to play 'X'", gameView.getPlayerTurnText().getText());
    }

    @Test
    public void testController() {

        // controller should have created models and views with the same size as input
        assertEquals(boardSize, game.getGameModel().getBlocksData().length);
        assertEquals(boardSize, game.getGameView().getBlocks().length);
    }

    @Test
    public void testNewGame() {
    
        // test default parameters
        assertEquals (RowGameModel.PLAYER_STRINGS[0], game.getGameModel().getPlayer());
        assertEquals (boardSize * boardSize, game.getGameModel().getMovesLeft());
    }

    @Test
    public void testLegalMoves() {

        // player 1 plays top left
        game.move(blocks[0][0]);
        assertEquals (RowGameModel.PLAYER_IDS[0], game.getGameModel().getBlocksData()[0][0].getContents());

        // player 2 plays bottom right
        game.move(blocks[2][2]);
        assertEquals (RowGameModel.PLAYER_IDS[1], game.getGameModel().getBlocksData()[2][2].getContents());
    }

    @Test
    public void testIllegalMoves() {

        // player 1 plays top left
        game.move(blocks[0][0]);
        assertEquals (RowGameModel.PLAYER_IDS[0], game.getGameModel().getBlocksData()[0][0].getContents());

        // player 2 tries to play top left
        game.move(blocks[0][0]);

        // nothing should have changed
        assertEquals (RowGameModel.PLAYER_IDS[0], game.getGameModel().getBlocksData()[0][0].getContents());

        // should stil be player 2's turn
        // player 2 tries to play in the center
        game.move(blocks[1][1]);
        assertEquals (RowGameModel.PLAYER_IDS[1], game.getGameModel().getBlocksData()[1][1].getContents());
    }

    @Test
    public void testPlayer1Wins() {

        // a sequence of moves where x and o play across the top and middle row respectively
        game.move(blocks[0][0]);
        game.move(blocks[1][0]);
        game.move(blocks[0][1]);
        game.move(blocks[1][1]);
        game.move(blocks[0][2]);

        // the game should have ended, disabling the board
        assertEquals (blocks[0][0].isEnabled(), false);
        assertEquals (blocks[1][1].isEnabled(), false);
        assertEquals (blocks[2][2].isEnabled(), false);
        assertEquals (blocks[0][1].isEnabled(), false);
        assertEquals (blocks[2][1].isEnabled(), false);

        // test final result and final text output
        assertEquals ("Player 1 wins!", game.getGameModel().getFinalResult());
        assertEquals ("Player 1 wins!", game.getGameView().getPlayerTurnText().getText());
    }

    @Test
    public void testPlayer2Wins() {

        // a sequence of moves where o wins
        game.move(blocks[0][0]);
        game.move(blocks[1][0]);
        game.move(blocks[0][1]);
        game.move(blocks[1][1]);
        game.move(blocks[2][0]);
        game.move(blocks[1][2]);

        // the game should have ended, disabling the board
        assertEquals (blocks[0][0].isEnabled(), false);
        assertEquals (blocks[2][1].isEnabled(), false);
        assertEquals (blocks[1][2].isEnabled(), false);
        assertEquals (blocks[2][0].isEnabled(), false);
        assertEquals (blocks[2][2].isEnabled(), false);

        // test final result and final text output
        assertEquals ("Player 2 wins!", game.getGameModel().getFinalResult());
        assertEquals ("Player 2 wins!", game.getGameView().getPlayerTurnText().getText());
    }

    @Test
    public void testPlayersTie() {

        // players fill the board without winning
        game.move(blocks[0][0]);
        game.move(blocks[1][0]);
        game.move(blocks[0][1]);
        game.move(blocks[1][1]);
        game.move(blocks[1][2]);
        game.move(blocks[0][2]);
        game.move(blocks[2][0]);
        game.move(blocks[2][1]);

        // test that the game as not ended at this point
        assertEquals (blocks[2][2].isEnabled(), true);
        assertNotEquals(RowGameModel.GAME_END_NOWINNER, game.getGameView().getPlayerTurnText().getText());

        game.move(blocks[2][2]);

        // the game should have ended, disabling the board
        assertEquals (blocks[0][1].isEnabled(), false);
        assertEquals (blocks[2][2].isEnabled(), false);
        assertEquals (blocks[1][1].isEnabled(), false);
        assertEquals (blocks[2][0].isEnabled(), false);
        assertEquals (blocks[2][2].isEnabled(), false);

        // test final result and final text output
        assertEquals (RowGameModel.GAME_END_NOWINNER, game.getGameModel().getFinalResult());
        assertEquals (RowGameModel.GAME_END_NOWINNER, game.getGameView().getPlayerTurnText().getText());
    }

    @Test
    public void testReset() {

        // play some moves
        game.move(blocks[0][0]);
        game.move(blocks[1][0]);
        game.move(blocks[0][1]);
        game.move(blocks[1][1]);

        assertEquals (RowGameModel.PLAYER_IDS[0], game.getGameModel().getBlocksData()[0][0].getContents());
        assertEquals (RowGameModel.PLAYER_IDS[1], game.getGameModel().getBlocksData()[1][0].getContents());

        // reset game
        game.resetGame();

        assertEquals ("", game.getGameModel().getBlocksData()[0][0].getContents());
        assertEquals ("", game.getGameModel().getBlocksData()[1][0].getContents());
        assertEquals ("", game.getGameModel().getBlocksData()[0][1].getContents());
        assertEquals ("", game.getGameModel().getBlocksData()[1][1].getContents());

        // a sequence of moves where o wins
        game.move(blocks[0][0]);
        game.move(blocks[1][0]);
        game.move(blocks[0][1]);
        game.move(blocks[1][1]);
        game.move(blocks[2][0]);
        game.move(blocks[1][2]);

        // test final result and final text output
        assertEquals ("Player 2 wins!", game.getGameModel().getFinalResult());
        assertEquals ("Player 2 wins!", game.getGameView().getPlayerTurnText().getText());

        // reset
        game.resetGame();
        assertEquals ("", game.getGameModel().getBlocksData()[0][0].getContents());
        assertEquals ("", game.getGameModel().getBlocksData()[0][1].getContents());
        assertEquals ("", game.getGameModel().getBlocksData()[0][2].getContents());
        assertEquals ("", game.getGameModel().getBlocksData()[1][0].getContents());
        assertEquals ("", game.getGameModel().getBlocksData()[1][1].getContents());
        assertEquals ("", game.getGameModel().getBlocksData()[1][2].getContents());
        assertEquals ("", game.getGameModel().getBlocksData()[2][1].getContents());
        assertEquals ("", game.getGameModel().getBlocksData()[2][2].getContents());

        assertEquals (null, game.getGameModel().getFinalResult());
        assertEquals ("Player 1 to play X", game.getGameView().getPlayerTurnText().getText());

    }


    @Test(expected = IllegalArgumentException.class)
    public void testNewBlockViolatesPrecondition() {
	
        RowBlockModel block = new RowBlockModel(null);
    }
}
