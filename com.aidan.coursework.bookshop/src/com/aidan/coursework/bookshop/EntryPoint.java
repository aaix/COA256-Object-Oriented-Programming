package com.aidan.coursework.bookshop;

import com.aidan.coursework.bookshop.gui.GUI;

import java.awt.EventQueue;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JOptionPane;


public class EntryPoint {

	public static void main(String[] args) throws Exception {
		
	
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				GUI gui;
				try {
					gui = new GUI();
				} catch (Exception e) {
					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					e.printStackTrace(pw);
					JOptionPane.showMessageDialog(null, "A CRITICAL ERROR HAS OCCURED :" + sw.toString());
					System.exit(1);

				}
			}
		});
		
	}

}
