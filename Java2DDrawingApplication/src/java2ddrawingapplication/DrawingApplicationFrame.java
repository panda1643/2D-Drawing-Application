/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java2ddrawingapplication;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author acv
 */
public class DrawingApplicationFrame extends JFrame
{

    // Create the panels for the top of the application. One panel for each
    JPanel line1 = new JPanel();
    JPanel line2 = new JPanel();
    
    // line and one to contain both of those panels.
    JPanel topPanel = new JPanel();

    // create the widgets for the firstLine Panel.
    String[] shapeList = {"Line", "Rectangle", "Oval"}; 
    JComboBox shapeopt = new JComboBox(shapeList);
    JButton color1 = new JButton("1st Color");
    JButton color2 = new JButton("2nd Color");
    JButton undo = new JButton("Undo");
    JButton clear = new JButton("Clear");

    //create the widgets for the secondLine Panel.
    JPanel fill = new JPanel();
    JCheckBox isFilled = new JCheckBox();
    JPanel grad = new JPanel();
    JCheckBox isGradient = new JCheckBox();
    JPanel dash = new JPanel();
    JCheckBox isDashed = new JCheckBox();
    
    //Spinners
    SpinnerModel model1 = new SpinnerNumberModel(1,0,20,1);
    SpinnerModel model2 = new SpinnerNumberModel(1,0,20,1);
    JSpinner width = new JSpinner(model1);
    JSpinner dashlen = new JSpinner(model2);
    
    // Variables for drawPanel.
    DrawPanel drawPanel = new DrawPanel();
    boolean drag = false;
    ArrayList<MyShapes> shapes = new ArrayList();
    int x1 = 0;
    int x2 = 0;
    int y1 = 20;
    int y2 = 20;

    // add status label
    JLabel status = new JLabel("");
  
    // Constructor for DrawingApplicationFrame
    public DrawingApplicationFrame()
    {
        // add widgets to panels
        
        // firstLine widgets
        line1.setLayout(new FlowLayout());
        line1.add(new JLabel("Shape:"));
        line1.add(shapeopt);
        line1.add(color1);
        line1.add(color2);
        line1.add(undo);
        line1.add(clear);
        line1.setBackground(Color.CYAN);

        // secondLine widgets
        line2.setLayout(new FlowLayout());
        line2.add(new JLabel("Options:"));
        
        fill.add(isFilled);
        fill.add(new JLabel("Filled"));
        fill.setBackground(Color.WHITE);
        
        grad.add(isGradient);
        grad.add(new JLabel("Use Gradient"));
        grad.setBackground(Color.WHITE);
        
        dash.add(isDashed);
        dash.add(new JLabel("Dashed"));
        dash.setBackground(Color.WHITE);
        
        line2.add(fill);
        line2.add(grad);
        line2.add(dash);
        
        line2.add(new JLabel("Line Width:"));
        line2.add(width);
        line2.add(new JLabel("Dash Length:"));
        line2.add(dashlen);
        line2.setBackground(Color.CYAN);
        
        // add top panel of two panels
        topPanel.setLayout(new GridLayout(2, 1));
        topPanel.add(line1);
        topPanel.add(line2);

        // add topPanel to North, drawPanel to Center, and statusLabel to South
        drawPanel.setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(drawPanel, BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);
                     
        //add listeners and event handlers
        color1.addActionListener(new ActionListener()
        {
        @Override
            public void actionPerformed(ActionEvent event)
            {
                color1.setBackground(JColorChooser.showDialog(null, "Pick Color 1", Color.BLACK));
            }
        }
        );
        
        color2.addActionListener(new ActionListener()
        {
         @Override
            public void actionPerformed(ActionEvent event)
            {
                color2.setBackground(JColorChooser.showDialog(null, "Pick Color 2", Color.BLACK));
            }
        });
        
        drawPanel.addMouseListener(drawPanel.Mouse_Listener);
	drawPanel.addMouseMotionListener(drawPanel.Motion_Listner);
        
        undo.addActionListener(new ActionListener()
        {
        @Override
            public void actionPerformed(ActionEvent arg0){
                if (shapes.size() != 0){
                    shapes.remove(shapes.size()-1);
                    repaint();
                }
            }
        });
        
        clear.addActionListener(new ActionListener() 
        {
        @Override
            public void actionPerformed(ActionEvent ae) {
                if (clear == ae.getSource())
                {
                    shapes.clear();
                    repaint();
                }
            }
        });
    }

