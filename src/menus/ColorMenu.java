package menus;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JColorChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import frames.DrawingPanel;
import global.Constants;
import global.Constants.EColorMenu;

public class ColorMenu extends JMenu{

	private static final long serialVersionUID = 1L;

	private DrawingPanel drawingPanel;
	
	public ColorMenu(String title) {
		super(title);

		menuActionHandler menulistener = new menuActionHandler();
		
		for(EColorMenu eMenuItem:EColorMenu.values()) {
			JMenuItem item =new JMenuItem(eMenuItem.getLabel());
			item.addActionListener(menulistener);
			item.setActionCommand(eMenuItem.name());
			item.setAccelerator(eMenuItem.getKeyStroke());
			this.add(item);
			this.addSeparator();
		}
	}

	public void associate(DrawingPanel drawingPanel) {
		this.drawingPanel=drawingPanel;
	}

	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	private void setLineColor() {
		Color lineColor = JColorChooser.showDialog(null, Constants.CColorMenu.LINECLABEL, null);
		if (lineColor != null) {
			drawingPanel.setColor(true, lineColor);
		}
	}

	private void setFillColor() {
		Color fillColor = JColorChooser.showDialog(null, Constants.CColorMenu.FILLCLABEL, null);
		if (fillColor != null) {
			drawingPanel.setColor(false, fillColor);
		}
	}

	private void noLine() {
		drawingPanel.setColor(true, null);
	}

	private void noFill() {
		drawingPanel.setColor(false, null);
	}

	public class menuActionHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			if(cmd.equals(EColorMenu.eSetLineColor.name())) setLineColor();
			else if(cmd.equals(EColorMenu.eSetFillColor.name())) setFillColor();
			else if(cmd.equals(EColorMenu.eSetNoneLine.name())) noLine();
			else if(cmd.equals(EColorMenu.eSetNoneFill.name())) noFill();
		}
	}

}
