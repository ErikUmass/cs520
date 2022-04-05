package controller;

import javax.swing.JButton;

import model.RowBlockModel;
import model.RowGameModel;
import view.RowGameGUI;

public class RowGameController {

    private RowGameModel gameModel;

    private RowGameGUI gameView;
	public RowGameGUI getGameView() { return this.gameView; }

    /**
     * Creates a new game initializing the GUI.
     */
    public RowGameController() {
	
		gameModel = new RowGameModel();

		gameView = new RowGameGUI(this);

        for(int row = 0; row<3; row++) {
            for(int column = 0; column<3 ;column++) {
	        
				gameModel.getBlocksData()[row][column].setContents("");
				gameModel.getBlocksData()[row][column].setIsLegalMove(true);
				gameView.updateBlock(gameModel,row,column);
            }
        }
    }

    /**
     * Moves the current player into the given block.
     *
     * @param block The block to be moved to by the current player
     */
    public void move(JButton block) {

		// decrement moves left
		gameModel.setMovesLeft(gameModel.getMovesLeft() - 1);

		// get current player
		String curPlayer = gameModel.getPlayer();

		// get next player
		String nextPlayer = curPlayer.equals(RowGameModel.PLAYER_STRINGS[0]) ? RowGameModel.PLAYER_STRINGS[1] : RowGameModel.PLAYER_STRINGS[0];

		// set player's ID for win condition checking
		String curPlayerID = RowGameModel.PLAYER_IDS[Integer.parseInt(curPlayer) - 1];

		// set next player's ID for status message
		String nextPlayerID = RowGameModel.PLAYER_IDS[Integer.parseInt(nextPlayer) - 1];
		
		// set status message telling next player it is their move
		gameView.getPlayerTurnText().setText("'" + nextPlayerID + "' : Player " + nextPlayer);
	
		// update model
		int blockPos = gameView.getBlockPosition(block);
		int x = blockPos / 10;
		int y = blockPos % 10;
		gameModel.setBlock(x, y, curPlayerID);

		// update view
		gameView.updateBlock(gameModel, x, y);

		// player to next player
		gameModel.setPlayer(nextPlayer);

		// check for win conditions
		if ( gameModel.getMovesLeft() < 7 && ( 	horizontalWinFound(gameModel, curPlayerID) || 
												verticalWinFound(gameModel, curPlayerID) || 
												inclineDiagWinFound(gameModel, curPlayerID) || 
												declineDiagWinFound(gameModel, curPlayerID) ) ) {

			// cur player won, set end game stuff
			gameModel.setFinalResult("Player " + curPlayer + " wins!");
			endGame();
		} else if (gameModel.getMovesLeft() == 0) {

			// if there are no moves left and there was not a win, declare tie
			gameModel.setFinalResult(RowGameModel.GAME_END_NOWINNER);
		}

		if (gameModel.getFinalResult() != null) {

			gameView.getPlayerTurnText().setText(gameModel.getFinalResult());
		}
	}

	// private helper methods for win condition checking
	private boolean horizontalWinFound(RowGameModel gameModel, String curPlayerID) {

		// store data so we don't have to keep retrieving it
		RowBlockModel[][] blocksData = gameModel.getBlocksData();

		// scan each row for a win
		for (int i = 0; i < blocksData.length; ++i) {

			int numInARow = 0;
			for (int j = 0; j < blocksData[0].length; ++j) {

				if (!blocksData[i][j].getContents().equals(curPlayerID)) {

					// we can skip this row once we identify no win con
					// reset counter
					numInARow = 0;
					break;
				}

				// found matching playerID, increment
				numInARow++;
			}

			// when we break from inner loop, check if we found the win condition
			if (numInARow == blocksData[0].length) {

				return true;
			}

			// otherwise we just keep going
		}

		return false;
	}

	private boolean verticalWinFound(RowGameModel gameModel, String curPlayerID) {

		// store data so we don't have to keep retrieving it
		RowBlockModel[][] blocksData = gameModel.getBlocksData();

		// scan each column for a win
		for (int i = 0; i < blocksData[0].length; ++i) {

			int numInACol = 0;
			for (int j = 0; j < blocksData.length; ++j) {

				if (!blocksData[j][i].getContents().equals(curPlayerID)) {

					// we can skip this row once we identify no win con
					// reset counter
					numInACol = 0;
					break;
				}

				// found matching playerID, increment
				numInACol++;
			}

			// when we break from inner loop, check if we found the win condition
			if (numInACol == blocksData.length) {

				return true;
			}

			// otherwise we just keep going
		}

		return false;
	}

	private boolean inclineDiagWinFound(RowGameModel gameModel, String curPlayerID) {

		// store data so we don't have to keep retrieving it
		RowBlockModel[][] blocksData = gameModel.getBlocksData();

		// scan diagonally upwards for a win
		int row = blocksData.length - 1;
		int col = 0;
		
		while (row >= 0) {

			if (!blocksData[row][col].getContents().equals(curPlayerID)) {

				// if any of the IDs on the diag are invalid, return false
				return false;
			}

			// move position
			row--;
			col++;
		}

		// all values on the diag are valid, return true
		return true;
	}

	private boolean declineDiagWinFound(RowGameModel gameModel, String curPlayerID) {

		// store data so we don't have to keep retrieving it
		RowBlockModel[][] blocksData = gameModel.getBlocksData();

		// scan diagonally downwards for a win
		int row = 0;
		int col = 0;

		while (row < blocksData[0].length) {

			if (!blocksData[row][col].getContents().equals(curPlayerID)) {

				// if any of the IDs on the diag are invalid, return false
				return false;
			}

			// move position
			row++;
			col++;
		}

		// all values on the diag are valid, return true
		return true;
	}


    /**
     * Ends the game disallowing further player turns.
     */
    public void endGame() {

		for(int row = 0;row<3;row++) {
	    	for(int column = 0;column<3;column++) {
		
				gameView.getBlocks()[row][column].setEnabled(false);
	    	}
		}
    }

    /**
     * Resets the game to be able to start playing again.
     */
    public void resetGame() {

        for(int row = 0;row<3;row++) {
            for(int column = 0;column<3;column++) {
        
				gameModel.getBlocksData()[row][column].reset();
				gameModel.getBlocksData()[row][column].setIsLegalMove(true);
				gameView.updateBlock(gameModel,row,column);
            }
        }
        
		gameModel.setPlayer("1");
        gameModel.setMovesLeft(9);
		gameModel.setFinalResult(null);
        gameView.getPlayerTurnText().setText("Player 1 to play 'X'");
    }
}
