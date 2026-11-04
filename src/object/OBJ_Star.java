package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Star extends Entity{
	
	GamePanel gp;
	
	public OBJ_Star(GamePanel gp) {
		super(gp);
		this.gp=gp;
		
		type = type_pickupOnly;
		name = "Star";
		down1 = setup("/objects/star",gp.tileSize,gp.tileSize);
		value = 1;
	}
	public void use(Entity entity) {
		
		gp.playSE(1);
		gp.ui.addMessage("You collected the " + name + "!");
		entity.hasStar = true;
	}
}
