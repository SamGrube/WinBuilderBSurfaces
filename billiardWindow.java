package billiardWindow;
import javax.swing.*;
import java.awt.*;
import java.awt.EventQueue;

import java.lang.Math;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JSlider;
import javax.swing.JPanel;
import java.awt.Frame;
import java.awt.Window.Type;
import java.awt.Color;
import javax.swing.JEditorPane;
import java.awt.Canvas;
import java.awt.Font;
import javax.swing.text.Element;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.border.BevelBorder;
import javax.swing.border.MatteBorder;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;

@SuppressWarnings("serial")
public class billiardWindow extends JFrame{

	private JFrame mainWindow;
	private JPanel canvas;
	private JTextField positionTextField;
	private JTextField angle2TextField;
	private JTextField angle1TextField;
 	private JTextField trajectoryTextField;
 	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
					billiardWindow window = new billiardWindow();
					window.mainWindow.setVisible(true);
			}
		});
	}
	
	public billiardWindow() {
		initialize();
	}
	
	public void initialize() {
		mainWindow = new JFrame();
		mainWindow.getContentPane().setForeground(new Color(0, 0, 0));
		mainWindow.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
		mainWindow.setBackground(new Color(240, 240, 240));
		mainWindow.setType(Type.POPUP);
		mainWindow.setUndecorated(false);
		mainWindow.setVisible(true);
		mainWindow.setBounds(50, 50, 800, 800);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setTitle("Triangle Surface Simulator");
		mainWindow.getContentPane().setLayout(new BorderLayout(0, 0));
			
		canvas = new JPanel() {
			
			public void paint(Graphics g) {									
				int XleftVertex = 100,XrightVertex = 700;			
				int L = XrightVertex - XleftVertex;										// Base of the triangle
				int YBase = 700;														// Where the base of the triangle lies
				double phi1 = radian(60.0);
				double phi2 = radian(60.0);
				//double phi1 = radian(Double.valueOf(angle1TextField.getText()));		// Left Angle of triangle
				//double phi2 = radian(Double.valueOf(angle2TextField.getText()));		// Right Angle of Triangle
				int peakX = XleftVertex + (int)Math.round(L*Math.sin(phi2)*Math.cos(phi1) / Math.sin(phi1 + phi2));
				int peakY = YBase - (int)Math.round(Math.tan(phi1)*(peakX-XleftVertex));// (X,Y) coordinates of the triangle's peak
				
				int position = 400;// position of initial shot, halfway between edges
				double trajectory = radian(Double.valueOf(trajectoryTextField.getText()));
				
				super.paint(g);
				// Triangle Surface
				g.drawLine(XleftVertex,YBase,XrightVertex,YBase);  		// Base of triangle
				g.drawLine(XleftVertex,YBase,peakX,peakY); 				// left triangle leg
				g.drawLine(XrightVertex,YBase,peakX,peakY);				// right triangle leg
				
				// Billiard Lines
				
				// for striking edge 1 ALMOST WORKING PROBABLY A ROUNDING ERROR
				if (0 <= Double.valueOf(trajectoryTextField.getText()) && Double.valueOf(trajectoryTextField.getText()) <= 90)
				{
					double x = (L*Math.tan(phi2) + position * Math.tan(trajectory)) / (Math.tan(trajectory) + Math.tan(phi2));
					double y = Math.tan(trajectory) * (x-position);
					g.drawLine(position,YBase,(int)x+XleftVertex,YBase-(int)y);				
				}
				// for striking edge 2 NOT WORKING AT ALL
				else if (90 < Double.valueOf(trajectoryTextField.getText()) && Double.valueOf(trajectoryTextField.getText()) < 180)
				{
					double x1 = position * Math.sin(trajectory) * Math.cos(phi1)*(1.0/Math.sin(trajectory-phi1));
					double y1 = Math.tan(trajectory)*(x1-position);
					g.drawLine(position,YBase,(int)Math.round(x1) + XleftVertex,YBase - (int)Math.round(y1));
				}
				
				// for striking edge 0
				
			}
		};
		canvas.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		canvas.setBackground(Color.WHITE);
		mainWindow.getContentPane().add(canvas, BorderLayout.CENTER);
		
		// control panel that holds input text fields and button
		JPanel controlPanel = new JPanel();
		mainWindow.getContentPane().add(controlPanel, BorderLayout.SOUTH);
		
		// Text Field for input of the triangle's leftmost angle
		/*
		angle1TextField = new JTextField();
		angle1TextField.setText("60");
		angle1TextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String phi1 = angle1TextField.getText();
				angle1TextField.setText(phi1);
			}
		});
		angle1TextField.setToolTipText("Left Angle (0<x<90)");
		controlPanel.add(angle1TextField);
		angle1TextField.setColumns(10);
		*/
		/*
		// Text Field for input of the triangle's rightmost angle
		angle2TextField = new JTextField();
		angle2TextField.setText("60");
		angle2TextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String phi2 = angle2TextField.getText();
				angle2TextField.setText(phi2);
			}
		});
		angle2TextField.setToolTipText("Right Angle 0<x<90");
		controlPanel.add(angle2TextField);
		angle2TextField.setColumns(10);
		*/
		// Text Field for input of trajectory angle
		trajectoryTextField = new JTextField();
		trajectoryTextField.setText("60");
		trajectoryTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String trajectory = trajectoryTextField.getText();
				trajectoryTextField.setText(trajectory);
			}
		});
		trajectoryTextField.setToolTipText("Trajectory Angle (0<x<180)");
		controlPanel.add(trajectoryTextField);
		trajectoryTextField.setColumns(10);
		
		// Begin Simulation button that repaints based off of text field inputs
		JButton btnBegin = new JButton("Begin Simulation");
		btnBegin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canvas.repaint();
			}
		});
		controlPanel.add(btnBegin);
		
		// TextField for inputing the initial position of the shot
				// Due to time constraints this will have to be postponed
				// (i.e.) Can only shoot from middle
				/*
				positionTextField = new JTextField();
				positionTextField.setText("300");
				positionTextField.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String position = positionTextField.getText();
						positionTextField.setText(position);
					}
				});
				positionTextField.setToolTipText("Position 0 < x < 720");
				controlPanel.add(positionTextField);
				positionTextField.setColumns(10);
				*/
		}
	
	// Convert degrees to radians because sin and cos take Rad arguments
	public double radian(double degree) {
		return ((degree * Math.PI) / 180.0);
	}
	
}