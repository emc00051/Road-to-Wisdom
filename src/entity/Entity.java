package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class Entity {
	
	GamePanel gp;
	public BufferedImage up1,up2,up3,up4,down1,down2,down3,down4,right1,
		right2,right3,right4,left1,left2,left3,left4;
	public BufferedImage dying1,dying2,dying3,dying4,dying5,dying6,dying7,
		dying8,dying9,dying10,dying11,dying12,dying13,dying14,dying15,dying16,
		dying17,dying18,dying19,dying20;
	public BufferedImage image,image2,image3;
	public BufferedImage attackUp,attackDown,attackRight,attackLeft;
	public Rectangle solidArea = new Rectangle(6,0,36,36);
	public Rectangle attackArea = new Rectangle(0,0,0,0);
	public String[] dialogues = new String[20];
	public int solidAreaDefaultX,solidAreaDefaultY;
	public boolean collision = false;
	
	// STATE
	public String direction="down";
	public int worldX,worldY;
	public int spriteNum=1;
	public int introEndDialogue=0;
	public int dialogueIndex=0;
	public boolean collisionOn = false;
	public boolean invincible=false;
	boolean attacking = false;
	public boolean alive = true;
	public boolean dying = false;
	boolean hpBarOn=false;
	public boolean onPath = false;
	public int pathIndex=0;
	public boolean knockback=false;
	
	
	// COUNTER
	public int actionLockCounter=0;
	public int spriteCounter=0;
	public int invincibleCounter=0;
	int dyingCounter = 0;
	int hpBarCounter=0;
	int knockbackCounter=0;
	int standCounter=0;
	
	// ITEM ATTRIBUTES
	public int value;
	public boolean hasCrest = false;
	public boolean hasCircle = false;
	public boolean hasLyre = false;
	public boolean hasStar = false;
	
	// CHARACTER ATTRIBUTES
	public int type; // 0 is player, 1 is npc, 2 is enemy
	public final int type_player=0;
	public final int type_npc=1;
	public final int type_enemy=2;
	public final int type_pickupOnly=3;
	public final int type_solidOnly=4;
	public int defaultSpeed;
	public int speed;
	public int maxLife;
	public int life;
	public String name;
	
	
	public Entity(GamePanel gp) {
		this.gp=gp;
		solidArea.width=gp.tileSize-12;
		solidArea.height=gp.tileSize-12;
		solidArea.x=solidAreaDefaultX-6;
	}
	public void setAction() {}
	public void damageReaction() {}
	public void speak() {
		
		if (dialogues[dialogueIndex]==null) {
			dialogueIndex=introEndDialogue-1;
		}
		gp.ui.currentDialogue=dialogues[dialogueIndex];
		dialogueIndex++;
		
		switch(gp.player.direction) {
		case "up":
			direction="down";
			break;
		case "down":
			direction="up";
			break;
		}
		
	}
	public void use(Entity entity) {}
	public void	checkDrop() {}
	public void dropItem(Entity droppedItem) {
		for (int i=0;i<gp.obj[1].length;i++) {
			if (gp.obj[gp.currentMap][i]==null) {
				gp.obj[gp.currentMap][i]=droppedItem;
				gp.obj[gp.currentMap][i].worldX=worldX; // Dead enemy's WorldX
				gp.obj[gp.currentMap][i].worldY=worldY; // Dead enemy's WorldY
				break;
			}
		}
	}
	
	public void checkCollision() {
		collisionOn=false;
		gp.cChecker.checkTile(this);
		gp.cChecker.checkObject(this, false);
		gp.cChecker.checkEntity(this, gp.npc);
		gp.cChecker.checkEntity(this, gp.enemy);
		boolean contactPlayer=gp.cChecker.checkPlayer(this);
		
		if (this.type==2&&contactPlayer==true) {
			if (gp.player.invincible==false) {
				gp.player.life--;
				gp.player.invincible=true;
			}
		}
	}
	
	public void update() {
		
//		if (knockback==true) {
//			checkCollision();
//			if(collisionOn==true) {
//				knockbackCounter=0;
//				knockback=false;
//				speed=defaultSpeed;
//			}
//			else if (collisionOn==false) {
//				switch(gp.player.direction) {
//				case "up": worldY-=speed;break;
//				case "down": worldY+=speed; break;
//				case "left": worldX-=speed; break;
//				case "right": worldX+=speed; break;
//				}
//			}
//			knockbackCounter++;
//			if(knockbackCounter==10) {
//				knockbackCounter=0;
//				knockback=false;
//				speed=defaultSpeed;
//			}
//		}
//		else {
			setAction();
			checkCollision();
			
			// IF COLLISION IS FALSE, PLAYER CAN MOVE 
			if (collisionOn==false) {
				switch(direction) {
				case "up": worldY-=speed;break;
				case "down": worldY+=speed; break;
				case "left": worldX-=speed; break;
				case "right": worldX+=speed; break;
				}
			}
//		}		
		spriteCounter++;
		if (spriteCounter>12) {
			if (spriteNum==1) {
				spriteNum=2;
			}
			else if (spriteNum==2) {
				spriteNum=1;
			}
			spriteCounter=0;
		}
		
		if (invincible==true) {
			invincibleCounter++;
			if (invincibleCounter>40) {
				invincible=false;
				invincibleCounter=0;
			}
		}
	}	
	public void draw(Graphics2D g2) {

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		BufferedImage image = null;
		int screenX=worldX-gp.player.worldX+gp.player.screenX;
		int screenY=worldY-gp.player.worldY+gp.player.screenY;
		
		if (worldX+gp.tileSize>gp.player.worldX-gp.player.screenX&&
			worldX-gp.tileSize<gp.player.worldX+gp.player.screenX&&
			worldY+gp.tileSize>gp.player.worldY-gp.player.screenY&&
			worldY-gp.tileSize<gp.player.worldY+gp.player.screenY) {
			
			switch(direction) {
			case "up":
				if (spriteNum==1) image=up1;
				if (spriteNum==2) image=up2;
				if (spriteNum==3) image=up3;
				if (spriteNum==4) image=up4;
				break;
			case "down":
				if (spriteNum==1) image=down1;
				if (spriteNum==2) image=down2;
				if (spriteNum==3) image=down3;
				if (spriteNum==4) image=down4;
				break;
			case "left":
				if (spriteNum==1) image=left1;
				if (spriteNum==2) image=left2;
				if (spriteNum==3) image=left3;
				if (spriteNum==4) image=left4;
				break;
			case "right":
				if (spriteNum==1) image=right1;
				if (spriteNum==2) image=right2;
				if (spriteNum==3) image=right3;
				if (spriteNum==4) image=right4;
				break;
			}
			
			// Enemy HP Bar
			if(type==2&&hpBarOn==true){
				
				double oneScale=(double)gp.tileSize/maxLife;
				double hpBarValue=oneScale*life;
				
				g2.setColor(new Color(35,35,35));
				g2.fillRect(screenX-1, screenY-gp.tileSize/4-1, gp.tileSize+2, gp.tileSize/6+2);
				
				g2.setColor(new Color(255,0,30));
				g2.fillRect(screenX, screenY-gp.tileSize/4, (int)hpBarValue, gp.tileSize/6);
				
				hpBarCounter++;
				
				if (hpBarCounter>600) {
					hpBarCounter=0;
					hpBarOn=false;
				}
			}
			
			if (invincible==true) {
				hpBarOn=true;
				hpBarCounter=0;
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.4f));
			}
			
			if (dying == true) {
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
				dyingAnimation(g2);
			}
			
			if (alive==true&&dying==false) {
				g2.drawImage(image, screenX, screenY, null);
			}
			
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
		}		
	}
	public void dyingAnimation(Graphics2D g2) {
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int screenX=worldX-gp.player.worldX+gp.player.screenX;
		int screenY=worldY-gp.player.worldY+gp.player.screenY;
		
		dyingCounter++;
		
		int i = 5;
		
		if (dyingCounter<=i) {changeDying(g2,dying1, screenX, screenY);}
		if (dyingCounter>i&&dyingCounter<=i*2) {changeDying(g2,dying2, screenX, screenY);}
		if (dyingCounter>i*2&&dyingCounter<=i*3) {changeDying(g2,dying3, screenX, screenY);}
		if (dyingCounter>i*3&&dyingCounter<=i*4) {changeDying(g2,dying4, screenX, screenY);}
		if (dyingCounter>i*4&&dyingCounter<=i*5) {changeDying(g2,dying5, screenX, screenY);}
		if (dyingCounter>i*5&&dyingCounter<=i*6) {changeDying(g2,dying6, screenX, screenY);}
		if (dyingCounter>i*6&&dyingCounter<=i*7) {changeDying(g2,dying7, screenX, screenY);}
		if (dyingCounter>i*7&&dyingCounter<=i*8) {changeDying(g2,dying8, screenX, screenY);}
		if (dyingCounter>i*8&&dyingCounter<=i*9) {changeDying(g2,dying9, screenX, screenY);}
		if (dyingCounter>i*9&&dyingCounter<=i*10) {changeDying(g2,dying10, screenX, screenY);}
		if (dyingCounter>i*10&&dyingCounter<=i*11) {changeDying(g2,dying11, screenX, screenY);}
		if (dyingCounter>i*11&&dyingCounter<=i*12) {changeDying(g2,dying12, screenX, screenY);}
		if (dyingCounter>i*12&&dyingCounter<=i*13) {changeDying(g2,dying13, screenX, screenY);}
		if (dyingCounter>i*13&&dyingCounter<=i*14) {changeDying(g2,dying14, screenX, screenY);}
		if (dyingCounter>i*14&&dyingCounter<=i*15) {changeDying(g2,dying15, screenX, screenY);}
		if (dyingCounter>i*15&&dyingCounter<=i*16) {changeDying(g2,dying16, screenX, screenY);}
		if (dyingCounter>i*16&&dyingCounter<=i*17) {changeDying(g2,dying17, screenX, screenY);}
		if (dyingCounter>i*17&&dyingCounter<=i*18) {changeDying(g2,dying18, screenX, screenY);}
		if (dyingCounter>i*18&&dyingCounter<=i*19) {changeDying(g2,dying19, screenX, screenY);}
		if (dyingCounter>i*19&&dyingCounter<=i*20) {changeDying(g2,dying20, screenX, screenY);}
		if (dyingCounter>i*20) {
			alive=false;
		}
	}
	public void changeDying(Graphics2D g2,BufferedImage image, int screenX,int screenY) {
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
	}
	public BufferedImage setup(String imageName,int width, int height) {
		UtilityTool uTool = new UtilityTool();
		BufferedImage image=null;
		
		try {
			image=ImageIO.read(getClass().getResourceAsStream(imageName+".png"));
			image=uTool.scaleImage(image, width, height);
			
		} catch (IOException e) {
			System.err.println("Failed to load tile: /tiles/" + imageName + ".png");
			e.printStackTrace();
		}
		
		return image;
	}
	public void searchPath(int goalCol, int goalRow) {
		int startCol=(worldX+solidArea.x)/gp.tileSize;
		int startRow=(worldY+solidArea.y)/gp.tileSize;
		
		gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow);
		
		if (gp.pFinder.search()==true){
			int nextX = gp.pFinder.pathList.get(0).col*gp.tileSize;
			int nextY = gp.pFinder.pathList.get(0).row*gp.tileSize;
			int enLeftX = worldX+solidArea.x;
			int enRightX = worldX+solidArea.x+solidArea.width;
			int enTopY = worldY+solidArea.y;
			int enBottomY = worldY+solidArea.y+solidArea.height;
			
			if(enTopY>nextY&&enLeftX>=nextX&&enRightX<nextX+gp.tileSize) {
				direction = "up";
			}
			else if(enTopY<nextY&&enLeftX>=nextX&&enRightX<nextX+gp.tileSize) {
				direction = "down";
			}
			else if(enTopY>=nextY&&enBottomY<nextY+gp.tileSize) {
				if(enLeftX>nextX) {
					direction="left";
				}
				if(enLeftX<nextX) {
					direction="right";
				}
			}
			else if(enTopY>nextY&&enLeftX>nextX) {
				direction = "up";
				checkCollision();
				if(collision==true) {
					direction = "left";
				}
			}
			else if(enTopY>nextY&&enLeftX<nextX) {
				direction = "up";
				checkCollision();
				if(collision==true) {
					direction = "right";
				}
			}
			else if(enTopY<nextY&&enLeftX>nextX) {
				direction = "down";
				checkCollision();
				if(collision==true) {
					direction = "left";
				}
			}
			else if(enTopY<nextY&&enLeftX< nextX) {
				direction = "down";
				checkCollision();
				if(collision==true) {
					direction = "right";
				}
			}
//			int nextCol =gp.pFinder.pathList.get(0).col;
//			int nextRow =gp.pFinder.pathList.get(0).row;
//			if(nextCol==goalCol&&nextRow==goalRow) {
//				onPath = false;
//			}
		}
	}
}
