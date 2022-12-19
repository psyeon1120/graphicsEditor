package frames;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import global.Constants.CMainFrame;

public class MainFrame extends JFrame {
	// attributes
	private static final long serialVersionUID = 1L;

	// components
	private DrawingPanel drawingPanel;
	private MenuBar menuBar;
	private ToolBar toolBar;

	public MainFrame() {
		// attributes
		this.setTitle(CMainFrame.TITLE);
		this.setLocation(new Point(CMainFrame.LOCATION));
		this.setSize(new Dimension(CMainFrame.SIZE));
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		Toolkit kit = Toolkit.getDefaultToolkit();
		Image icon=kit.getImage(CMainFrame.ICON);
		this.setIconImage(icon);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				quit();
			}
		});

		// components
		BorderLayout lm=new BorderLayout();
		this.setLayout(lm);
		
		this.menuBar = new MenuBar();
		this.setJMenuBar(menuBar);
		
		
		this.toolBar=new ToolBar();
		this.add(this.toolBar,BorderLayout.NORTH);
		
		this.drawingPanel = new DrawingPanel();
		this.add(this.drawingPanel,BorderLayout.CENTER);
		
		// association
		this.toolBar.associate(this.drawingPanel);
		this.menuBar.associate(this.drawingPanel,this);
	}
	
	private void quit() {
		this.menuBar.getFileMenu().quit();
	}

	public void initialize() {
		this.menuBar.initialize();
		this.toolBar.initialize();
		this.drawingPanel.initialize();
	}

}
