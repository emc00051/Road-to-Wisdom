package tile_interactive;

import entity.Entity;
import main.GamePanel;

public class IT_Flower extends InteractiveTile{
	
	GamePanel gp;
	
	public IT_Flower(GamePanel gp,int col, int row) {
		super(gp,col,row);
		this.gp=gp;
		
		this.worldX=gp.tileSize*col;
		this.worldY=gp.tileSize*row;
		
		down1=setup("/tilesInteractive/flower",gp.tileSize,gp.tileSize);
		destructible=false;
	}
}
