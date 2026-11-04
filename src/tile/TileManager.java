package tile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class TileManager {
	
	GamePanel gp;
	public Tile[] tile;
	public int mapTileNum[][][];
	boolean drawPath = true;
	
	public TileManager(GamePanel gp) {
		
		this.gp=gp;
		
		tile = new Tile[100];
		mapTileNum = new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];
		
		getTileImage();
		loadMap("/maps/starterIsland.txt",0);
		
	}
	
	public void getTileImage() {
		setup(0,"void",true);
		setup(1,"aSky",true);
		setup(2,"bigCloud01",true);
		setup(3,"bigCloud02",true);
		setup(4,"bigCloud03",true);
		setup(5,"bottomPillar",true);
		setup(6,"circlePillarFilled",true);
		setup(7,"circlePillarUnfilled",true);
		setup(8,"coatOfArmsLit01",false);
		setup(9,"coatOfArmsLit02",false);
		setup(10,"coatOfArmsLit03",false);
		setup(11,"coatOfArmsLit04",false);
		setup(12,"coatOfArmsLit05",false);
		setup(13,"coatOfArmsLit06",false);
		setup(14,"coatOfArmsLit07",false);
		setup(15,"coatOfArmsLit08",false);
		setup(16,"coatOfArmsLit09",false);
		setup(17,"crestPillarFilled",true);
		setup(18,"crestPillarUnfilled",true);
		setup(19,"lyrePillarFilled",true);
		setup(20,"lyrePillarUnfilled",true);
		setup(21,"mediumCloud01",true);
		setup(22,"mediumCloud02",true);
		setup(23,"mediumCloud03",true);
		setup(24,"skyBricks01",false);
		setup(25,"skyBricks02",false);
		setup(26,"skyBricks03",false);
		setup(27,"skyBricks04",false);
		setup(28,"skyBricks05",false);
		setup(29,"skyBricks06",false);
		setup(30,"skyBricks07",true);
		setup(31,"skyBricks08",true);
		setup(32,"skyBricks09",true);
		setup(33,"skyPath01",false);
		setup(34,"skyPath02",false);
		setup(35,"skyPath03",false);
		setup(36,"skyPath04",false);
		setup(37,"skyPath05",false);
		setup(38,"skyPath06",false);
		setup(39,"skyPath07",false);
		setup(40,"skyPath08",false);
		setup(41,"skyPath09",false);
		setup(42,"skyPath10",false);
		setup(43,"skyPath11",false);
		setup(44,"skyPath12",false);
		setup(45,"skyPath13",false);
		setup(46,"skyStairs01",false);
		setup(47,"skyStairs02",false);
		setup(48,"skyStairs03",false);
		setup(49,"smallCloud01",true);
		setup(50,"smallCloud02",true);
		setup(51,"starPillarFilled",true);
		setup(52,"starPillarUnfilled",true);
		setup(53,"topPillar",true);
		setup(54,"trialBricks01",true); // 53
		setup(55,"trialBricks02",true);
		setup(56,"trialBricks03",true);
		setup(57,"trialBricks04",true);
		setup(58,"trialBricks05",true);
		setup(59,"trialBricks06",true);
		setup(60,"trialBricks07",true);
		setup(61,"trialBricks08",true);
		setup(62,"trialBricks09",true);
		setup(63,"trialBricks10",true);
		setup(64,"trialBricks11",true);
		setup(65,"trialBricks12",true);
		setup(66,"trialBricks13",true);
		setup(67,"trialPath01",false);
	}
	
	public void setup(int index, String imageName, boolean collision) {
		
		UtilityTool uTool = new UtilityTool();
		
		try {
			tile[index] = new Tile();
			tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName+".png"));
			tile[index].image=uTool.scaleImage(tile[index].image,gp.tileSize,gp.tileSize);
			tile[index].collision=collision;
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void loadMap(String filePath,int map) {
		try {
			
			InputStream is = getClass().getResourceAsStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			int col=0;
			int row=0;
			while(col<gp.maxWorldCol&&row<gp.maxWorldRow) {
				
				String line = br.readLine();
				
				while(col<gp.maxWorldCol) {
					
					String numbers[]=line.split(" ");
					
					int num = Integer.parseInt(numbers[col]);
					
					mapTileNum[map][col][row]=num;
					col++;
					
					if (col == gp.maxWorldCol) {
						col=0;
						row++;
						break;
					}
				}
			}
			br.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g2) {
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int worldCol = 0;
		int worldRow = 0;
		while(worldCol<gp.maxWorldCol&& worldRow<gp.maxWorldRow) {
			
			int tileNum=mapTileNum[gp.currentMap][worldCol][worldRow];
			
			int worldX = worldCol*gp.tileSize;
			int worldY = worldRow*gp.tileSize;
			int screenX=worldX-gp.player.worldX+gp.player.screenX;
			int screenY=worldY-gp.player.worldY+gp.player.screenY;
			
			if (worldX+gp.tileSize>gp.player.worldX-gp.player.screenX&&
				worldX-gp.tileSize<gp.player.worldX+gp.player.screenX&&
				worldY+gp.tileSize>gp.player.worldY-gp.player.screenY&&
				worldY-gp.tileSize<gp.player.worldY+gp.player.screenY) {
				if (tileNum >= 0 && tileNum < tile.length && tile[tileNum] != null) {
					g2.drawImage(tile[tileNum].image, screenX, screenY, null);
				}
			}
			worldCol++;
			
			if(worldCol==gp.maxWorldCol) {
				worldCol=0;
				worldRow++;
			}
		}
		// Debug Color
//		if (drawPath ==true) {
//			g2.setColor(new Color(255,0,0,70));
//			for(int i=0;i<gp.pFinder.pathList.size();i++) {
//				int worldX = gp.pFinder.pathList.get(i).col*gp.tileSize;
//				int worldY = gp.pFinder.pathList.get(i).row*gp.tileSize;
//				int screenX=worldX-gp.player.worldX+gp.player.screenX;
//				int screenY=worldY-gp.player.worldY+gp.player.screenY;
//				
//				g2.fillRect(screenX,screenY,gp.tileSize,gp.tileSize);
//			}
//		}
	}
}
