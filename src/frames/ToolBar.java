package frames;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;

import global.Constants;
import global.Constants.CIconSize;
import global.Constants.EDashTools;
import global.Constants.ETools;

public class ToolBar extends JToolBar {

	//attributes
	private static final long serialVersionUID = 1L;

	//components
	private ButtonGroup eToolbtnGroup=new ButtonGroup();
	private ButtonGroup eDashToolbtnGroup=new ButtonGroup();
	private JComboBox<Integer> strokeSizeCombo;
	
	// associations
	private DrawingPanel drawingPanel;
	
	public ToolBar() {
		//components
		ToolShooseHandler toolChooseHandler=new ToolShooseHandler();
		
		for(ETools eTool:ETools.values()) {
			ImageIcon icon = resizeIcon(eTool.getIcon());
			JRadioButton radioButton=new JRadioButton(icon);
			icon = resizeIcon(eTool.getSelectedIcon());
			radioButton.setSelectedIcon(icon);
			radioButton.setActionCommand(eTool.name());
			radioButton.setBorderPainted(true);
			radioButton.setBorder(BorderFactory.createRaisedBevelBorder());
			radioButton.setToolTipText(eTool.getLabel());
			radioButton.addActionListener(toolChooseHandler);
			this.add(radioButton);
			this.addSeparator();
			eToolbtnGroup.add(radioButton);
		}
		
		StrokeChooseHandler strokeChooseHandler=new StrokeChooseHandler();
		JLabel strokeSizeLable = new JLabel(Constants.CStrokeTools.STROKESIZETITLE);
		this.add(strokeSizeLable);
		
		this.strokeSizeCombo = new JComboBox<>();
		for(int i=1;i<=10; i++) {
			this.strokeSizeCombo.addItem(i);
		}
		this.strokeSizeCombo.addActionListener(strokeChooseHandler);
		this.strokeSizeCombo.setActionCommand(Constants.CStrokeTools.STROKESIZETITLE);
		this.strokeSizeCombo.setMaximumSize(new Dimension(120,30));
		this.add(this.strokeSizeCombo);
		
		
		JLabel dashLabel = new JLabel(Constants.CStrokeTools.DASHTITLE);
		this.add(dashLabel);
		

		DashChooseHandler dashChooseHandler=new DashChooseHandler();
		
		for(EDashTools eDashTool:EDashTools.values()) {
			ImageIcon icon = resizeIcon(eDashTool.getIcon());
			JRadioButton radioButton=new JRadioButton(icon);
			icon = resizeIcon(eDashTool.getSelectedIcon());
			radioButton.setSelectedIcon(icon);
			radioButton.setActionCommand(eDashTool.name());
			radioButton.setBorderPainted(true);
			radioButton.setBorder(BorderFactory.createRaisedBevelBorder());
			radioButton.setToolTipText(eDashTool.getLabel());
			radioButton.addActionListener(dashChooseHandler);
			this.add(radioButton);
			this.addSeparator();
			eDashToolbtnGroup.add(radioButton);
		}
	}

	public void associate(DrawingPanel drawingPanel) {
		this.drawingPanel=drawingPanel;
		JRadioButton defaultButton=(JRadioButton) this.getComponent(ETools.eSelection.ordinal());
		defaultButton.doClick();
	}

	public void initialize() {
	}

	private ImageIcon resizeIcon(String iconPath) {
		ImageIcon icon=new ImageIcon(iconPath);
		Image img=icon.getImage().getScaledInstance(CIconSize.WIDTH, CIconSize.HEIGHT, Image.SCALE_SMOOTH);
		icon=new ImageIcon(img);
		return icon;
	}
	
	public String getSelectedShape() {
		return this.eToolbtnGroup.getSelection().getActionCommand();
	}
	
	private class ToolShooseHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			drawingPanel.setSelectedTool(ETools.valueOf(e.getActionCommand()));
		}
	}
	
	class StrokeChooseHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			int stroke = (int) strokeSizeCombo.getSelectedItem();
			drawingPanel.setStroke(stroke);
		}
	}
	
	class DashChooseHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			drawingPanel.setDash(EDashTools.valueOf(e.getActionCommand()).getDash());
		}
	}
	
}
