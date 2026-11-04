package entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import main.GamePanel;
import main.KeyHandler;
import main.MouseHandler;

public class Player extends Entity{

	KeyHandler keyH;
	MouseHandler mouseH;
	public final int screenX;
	public final int screenY;
	public int killCount=0;
	public int flowerCount=0;
	public boolean crestDone=false;
	public boolean circleDone=false;
	public boolean starDone=false;
	public boolean lyreDone=false;
	public boolean pianoDone=false;
	public boolean introDigalogueDone=false;
	
	public Player(GamePanel gp,KeyHandler keyH,MouseHandler mouseH) {
		super(gp);
		this.keyH=keyH;
		this.mouseH=mouseH;
		
		screenX=gp.screenWidth/2-(gp.tileSize/2);
		screenY=gp.screenHeight/2-(gp.tileSize/2);
		
		solidArea = new Rectangle();
		solidArea.x=gp.tileSize/6+gp.tileSize/24;
		solidArea.y=gp.tileSize/3;
		solidAreaDefaultX=solidArea.x;
		solidAreaDefaultY=solidArea.y;
		solidArea.width=gp.tileSize-(gp.tileSize/3);
		solidArea.height=gp.tileSize-(gp.tileSize/3)-(gp.tileSize/12);
		
		attackArea.width=36;
		attackArea.height=36;
		
		setDefaultValues();
		getPlayerImage();
		getPlayerAttackImage();
	}
	public void setDefaultValues() {
		if (gp.currentMap==0) {
			worldX=gp.tileSize*25;
			worldY=gp.tileSize*32;
		}
		else if (gp.currentMap==1) {
			worldX=gp.tileSize*53;
			worldY=gp.tileSize*88;
		}
		defaultSpeed=gp.tileSize/12;
		speed=defaultSpeed;
		direction = "down";
		
		// PLAYER STATUS
		maxLife=10;
		life=maxLife;
	}
	public void setDefaultPositions() {
		if (gp.currentMap==0) {
			worldX=gp.tileSize*25;
			worldY=gp.tileSize*32;
		}
		else if (gp.currentMap==1) {
			worldX=gp.tileSize*53;
			worldY=gp.tileSize*88;
		}

		direction = "down";
	}
	public void restoreLife() {
		life=maxLife;
		invincible=false;
	}
	public void getPlayerImage() {
		
		up1 = setup("/player/playerUp1",gp.tileSize,gp.tileSize);
		up2 = setup("/player/playerUp2",gp.tileSize,gp.tileSize);
		up3 = setup("/player/playerUp3",gp.tileSize,gp.tileSize);
		up4 = setup("/player/playerUp4",gp.tileSize,gp.tileSize);
		down1 = setup("/player/playerDown1",gp.tileSize,gp.tileSize);
		down2 = setup("/player/playerDown2",gp.tileSize,gp.tileSize);
		down3 = setup("/player/playerDown3",gp.tileSize,gp.tileSize);
		down4 = setup("/player/playerDown4",gp.tileSize,gp.tileSize);
		left1 = setup("/player/playerLeft1",gp.tileSize,gp.tileSize);
		left2 = setup("/player/playerLeft2",gp.tileSize,gp.tileSize);
		left3 = setup("/player/playerLeft3",gp.tileSize,gp.tileSize);
		left4 = setup("/player/playerLeft4",gp.tileSize,gp.tileSize);
		right1 = setup("/player/playerRight1",gp.tileSize,gp.tileSize);
		right2 = setup("/player/playerRight2",gp.tileSize,gp.tileSize);
		right3 = setup("/player/playerRight3",gp.tileSize,gp.tileSize);
		right4 = setup("/player/playerRight4",gp.tileSize,gp.tileSize);
	}
	public void getPlayerAttackImage() {
		attackUp=setup("/player/playerAttackUp",gp.tileSize,gp.tileSize);
		attackDown=setup("/player/playerAttackDown",gp.tileSize,gp.tileSize);
		attackRight=setup("/player/playerAttackRight",gp.tileSize,gp.tileSize);
		attackLeft=setup("/player/playerAttackLeft",gp.tileSize,gp.tileSize);
	}
	public void update() {
		
		if (attacking ==true) {
			attacking();
		}
		else if (keyH.upPressed==true || keyH.rightPressed==true || 
				keyH.downPressed==true || keyH.leftPressed==true || 
				keyH.ePressed==true  || mouseH.leftClicked==true) {
			if (keyH.upPressed==true) {
				direction="up";				
			}
			else if (keyH.rightPressed==true) {
				direction="right";				
			}
			else if (keyH.downPressed==true) {
				direction="down";				
			}
			else if (keyH.leftPressed==true) {
				direction="left";				
			}
			else if (mouseH.leftClicked==true) {
				gp.playSE(7);
				attacking=true;		
				mouseH.leftClicked=false;
			}
			
			// CHECK TILE COLLISION
			collisionOn=false;
			gp.cChecker.checkTile(this);
			
			// CHECK OBJECT COLLISION
			int objIndex=gp.cChecker.checkObject(this, true);
			pickUpObject(objIndex);
			
			// CHECK NPC COLLISION
			int npcIndex=gp.cChecker.checkEntity(this,gp.npc);
			interactNPC(npcIndex);
			
			// CHECK NPC COLLISION
			int enemyIndex=gp.cChecker.checkEntity(this,gp.enemy);
			contactEnemy(enemyIndex);
			
			// CHECK INTERACTIVE TILE COLLISION
			int iTileIndex = gp.cChecker.checkEntity(this,gp.iTile);
			interactTile(iTileIndex);
			
			// CHECK EVENT 
			gp.eHandler.checkEvent();
			
			
			// IF COLLISION IS FALSE, PLAYER CAN MOVE 
			if (collisionOn==false&& keyH.ePressed==false) {
				switch(direction) {
				case "up": worldY-=speed;break;
				case "down": worldY+=speed; break;
				case "left": worldX-=speed; break;
				case "right": worldX+=speed; break;
				}
			}
			
			gp.keyH.ePressed=false;
			
			spriteCounter++;
			if (spriteCounter>12) {
				if (spriteNum==1) {
					spriteNum=2;
				}
				else if (spriteNum==2) {
					spriteNum=3;
				}
				else if (spriteNum==3) {
					spriteNum=4;
				}
				else if (spriteNum==4) {
					spriteNum=1;
				}
				spriteCounter=0;
			}
		}
		else {
			
			standCounter++;
			if (standCounter==20) {
				spriteNum=1;
				standCounter=0;
			}
		}
		
		// This needs to be outside of key IF statement
		if (invincible==true) {
			invincibleCounter++;
			if (invincibleCounter>60) {
				invincible=false;
				invincibleCounter=0;
			}
		}
		if(life>maxLife) {
			life=maxLife;
		}
		if(life<=0) {
			gp.gameState=gp.gameOverState;
			gp.stopMusic();
		}
	}
	public void attacking() {
		
		spriteCounter++;
		
		if (spriteCounter<=25) {
			spriteNum=1;
			
			// Save the current worldX, worldY, solidArea
			int currentWorldX=worldX;
			int currentWorldY=worldY;
			int solidAreaWidth=solidArea.width;
			int solidAreaHeight=solidArea.height;
			
			// Adjust player's worldX/Y for the attackArea
			switch (direction) {
			case "up": worldY-=attackArea.height; break;
			case "down": worldY+=attackArea.height; break;
			case "right": worldX+=attackArea.width; break;
			case "left": worldX-=attackArea.width; break;
			}
			
			// attackArea becomes solidArea
			solidArea.width=attackArea.width;
			solidArea.height=attackArea.height;
			
			// Check enemy collision with the updated worldX/Y, and solidArea
			int enemyIndex=gp.cChecker.checkEntity(this,gp.enemy);
			damageEnemy(enemyIndex);
			
			// After checking collision, restore original data
			worldX=currentWorldX;
			worldY=currentWorldY;
			solidArea.width=solidAreaWidth;
			solidArea.height=solidAreaHeight;
		}
		if (spriteCounter>25) {
			spriteNum=1;
			spriteCounter=0;
			attacking=false;
		}
	}
	public void pickUpObject(int i) {
		
		if(i != 999) {
			
			if (gp.obj[gp.currentMap][i].type==type_pickupOnly) {
				gp.obj[gp.currentMap][i].use(this);
				gp.obj[gp.currentMap][i]=null;
			}
		}
	}
	public void interactNPC(int i) {
		
		if(i != 999) {
			if (i != 999) {
		        Entity npc = gp.npc[gp.currentMap][i];
		        if (keyH.ePressed) {
		            gp.gameState = gp.dialogueState;
		            npc.speak();
		            
		        }
		    }
		}
	}
	public void interactTile(int i) {
		if(i != 999) {
			if (crestDone==true) {
				gp.iTile[gp.currentMap][i]=null;
				crestDone=false;
			}
		}
	}
	public void contactEnemy(int i) {
		
		if(i != 999) {
			if (invincible==false&&gp.enemy[gp.currentMap][i].dying==false) {
				gp.playSE(6);
				life--;
				invincible=true;
			}
		}
	}
	public void damageEnemy(int i) {
		
		if(i != 999) {

			if (gp.enemy[gp.currentMap][i].invincible==false) {
				
				gp.playSE(5);
				
//				knockback(gp.enemy[gp.currentMap][i]);
				
				int damage = 1;
				
				gp.enemy[gp.currentMap][i].life--;
				gp.ui.addMessage(damage+" damage!");
				
				gp.enemy[gp.currentMap][i].invincible=true;
				gp.enemy[gp.currentMap][i].damageReaction();
				if (gp.enemy[gp.currentMap][i].life<=0) {
					gp.enemy[gp.currentMap][i].dying = true;
					gp.ui.addMessage("Killed the "+gp.enemy[gp.currentMap][i].name+"!");
					killCount++;
				}
			}
		}
	}
	public void draw(Graphics2D g2) {
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		BufferedImage image = null;
		switch(direction) {
		case "up":
			if (attacking==false) {
				if (spriteNum==1) image=up1;
				if (spriteNum==2) image=up2;
				if (spriteNum==3) image=up3;
				if (spriteNum==4) image=up4;
			}
			if (attacking ==true) {
				if (spriteNum==1) image=attackUp;
			}
			break;
		case "down":
			if (attacking==false) {
				if (spriteNum==1) image=down1;
				if (spriteNum==2) image=down2;
				if (spriteNum==3) image=down3;
				if (spriteNum==4) image=down4;
			}
			if (attacking ==true) {
				if (spriteNum==1) image=attackDown;
			}
			break;
		case "left":
			if (attacking==false) {
				if (spriteNum==1) image=left1;
				if (spriteNum==2) image=left2;
				if (spriteNum==3) image=left3;
				if (spriteNum==4) image=left4;
			}
			if (attacking ==true) {
				if (spriteNum==1) image=attackLeft;
			}
			break;
		case "right":
			if (attacking==false) {
				if (spriteNum==1) image=right1;
				if (spriteNum==2) image=right2;
				if (spriteNum==3) image=right3;
				if (spriteNum==4) image=right4;
			}
			if (attacking ==true) {
				if (spriteNum==1) image=attackRight;
			}
			break;
		}
		
		if (invincible==true) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.4f));
		}
		
		g2.drawImage(image,screenX,screenY,null);
		
		// Reset Alpha
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
		// HITBOX
//		g2.setColor(Color.red);
//		g2.drawRect(screenX+solidArea.x,screenY+solidArea.y,solidArea.width,solidArea.height);
		
//		// DEBUG
//		g2.setFont(new Font("Arial",Font.PLAIN,26));
//		g2.setColor(Color.white);
//		g2.drawString("Invincible"+invincibleCounter, 10, 400);
	}
	public void knockback(Entity entity) {
		entity.direction = direction;
		entity.speed +=10;;
		entity.knockback= true;
				
	}
}
