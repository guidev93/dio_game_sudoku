package edu.sudokuDio;

import java.awt.Dimension;

import edu.sudokuDio.graphicalInterface.frameDesign;

public class Main {

	public static void main(String[] args) {

		var diomension = new Dimension(600, 600);

		frameDesign Mainframe = new frameDesign(diomension);

		Mainframe.revalidate();
		Mainframe.repaint();

	}

}
