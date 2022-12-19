package frames;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import global.Constants.ETools;
import global.Constants.ETransformationStyle;
import shapes.TShape;
import transformer.Drawer;
import transformer.Transformer;

public class DrawingPanel extends JPanel implements Printable {
	private static final long serialVersionUID = 1L;
	private boolean updated;
	
	private Vector<TShape> shapes;
	private Vector<TShape> shapesHistory;
	private Vector<TShape> redoClipShapes;
	private Vector<TShape> selectedShapes;
	private Vector<TShape> clipShapes;
	private Vector<Transformer> transformers;
	
	private BufferedImage bufferedImage;
	private Graphics2D g2BufferredImage;

	private enum EDrawingState {
		eIdle, e2PointDrawing, eNPointDrawing, eMoving, eRotating, eResizing
	}

	private EDrawingState eDrawingState;

	private ETools selectedTool;
	private TShape currentShape;
	private Color lineColor, fillColor;
	private float[] dashes;
	private int stroke;
	
	public DrawingPanel() {
		this.eDrawingState = EDrawingState.eIdle;
		this.updated=false;
		this.setBackground(Color.WHITE);
		lineColor = Color.BLACK;
		fillColor = Color.WHITE;
		this.dashes=null;
		this.stroke=1;

		//components
		this.shapes=new Vector<TShape>();
		this.shapesHistory=new Vector<TShape>();
		this.selectedShapes=new Vector<TShape>();
		this.redoClipShapes=new Vector<TShape>();
		this.clipShapes=new Vector<TShape>();
		this.transformers=new Vector<Transformer>();
		
		MouseHandler mouseHandler = new MouseHandler();
		// button
		this.addMouseListener(mouseHandler);
		// position
		this.addMouseMotionListener(mouseHandler);
		// wheel
		this.addMouseWheelListener(mouseHandler);
	}

	public void initialize() {
		Dimension size=Toolkit.getDefaultToolkit().getScreenSize();
		this.bufferedImage=(BufferedImage) this.createImage((int)size.getWidth(),(int)size.getHeight());
		this.g2BufferredImage=(Graphics2D) this.bufferedImage.getGraphics();
		this.g2BufferredImage.setBackground(Color.WHITE);
		this.g2BufferredImage.clearRect(0, 0, this.bufferedImage.getWidth(),this.bufferedImage.getHeight());
	}

	public void setSelectedTool(ETools selectedTool) {
		this.selectedTool = selectedTool;
	}

	public boolean isUpdated() {
		return this.updated;
	}
	
	public void setUpdated(boolean updated) {
		this.updated=updated;
	}

	@SuppressWarnings("unchecked")
	public void setShapes(Object shapes) {
		this.shapes=(Vector<TShape>) shapes;
		this.repaint();
	}

	public Object getShapes() {
		return this.shapes;
	}
	
	public void clearShapes() {
		this.shapes.clear();
		this.repaint();
	}

	public void paint(Graphics g) {
		super.paint(g);
		this.g2BufferredImage.clearRect(0, 0, this.bufferedImage.getWidth(), this.bufferedImage.getHeight());
		for(TShape shape: this.shapes) {
			shape.draw(g2BufferredImage);
		}

		g.drawImage(this.bufferedImage, 0,0,this);
	}

	private void prepareTransforming(int x, int y) {
		if (selectedTool == ETools.eSelection) {
			if(this.selectedShapes.size()!=0&&onShape(x, y)!=null) {
				for(TShape s:this.selectedShapes) {
					s.setbTransforming(true);
					this.transformers.add(s.geteSelectedEAnchor().getTransformer(s));
				}
			}else {
				for(TShape s:this.selectedShapes) {
					s.setSelected(false);
				}
				this.selectedShapes.clear();
				this.currentShape = this.selectedTool.newShape();
				this.transformers.add(new Drawer(this.currentShape));
			}
		} else {
			this.currentShape = this.selectedTool.newShape();
			this.currentShape.setDash(this.dashes);
			this.currentShape.setStroke(this.stroke);
			this.currentShape.setLineColor(this.lineColor);
			this.currentShape.setFillColor(this.fillColor);
			this.transformers.add(new Drawer(this.currentShape));
		}

		for(Transformer t:this.transformers) {
			t.prepare(x, y);
		}
		
		repaint();
		this.g2BufferredImage.setXORMode(this.getBackground());
		this.currentShape.draw(g2BufferredImage);
	}

