package transformer;

import shapes.TShape;

public class Mover extends Transformer{

	public Mover(TShape selectedShape) {
		super(selectedShape);
	}

	@Override
	public Transformer clone(TShape shape) {
		return new Mover(shape);
	}
	
	@Override
	public void prepare(int x, int y) {
		this.px=x;
		this.py=y;
	}
	
	@Override
	public void keepTransforming(int x, int y) {
		this.affineTransform.translate(x-this.px, y-this.py);
		this.px=x;
		this.py=y;
	}
	
	@Override
	public void finalize(int x, int y) {
		this.selectedShape.setShape(affineTransform.createTransformedShape(this.selectedShape.getShape()));
		this.affineTransform.setToIdentity();
	}
	
}
