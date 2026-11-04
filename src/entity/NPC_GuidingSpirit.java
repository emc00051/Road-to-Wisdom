package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class NPC_GuidingSpirit extends Entity{
	
	public NPC_GuidingSpirit(GamePanel gp) {
		super(gp);
		
		direction="down";
		speed=2;
		
		getImage();
		setDialogue();
	}
	
	public void getImage() {
		
		up1 = setup("/npc/guidingSpiritBack01",gp.tileSize,gp.tileSize);
		up2 = setup("/npc/guidingSpiritBack02",gp.tileSize,gp.tileSize);
		up3 = setup("/npc/guidingSpiritBack03",gp.tileSize,gp.tileSize);
		down1 = setup("/npc/guidingSpiritFront01",gp.tileSize,gp.tileSize);
		down2 = setup("/npc/guidingSpiritFront02",gp.tileSize,gp.tileSize);
		down3 = setup("/npc/guidingSpiritFront03",gp.tileSize,gp.tileSize);
	}
	
	public void setDialogue() {
		introEndDialogue=4;
		dialogues[0]="Hello, K.";
		dialogues[1]="You may not realize where you are, but \nI am in great need for your help.";
		dialogues[2]="You must travel to the world down below \nus, and complete 4 challenges, each \nfocusing on a different skillset of yours.";
		dialogues[3]="The first is Service. It is a test of your \nability to do good and help others \n through coordinated endeavors";
		dialogues[5]=null;

	}
	
	public void setAction() {
		
		if(onPath==true) {
			int goalCol=24;
			int goalRow=(gp.player.worldY+gp.player.solidArea.y)/gp.tileSize;
			searchPath(goalCol,goalRow);
		}
		
	}
	
	public void speak() {
		
		// Do this character specific stuff
		
		super.speak();
		if(dialogueIndex==introEndDialogue) {
			onPath = true;
			gp.player.introDigalogueDone=true;
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
			}
			
			g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
		}
	}

	public void update() {
		setAction();
		
		collisionOn=false;
		gp.cChecker.checkTile(this);
		gp.cChecker.checkObject(this, false);
		gp.cChecker.checkPlayer(this);
		
		// IF COLLISION IS FALSE, PLAYER CAN MOVE 
		if (collisionOn==false&&onPath==true) {
			switch(direction) {
			case "up": worldY-=speed;break;
			case "down": worldY+=speed; break;
//			case "left": worldX-=speed; break;
//			case "right": worldX+=speed; break;
			}
		}
		
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
	public void searchPath(int goalCol, int goalRow) {
		int startCol=(worldX+solidArea.x)/gp.tileSize;
		int startRow=(worldY+solidArea.y)/gp.tileSize;
		
		gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow);
		
		if (gp.pFinder.search()==true){
			int nextY = gp.pFinder.pathList.get(0).row*gp.tileSize;
			int enTopY = worldY+solidArea.y;
			
			if(enTopY>nextY) {
				direction = "up";
				checkCollision();
			}
			else if(enTopY<nextY) {
				direction = "down";
				checkCollision();
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
