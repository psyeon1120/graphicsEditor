package frames;
import javax.swing.JMenuBar;

import global.Constants;
import menus.ColorMenu;
import menus.EditMenu;
import menus.FileMenu;

public class MenuBar extends JMenuBar {
	// attributes
	private static final long serialVersionUID = 1L;

	// associations
	private DrawingPanel drawingPanel;
	private MainFrame mainFrame;
	
	//components
	private FileMenu fileMenu;
	private EditMenu editMenu;
	private ColorMenu colorMenu;

	public MenuBar() {
		//components
		this.fileMenu = new FileMenu(Constants.CFileMenu.TITLE);
		this.add(this.fileMenu);

		this.editMenu = new EditMenu(Constants.CEditMenu.TITLE);
		this.add(this.editMenu);
		
		this.colorMenu = new ColorMenu(Constants.CColorMenu.TITLE);
		this.add(this.colorMenu);
	}

	public FileMenu getFileMenu() {
		return fileMenu;
	}

	public void associate(DrawingPanel drawingPanel, MainFrame mainFrame) {
		this.drawingPanel=drawingPanel;
		this.mainFrame=mainFrame;
		this.fileMenu.associate(this.drawingPanel,this.mainFrame);
		this.editMenu.associate(this.drawingPanel);
		this.colorMenu.associate(this.drawingPanel);
	}

	public void initialize() {
		this.fileMenu.initialize();
		this.editMenu.initialize();
		this.colorMenu.initialize();
	}


}
