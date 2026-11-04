package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Lyre extends Entity{
	
	GamePanel gp;
	
	public OBJ_Lyre(GamePanel gp) {
		super(gp);
		this.gp=gp;
		
		type = type_pickupOnly;
		name = "Lyre";
		down1 = setup("/objects/lyre",gp.tileSize,gp.tileSize);
		value = 1;
	}
	public void use(Entity entity) {
		
		gp.playSE(1);
		gp.ui.addMessage("You collected the " + name + "!");
		entity.hasLyre = true;
	}
}
