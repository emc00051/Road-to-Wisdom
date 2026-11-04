package main;

import entity.NPC_Danny;
import entity.NPC_GuidingSpirit;
import entity.NPC_Seth;
import monster.ENEMY_CultMember;
import object.OBJ_Crest;
import object.OBJ_Heart;
import object.OBJ_Piano;
import object.OBJ_Star;
import tile_interactive.IT_Bud;
import tile_interactive.IT_CirclePillar;
import tile_interactive.IT_CrestPillar;
import tile_interactive.IT_LyrePillar;
import tile_interactive.IT_StarPillar;

public class AssetSetter {
	
	GamePanel gp;
	
	public AssetSetter(GamePanel gp) {
		this.gp=gp;
	}
	
	public void setObject() {
		int mapNum=1;
		int i =1;
		gp.obj[mapNum][i]=new OBJ_Star(gp);
		gp.obj[mapNum][i].worldX=gp.tileSize*10;
		gp.obj[mapNum][i].worldY=gp.tileSize*8;
		i++;
		gp.obj[mapNum][i]=new OBJ_Piano(gp);
		gp.obj[mapNum][i].worldX=gp.tileSize*64;
		gp.obj[mapNum][i].worldY=gp.tileSize*86;
		i++;
		
		
	}
	
	public void setNPC() {
		int mapNum=0;
		int i =0;
		gp.npc[mapNum][i]=new NPC_GuidingSpirit(gp);
		gp.npc[mapNum][i].worldX=gp.tileSize*24;
		gp.npc[mapNum][i].worldY=gp.tileSize*29;
		i++;
		
		mapNum=1;
		i =0;
		gp.npc[mapNum][i]=new NPC_Danny(gp);
		gp.npc[mapNum][i].worldX=gp.tileSize*45;
		gp.npc[mapNum][i].worldY=gp.tileSize*55;
		i++;
		gp.npc[mapNum][i]=new NPC_Seth(gp);
		gp.npc[mapNum][i].worldX=gp.tileSize*48;
		gp.npc[mapNum][i].worldY=gp.tileSize*54;
		i++;
	}
	
	public void setEnemy() {
		int mapNum=0;
		int i=0;
		mapNum=1;
		// Place enemies for trial here
		gp.enemy[mapNum][i]=new ENEMY_CultMember(gp);
		gp.enemy[mapNum][i].worldX=gp.tileSize*46;
		gp.enemy[mapNum][i].worldY=gp.tileSize*54;
		i++;
		gp.enemy[mapNum][i]=new ENEMY_CultMember(gp);
		gp.enemy[mapNum][i].worldX=gp.tileSize*48;
		gp.enemy[mapNum][i].worldY=gp.tileSize*52;
		i++;
		gp.enemy[mapNum][i]=new ENEMY_CultMember(gp);
		gp.enemy[mapNum][i].worldX=gp.tileSize*49;
		gp.enemy[mapNum][i].worldY=gp.tileSize*54;
		i++;
		gp.enemy[mapNum][i]=new ENEMY_CultMember(gp);
		gp.enemy[mapNum][i].worldX=gp.tileSize*48;
		gp.enemy[mapNum][i].worldY=gp.tileSize*58;
		i++;
	}
	public void setInteractiveTiles() {
		int mapNum=0;
		int i =0;
		gp.iTile[mapNum][i]=new IT_CrestPillar(gp,22,15);i++;
		gp.iTile[mapNum][i]=new IT_CirclePillar(gp,30,18);i++;
		gp.iTile[mapNum][i]=new IT_StarPillar(gp,20,18);i++;
		gp.iTile[mapNum][i]=new IT_LyrePillar(gp,28,15);i++;
		
		mapNum=1;
		i=0;
		gp.iTile[mapNum][i]=new IT_Bud(gp,37,83);i++;
		gp.iTile[mapNum][i]=new IT_Bud(gp,33,85);i++;
		gp.iTile[mapNum][i]=new IT_Bud(gp,36,89);i++;
	}
}
