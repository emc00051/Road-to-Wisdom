package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Piano extends Entity{
	
	GamePanel gp;
	
	public OBJ_Piano(GamePanel gp) {
		super(gp);
		this.gp=gp;
		
		type = type_solidOnly;
		name = "Piano";
		down1 = setup("/objects/piano",gp.tileSize*3,gp.tileSize*3);
		value = 1;
		collisionOn=true;
	}
	public void use(Entity entity) {
		
	}
}
