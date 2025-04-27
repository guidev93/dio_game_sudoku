package edu.sudokuDio.graphicalInterface;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class buttonPainel extends JPanel {

	private static final long serialVersionUID = 1L;
	int GRID_SIZE;
	JButton checkButton;
	JButton resetButton;
	JFrame mainFrame;

	public buttonPainel(int GRID_SIZE,JTextField[][] cells,JFrame mainFrame) {

		this.GRID_SIZE = GRID_SIZE;
		this.checkButton = new JButton("Validar solução");
		this.resetButton = new JButton("Reset");

		this.add(checkButton);
		this.add(resetButton);
		
		
		this.resetButton.addActionListener(e -> resetGrid(cells,mainFrame,resetButton));
		this.checkButton.addActionListener(e -> isValidSudoku(cells));
		this.checkButton.addActionListener(e -> {
            if (isValidSudoku(cells)) {
                JOptionPane.showMessageDialog(mainFrame, "O Sudoku é válido!");
            } else {
                JOptionPane.showMessageDialog(mainFrame, "O Sudoku não é válido.");
            }
        });

	}

	JButton getResetButton() {
		return this.resetButton;
	}
	
	private static void resetGrid(JTextField[][] cells,JFrame mainFrame,JButton ResetButton ) {
		
		mainFrame.dispose();
		frameDesign newFrame = new frameDesign(new Dimension(600, 600));
		newFrame.setVisible(true);
	}
	
    public static boolean isValidSudoku(JTextField[][] board) {
        // Verificar as linhas
        for (int row = 0; row < 9; row++) {
            if (!isValidRow(board, row)) {
                return false;
            }
        }

        // Verificar as colunas
        for (int col = 0; col < 9; col++) {
            if (!isValidCol(board, col)) {
                return false;
            }
        }

        // Verificar as subgrids (3x3)
        for (int row = 0; row < 9; row += 3) {
            for (int col = 0; col < 9; col += 3) {
                if (!isValidSubgrid(board, row, col)) {
                    return false;
                }
            }
        }

        // Se todas as verificações passarem
        return true;
    }

    // Verifica se a linha contém números válidos (sem repetição)
    private static boolean isValidRow(JTextField[][] board, int row) {
        boolean[] seen = new boolean[9];  // Para marcar números de 1 a 9
        for (int col = 0; col < 9; col++) {
            int num = getValue(board[row][col]);
            if (num != 0) {  // Ignora células vazias (0)
                if (seen[num - 1]) {
                    return false;  // Número já visto, linha inválida
                }
                seen[num - 1] = true;
            }
        }
        return true;
    }

    // Verifica se a coluna contém números válidos (sem repetição)
    private static boolean isValidCol(JTextField[][] board, int col) {
        boolean[] seen = new boolean[9];  // Para marcar números de 1 a 9
        for (int row = 0; row < 9; row++) {
            int num = getValue(board[row][col]);
            if (num != 0) {  // Ignora células vazias (0)
                if (seen[num - 1]) {
                    return false;  // Número já visto, coluna inválida
                }
                seen[num - 1] = true;
            }
        }
        return true;
    }

    // Verifica se a subgrid 3x3 contém números válidos (sem repetição)
    private static boolean isValidSubgrid(JTextField[][] board, int startRow, int startCol) {
        boolean[] seen = new boolean[9];  // Para marcar números de 1 a 9
        for (int row = startRow; row < startRow + 3; row++) {
            for (int col = startCol; col < startCol + 3; col++) {
                int num = getValue(board[row][col]);
                if (num != 0) {  // Ignora células vazias (0)
                    if (seen[num - 1]) {
                        return false;  // Número já visto, subgrid inválido
                    }
                    seen[num - 1] = true;
                }
            }
        }
        return true;
    }

    // Função auxiliar para obter o valor numérico do JTextField
    private static int getValue(JTextField textField) {
        try {
            String text = textField.getText().trim();
            if (!text.isEmpty()) {
                return Integer.parseInt(text);
            }
        } catch (NumberFormatException e) {
            // Se não for um número válido, retorna 0 (representando uma célula vazia)
        }
        return 0;  // Se estiver vazio ou não for um número válido, considera como célula vazia
    }

}
