package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import controller.RowGameController;
import model.RowGameModel;

public class RowGameGUI {

    private JFrame gui = new JFrame("Tic Tac Toe");
    public JFrame getGUI() { return this.gui; }

    // value from original code is not used?
    //private RowGameModel gameModel = new RowGameModel();

    private JButton[][] blockButtons = new JButton[3][3];
    public JButton[][] getBlocks() { return this.blockButtons; }
    public int getBlockPosition(JButton block) {

        // iterate through all blocks in blockButtons to determine which block this is
        for(int i = 0; i < blockButtons.length; ++i) {
            for(int j = 0; j < blockButtons[0].length; ++j) {
            
                if (blockButtons[i][j] == block) {

                    // return encoded integer ij
                    return (i * 10) + j;
                }
            }
        }

        // block not found
        return -1;
    }

    private JButton resetButton = new JButton("Reset");

    private JTextArea playerTurnText = new JTextArea();
    public JTextArea getPlayerTurnText() { return this.playerTurnText; }

    /**
     * Creates a new game initializing the GUI.
     */
    public RowGameGUI(RowGameController controller) {

        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setSize(new Dimension(500, 350));
        gui.setResizable(true);

        JPanel gamePanel = new JPanel(new FlowLayout());
        JPanel game = new JPanel(new GridLayout(3,3));
        gamePanel.add(game, BorderLayout.CENTER);

        JPanel options = new JPanel(new FlowLayout());
        options.add(resetButton);
        JPanel messages = new JPanel(new FlowLayout());
        messages.setBackground(Color.white);

        gui.add(gamePanel, BorderLayout.NORTH);
        gui.add(options, BorderLayout.CENTER);
        gui.add(messages, BorderLayout.SOUTH);

        messages.add(playerTurnText);
        playerTurnText.setText("Player 1 to play 'X'");

        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.resetGame();
            }
        });

        // Initialize a JButton for each cell of the 3x3 game board.
        for(int row = 0; row<3; row++) {
            for(int column = 0; column<3 ;column++) {
                blockButtons[row][column] = new JButton();
                blockButtons[row][column].setPreferredSize(new Dimension(75,75));
                game.add(blockButtons[row][column]);
                blockButtons[row][column].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
			            
                        controller.move((JButton)e.getSource());
                    }
                });
            }
        }
    }

    /**
     * Updates the block at the given row and column 
     * after one of the player's moves.
     *
     * @param gameModel The RowGameModel containing the block
     * @param row The row that contains the block
     * @param column The column that contains the block
     */
    public void updateBlock(RowGameModel gameModel, int row, int column) {
	    
        blockButtons[row][column].setText(gameModel.getBlocksData()[row][column].getContents());
	    blockButtons[row][column].setEnabled(gameModel.getBlocksData()[row][column].getIsLegalMove());
    }
}
