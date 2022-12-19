package shapes;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

public class TPolygon extends TShape {
	private static final long serialVersionUID = 1L;

	public TPolygon() {
		this.shape=new Polygon();
	}

	public TPolygon(Shape shape, Shape transformedShape, AffineTransform affineTransform,TAnchors anchors) {
		this.shape=shape;
		this.transformedShape=transformedShape;
		this.affineTransform=affineTransform;
		this.anchors=anchors;
		this.bSelected=false;
		this.bTransforming=false;
	}
	
	@Override
	public TShape clone() {
		return new TPolygon(this.shape,this.transformedShape,this.affineTransform,this.anchors);
	}

	@Override
	public TShape cloneNew() {
		return new TPolygon();
	}
	
	public void prepareDrawing(int x,int y) {
		Polygon polygon=(Polygon) this.shape;
		polygon.addPoint(x, y);
		polygon.addPoint(x, y);
	}
	
	@Override
	public void addPoint(int x, int y) {
		Polygon polygon=(Polygon) this.shape;
		polygon.addPoint(x, y);
	}

	public void keepDrawing(int x, int y) {
		Polygon polygon=(Polygon) this.shape;
		polygon.xpoints[polygon.npoints-1]=x;
		polygon.ypoints[polygon.npoints-1]=y;
	}
	
}
