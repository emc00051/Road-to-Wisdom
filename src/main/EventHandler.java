package main;

import entity.NPC_Danny;
import entity.NPC_Seth;
import object.OBJ_Circle;
import object.OBJ_Crest;
import object.OBJ_Lyre;
import tile_interactive.IT_Flower;

public class EventHandler {
	
	GamePanel gp;
	EventRect eventRect[][][];
	
	int previousEventX,previousEventY;
	boolean canTouchEvent=true;
	
	public EventHandler(GamePanel gp) {
		this.gp=gp;
		eventRect= new EventRect[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];
		int map =0;
		int col = 0;
		int row = 0;
		while (map<gp.maxMap&&col<gp.maxWorldCol && row <gp.maxWorldRow) {
			eventRect[map][col][row]=new EventRect();
			eventRect[map][col][row].x=23;
			eventRect[map][col][row].y=23;
			eventRect[map][col][row].width=2;
			eventRect[map][col][row].height=2;
			eventRect[map][col][row].eventRectDefaultX=eventRect[map][col][row].x;
			eventRect[map][col][row].eventRectDefaultY=eventRect[map][col][row].y;
			col++;
			if(col==gp.maxWorldCol) {
				col=0;
				row++;
				if (row==gp.maxWorldRow) {
					row=0;
					map++;
				}
			}
		}	
	}
	
	public void checkEvent() {
		
		// Check if the Player character is more than 1 tile away from the last event.
		int xDistance=Math.abs(gp.player.worldX-previousEventX);
		int yDistance=Math.abs(gp.player.worldY-previousEventY);
		int distance = Math.max(xDistance,yDistance);
		if (distance>gp.tileSize) {
			canTouchEvent=true;
		}
		
		if (canTouchEvent==true) {
			if(hit(0,22,17,"any")==true) {crestDone(0,22,17,gp.dialogueState);}
			else if(hit(0,28,17,"any")==true) {lyreDone(0,28,17,gp.dialogueState);}
			else if(hit(0,20,20,"any")==true) {starDone(0,20,20,gp.dialogueState);}
			else if(hit(0,30,20,"any")==true) {circleDone(0,30,20,gp.dialogueState);}
			else if(hit(1,65,88,"any")==true) {pianoEvent(1,64,86,gp.pianoState);}
			else if(hit(1,37,84,"any")==true) {waterFlower(1,37,83);}
			else if(hit(1,33,86,"any")==true) {waterFlower(1,33,85);}
			else if(hit(1,36,90,"any")==true) {waterFlower(1,36,89);}
			else if(hit(0,25,19,"any")==true) {if(gp.player.introDigalogueDone==true&&(gp.player.hasCircle==false||gp.player.hasCrest==false||gp.player.hasLyre==false||gp.player.hasStar==false)){teleport(1,53,88);}}
			else if(hit(1,9,8,"any")==true) {if(gp.player.hasCrest==true){teleport(0,25,32);
			int mapNum=0;
			int i =0;
			gp.npc[mapNum][i]=new NPC_Danny(gp);
			gp.npc[mapNum][i].worldX=gp.tileSize*29;
			gp.npc[mapNum][i].worldY=gp.tileSize*31;
			i++;
			gp.npc[mapNum][i]=new NPC_Seth(gp);
			gp.npc[mapNum][i].worldX=gp.tileSize*29;
			gp.npc[mapNum][i].worldY=gp.tileSize*33;
			i++;}}
		}
		
		if (gp.player.killCount>=2) {
			crestComplete(1,53,53,gp.dialogueState);
			gp.player.killCount=0;
		}
		if (gp.player.flowerCount>=3) {
			circleComplete(1,38,86,gp.dialogueState);
			gp.player.flowerCount=0;
		}
	}
	
	public boolean hit(int map,int col,int row,String reqDirection) {
		
		boolean hit = false;
		if(map==gp.currentMap) {
			gp.player.solidArea.x=gp.player.worldX+gp.player.solidArea.x;
			gp.player.solidArea.y=gp.player.worldY+gp.player.solidArea.y;
			eventRect[map][col][row].x=col*gp.tileSize+eventRect[map][col][row].x;
			eventRect[map][col][row].y=row*gp.tileSize+eventRect[map][col][row].y;
			
			if (gp.player.solidArea.intersects(eventRect[map][col][row])&& eventRect[map][col][row].eventDone==false) {
				if (gp.player.direction.contentEquals(reqDirection)||reqDirection.contentEquals("any")){
					hit = true;
					
					previousEventX=gp.player.worldX;
					previousEventY=gp.player.worldY;
				}
			}
			
			gp.player.solidArea.x=gp.player.solidAreaDefaultX;
			gp.player.solidArea.y=gp.player.solidAreaDefaultY;
			eventRect[map][col][row].x=eventRect[map][col][row].eventRectDefaultX;
			eventRect[map][col][row].y=eventRect[map][col][row].eventRectDefaultY;
		}
		return hit;
	}
	
	public void waterFlower(int map, int col, int row) {
		if(gp.keyH.ePressed==true) {
			gp.player.flowerCount++;
			for (int i=0;i<gp.iTile[1].length;i++) {
	            if (gp.iTile[gp.currentMap][i]!=null&&gp.iTile[gp.currentMap][i].worldX==gp.tileSize*col&&gp.iTile[gp.currentMap][i].worldY==gp.tileSize*row) {
	                gp.iTile[gp.currentMap][i]=null;
	    			eventRect[map][col][row].eventDone=true;
	    			canTouchEvent=false;
	    			
	    			int mapNum=1;
	    			int j =6;
	    			gp.iTile[mapNum][j]=new IT_Flower(gp,col,row);
	    			gp.iTile[mapNum][j].worldX=gp.tileSize*col;
	    			gp.iTile[mapNum][j].worldY=gp.tileSize*row;
	    			break;
	    			
	    			
	            }
	        }
		}
	}
	
