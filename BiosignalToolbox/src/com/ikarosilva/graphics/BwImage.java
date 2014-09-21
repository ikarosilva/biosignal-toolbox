package com.ikarosilva.graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;

public class BwImage extends Component {
	
	int recX=190, rectY=190;
	int rectWidth=5, rectHeight=5;
	
	@Override 
	public void paint(Graphics g){
		g.setColor(Color.BLACK);
		g.fillOval(recX, rectY, rectWidth, rectHeight);
		//g.fillRect(recX, rectY, rectWidth, rectHeight);
	}
	
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(100,100);
	}
	
	public static void main(final String[] args){
		final JFrame jf=new JFrame();
		jf.add(new BwImage());
		jf.setBounds(200,200,200,200);
		jf.setVisible(true);
	}

}
