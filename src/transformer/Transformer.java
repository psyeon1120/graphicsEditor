package transformer;

import java.awt.geom.AffineTransform;

import shapes.TAnchors;
import shapes.TShape;

public abstract class Transformer {

	protected TShape selectedShape;
	protected AffineTransform affineTransform;
	protected TAnchors anchors;
	
	protected double px, py;  //원래점
	
	public Transformer(TShape shape){
		this.selectedShape=shape;
		this.affineTransform =this.selectedShape.getAffineTransform();
		this.anchors=this.selectedShape.getAnchors();
	}
	
	public abstract void prepare(int x, int y);
	
	public abstract void keepTransforming(int x, int y);
	
	public abstract void finalize(int x, int y);

	public abstract Transformer clone(TShape shape);
	
}
