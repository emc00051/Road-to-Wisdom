package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Key extends Entity{
	
	GamePanel gp;
	
	public OBJ_Key(GamePanel gp) {
		super(gp);
		
		name = "Key";
		down1=setup("/objects/key",gp.tileSize,gp.tileSize);
		
	}
	public void use(Entity entity) {
		gp.gameState=gp.dialogueState;
		gp.ui.currentDialogue = "";
		
	}
}