	private void keepTransforming(int x, int y) {
		if (selectedTool != ETools.eSelection) {
			this.currentShape.drawTransforming(this.g2BufferredImage);
			for (Transformer t : this.transformers) t.keepTransforming(x, y);
			this.currentShape.drawTransforming(this.g2BufferredImage);
		} else {
			if (this.selectedShapes.size() != 0) {
				for (TShape s : this.selectedShapes) s.drawTransforming(this.g2BufferredImage);
				for (Transformer t : this.transformers) t.keepTransforming(x, y);
				for (TShape s : this.selectedShapes) s.drawTransforming(this.g2BufferredImage);
			}else {
				this.currentShape.drawTransforming(this.g2BufferredImage);
				for (Transformer t : this.transformers) t.keepTransforming(x, y);
				this.currentShape.drawTransforming(this.g2BufferredImage);
			}
		}
		this.getGraphics().drawImage(bufferedImage, 0, 0, this);
	}

	private void continueTransforming(int x, int y) {
		this.currentShape.addPoint(x, y);
	}

	private void finishTransforming(int x, int y) {
		this.g2BufferredImage.setPaintMode();
		for (Transformer t : this.transformers) {
			t.finalize(x, y);
		}
		if (selectedTool == ETools.eSelection) {
			if (this.selectedShapes.size() != 0) {
				for (TShape s : this.selectedShapes) s.setbTransforming(false);
			} else {
				this.currentShape.setbTransforming(false);
				for (TShape s : this.selectedShapes) s.setSelected(false);
				this.selectedShapes.clear();
				finishSelecting(x, y);
			}
		} else {
			this.currentShape.setbTransforming(false);

			for (TShape s : this.selectedShapes) s.setSelected(false);
			this.selectedShapes.clear();

			this.redoClipShapes.clear();
			if (this.transformers.get(0) instanceof Drawer) {
				this.shapes.add(currentShape);
				this.shapesHistory.add(currentShape);
			}
			this.currentShape.setSelected(true);
			this.selectedShapes.add(currentShape);
			this.setUpdated(true);
		}

		this.transformers.clear();
		this.repaint();
	}

	private TShape onShape(int x, int y) {
		for (int i = this.shapes.size() - 1; i >= 0; i--) {
			if (this.shapes.get(i).contains(x, y))
				return this.shapes.get(i);
		}
		return null;
	}
	
	private void changeSelection(int x, int y) {
		for(TShape s:this.selectedShapes) s.setSelected(false);
		this.selectedShapes.clear();
		TShape sShape=this.onShape(x, y);
		if(sShape!=null) {
			sShape.setSelected(true);
			this.selectedShapes.add(sShape);
		}
		this.repaint();
	}
	
	private void changeCursor(int x, int y) {
		Cursor cursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
		if (this.selectedTool == ETools.eSelection) {
			cursor = new Cursor(Cursor.DEFAULT_CURSOR);
			TShape shape = this.onShape(x, y);
			if (shape != null)
				cursor = shape.getAnchors().geteSelectedEAnchor().getCursor();
		}
		this.setCursor(cursor);
	}
	
	private void finishSelecting(int x, int y) {
		this.currentShape.draw(g2BufferredImage);
		Rectangle r=this.currentShape.getShape().getBounds();
		inSelectBox(r.getMinX(),r.getMaxX(),r.getMinY(),r.getMaxY());
	}

	private void inSelectBox(double minX, double maxX, double minY, double maxY) {
		this.selectedShapes.clear();
		for(TShape shape:this.shapes) {
			Rectangle r=shape.getShape().getBounds();
			if(r.getMinX()>minX&&r.getMaxX()<maxX&&r.getMinY()>minY&&r.getMaxY()<maxY) {
				shape.setSelected(true);
				this.selectedShapes.add(shape);
			}
		}
	}

