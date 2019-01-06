import java.awt.Color;
import java.awt.Rectangle;

public class HorizontalFlashingEnemy extends Enemy {
	
	private int xSpeed;
	private int screenWidth;
	private boolean active;

	
	public HorizontalFlashingEnemy(int x, int y, int w, int h, int xS, int sW) {
		
		super(x,y,w,h);
		xSpeed = xS;
		screenWidth = sW;
		active = true;
		
	}
	
	@Override
    public Color getColor() {
		if(this.getActive()) {
			return Color.ORANGE;
		} else {
			return Color.WHITE;
		}
        
    }
	
	@Override
    public void move() {
    	Rectangle rect = getRectangle();
    	rect.x += xSpeed;
    	if(rect.x <= 0 || (rect.x + rect.width) >= screenWidth) {
    		xSpeed = xSpeed * -1;
    	}
    }

	public boolean getActive() {
    	return active;
    }
    
    public void setActive(boolean stuff) {
    	this.active = stuff;
    }
	
}
