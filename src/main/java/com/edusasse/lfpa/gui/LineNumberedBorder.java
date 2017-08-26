package com.edusasse.lfpa.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JTextArea;
import javax.swing.border.AbstractBorder;

public class LineNumberedBorder extends AbstractBorder {
	// Numeros desenhados a Esquerda
	public static int LEFT_SIDE = -2;
	// Numeros desenhados a Direita
	public static int RIGHT_SIDE = -1;
	// Alinhamento dos Numeros a Direita
	public static int LEFT_JUSTIFY = 1;
	// Alinhamento dos Numeros a Direita
	public static int RIGHT_JUSTIFY = 0;

	// Opcores correntes
	private int lineNumberJustification = RIGHT_JUSTIFY;
	private int location = LEFT_SIDE;

	// Construtor
	public LineNumberedBorder(int location, int justify) {
		this.setLocation(location);
		this.setLineNumberJustification(justify);
	}

	@Override
	//** Define o espaco das bordas */
	public Insets getBorderInsets(Component c) {
		return getBorderInsets(c, new Insets(0, 0, 0, 0));
	}

	@Override
	/** Adiciona espaco apos a impressao do Numero da linha */
	public Insets getBorderInsets(Component c, Insets insets) {
		// if c is not a JTextArea...nothing is done...
		if (c instanceof JTextArea) {
			int width = lineNumberWidth((JTextArea) c);
			if (location == LEFT_SIDE) {
				insets.left = width * 2;
			} else {
				insets.right = width * 2;
			}
		}
		return insets;
	}
 
	//** Tipo de alinhamento do texto */
	public void setLineNumberJustification(int justify) {
		if (justify == RIGHT_JUSTIFY || justify == LEFT_JUSTIFY) {
			this.lineNumberJustification = justify;
		}
	}
	
	//** Localizacao da barra de numeros */
	public void setLocation(int loc) {
		if (loc == RIGHT_SIDE || loc == LEFT_SIDE) {
			this.location = loc;
		}
	}

	// ** Tamanho do maior numero */
	private int lineNumberWidth(JTextArea textArea) {
		int lineCount = Math.max(textArea.getRows(), textArea.getLineCount() + 1);
		return textArea.getFontMetrics(textArea.getFont()).stringWidth(lineCount + " ");
	}

	@Override
	//** Faz o desenho da borda com os numeros */
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Rectangle clip = g.getClipBounds();
		FontMetrics fm = g.getFontMetrics();
		int fontHeight = fm.getHeight();

		// Comeca do topo da pagina
		// y é o comeco da fonte base
		int ybaseline = y + fm.getAscent();

		// determina se é o topo
		int startingLineNumber = (clip.y / fontHeight) + 1;
		if (ybaseline < clip.y) {
			ybaseline = y + startingLineNumber * fontHeight
					- (fontHeight - fm.getAscent());
		}
		int yend = ybaseline + height;
		if (yend > (y + height)) {
			yend = y + height;
		}

		JTextArea jta = (JTextArea) c;
		int lineWidth = lineNumberWidth(jta);

		// posicao x da linha
		int lnxstart = x;
		if (location == LEFT_SIDE) {
			if (lineNumberJustification == LEFT_JUSTIFY) {
				lnxstart = x;
			} else {
				lnxstart = x + lineWidth;
			}
		} else {
			if (lineNumberJustification == LEFT_JUSTIFY) {
				lnxstart = (y + width) - lineWidth;
			} else {
				lnxstart = (y + width);
			}
		}

		g.setColor(c.getForeground());
		int length = ("" + Math.max(jta.getRows(), jta.getLineCount() + 1)).length();
		g.setFont(new Font("Courier New",Font.BOLD,11));
		g.setColor(Color.lightGray);
		while (ybaseline < yend) {
			if (lineNumberJustification == LEFT_JUSTIFY) {
				g.drawString(startingLineNumber + " ", lnxstart, ybaseline);
			} else {
				String label = padLabel(startingLineNumber, length, true);
				g.drawString(label.trim()+".", lnxstart - fm.stringWidth(label), ybaseline);
			}

			ybaseline += fontHeight;
			startingLineNumber++;
		}
	}
	
	//** Adiciona o numero da linha */
	private static String padLabel(int lineNumber, int length, boolean addSpace) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(lineNumber);
		for (int count = (length - buffer.length()); count > 0; count--) {
			buffer.insert(0, ' ');
		}
		if (addSpace) {
			buffer.append(' ');
		}
		return buffer.toString();
	}
}