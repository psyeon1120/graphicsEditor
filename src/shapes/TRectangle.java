package shapes;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

public class TRectangle extends TShape {
	private static final long serialVersionUID = 1L;

	public TRectangle() {
		this.shape=new Rectangle();
	}
	
	public TRectangle(Shape shape, Shape transformedShape, AffineTransform affineTransform,TAnchors anchors) {
		this.shape=shape;
		this.transformedShape=transformedShape;
		this.affineTransform=affineTransform;
		this.anchors=anchors;
		this.bSelected=false;
		this.bTransforming=false;
	}
	
	@Override
	public TShape clone() {
		return new TRectangle(this.shape,this.transformedShape,this.affineTransform,this.anchors);
	}

	@Override
	public TShape cloneNew() {
		return new TRectangle();
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
}
