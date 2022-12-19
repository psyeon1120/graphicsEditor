package shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.Serializable;

import global.Constants.EAnchors;

public class TAnchors implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final static int WIDTH = 10;
	private final static int HEIGHT = 10;
	
	private Ellipse2D anchors[];
	protected EAnchors eSelectedEAnchor;
	protected EAnchors eResizeEAnchor;
	
	public EAnchors geteSelectedEAnchor() {
		return eSelectedEAnchor;
	}

	public void seteSelectedEAnchor(EAnchors eSelectedEAnchor) {
		this.eSelectedEAnchor = eSelectedEAnchor;
	}

	public EAnchors geteResizeEAnchor() {
		return eResizeEAnchor;
	}

	public void seteResizeEAnchor(EAnchors eResizeEAnchor) {
		this.eResizeEAnchor = eResizeEAnchor;
	}

	public TAnchors() {
		this.anchors = new Ellipse2D.Double[EAnchors.values().length];
		this.eSelectedEAnchor=EAnchors.eMove;
		for(EAnchors eAnchor: EAnchors.values()) {
			this.anchors[eAnchor.ordinal()] = new Ellipse2D.Double();
		}
	}
	
	public void draw(Graphics2D g2, Rectangle BoundingRectangle) {
		for (int i=0; i<EAnchors.values().length-1; i++) {
			int x = BoundingRectangle.x-WIDTH/2;
			int y = BoundingRectangle.y-HEIGHT/2;
			int w = BoundingRectangle.width;
			int h = BoundingRectangle.height;
			
			EAnchors eAnchor = EAnchors.values()[i];
			switch (eAnchor) {
			case eNW:
				break;
			case eWW:
				y = y + h/2;
				break;
			case eSW:
				y = y + h;
				break;
			case eSS:
				x = x + w/2;
				y = y + h;
				break;
			case eSE:
				x = x + w;
				y = y + h;
				break;
			case eEE:
				x = x + w;
				y = y + h/2;
				break;
			case eNE:
				x = x + w;
				break;
			case eNN:
				x = x + w/2;
				break;
			case eRR:
				x = x + w/2;
				y = y - 30;
				break;
			default: 
				break;
			}
			this.anchors[eAnchor.ordinal()].setFrame(x, y, WIDTH, HEIGHT);
			g2.setColor(Color.WHITE);
			g2.fill(this.anchors[eAnchor.ordinal()]);
			g2.setColor(Color.BLACK);
			g2.draw(this.anchors[eAnchor.ordinal()]);
		}
	}

	public boolean contains(int x, int y) {
		for(int i=0;i<EAnchors.values().length-1;i++) {
			if (this.anchors[i].contains(x, y)) {
				this.eSelectedEAnchor = EAnchors.values()[i];
				return true;
			}
		}
		return false;
	}
	
	public Ellipse2D getAnchor(EAnchors anchor) {
		for(EAnchors eAnchors:EAnchors.values()) {
			if(eAnchors.name()==anchor.name())
				return this.anchors[eAnchors.ordinal()];
		}
		return null;
	}

	public Point2D getResizeAnchorPoint(int x, int y) {
		switch (this.eSelectedEAnchor) {
		case eNW:
			this.eResizeEAnchor=EAnchors.eSE;
			break;
		case eWW:
			this.eResizeEAnchor=EAnchors.eEE;
			break;
		case eSW:
			this.eResizeEAnchor=EAnchors.eNE;
			break;
		case eSS:
			this.eResizeEAnchor=EAnchors.eNN;
			break;
		case eSE:
			this.eResizeEAnchor=EAnchors.eNW;
			break;
		case eEE:
			this.eResizeEAnchor=EAnchors.eWW;
			break;
		case eNE:
			this.eResizeEAnchor=EAnchors.eSW;
			break;
		case eNN:
			this.eResizeEAnchor=EAnchors.eSS;
			break;
		default: 
			break;
		}
		Point2D point = new Point2D.Double(this.anchors[this.eResizeEAnchor.ordinal()].getCenterX(),
				this.anchors[this.eResizeEAnchor.ordinal()].getCenterY());
		return point;
	}

}