	public void circleComplete(int map,int col,int row,int gameState) {
		int mapNum=1;
		int i =0;
		gp.obj[mapNum][i]=new OBJ_Circle(gp);
		gp.obj[mapNum][i].worldX=gp.tileSize*col;
		gp.obj[mapNum][i].worldY=gp.tileSize*row;
		
	}
	
	public void pianoEvent(int map,int col,int row, int gameState) {
		if(gp.keyH.ePressed==true) {
			for (int i=0;i<gp.obj[1].length;i++) {
	            if (gp.obj[gp.currentMap][i]!=null&&gp.obj[gp.currentMap][i].worldX==gp.tileSize*col&&gp.obj[gp.currentMap][i].worldY==gp.tileSize*row) {
	                gp.gameState=gameState;
	    			eventRect[map][col][row].eventDone=true;
	    			canTouchEvent=false;
	    			gp.player.pianoDone=true;
	    			int mapNum=1;
	    			int j =0;
	    			gp.obj[mapNum][j]=new OBJ_Lyre(gp);
	    			gp.obj[mapNum][j].worldX=gp.tileSize*65;
	    			gp.obj[mapNum][j].worldY=gp.tileSize*89;
	                break;
	            }
	        }
		}
	}
	
	public void crestComplete(int map,int col,int row,int gameState) {
		int mapNum=1;
		int i =0;
		gp.obj[mapNum][i]=new OBJ_Crest(gp);
		gp.obj[mapNum][i].worldX=gp.tileSize*53;
		gp.obj[mapNum][i].worldY=gp.tileSize*53;
		
	}
	
	public void crestDone(int map,int col, int row, int gameState) {
		if(gp.keyH.ePressed==true) {
			for (int i=0;i<gp.iTile[1].length;i++) {
	            if (gp.iTile[gp.currentMap][i]!=null&&gp.iTile[gp.currentMap][i].worldX==gp.tileSize*22&&gp.iTile[gp.currentMap][i].worldY==gp.tileSize*15&&gp.iTile[gp.currentMap][i].isCorrectItem(gp.player)==true) {
	                gp.iTile[gp.currentMap][i]=null;
	                gp.gameState=gameState;
	    			gp.ui.currentDialogue="You have successfully completed the Brotherhood Trial!";
	    			eventRect[map][col][row].eventDone=true;
	    			canTouchEvent=false;
	    			gp.player.crestDone=true;
	                break;
	            }
	        }
		}
	}
	
	public void lyreDone(int map, int col, int row, int gameState) {
		if(gp.keyH.ePressed==true) {
			for (int i=0;i<gp.iTile[1].length;i++) {
	            if (gp.iTile[gp.currentMap][i]!=null&&gp.iTile[gp.currentMap][i].worldX==gp.tileSize*28&&gp.iTile[gp.currentMap][i].worldY==gp.tileSize*15&&gp.iTile[gp.currentMap][i].isCorrectItem(gp.player)==true) {
	                gp.iTile[gp.currentMap][i]=null;
	                gp.gameState=gameState;
	    			gp.ui.currentDialogue="You have successfully completed the Musicianship Trial!";
	    			eventRect[map][col][row].eventDone=true;
	    			canTouchEvent=false;
	    			gp.player.lyreDone=true;
	                break;
	            }
	        }
		}
	}
	
	public void starDone(int map,int col, int row, int gameState) {
		if(gp.keyH.ePressed==true) {
			for (int i=0;i<gp.iTile[1].length;i++) {
	            if (gp.iTile[gp.currentMap][i]!=null&&gp.iTile[gp.currentMap][i].worldX==gp.tileSize*20&&gp.iTile[gp.currentMap][i].worldY==gp.tileSize*18&&gp.iTile[gp.currentMap][i].isCorrectItem(gp.player)==true) {
	                gp.iTile[gp.currentMap][i]=null;
	                gp.gameState=gameState;
	    			gp.ui.currentDialogue="You have successfully completed the Leadership Trial!";
	    			eventRect[map][col][row].eventDone=true;
	    			canTouchEvent=false;
	    			gp.player.starDone=true;
	                break;
	            }
	        }
		}
	}
	
	public void circleDone(int map,int col, int row, int gameState) {
		if(gp.keyH.ePressed==true) {
			for (int i=0;i<gp.iTile[1].length;i++) {
	            if (gp.iTile[gp.currentMap][i]!=null&&gp.iTile[gp.currentMap][i].worldX==gp.tileSize*30&&gp.iTile[gp.currentMap][i].worldY==gp.tileSize*18&&gp.iTile[gp.currentMap][i].isCorrectItem(gp.player)==true) {
	                gp.iTile[gp.currentMap][i]=null;
	                gp.gameState=gameState;
	    			gp.ui.currentDialogue="You have successfully completed the Service Trial!";
	    			eventRect[map][col][row].eventDone=true;
	    			canTouchEvent=false;
	    			gp.player.circleDone=true;
	                break;
	            }
	        }
		}
	}
	
	public void healingPool(int map, int col, int row, int gameState) {
		if(gp.keyH.ePressed==true) {
			gp.gameState=gameState;
			gp.ui.currentDialogue="You heal life.";
			gp.player.life=gp.player.maxLife;
		}
	}
	
	public void teleport(int map,int col, int row) {
		gp.currentMap=map;
		gp.player.worldX=gp.tileSize*col;
		gp.player.worldY=gp.tileSize*row;
		previousEventX=gp.player.worldX;
		previousEventY=gp.player.worldY;
		canTouchEvent=false;
		gp.tileM.loadMap("/maps/trial.txt",1);
	}
}
