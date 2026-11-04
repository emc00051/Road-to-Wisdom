package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;

import ai.PathFinder;
import entity.Entity;
import entity.Player;
import tile.TileManager;
import tile_interactive.InteractiveTile;

public class GamePanel extends JPanel implements Runnable{
	
	// SCREEN SETTINGS
	final int originalTileSize = 32; // 16x16 tile
	public final int scale = 2;
	
	public final int tileSize = originalTileSize*scale; // 48x48 tile
	public final int maxScreenCol = 16;
	public final int maxScreenRow = 12;
	public final int screenWidth = tileSize*maxScreenCol; // 768 pixels
	public final int screenHeight = tileSize*maxScreenRow; // 576 pixels
	
	//WORLD SETTINGS
	public final int maxWorldCol=100;
	public final int maxWorldRow=100;
	public final int maxMap=2;
	public int currentMap=0;
	public boolean fullScreenOn=false;
	
	// FPS
	int FPS = 60;
	
	// SYSTEM
	public TileManager tileM = new TileManager(this);
	public KeyHandler keyH = new KeyHandler(this);
	MouseHandler mouseH = new MouseHandler(this);
	Sound music = new Sound();
	Sound SE = new Sound();
	public CollisionChecker cChecker = new CollisionChecker(this);
	public AssetSetter aSetter = new AssetSetter(this);
	public UI ui = new UI(this);
	public EventHandler eHandler = new EventHandler(this);
	public PathFinder pFinder = new PathFinder(this);
	Thread gameThread;
	
	// ENTITY AND OBJECT
	public Player player = new Player(this,keyH,mouseH);
	public Entity obj[][] = new Entity[maxMap][20];
	public Entity npc[][] = new Entity[maxMap][20];
	public Entity enemy[][] = new Entity[maxMap][20];
	public InteractiveTile iTile[][]=new InteractiveTile[maxMap][50];
	ArrayList<Entity> entityList=new ArrayList<>();
	
	// GAME STATE 
	boolean pause=false;
	public int gameState;
	public final int titleState = 0;
	public final int playState = 1;
	public final int pauseState=2;
	public final int dialogueState=3;
	public final int optionsState=4;
	public final int gameOverState=5;
	public final int pianoState=6;
	
	public GamePanel() {
		
		this.setPreferredSize(new Dimension(screenWidth,screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.addMouseListener(mouseH);
		this.setFocusable(true);
		setBorder(null);
	}
	public void setUpGame() {
		
		aSetter.setObject();
		aSetter.setNPC();
		aSetter.setEnemy();
		aSetter.setInteractiveTiles();
//		playMusic(0);
		gameState=titleState;
	}
	public void retry() {
		player.setDefaultPositions();
		player.restoreLife();
		aSetter.setNPC();
		aSetter.setEnemy();
		playMusic(0);
	}
	public void restart() {
		player.setDefaultValues();
		player.setDefaultPositions();
		player.restoreLife();
		aSetter.setObject();
		aSetter.setNPC();
		aSetter.setEnemy();
		aSetter.setInteractiveTiles();
	}
	public void startGameThread() {
		
		gameThread = new Thread(this);
		gameThread.start();
	}
	@Override
	public void run() {
		
		double drawInterval = 1000000000/FPS; // 0.016667 seconds
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer=0;
		long drawCount = 0;
		
		while (gameThread != null) {
			
			currentTime=System.nanoTime();
			
			delta+=(currentTime-lastTime)/drawInterval;
			timer+=(currentTime-lastTime);
			lastTime=currentTime;
			
			if (delta>=1) {
				update();
				repaint();
				delta--;
				drawCount++;
			}
			
			if (timer >=1000000000) {
				timer = 0;
				drawCount=0;
			}
		}
	}
	public void update() {
		
		if (gameState==playState) {
			player.update();
			// NPC
			for (int i=0;i<npc[1].length;i++) {
				if (npc[currentMap][i]!=null) {
					npc[currentMap][i].update();
				}
			}
			// ENEMY
			for (int i=0;i<enemy[1].length;i++) {
				if (enemy[currentMap][i]!=null) {
					if (enemy[currentMap][i].alive==true&&enemy[currentMap][i].dying==false) {
						enemy[currentMap][i].update();
					}
					if (enemy[currentMap][i].alive==false) {
						enemy[currentMap][i].checkDrop();
						enemy[currentMap][i]= null;
					}
				}
			}
			
			// INTERACTIVE TILE
			for (int i=0;i<iTile[1].length;i++) {
				if (iTile[currentMap][i]!=null) {
					iTile[currentMap][i].update();
				}
			}
		}
		if (gameState==pauseState){
			pauseMusic();
			pause = true;
		}
		if (gameState==playState&& pause==true) {
			resumeMusic();
			pause=false;
		}
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// DEBUG
		long drawStart=0;
		if (keyH.showDebugText==true) {
			drawStart = System.nanoTime();
		}
		
		// TITLE SCREEN
		if (gameState==titleState) {
			ui.draw(g2);
		}
		
		// OTHERS
		else {
			// TILE 
			tileM.draw(g2);
			
			for (int i =0;i<iTile[1].length;i++) {
				if (iTile[currentMap][i]!=null) {
					iTile[currentMap][i].draw(g2);
				}
			}
			
			// ADD ENTITIES TO THE LIST
			entityList.add(player);
			for (int i=0;i<npc[1].length;i++) {
				if (npc[currentMap][i]!=null) {
					entityList.add(npc[currentMap][i]);
				}
			}
			
			for (int i=0;i<obj[1].length;i++) {
				if (obj[currentMap][i]!=null) {
					entityList.add(obj[currentMap][i]);
				}
			}
			for (int i=0;i<enemy[1].length;i++) {
				if (enemy[currentMap][i]!=null) {
					entityList.add(enemy[currentMap][i]);
				}
			}
			
			// SORT
			Collections.sort(entityList, new Comparator<Entity>() {
				@Override
				public int compare(Entity e1,Entity e2) {
					
					int result = Integer.compare(e1.worldY, e2.worldY);
					return result;
				}
			});
			
			// DRAW ENTITIES
			for (int i=0;i<entityList.size();i++) {
				entityList.get(i).draw(g2);
			}
			
			// EMPTY ENTITY LIST
			entityList.clear();
			
			// PLAYER
			player.draw(g2);
		
			// UI
			ui.draw(g2);
		}
		
		
		
		// DEBUG
		if (keyH.showDebugText==true) {
			long drawEnd = System.nanoTime();
			long passed = drawEnd-drawStart;
			
			
			g2.setFont(new Font("Arial",Font.PLAIN,20));
			g2.setColor(Color.white);
			int x = 10;
			int y = 400;
			int lineHeight = 20;
			
			g2.drawString("WorldX: "+player.worldX,x,y); y+=lineHeight;
			g2.drawString("WorldY: "+player.worldY,x,y); y+=lineHeight;
			g2.drawString("Col: "+(player.worldX+player.solidArea.x)/tileSize,x,y); y+=lineHeight;
			g2.drawString("Row: "+(player.worldY+player.solidArea.y)/tileSize,x,y); y+=lineHeight;
			g2.drawString("Draw Time "+passed,x,y);
		}
		
		g2.dispose();
		
	}
	public void playMusic(int i) {
		
		music.setFile(i);
		music.play();
		music.loop();
	}
	public void stopMusic() {
		
		music.stop();
	}
	public void pauseMusic() {

		music.pause();
	}
	public void resumeMusic () {
		music.resume();
	}
	public void playSE(int i) {
		
		SE.setFile(i);
		SE.play();
	}
}


