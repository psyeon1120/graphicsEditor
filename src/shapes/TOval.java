package shapes;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

public class TOval extends TShape {
	private static final long serialVersionUID = 1L;

	public TOval() {
		this.shape=new Ellipse2D.Double();
	}

	public TOval(Shape shape, Shape transformedShape, AffineTransform affineTransform,TAnchors anchors) {
		this.shape=shape;
		this.transformedShape=transformedShape;
		this.affineTransform=affineTransform;
		this.anchors=anchors;
		this.bSelected=false;
		this.bTransforming=false;
	}
	
	@Override
	public TShape clone() {
		return new TOval(this.shape,this.transformedShape,this.affineTransform,this.anchors);
	}

	@Override
	public TShape cloneNew() {
		return new TOval();
	}
	
	public void prepareDrawing(int x,int y) {
		Ellipse2D.Double oval=(Ellipse2D.Double) this.shape;
		oval.setFrame(x, y, 0, 0);
		startX=x;
		startY=y;
	}

	public void keepDrawing(int x, int y) {
		Ellipse2D.Double oval=(Ellipse2D.Double) this.shape;
		int opp =isPointOpposite(x, y);
		switch(opp) {
		case 2: oval.setFrame(x, y, startX-x, startY-y); break;
		case 1: oval.setFrame(x, startY, startX-x, y-startY); break;
		case 0: oval.setFrame(startX, y, x-startX, startY-y); break;
		default: oval.setFrame(startX, startY, x-startX, y-startY);
		}
	}
	
}
