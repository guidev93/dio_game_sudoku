package edu.sudokuDio.graphicalInterface;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

public class frameDesign extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final int GRID_SIZE = 9; // Grid size for Sudoku
	private static final int SUBGRID_SIZE = 3; // Subgrid size
	buttonPainel bp;

	public frameDesign(Dimension dimencion) {

		super("Sudoku Game");

		gridPainel mainGrid = new gridPainel(GRID_SIZE, SUBGRID_SIZE, dimencion);
		bp = new buttonPainel(GRID_SIZE, mainGrid.getCells(), this);

		this.add(bp, BorderLayout.NORTH);
		this.add(mainGrid, BorderLayout.CENTER);

		this.setSize(dimencion);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setLayout(new BorderLayout());

	}

}
