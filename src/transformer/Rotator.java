package transformer;

import shapes.TShape;

public class Rotator extends Transformer{

	private double cx,cy;

	public Rotator(TShape selectedShape) {
		super(selectedShape);
	}

	@Override
	public Transformer clone(TShape shape) {
		return new Rotator(shape);
	}

	@Override
	public void prepare(int x, int y) {
		this.cx=this.selectedShape.getCenterX();
		this.cy=this.selectedShape.getCenterY();
		this.px=x;
		this.py=y;
	}

	@Override
	public void keepTransforming(int x, int y) {
		double start=Math.atan2(py-cy,px-cx);
		double end=Math.atan2(y-cy,x-cx);
		double angle=end-start;
		
		this.affineTransform.rotate(angle,this.cx,this.cy);
		this.px=x;
		this.py=y;
	}

	@Override
	public void finalize(int x, int y) {
		this.selectedShape.setShape(affineTransform.createTransformedShape(this.selectedShape.getShape()));
		this.affineTransform.setToIdentity();
	}
	
}
