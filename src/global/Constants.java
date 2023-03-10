package global;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import shapes.TLine;
import shapes.TOval;
import shapes.TPolygon;
import shapes.TRectangle;
import shapes.TSelection;
import shapes.TShape;
import transformer.Mover;
import transformer.Resizer;
import transformer.Rotator;
import transformer.Transformer;

@SuppressWarnings("deprecation")
public class Constants {

	public enum ETransformationStyle{
		e2PTransformation,
		eNPTransformation;
	}
	
	public enum ETools {
		eSelection(CETools.SELECTLABEL, new TSelection(),ETransformationStyle.e2PTransformation),
		eRectangle(CETools.RECTANGLELABEL,new TRectangle(), ETransformationStyle.e2PTransformation),
		eOval(CETools.OVALLABEL,new TOval(), ETransformationStyle.e2PTransformation),
		eLine(CETools.LINELABEL,new TLine(), ETransformationStyle.e2PTransformation),
		ePolygon(CETools.POLYGONLABEL,new TPolygon(), ETransformationStyle.eNPTransformation);
		
		private String label;
		private TShape tool;
		private String icon;
		private String selectedIcon;
		private ETransformationStyle eTransformationStyle;
		
		ETools(String label, TShape tool, ETransformationStyle entransformation){
			this.label=label;
			this.tool=tool;
			this.eTransformationStyle=entransformation;
			this.icon="src\\image\\"+this.name()+".png";
			this.selectedIcon="src\\image\\"+this.name()+"S.jpg";
		}

		public String getIcon() {
			return this.icon;
		}
		
		public String getSelectedIcon() {
			return this.selectedIcon;
		}
		
		public String getLabel() {
			return this.label;
		}
		
		public TShape newShape() {
			return this.tool.cloneNew();
		}

		public ETransformationStyle getTransformationStyle() {
			return this.eTransformationStyle;
		}
	}
	
	public enum EDashTools {
		eFstDash(CDashTools.STRAIGHTLABEL, null),
		eScndDash(CDashTools.MIDDLEDOTLABEL,new float[] {6f, 6f}),
		eTrdDash(CDashTools.BIGDOTLABEL,new float[] {12f, 12f}),
		eFourDash(CDashTools.SMALLDOTLABEL,new float[] {5f, 1f});
		
		private String label;
		private float[] dash;
		private String icon;
		private String selectedIcon;
		
		EDashTools(String label, float[] dash){
			this.label=label;
			this.dash=dash;
			this.icon="src\\image\\"+this.name()+".png";
			this.selectedIcon="src\\image\\"+this.name()+"S.jpg";
		}

		public String getIcon() {
			return this.icon;
		}
		
		public String getSelectedIcon() {
			return this.selectedIcon;
		}
		
		public String getLabel() {
			return this.label;
		}
		
		public float[] getDash() {
			return this.dash;
		}
	}

	public enum EAnchors {

		eNW(Cursor.NW_RESIZE_CURSOR),
		eWW(Cursor.W_RESIZE_CURSOR),
		eSW(Cursor.SW_RESIZE_CURSOR),
		eNN(Cursor.N_RESIZE_CURSOR),
		eSS(Cursor.S_RESIZE_CURSOR),
		eSE(Cursor.SE_RESIZE_CURSOR),
		eEE(Cursor.E_RESIZE_CURSOR),
		eNE(Cursor.NE_RESIZE_CURSOR),
		eRR(-1),
		eMove(Cursor.MOVE_CURSOR);

		private Cursor cursor;
		private int cursorNum;
		private Transformer transformer;

		EAnchors(int cursorNum) {
			this.cursorNum=cursorNum;
			if (cursorNum != -1) this.cursor = new Cursor(cursorNum);
			else this.cursor = Toolkit.getDefaultToolkit().createCustomCursor(
						new ImageIcon(Constants.rotateImageIcon).getImage(), new Point(15, 15), null);
		}

		public Cursor getCursor() {
			return cursor;
		}
		public int getCursorNum() {
			return cursorNum;
		}
		
		public Transformer getTransformer(TShape currentShape) {
			EAnchors eAnchor=currentShape.geteSelectedEAnchor();
			switch(eAnchor) {
			case eNW:
			case eWW:
			case eSW:
			case eNN:
			case eSS:
			case eSE:
			case eEE:
			case eNE: this.transformer=new Resizer(currentShape); break;
			case eRR: this.transformer=new Rotator(currentShape); break;
			case eMove: this.transformer=new Mover(currentShape); break;
			}
			return this.transformer;
		}
	}

	public enum EEditMenus {
		eRedo(CEditMenu.REDOLABEL,CEditMenu.REDOKEY),
		eUndo(CEditMenu.UNDOLABEL,CEditMenu.UNDOKEY),
		eCut(CEditMenu.CUTLABEL,CEditMenu.CUTKEY),
		eCopy(CEditMenu.COPYLABEL,CEditMenu.COPYKEY),
		ePaste(CEditMenu.PASTELABEL,CEditMenu.PASTEKEY),
		eErase(CEditMenu.ERASELABEL,CEditMenu.ERASEKEY),
		eDown(CEditMenu.DOWNLABEL,CEditMenu.DOWNKEY),
		eUp(CEditMenu.UPLABEL,CEditMenu.UPKEY);
		
		private String label;
		private KeyStroke keyStroke;
		EEditMenus(String label, char key){
			this.label=label;
			this.keyStroke=KeyStroke.getKeyStroke(key,Event.CTRL_MASK);
		}
		
