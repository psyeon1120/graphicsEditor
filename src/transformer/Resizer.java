package transformer;

import java.awt.geom.Point2D;

import global.Constants.EAnchors;
import shapes.TShape;

public class Resizer extends Transformer{
	private double cetaX, cetaY;  //기준점
	private double xScale, yScale;  //스케일배수
	
	public Resizer(TShape selectedShape) {
		super(selectedShape);
	}

	@Override
	public Transformer clone(TShape shape) {
		return new Resizer(shape);
	}

	@Override
	public void prepare(int x, int y) {
		this.px=x;
		this.py=y;
		Point2D resizeAnchorPoint= this.anchors.getResizeAnchorPoint(x,y);
		this.cetaX=resizeAnchorPoint.getX();
		this.cetaY=resizeAnchorPoint.getY();
	}

	@Override
	public void keepTransforming(int x, int y) {
		this.getResizeScale(x,y);
		this.affineTransform.translate(cetaX, cetaY);
		this.affineTransform.scale(this.xScale, this.yScale);
		this.affineTransform.translate(-cetaX, -cetaY);
		this.px=x;
		this.py=y;
	}

	@Override
	public void finalize(int x, int y) {
		this.selectedShape.setShape(affineTransform.createTransformedShape(this.selectedShape.getShape()));
		this.affineTransform.setToIdentity();
	}
	
	private void getResizeScale(int x, int y) {
		EAnchors eResizeAncchor =this.anchors.geteResizeEAnchor();
		double w1=px-cetaX;
		double w2=x-cetaX;
		double h1=py-cetaY;
		double h2=y-cetaY;
		
		this.xScale=1;
		this.yScale=1;
		switch (eResizeAncchor) {
		case eNW:
			xScale=w2/w1; yScale=h2/h1;
			break;
		case eWW:
			xScale=w2/w1;
			break;
		case eSW:
			xScale=w2/w1; yScale=h2/h1;
			break;
		case eSS:
			yScale=h2/h1;
			break;
		case eSE:
			xScale=w2/w1; yScale=h2/h1;
			break;
		case eEE:
			xScale=w2/w1;
			break;
		case eNE:
			xScale=w2/w1; yScale=h2/h1;
			break;
		case eNN:
			yScale=h2/h1;
			break;
		default: 
			break;
		}
	}
	
}
