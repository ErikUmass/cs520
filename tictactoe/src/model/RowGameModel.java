package model;

public class RowGameModel {

    public static final String GAME_END_NOWINNER = "Game ends in a draw";

    public static final String[] PLAYER_IDS = {"X", "O"};
    public static final String[] PLAYER_STRINGS = {"1", "2"};

    private RowBlockModel[][] blocksData = new RowBlockModel[3][3];
    public RowBlockModel[][] getBlocksData() { return this.blocksData; }

    /**
     * The current player taking their turn
     */
    private String player = "1";
    public String getPlayer() { return this.player; }
    public void setPlayer(String player) { this.player = player; }

    private int movesLeft = 9;
    public int getMovesLeft() { return this.movesLeft; }
    public void setMovesLeft(int movesLeft) { this.movesLeft = movesLeft; }

    private String finalResult = null;

    public RowGameModel() {
	
        super();
	
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
		
                blocksData[row][col] = new RowBlockModel(this);	    
            } // end for col
	    } // end for row
    }

    public String getFinalResult() {
	
            return this.finalResult;
    }

    public void setFinalResult(String finalResult) {
	
        this.finalResult = finalResult;
    }

    public void setBlock(int xBlockPos, int yBlockPos, String playerID) {
    
        blocksData[xBlockPos][yBlockPos].setContents(playerID);
    }
}
