package menus;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import frames.DrawingPanel;
import global.Constants.EEditMenus;

public class EditMenu extends JMenu {

	private static final long serialVersionUID = 1L;

	private DrawingPanel drawingPanel;
	
	public EditMenu(String s) {
		super(s);

		menuActionListener menulistener = new menuActionListener();

		for(EEditMenus eEditMenu:EEditMenus.values()) {
			JMenuItem item=new JMenuItem(eEditMenu.getLabel());
			item.addActionListener(menulistener);
			item.setActionCommand(eEditMenu.name());
			item.setAccelerator(eEditMenu.getKeyStroke());
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


	public void redo() {
		this.drawingPanel.redo();
	}

	public void undo() {
		this.drawingPanel.undo();
	}

	public void up() {
		this.drawingPanel.up();
	}

	public void down() {
		this.drawingPanel.down();
	}

	public void erase() {
		this.drawingPanel.erase();
	}

	public void paste() {
		this.drawingPanel.paste();
	}

	public void copy() {
		this.drawingPanel.copy();
	}

	public void cut() {
		this.drawingPanel.cut();
	}

	public class menuActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			if(cmd.equals(EEditMenus.eRedo.name())) redo();
			else if(cmd.equals(EEditMenus.eUndo.name())) undo();
			else if(cmd.equals(EEditMenus.eCut.name())) cut();
			else if(cmd.equals(EEditMenus.eCopy.name())) copy();
			else if(cmd.equals(EEditMenus.ePaste.name())) paste();
			else if(cmd.equals(EEditMenus.eErase.name())) erase();
			else if(cmd.equals(EEditMenus.eDown.name())) down();
			else if(cmd.equals(EEditMenus.eUp.name())) up();
		}

}
}
