package shapes;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.io.Serializable;

import global.Constants.EAnchors;

abstract public class TShape implements Serializable{
	private static final long serialVersionUID = 1L;
	//component
	protected Shape shape;
	protected Shape transformedShape;
	protected AffineTransform affineTransform;
	protected TAnchors anchors;
	//attribute
	protected Color lineColor, fillColor;
	protected int stroke;
	protected float[] dashes;
	protected boolean bSelected;
	protected boolean bTransforming;
	protected int startX,startY;
	
	public TShape(){
		this.dashes =null;
		this.stroke=0;
		this.lineColor=Color.black;
		this.fillColor=Color.white;
		this.bSelected=false;
		this.bTransforming=false;
		this.anchors=new TAnchors();
		this.affineTransform=new AffineTransform();
		this.affineTransform.setToIdentity();
	}
	
	public TShape(Shape shape, Shape transformedShape, AffineTransform affineTransform,TAnchors anchors) {
		this.shape=shape;
		this.transformedShape=transformedShape;
		this.affineTransform=affineTransform;
		this.anchors=anchors;
		this.bSelected=false;
		this.bTransforming=false;
		this.dashes = null;
		this.lineColor=Color.black;
		this.fillColor=Color.white;
		this.dashes =null;
		this.stroke=0;
	}
	
	public abstract TShape cloneNew();
	public abstract TShape clone();
	
	public Shape getShape() {
		return this.shape;
	}

	public void setShape(Shape shape) {
		this.shape=shape;
	}

	public void setShapeToAffine() {
		this.shape=this.affineTransform.createTransformedShape(this.shape);
	}

	public TAnchors getAnchors() {
		return anchors;
	}
	
	public Shape getTransformedShape() {
		return this.transformedShape;
	}
	
	public AffineTransform getAffineTransform() {
		return this.affineTransform;
	}
	
	public void setAffineTransform(AffineTransform affineTransform) {
		this.affineTransform=affineTransform;
	}
	
	public EAnchors geteSelectedEAnchor() {
		return this.anchors.geteSelectedEAnchor();
	}
	
	public TAnchors geteSelectedTAnchor() {
		return this.anchors;
	}
	
	public boolean isSelected() {
		return this.bSelected;
	}
	
	public void setSelected(boolean bSelected) {
		this.bSelected=bSelected;
	}

	public boolean isbTransforming() {
		return bTransforming;
	}

	public void setbTransforming(boolean bTransforming) {
		this.bTransforming = bTransforming;
	}
	
	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}
	public void setStroke(int stroke) {
		this.stroke=stroke;
	}
	public void setDash(float[] dash) {
		this.dashes = dash;
	}

	public void setTransformedShape() {
		this.transformedShape=this.affineTransform.createTransformedShape(this.shape);
	}

	public double getCenterX() {
		return this.shape.getBounds2D().getCenterX();
	}

	public double getCenterY() {
		return this.shape.getBounds2D().getCenterY();
	}
		
	public abstract void prepareDrawing(int x,int y);
	public abstract void keepDrawing(int x, int y);

	public void drawTransforming(Graphics2D g2) {
		setTransformedShape();
		if (this.stroke != 0) {
			g2.setStroke(new BasicStroke(this.stroke));
		}
		if(this.dashes!=null) {
			g2.setStroke(new BasicStroke(this.stroke, BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_MITER, 1.0f, this.dashes, 0.0f));
		}
		if (this.fillColor != null) {
			g2.setColor(this.fillColor);
			g2.fill(transformedShape);
		}
		if(this.lineColor!=null) g2.setColor(this.lineColor);
		
		g2.draw(transformedShape);
		g2.setStroke(new BasicStroke(1));
		
		if(this.bSelected&&!this.bTransforming) {
			this.anchors.draw(g2, transformedShape.getBounds());
		}
	}
	public void draw(Graphics2D g2) {
		if (this.stroke != 0) {
			g2.setStroke(new BasicStroke(this.stroke));
		}
		if(this.dashes!=null) {
			g2.setStroke(new BasicStroke(this.stroke, BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_MITER, 1.0f, this.dashes, 0.0f));
		}
		if (this.fillColor != null) {
			g2.setColor(this.fillColor);
			g2.fill(shape);
		}
		if(this.lineColor!=null) g2.setColor(this.lineColor);
		
		g2.draw(shape);
		g2.setStroke(new BasicStroke(1));
		
		if(this.bSelected&&!this.bTransforming) {
			this.anchors.draw(g2, shape.getBounds());
		}
	}
	
	public void drawAnchor(Graphics2D g2) {
		this.anchors.draw(g2, shape.getBounds());
	}
	
	public boolean contains(int x, int y) {
		if(this.bSelected) {
			if(this.anchors.contains(x,y)) {
				return true;
			}
		}
		if(shape.contains(x, y)) {
			this.anchors.seteSelectedEAnchor(EAnchors.eMove);
			return true;
		}
		return false;
	}
	
	public void addPoint(int x, int y) {}
	
	public int isPointOpposite(int x, int y) {
		if(startX >= x && startY >= y) return 2;
		else if(startX >= x) return 1;
		else if(startY >= y) return 0;
		else return -1;
	}
}
