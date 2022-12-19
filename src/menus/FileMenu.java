package menus;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import frames.DrawingPanel;
import frames.MainFrame;
import global.Constants;
import global.Constants.CFileMenu;
import global.Constants.CMainFrame;
import global.Constants.EFileMenus;

public class FileMenu extends JMenu{

	private static final long serialVersionUID = 1L;

	private DrawingPanel drawingPanel;
	private MainFrame mainFrame;

	private File file;
	private File defaultDirectory=new File(Constants.defaultDirectory);
	
	public FileMenu(String title) {
		super(title);
		this.file=null;

		menuActionHandler menulistener = new menuActionHandler();
		
		for(EFileMenus eMenuItem:EFileMenus.values()) {
			JMenuItem item =new JMenuItem(eMenuItem.getLabel());
			item.addActionListener(menulistener);
			item.setActionCommand(eMenuItem.name());
			item.setAccelerator(eMenuItem.getKeyStroke());
			this.add(item);
			this.addSeparator();
		}
	}

	public void associate(DrawingPanel drawingPanel, MainFrame mainFrame) {
		this.drawingPanel=drawingPanel;
		this.mainFrame=mainFrame;
	}

	public void initialize() {
	}

	private void newPanel() {
		if (this.checkSaveDialog()) {
			this.mainFrame.setTitle(CMainFrame.TITLE);
			this.drawingPanel.clearShapes();
			this.drawingPanel.setUpdated(false);
			this.file = null;
		}
	}

	private void open() {
		if (this.checkSaveDialog()) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(this.defaultDirectory);
			int returnVal = fileChooser.showOpenDialog(this.drawingPanel);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				this.file = fileChooser.getSelectedFile();
			this.defaultDirectory=fileChooser.getCurrentDirectory();
				this.load(this.file);
				this.setNewState();
			}
		}
	}
	
	private void load(File file) {
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			ObjectInputStream objectInputStream=new ObjectInputStream(fileInputStream);
			Object object=objectInputStream.readObject();
			this.drawingPanel.setShapes(object);
			objectInputStream.close();
		} catch (IOException |ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	private boolean checkSaveDialog() {
		boolean isYesOrNo=true;
		if(this.drawingPanel.isUpdated()) {
			int reply = JOptionPane.showConfirmDialog(this.drawingPanel, CFileMenu.QSAVE, CFileMenu.SAVELABEL, JOptionPane.YES_NO_CANCEL_OPTION);
			if (reply == JOptionPane.CANCEL_OPTION || reply == JOptionPane.CLOSED_OPTION) isYesOrNo=false;
			else if (reply == JOptionPane.YES_OPTION) isYesOrNo=this.save();
		}
		return isYesOrNo;
	}

	public boolean save() {
		if(this.drawingPanel.isUpdated()) {
			if(this.file==null) return this.saveAs();
			else return this.store(this.file);
		}
		return true;
	}

	public boolean saveAs() {
		JFileChooser fileChooser=new JFileChooser();
		fileChooser.setCurrentDirectory(this.defaultDirectory);
		int returnVal=fileChooser.showSaveDialog(this.drawingPanel);
		if(returnVal==JFileChooser.APPROVE_OPTION) {
			this.file=fileChooser.getSelectedFile();
			this.defaultDirectory=fileChooser.getCurrentDirectory();
			return this.store(this.file);
		}
		return false;
	}

	public boolean store(File file) {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(this.file);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(this.drawingPanel.getShapes());
			objectOutputStream.close();
			
			this.setNewState();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private void setNewState() {
		this.mainFrame.setTitle(CMainFrame.TITLE + " - " + this.file.getName());
		this.drawingPanel.setUpdated(false);
	}

	public void print() {
		this.drawingPanel.print();
	}
	
	public void quit() {
		if(this.checkSaveDialog()) System.exit(0);
	}
	
	public class menuActionHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			if(cmd.equals(EFileMenus.eNew.name())) newPanel();
			else if(cmd.equals(EFileMenus.eOpen.name())) open();
			else if(cmd.equals(EFileMenus.eSave.name())) save();
			else if(cmd.equals(EFileMenus.eSaveAs.name())) saveAs();
			else if(cmd.equals(EFileMenus.ePrint.name())) print();
			else if(cmd.equals(EFileMenus.eQuit.name())) quit();
		}
	}

}
