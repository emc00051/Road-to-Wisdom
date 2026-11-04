package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Heart;

public class ENEMY_CultMember extends Entity{
	
	GamePanel gp;

	public ENEMY_CultMember(GamePanel gp) {
		super(gp);
		
		this.gp=gp;

		type = 2;
		name = "SAI Member";
		defaultSpeed=gp.tileSize/24;
		speed = defaultSpeed;
		maxLife=4;
		life=maxLife;
		
		solidArea.x=7*gp.scale;
		solidArea.y=10*gp.scale;
		solidArea.width=20*gp.scale;
		solidArea.height=22*gp.scale;
		solidAreaDefaultX=solidArea.x;
		solidAreaDefaultY=solidArea.y;
		getImage();
	}
	public void getImage() {
		up1=setup("/enemy/cultMember1",gp.tileSize,gp.tileSize);
		up2=setup("/enemy/cultMember2",gp.tileSize,gp.tileSize);
		down1=setup("/enemy/cultMember1",gp.tileSize,gp.tileSize);
		down2=setup("/enemy/cultMember2",gp.tileSize,gp.tileSize);
		left1=setup("/enemy/cultMember1",gp.tileSize,gp.tileSize);
		left2=setup("/enemy/cultMember2",gp.tileSize,gp.tileSize);
		right1=setup("/enemy/cultMember1",gp.tileSize,gp.tileSize);
		right2=setup("/enemy/cultMember2",gp.tileSize,gp.tileSize);
		
		dying1=setup("/enemy/cultMemberDying1",gp.tileSize,gp.tileSize);
		dying2=setup("/enemy/cultMemberDying2-1.png",gp.tileSize,gp.tileSize);
		dying3=setup("/enemy/cultMemberDying3-1.png",gp.tileSize,gp.tileSize);
		dying4=setup("/enemy/cultMemberDying4-1.png",gp.tileSize,gp.tileSize);
		dying5=setup("/enemy/cultMemberDying5-1.png",gp.tileSize,gp.tileSize);
		dying6=setup("/enemy/cultMemberDying6-1.png",gp.tileSize,gp.tileSize);
		dying7=setup("/enemy/cultMemberDying7-1.png",gp.tileSize,gp.tileSize);
		dying8=setup("/enemy/cultMemberDying8-1.png",gp.tileSize,gp.tileSize);
		dying9=setup("/enemy/cultMemberDying9-1.png",gp.tileSize,gp.tileSize);
		dying10=setup("/enemy/cultMemberDying10-1",gp.tileSize,gp.tileSize);
		dying11=setup("/enemy/cultMemberDying11-1.png",gp.tileSize,gp.tileSize);
		dying12=setup("/enemy/cultMemberDying12-1.png",gp.tileSize,gp.tileSize);
		dying13=setup("/enemy/cultMemberDying13-1.png",gp.tileSize,gp.tileSize);
		dying14=setup("/enemy/cultMemberDying14-1.png",gp.tileSize,gp.tileSize);
		dying15=setup("/enemy/cultMemberDying15-1.png",gp.tileSize,gp.tileSize);
		dying16=setup("/enemy/cultMemberDying16-1.png",gp.tileSize,gp.tileSize);
		dying17=setup("/enemy/cultMemberDying17-1.png",gp.tileSize,gp.tileSize);
		dying18=setup("/enemy/cultMemberDying18-1.png",gp.tileSize,gp.tileSize);
		dying19=setup("/enemy/cultMemberDying19-1.png",gp.tileSize,gp.tileSize);
		dying20=setup("/enemy/cultMemberDying20-1.png",gp.tileSize,gp.tileSize);
		
	}
	public void setAction() {
		
		if(onPath==true) {
			int goalCol=(gp.player.worldX+gp.player.solidArea.x)/gp.tileSize;
			int goalRow=(gp.player.worldY+gp.player.solidArea.y)/gp.tileSize;
			searchPath(goalCol,goalRow);
		}
		else {
			actionLockCounter++;
			
			if(actionLockCounter==120) {
				Random random = new Random();
				int i = random.nextInt(100)+1; // picking a number from 1 to 100
				if (i<=25) {
					direction ="up";
				}
				if (i>25&&i<=50) {
					direction ="down";
				}
				if (i>50&&i<=75) {
					direction ="left";
				}
				if (i>75&&i<=100) {
					direction ="right";
				}
				actionLockCounter=0;
			}
		}
	}
	public void update() {
		setAction();
		
		collisionOn=false;
		gp.cChecker.checkTile(this);
		gp.cChecker.checkObject(this, false);
		gp.cChecker.checkEntity(this, gp.npc);
		gp.cChecker.checkEntity(this, gp.enemy);
		gp.cChecker.checkPlayer(this);
		boolean contactPlayer=gp.cChecker.checkPlayer(this);
		
		if (this.type==2&&contactPlayer==true) {
			if (gp.player.invincible==false) {
				gp.playSE(6);
				gp.player.life--;
				gp.player.invincible=true;
			}
		}
		
		// IF COLLISION IS FALSE, PLAYER CAN MOVE 
		if (collisionOn==false) {
			switch(direction) {
			case "up": worldY-=speed;break;
			case "down": worldY+=speed; break;
			case "left": worldX-=speed; break;
			case "right": worldX+=speed; break;
			}
		}
		
		spriteCounter++;
		if (spriteCounter>20) {
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
		
		int xDistance = Math.abs(worldX-gp.player.worldX);
		int yDistance = Math.abs(worldY-gp.player.worldY);
		int tileDistance = (xDistance+yDistance)/gp.tileSize;
		
		if(onPath==false&&tileDistance<5) {
			
			int i = new Random().nextInt(100)+1;
			if(i>50) {
				onPath = true;
			}
		}
		if(onPath == true && tileDistance > 20) {
			onPath =false;
		}
	}
	public void damageReaction() {
		actionLockCounter=0;
//		direction=gp.player.direction;
		onPath = true;
	}
	public void checkDrop() {
		
		// CAST A DIE
		int i = new Random().nextInt(100)+1;
		if (i<50) {
			dropItem(new OBJ_Heart(gp));
		}
		else {
			return;
		}
	}
}
