package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Circle extends Entity{
	
	GamePanel gp;
	
	public OBJ_Circle(GamePanel gp) {
		super(gp);
		this.gp=gp;
		
		type = type_pickupOnly;
		name = "Circle";
		down1 = setup("/objects/circle",gp.tileSize,gp.tileSize);
		value = 1;
	}
	public void use(Entity entity) {
		
		gp.playSE(1);
		gp.ui.addMessage("You collected the " + name + "!");
		entity.hasCircle = true;
	}
}
