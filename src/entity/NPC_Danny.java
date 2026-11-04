package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class NPC_Danny extends Entity{
	
	public NPC_Danny(GamePanel gp) {
		super(gp);
		
		direction="down";
		speed=4;
		
		getImage();
		setDialogue();
	}
	
	public void getImage() {
		
		up1 = setup("/npc/dannyUp01",gp.tileSize,gp.tileSize);
		up2 = setup("/npc/dannyUp02",gp.tileSize,gp.tileSize);
		up3 = setup("/npc/dannyUp03",gp.tileSize,gp.tileSize);
		down1 = setup("/npc/dannyDown01",gp.tileSize,gp.tileSize);
		down2 = setup("/npc/dannyDown02",gp.tileSize,gp.tileSize);
		down3 = setup("/npc/dannyDown03",gp.tileSize,gp.tileSize);
		left1 = setup("/npc/dannyLeft01",gp.tileSize,gp.tileSize);
		left2 = setup("/npc/dannyLeft02",gp.tileSize,gp.tileSize);
		right1 = setup("/npc/dannyRight01",gp.tileSize,gp.tileSize);
		right2 = setup("/npc/dannyRight02",gp.tileSize,gp.tileSize);
	}
	
	public void setDialogue() {
		introEndDialogue=2;
		dialogues[0]="Yo K, I'm Danny, thanks for saving me!";
		dialogues[1]="Can you lead me and Seth back to the Island? Thanks.";
		dialogues[2]=null;

	}
	
	public void setAction() {
		
		if(onPath==true&&gp.currentMap==1) {
			int goalCol=(gp.player.worldX+gp.player.solidArea.x)/gp.tileSize;;
			int goalRow=(gp.player.worldY+gp.player.solidArea.y)/gp.tileSize;
			searchPath(goalCol,goalRow);
		}
		
	}
	
	public void speak() {
		
		// Do this character specific stuff
		if(gp.player.hasCrest==true) {
			super.speak();
			if(dialogueIndex==introEndDialogue) {
				onPath = true;
				gp.player.introDigalogueDone=true;
			}
		}
	}
	public void draw(Graphics2D g2) {
		
		BufferedImage image = null;
		int screenX=worldX-gp.player.worldX+gp.player.screenX;
		int screenY=worldY-gp.player.worldY+gp.player.screenY;
		
		if (worldX+gp.tileSize>gp.player.worldX-gp.player.screenX&&
			worldX-gp.tileSize<gp.player.worldX+gp.player.screenX&&
			worldY+gp.tileSize>gp.player.worldY-gp.player.screenY&&
			worldY-gp.tileSize<gp.player.worldY+gp.player.screenY) {
			
			switch(direction) {
			case "up":
				if (spriteNum==1)
					image=up1;
				if (spriteNum==2)
					image=up2;
				if (spriteNum==3)
					image=up3;
				break;
			case "down":
				if (spriteNum==1)
					image=down1;
				if (spriteNum==2)
					image=down2;
				if (spriteNum==3)
					image=down3;
				break;
			case "left":
				if (spriteNum==1)
					image=left1;
				if (spriteNum==2)
					image=left2;
				if (spriteNum==3)
					image=left1;
				break;
			case "right":
				if (spriteNum==1)
					image=right1;
				if (spriteNum==2)
					image=right2;
				if (spriteNum==3)
					image=right1;
				break;
			}
			
			g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
		}
	}

	public void update() {
		setAction();
		
		collisionOn=false;
		gp.cChecker.checkObject(this, false);
		gp.cChecker.checkTile(this);
		gp.cChecker.checkPlayer(this);
		
		// IF COLLISION IS FALSE, PLAYER CAN MOVE 
		if (collisionOn==false&&onPath==true&&gp.currentMap==1) {
			switch(direction) {
			case "up": worldY-=speed;break;
			case "down": worldY+=speed; break;
			case "left": worldX-=speed; break;
			case "right": worldX+=speed; break;
			}
		}
		if(gp.player.hasCrest==true) {
			spriteCounter++;
			if (spriteCounter>12) {
				if (spriteNum==1) {
					spriteNum=2;
				}
				else if (spriteNum==2) {
					spriteNum=3;
				}
				else if (spriteNum==3) {
					spriteNum=1;
				}
				spriteCounter=0;
			}
		}
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
				checkCollision();
				direction = "up";
			}
			else if(enTopY<nextY&&enLeftX>=nextX&&enRightX<nextX+gp.tileSize) {
				checkCollision();
				direction = "down";
			}
			else if(enTopY>=nextY&&enBottomY<nextY+gp.tileSize) {
				if(enLeftX>nextX) {
					checkCollision();
					direction="left";
				}
				if(enLeftX<nextX) {
					checkCollision();
					direction="right";
				}
			}
			else if(enTopY>nextY&&enLeftX<nextX) {
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
			else if(enTopY<nextY&&enLeftX<nextX) {
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
//				direction ="down";
//			}
		}
	}
	
}
