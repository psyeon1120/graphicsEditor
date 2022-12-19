package shapes;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

import global.Constants.EAnchors;

public class TLine extends TShape{
	private static final long serialVersionUID = 1L;

	public TLine() {
		this.shape=new Line2D.Double();
	}
	
	public TLine(Shape shape, Shape transformedShape, AffineTransform affineTransform,TAnchors anchors) {
		this.shape=shape;
		this.transformedShape=transformedShape;
		this.affineTransform=affineTransform;
		this.anchors=anchors;
		this.bSelected=false;
		this.bTransforming=false;
	}
	
	@Override
	public TShape clone() {
		return new TLine(this.shape,this.transformedShape,this.affineTransform,this.anchors);
	}

	@Override
	public TShape cloneNew() {
		return new TLine();
	}
	
	@Override
	public void keepDrawing(int x, int y) {
		Line2D.Double line=(Line2D.Double) this.shape;
		line.setLine(line.x1, line.y1, x, y);
	}
	
	@Override
	public void prepareDrawing(int x,int y) {
		Line2D.Double line=(Line2D.Double) this.shape;
		line.setLine(x, y, x, y);
	}
	
	@Override
	public boolean contains(int x, int y) {
		this.setTransformedShape();
		if(this.bSelected) {
			if(this.anchors.contains(x,y)) {
				return true;
			}
		}
		if(getTransformedShape().getBounds().contains(x, y)) {
			this.anchors.seteSelectedEAnchor(EAnchors.eMove);
			return true;
		}
		return false;
	}
	
}
