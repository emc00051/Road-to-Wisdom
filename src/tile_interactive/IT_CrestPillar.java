package tile_interactive;

import entity.Entity;
import main.GamePanel;

public class IT_CrestPillar extends InteractiveTile{
	
	GamePanel gp;
	
	public IT_CrestPillar(GamePanel gp,int col, int row) {
		super(gp,col,row);
		this.gp=gp;
		
		this.worldX=gp.tileSize*col;
		this.worldY=gp.tileSize*row;
		
		down1=setup("/tilesInteractive/crestPillarUnfilled",gp.tileSize,gp.tileSize);
		destructible=true;
	}
	public boolean isCorrectItem(Entity entity) {
		boolean isCorrectItem = false;
		if(entity.hasCrest==true) {
			isCorrectItem=true;
		}
		return isCorrectItem;
	}
	
}