	private class MouseHandler implements MouseInputListener, MouseMotionListener, MouseWheelListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (e.getClickCount() == 1) lButtonClick(e);
				else if (e.getClickCount() == 2) lButtonDoubleClick(e);
			}
		}

		private void lButtonClick(MouseEvent e) {
			if(eDrawingState==EDrawingState.eIdle) {
				changeSelection(e.getX(), e.getY());
				if (selectedTool.getTransformationStyle()==ETransformationStyle.eNPTransformation) {
					prepareTransforming(e.getX(), e.getY());
					eDrawingState=EDrawingState.eNPointDrawing;
				}
			} else if(eDrawingState==EDrawingState.eNPointDrawing)
				continueTransforming(e.getX(), e.getY());
		}

		private void lButtonDoubleClick(MouseEvent e) {
			if (eDrawingState == EDrawingState.eNPointDrawing) {
				finishTransforming(e.getX(), e.getY());
				eDrawingState = EDrawingState.eIdle;
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if (eDrawingState == EDrawingState.eNPointDrawing)
				keepTransforming(e.getX(), e.getY());
			else if(eDrawingState==EDrawingState.eIdle)
				changeCursor(e.getX(), e.getY());
		}		
		
		@Override
		public void mousePressed(MouseEvent e) {
			if (eDrawingState == EDrawingState.eIdle) {
				if (selectedTool.getTransformationStyle() == ETransformationStyle.e2PTransformation) {
					prepareTransforming(e.getX(), e.getY());
					eDrawingState = EDrawingState.e2PointDrawing;
				}
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if(eDrawingState==EDrawingState.e2PointDrawing)
				keepTransforming(e.getX(), e.getY());
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if(eDrawingState==EDrawingState.e2PointDrawing) {
				finishTransforming(e.getX(), e.getY());
				eDrawingState = EDrawingState.eIdle;
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
		}
	
}

	public void redo() {
		if (this.redoClipShapes.size() != 0) {
			for (TShape s : this.selectedShapes) s.setSelected(false);
			this.selectedShapes.clear();
			TShape shape = this.redoClipShapes.get(this.redoClipShapes.size() - 1);
			shape.setSelected(true);
			this.shapes.add(shape);
			shape.setSelected(false);
			this.shapesHistory.add(shape);
			this.redoClipShapes.remove(this.redoClipShapes.size() - 1);
			this.repaint();
		}
	}

	public void undo() {
		if (this.shapesHistory.size() == 0) return;
		
		for(TShape s:this.selectedShapes) s.setSelected(false);
		this.selectedShapes.clear();
		
		TShape shape = this.shapesHistory.get(this.shapesHistory.size() - 1);
		this.shapes.remove(shape);
		this.redoClipShapes.add(shape);
		this.shapesHistory.remove(this.shapesHistory.size() - 1);
		this.repaint();
	}

	public void erase() {
		this.redoClipShapes.clear();
		for(TShape s:this.selectedShapes){
			s.setSelected(false);
			this.shapesHistory.add(s);
			this.shapes.remove(s);
			this.repaint();
		}
		this.selectedShapes.clear();
	}

	public void paste() {
		if(this.clipShapes.size()!=0) {
			this.redoClipShapes.clear();
			for(TShape s:this.selectedShapes) s.setSelected(false);
			this.selectedShapes.clear();
			
			for(TShape cs:clipShapes) {
				cs.setSelected(true);
				this.shapes.add(cs);
				this.selectedShapes.add(cs);
				this.shapesHistory.add(cs);
			}
			this.clipShapes.clear();
			this.repaint();
		}
		
	}

	public void copy() {
		for(TShape s:this.selectedShapes){
			if(s.isSelected()) this.clipShapes.add(s.clone());
		}
	}

	public void cut() {
		this.redoClipShapes.clear();
		for(TShape s:this.selectedShapes){
			if(s.isSelected()) {
				this.clipShapes.add(s);
				this.shapes.remove(s);
				this.shapesHistory.add(s);
			}
		}
		this.repaint();
		this.selectedShapes.clear();
	}

	public void up() {
		if(this.selectedShapes.size()!=0) {
			for(TShape s:this.selectedShapes) {
				int index=this.shapes.indexOf(s);
				if(index==this.shapes.size()-1) continue;
				this.shapes.remove(s);
				this.shapes.add(index+1, s);
			}
		}
		this.repaint();
	}

	public void down() {
		if(this.selectedShapes.size()!=0) {
			for(TShape s:this.selectedShapes) {
				int index=this.shapes.indexOf(s);
				if(index==0) continue;
				this.shapes.remove(s);
				this.shapes.add(index-1, s);
			}
		}
		this.repaint();
	}
	
	public void print() {
		PrinterJob job = PrinterJob.getPrinterJob();
		job.setPrintable(this);
		boolean isPrinted = job.printDialog();
		if(isPrinted) {
			try {
				job.print();
			} catch (PrinterException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setStroke(int stroke) {
		if (this.selectedShapes.size() != 0) {
			for (TShape s : this.selectedShapes) s.setStroke(stroke);
			this.repaint();
		} else this.stroke = stroke;
	}
	
	public void setDash(float[] dash) {
		if (this.selectedShapes.size() != 0) {
			for (TShape s : this.selectedShapes) s.setDash(dash);
			this.repaint();
		} else this.dashes = dash;
	}
	
	public void setColor(boolean line, Color color) {		
		if (this.selectedShapes.size() != 0) {
			for (TShape s : this.selectedShapes) {
				if (line) s.setLineColor(color);
				else s.setFillColor(color);
			}
			this.repaint();
		} else {
			if (line) this.lineColor=color;
			else this.fillColor=color;
		}
	}

	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
		if(pageIndex>0) return NO_SUCH_PAGE;
		Graphics2D g2 = (Graphics2D)graphics;
		g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
		for (TShape shape : this.shapes) {
			shape.draw(g2);
		}
		return PAGE_EXISTS;
	}

}
