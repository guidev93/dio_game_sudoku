package edu.sudokuDio.graphicalInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class gridPainel extends JPanel {

	private static final long serialVersionUID = 1L;
	JTextField[][] cells;

	public gridPainel(int gridSize, int subGridSize, Dimension dimencion) {

		JPanel gridPanel = new JPanel(new GridLayout(gridSize, gridSize));

		gridPanel.setSize(new Dimension(500, 500));
		gridPanel.setPreferredSize(new Dimension(510, 510));
		cells = new JTextField[gridSize][gridSize];

		for (int row = 0; row < gridSize; row++) {
			for (int col = 0; col < gridSize; col++) {
				JTextField cell = new JTextField();
				cell.setHorizontalAlignment(JTextField.CENTER);

				((AbstractDocument) cell.getDocument()).setDocumentFilter(new NumberFilter());

				int top = (row % subGridSize == 0) ? 3 : 1;
				int left = (col % subGridSize == 0) ? 3 : 1;
				int bottom = 1;
				int right = 1;

				cell.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));

				cells[row][col] = cell;
				gridPanel.add(cell);
			}
		}

		fillSolvedSudoku(); // Preenche o Sudoku resolvido
		removeValuesFromSubGrids(4); // Remove 5 valores para criar o desafio
		this.add(gridPanel, BorderLayout.CENTER);
	}

	private static class NumberFilter extends DocumentFilter {

		@Override
		public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
				throws BadLocationException {

			int currentLength = fb.getDocument().getLength();
			int newLength = currentLength - length + text.length();

			if (newLength <= 1) {
				if (isValidInput(text)) {
					super.replace(fb, offset, length, text, attrs);
				}
			}

		}

		@Override
		public void insertString(FilterBypass fb, int offset, String text, AttributeSet attrs)
				throws BadLocationException {
			if (isValidInput(text)) {
				super.insertString(fb, offset, text, attrs);
			}
		}

		// Verifica se o texto é um número válido de 1 a 9
		private boolean isValidInput(String text) {
			return text.isEmpty() || text.matches("[1-9]");
		}
	}

	// Preenche o Sudoku com uma solução válida
	private void fillSolvedSudoku() {
		int[][] board = generateSolvedSudoku();
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				cells[row][col].setText(String.valueOf(board[row][col]));
				cells[row][col].setEditable(false); // Impede que o valor seja editado
				cells[row][col].setBackground(new Color(230, 230, 250)); // Muda a cor para diferenciar
			}
		}
	}

	// Gera um Sudoku resolvido
	private int[][] generateSolvedSudoku() {
		int[][] board = new int[9][9];
		solveSudoku(board); // Preenche com uma solução válida
		return board;
	}

	// Resolve o Sudoku, preenchendo o tabuleiro
	private boolean solveSudoku(int[][] board) {
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				if (board[row][col] == 0) { // Encontra uma célula vazia
					List<Integer> numbers = getShuffledNumbers(); // Embaralha os números de 1 a 9
					for (int num : numbers) {
						if (isValid(board, row, col, num)) {
							board[row][col] = num;
							if (solveSudoku(board)) {
								return true; // Continua com a solução recursivamente
							}
							board[row][col] = 0; // Desfaz se não for possível
						}
					}
					return false; // Se não for possível colocar nenhum número, retorna falso
				}
			}
		}
		return true; // Quando o tabuleiro estiver totalmente preenchido
	}

	// Gera uma lista com números de 1 a 9 embaralhados
	private List<Integer> getShuffledNumbers() {
		List<Integer> numbers = new ArrayList<>();
		for (int i = 1; i <= 9; i++) {
			numbers.add(i);
		}
		Collections.shuffle(numbers);
		return numbers;
	}

	// Verifica se o número pode ser colocado na posição (linha, coluna)
	private boolean isValid(int[][] board, int row, int col, int num) {
		// Verifica a linha
		for (int c = 0; c < 9; c++) {
			if (board[row][c] == num) {
				return false;
			}
		}

		// Verifica a coluna
		for (int r = 0; r < 9; r++) {
			if (board[r][col] == num) {
				return false;
			}
		}

		// Verifica a subgrade 3x3
		int startRow = row - row % 3;
		int startCol = col - col % 3;
		for (int r = startRow; r < startRow + 3; r++) {
			for (int c = startCol; c < startCol + 3; c++) {
				if (board[r][c] == num) {
					return false;
				}
			}
		}

		return true;
	}

	private void removeValuesFromSubGrids(int numToRemove) {
	    int removedCount = 0;
	    for (int subgridRow = 0; subgridRow < 3; subgridRow++) {
	        for (int subgridCol = 0; subgridCol < 3; subgridCol++) {
	            List<int[]> cellsInSubgrid = getCellsInSubgrid(subgridRow, subgridCol);
	            Collections.shuffle(cellsInSubgrid); // Embaralha as células dentro da subgrid
	            int count = 0;
	            // Remove até 5 células de cada subgrid
	            for (int[] position : cellsInSubgrid) {
	                if (count < numToRemove) {
	                    int row = position[0];
	                    int col = position[1];
	                    JTextField cell = cells[row][col];
	                    if (!cell.getText().equals("")) { // Verifica se já não foi apagado
	                    	cell.setText(""); // Apaga o valor
	                        cell.setBackground(Color.WHITE); // Altera o fundo para branco
	                        cell.setEditable(true); // Habilita a célula para edição
	                        count++;
	                    }
	                }
	            }
	            removedCount += count;
	        }
	    }
	    // Garantir que o painel seja redesenhado
	    SwingUtilities.invokeLater(() -> {
	        this.revalidate(); // Revalida o painel para garantir que as mudanças sejam aplicadas
	        this.repaint(); // Força o repintar do painel
	    });
	}



	// Obtém todas as células de uma subgrid
	private List<int[]> getCellsInSubgrid(int subgridRow, int subgridCol) {
		List<int[]> positions = new ArrayList<>();
		int startRow = subgridRow * 3;
		int startCol = subgridCol * 3;

		for (int row = startRow; row < startRow + 3; row++) {
			for (int col = startCol; col < startCol + 3; col++) {
				positions.add(new int[] { row, col });
			}
		}
		return positions;
	}

	public JTextField[][] getCells() {
		return cells;
	}

}