		public String getLabel() {
			return this.label;
		}
		
		public KeyStroke getKeyStroke() {
			return this.keyStroke;
		}
	}

	public enum EFileMenus {
		eNew(CFileMenu.NEWLABEL,CFileMenu.NEWKEY),
		eOpen(CFileMenu.OPENLABEL,CFileMenu.OPENKEY),
		eSave(CFileMenu.SAVELABEL,CFileMenu.SAVEKEY),
		eSaveAs(CFileMenu.SAVEASLABEL,CFileMenu.SAVEASKEY),
		ePrint(CFileMenu.PRINTLABEL,CFileMenu.PRINTKEY),
		eQuit(CFileMenu.QUITLABEL,CFileMenu.QUITKEY);
		
		private String label;
		private KeyStroke keyStroke;
		EFileMenus(String label, char key){
			this.label=label;
			this.keyStroke=KeyStroke.getKeyStroke(key,Event.CTRL_MASK);
		}
		
		public String getLabel() {
			return this.label;
		}
		
		public KeyStroke getKeyStroke() {
			return this.keyStroke;
		}
	}
	
	public enum EColorMenu {
		eSetLineColor(CColorMenu.LINECLABEL, CColorMenu.LINECKEY),
		eSetFillColor(CColorMenu.FILLCLABEL, CColorMenu.FILLCKEY),
		eSetNoneLine(CColorMenu.NONELINECLABEL, CColorMenu.FILLCKEY),
		eSetNoneFill(CColorMenu.NONEFILLCLABEL, CColorMenu.FILLCKEY);
		
		private String label;
		private KeyStroke keyStroke;
		EColorMenu(String label, char key){
			this.label=label;
			this.keyStroke=KeyStroke.getKeyStroke(key,Event.CTRL_MASK);
		}
		
		public String getLabel() {
			return this.label;
		}
		
		public KeyStroke getKeyStroke() {
			return this.keyStroke;
		}
	}
	
	public static class CIconSize {
		public static final int WIDTH = 35;
		public static final int HEIGHT = 35;
	}

	public static class CMainFrame {
		public static final Point LOCATION = new Point(0, 0);
		public static final Dimension SIZE = new Dimension(600, 700);
		public static final String TITLE = "?????????";
		public static final String ICON = "src\\image\\frameIcon.png";
		public static final String SAVEMSG = "??????????????? ??????????????????.";
	}
	
	public static class CETools {
		public static final String RECTANGLELABEL = "?????????";
		public static final String OVALLABEL = "???";
		public static final String LINELABEL = "???";
		public static final String POLYGONLABEL = "?????????";
		public static final String SELECTLABEL = "?????? ??????";
	}
	
	public static class CDashTools {
		public static final String STRAIGHTLABEL = "??????";
		public static final String MIDDLEDOTLABEL = "?????? ??????";
		public static final String BIGDOTLABEL = "??? ??????";
		public static final String SMALLDOTLABEL = "?????? ??????";
	}
	
	public static class CStrokeTools {
		public static final String STROKESIZETITLE = "??? ??????";
		public static final String DASHTITLE = "??? ??????";
	}
	
	public static class CEditMenu {
		public static final String TITLE = "??????";
		public static final String REDOLABEL = "????????????";
		public static final char REDOKEY = 'Y';
		public static final String UNDOLABEL = "????????????";
		public static final char UNDOKEY = 'Z';
		public static final String CUTLABEL = "????????????";
		public static final char CUTKEY = 'X';
		public static final String COPYLABEL = "??????";
		public static final char COPYKEY = 'C';
		public static final String PASTELABEL = "????????????";
		public static final char PASTEKEY = 'V';
		public static final String ERASELABEL = "?????????";
		public static final char ERASEKEY = 'E';
		public static final String DOWNLABEL = "???????????????";
		public static final char DOWNKEY = '[';
		public static final String UPLABEL = "??????????????????";
		public static final char UPKEY = ']';
	}

	public static class CFileMenu {
		public static final String TITLE = "??????";
		public static final String NEWLABEL = "???????????????";
		public static final char NEWKEY = 'N';
		public static final String OPENLABEL = "??????";
		public static final char OPENKEY = 'O';
		public static final String SAVELABEL = "??????";
		public static final char SAVEKEY = 'S';
		public static final String SAVEASLABEL = "?????????????????? ??????";
		public static final char SAVEASKEY = 'D';
		public static final String PRINTLABEL = "?????????";
		public static final char PRINTKEY = 'P';
		public static final String QUITLABEL = "??????";
		public static final char QUITKEY = 'Q';
		public static final String QSAVE = "?????? ???????????????????";
	}

	public static class CColorMenu {
		public static final String TITLE = "????????????";
		public static final String LINECLABEL = "????????? ?????? ??????";
		public static final char LINECKEY = 'L';
		public static final String FILLCLABEL = "????????? ?????? ??????";
		public static final char FILLCKEY = 'F';
		public static final String NONELINECLABEL = "????????? ??????";
		public static final char NONELINECKEY = 'I';
		public static final String NONEFILLCLABEL = "????????? ??????";
		public static final char NONEFILLCKEY = 'K';
	}

	public static String rotateImageIcon = "src\\image\\rotate.png";
	public static String defaultDirectory = "C:\\Users\\SeoyeonPark\\Downloads";
}
