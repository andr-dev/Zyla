package com.zyla.gui;

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;

public class TextAreaOutputStream extends OutputStream {
	private JTextArea console;
	
	public TextAreaOutputStream(JTextArea textArea) {
		console = textArea;
	}
	
	public void write( int b ) throws IOException {
		console.append(String.valueOf((char) b));
	}
}