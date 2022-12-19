package shapes;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;

public class TSelection extends TShape {
	private static final long serialVersionUID = 1L;

	public TSelection() {
		this.shape=new Rectangle();
	}
	
	@Override
	public TShape clone() {
		return new TSelection();
	}

	@Override
	public TShape cloneNew() {
		return new TSelection();
	}
	
	public void prepareDrawing(int x,int y) {
		Rectangle rectangle=(Rectangle) this.shape;
		rectangle.setFrame(x, y, 0, 0);
		startX=x;
		startY=y;
	}

	public void keepDrawing(int x, int y) {
		Rectangle rectangle=(Rectangle) this.shape;
		int opp =isPointOpposite(x, y);
		switch(opp) {
		case 2: rectangle.setFrame(x, y, startX-x, startY-y); break;
		case 1: rectangle.setFrame(x, startY, startX-x, y-startY); break;
		case 0: rectangle.setFrame(startX, y, x-startX, startY-y); break;
		default: rectangle.setFrame(startX, startY, x-startX, y-startY);
		}
	}

	public void drawTransforming(Graphics2D g2) {
		float[] dashingPattern1 = {2f, 2f};
		Stroke stroke = new BasicStroke(2f, BasicStroke.CAP_BUTT,
		        BasicStroke.JOIN_MITER, 1.0f, dashingPattern1, 2.0f);
		g2.setStroke(stroke);
		g2.draw(this.shape);
	}

	
}