    // Create event handlers, if needed
    public static boolean isNumber(String strNum) {
	try {
            if (strNum == null) {
		return false;
		}
            double dbl = Double.parseDouble(strNum);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    // Create a private inner class for the DrawPanel.
    private class DrawPanel extends JPanel
    {
        MouseMotionListener Motion_Listner = new MouseHandler();
	MouseListener Mouse_Listener = new MouseHandler();
	DrawPanel drawPanel;

        public DrawPanel()
        {
            drawPanel = this;
        }
        
        public void myRepaint() {
            this.validate();
            this.repaint();
	}

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            //loop through and draw each shape in the shapes arraylist
            for (MyShapes shape : shapes)
            {
                shape.draw(g2d);
            }

        }


        private class MouseHandler extends MouseAdapter implements MouseMotionListener
        {

            public void mousePressed(MouseEvent event)
            {
                x1 = event.getX();
		y1 = event.getY();
		drag = true;
                Paint tempain = new GradientPaint(0, 0, color1.getBackground(), 50, 50, color1.getBackground(), true);
                Stroke tempstr = new BasicStroke(0, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
                shapes.add(new MyLine(new Point(0,20), new Point(0,20), tempain, tempstr));
                status.setText("x:" + event.getX() + ",y:" + event.getY());
                
            }

            public void mouseReleased(MouseEvent event)
            {
                drag = false;
                status.setText("x:" + event.getX() + ",y:" + event.getY());
		drawPanel.myRepaint();
            }

            @Override
            public void mouseDragged(MouseEvent event)
            {
                if (drag) {
                    x2 = event.getX();
                    y2 = event.getY();
                    Point _point1 = new Point(x1, y1);
                    Point _point2 = new Point(x2, y2);
                    status.setText("x:" + event.getX() + ",y:" + event.getY());
                    Paint paintColor;
                    if (isGradient.isSelected()) {
                        paintColor = new GradientPaint(0, 0, color1.getBackground(), 50, 50, color2.getBackground(), true);
                    } else {
                        paintColor = new GradientPaint(0, 0, color1.getBackground(), 50, 50, color1.getBackground(), true);
                    }

                    int linew = 1;
                    if (isNumber(width.getValue().toString())) {
                        int stroke = Integer.parseInt(width.getValue().toString());
                        if (stroke < 1) {
                        stroke = 1;
                        }
                        linew = stroke;
                    }

                    Stroke basicStroke;
                    if (isDashed.isSelected()) {
                        float dashes[] = { 10.0f };
                        if (isNumber(dashlen.getValue().toString())) {
                            float s_length = Integer.parseInt(dashlen.getValue().toString());
                            if (s_length > 0) {
                            dashes[0] = s_length;
                            }
                        }
                        basicStroke = new BasicStroke(linew, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, dashes, 0);
                    } else {
                        basicStroke = new BasicStroke(linew, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
                    }

                    boolean ifFill = (isFilled.isSelected() ? true : false);
                    MyShapes s1;
                    if (shapeopt.getSelectedItem().toString()=="Oval") {
                        s1 = new MyOval(_point1, _point2, paintColor, basicStroke, ifFill);
                    }
                    else if (shapeopt.getSelectedItem().toString()=="Rectangle"){
                        s1 = new MyRectangle(_point1, _point2, paintColor, basicStroke, ifFill);
                    }
                    else{
                        s1 = new MyLine(_point1, _point2, paintColor, basicStroke);
                    }
                    shapes.remove(shapes.size()-1);
                    shapes.add(s1);
                    drawPanel.myRepaint();                
                }
                
            }

            @Override
            public void mouseMoved(MouseEvent event)
            {
                status.setText("x:" + event.getX() + ",y:" + event.getY());
            }
        }

    }
}
