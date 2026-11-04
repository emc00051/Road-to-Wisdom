package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

import object.OBJ_Heart;
import object.OBJ_Key;
import entity.Entity;

public class UI {
	
	GamePanel gp;
	Graphics2D g2;
	Font maruMonica, purisaB;
	BufferedImage heart_full,heart_half,heart_blank;
	public boolean messageOn=false;
//	public String message = "";
//	int messageCounter = 0;
	ArrayList<String> message = new ArrayList<>();
	ArrayList<Integer> messageCounter = new ArrayList<>();
	public boolean gameFinished = false;
	public String currentDialogue="";
	public int commandNum=0;
	public int titleScreenState=0; // 0: first screen, 1: second screen
	int subState=0;
	
 	public UI(GamePanel gp) {
		this.gp=gp;
		
		
		try {
			InputStream is = getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf");
			maruMonica = Font.createFont(Font.TRUETYPE_FONT,is);
			is = getClass().getResourceAsStream("/font/Purisa Bold.ttf");
			purisaB = Font.createFont(Font.TRUETYPE_FONT,is);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// CREATE HUD OBJECT
		Entity heart = new OBJ_Heart(gp);
		heart_full=heart.image;
		heart_half=heart.image2;
		heart_blank=heart.image3;
	}
	public void addMessage(String text) {
//		message = text;
//		messageOn=true;
		message.add(text);
		messageCounter.add(0);
	}
	public void draw(Graphics2D g2) {
		
		this.g2=g2;
		
		g2.setFont(maruMonica);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setColor(Color.white);
		
		// TITLE STATE
		if (gp.gameState==gp.titleState) {
			drawTitleScreen();
			
		} 
		
		// PLAY STATE
		if (gp.gameState==gp.playState) {
			drawPlayerLife();
			drawMessage();
		} 
		// PAUSE STATE
		if (gp.gameState==gp.pauseState) {
			drawPlayerLife();
			drawPauseScreen();
		}
		// DIALOGUE STATE
		if (gp.gameState==gp.dialogueState) {
			drawPlayerLife();
			drawDialogueScreen();
		}
		// OPTIONS STATE
		if (gp.gameState==gp.optionsState) {
			drawOptionsScreen();
		}
		// GAME OVER STATE
		if (gp.gameState==gp.gameOverState) {
			drawGameOverScreen();
		}
		// GAME OVER STATE
		if (gp.gameState==gp.pianoState) {
			drawPianoScreen();
		}
	}
	
	public void drawPianoScreen() {
		// WINDOW
		int x=gp.tileSize*2;
		int y=gp.tileSize/2;
		int width=gp.screenWidth - (gp.tileSize*4);
		int height = gp.tileSize*4;
		
		drawSubWindow(x,y,width,height);
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN,32F));
		x+=gp.tileSize;
		y+=gp.tileSize;
		
		currentDialogue = "You must play the well-known song \n\"Ring Around the Rosie\".\nW is your starting note at G, then A at E, \nD at A, and S at C.";
		
		for (String line : currentDialogue.split("\n")) {
			g2.drawString(line,x,y);
			y+=40;
		}
	}
	public void drawPlayerLife() {
		int x = gp.tileSize/2;
		int y = gp.tileSize/2;
		int i =0;
		
		// DRAW BLANK HEART
		while(i<gp.player.maxLife/2) {
			g2.drawImage(heart_blank, x, y, null);
			i++;
			x+=gp.tileSize;
		}
		
		// RESET
		x = gp.tileSize/2;
		y = gp.tileSize/2;
		i =0;
		
		// DRAW CURRENT LIFE
		while (i<gp.player.life) {
			g2.drawImage(heart_half, x, y, null);
			i++;
			if (i<gp.player.life) {
				g2.drawImage(heart_full, x, y, null);
			}
			i++;
			x+=gp.tileSize;
		}
	}
	public void drawMessage() {
		
		int messageX=gp.tileSize;
		int messageY=gp.tileSize*4;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,32F));
		
		for (int i=0;i<message.size();i++) {
			
			if(message.get(i)!=null) {
				
				g2.setColor(Color.black);
				g2.drawString(message.get(i),messageX+2,messageY+2);
				g2.setColor(Color.white);
				g2.drawString(message.get(i),messageX,messageY);
				
				int counter = messageCounter.get(i)+1; // messageCounter++
				messageCounter.set(i, counter); // Set the counter to the array
				messageY+=50;
				
				if (messageCounter.get(i)>180) {
					message.remove(i);
					messageCounter.remove(i);
				}
			}
		}
	}
	public void drawTitleScreen() {
		
		if (titleScreenState==0) {
			g2.setColor(new Color(0,0,0));
			g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);
			
			// TITLE NAME
			g2.setFont(g2.getFont().deriveFont(Font.BOLD,96F));
			String text = "Road to Wisdom";
			int x=getXForCenteredText(text);
			int y=gp.tileSize*3;
			
			// SHADOW
			g2.setColor(Color.gray);
			g2.drawString(text,x+5,y+5);
			
			// MAIN COLOR
			g2.setColor(Color.white);
			g2.drawString(text, x, y);
			
			// CHARACTER IMAGE
			x = gp.screenWidth/2-(gp.tileSize*2)/2;
			y += gp.tileSize*2;
			g2.drawImage(gp.player.down1, x, y, gp.tileSize*2, gp.tileSize*2, null);
			
			// MENU
			g2.setFont(g2.getFont().deriveFont(Font.BOLD,48F));
			
			text = "NEW GAME";
			x = getXForCenteredText(text);
			y += gp.tileSize*3.5;
			g2.drawString(text,x,y);
			if (commandNum==0) {
				g2.drawString(">",x-gp.tileSize,y);
			}

			text = "LOAD GAME";
			x = getXForCenteredText(text);
			y += gp.tileSize;
			g2.drawString(text,x,y);
			if (commandNum==1) {
				g2.drawString(">",x-gp.tileSize,y);
			}
			
			text = "QUIT";
			x = getXForCenteredText(text);
			y += gp.tileSize;
			g2.drawString(text,x,y);
			if (commandNum==2) {
				g2.drawString(">",x-gp.tileSize,y);
			}
		}
		else if (titleScreenState==1) {
			
			// CONTROLS SCREEN
			g2.setColor(Color.white);
			g2.setFont(g2.getFont().deriveFont(84F));
			
			String text = "CONTROLS";
			int x = getXForCenteredText(text);
			int y = gp.tileSize*2;
			g2.drawString(text, x, y);
			
			g2.setFont(g2.getFont().deriveFont(42F));
			
			text = "UP: W";
			x = getXForCenteredText(text);
			y += gp.tileSize*3;
			g2.drawString(text, x, y);
			text = "LEFT: A";
			x = getXForCenteredText(text);
			y += gp.tileSize;
			g2.drawString(text, x, y);
			text = "DOWN: S";
			x = getXForCenteredText(text);
			y += gp.tileSize;
			g2.drawString(text, x, y);
			text = "RIGHT: D";
			x = getXForCenteredText(text);
			y += gp.tileSize;
			g2.drawString(text, x, y);
			text = "PAUSE: ESC";
			x = getXForCenteredText(text);
			y += gp.tileSize;
			g2.drawString(text, x, y);
			text = "SELECT: ENTER";
			x = getXForCenteredText(text);
			y += gp.tileSize;
			g2.drawString(text, x, y);
		}
	}
	public void drawPauseScreen() {
		
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN,80));
		String text = "PAUSED";
		int x = getXForCenteredText(text);
		int y=gp.screenHeight/2;
		
		g2.drawString(text, x, y);
	}
	public void drawDialogueScreen(){
		
		// WINDOW
		int x=gp.tileSize*2;
		int y=gp.tileSize/2;
		int width=gp.screenWidth - (gp.tileSize*4);
		int height = gp.tileSize*4;
		
		drawSubWindow(x,y,width,height);
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN,32F));
		x+=gp.tileSize;
		y+=gp.tileSize;
		
		for (String line : currentDialogue.split("\n")) {
			g2.drawString(line,x,y);
			y+=40;
		}
	}
	public void drawOptionsScreen() {
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(32F));
		
		// SUB WINDOW
		int frameX=gp.tileSize*4;
		int frameY=gp.tileSize;
		int frameWidth=gp.tileSize*8;
		int frameHeight=gp.tileSize*10;
		
		drawSubWindow(frameX,frameY,frameWidth,frameHeight);
		
		switch(subState) {
		case 0: options_top(frameX,frameY);break;
		case 1: break;
		case 2: break;
		case 3: options_endGameConfirmation(frameX,frameY);break;
		}
		
		gp.keyH.ePressed=false;
	}
	public void drawGameOverScreen() {
		g2.setColor(new Color(0,0,0,150));
		g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);
		int x;
		int y;
		String text;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,110f));
		text = "Game Over";
		// Shadow
		x = getXForCenteredText(text);
		y = gp.tileSize *4;
		g2.drawString(text, x, y);
		// Main
		g2.setColor(Color.white);
		g2.drawString(text,x-4,y-4);
		// Retry
		g2.setFont(g2.getFont().deriveFont(50f));
		text = "Retry";
		x = getXForCenteredText(text);
		y += gp.tileSize*4;
		g2.drawString(text, x, y);
		if(commandNum==0) {
			g2.drawString(">",x-40,y);
		}
		// Back to the title Screen
		text = "Quit";
		x = getXForCenteredText(text);
		y += 55;
		g2.drawString(text, x, y);
		if(commandNum==1) {
			g2.drawString(">",x-40,y);
		}
	}

	public void options_top(int frameX,int frameY) {
		int textX;
		int textY;
		
		// TITLE
		String text = "Options";
		textX=getXForCenteredText(text);
		textY=frameY+gp.tileSize;
		g2.drawString(text,textX,textY);
		// FULL SCREEN
		textX=frameX+gp.tileSize;
		textY=gp.tileSize*4;
		g2.drawString("Full Screen",textX,textY);
		if (commandNum==0) {
			g2.drawString(">",textX-gp.tileSize/2,textY);
			if (gp.keyH.ePressed==true) {
				if(gp.fullScreenOn==false) {
					gp.fullScreenOn=true;
				} else if(gp.fullScreenOn==true) {
					gp.fullScreenOn=false;
				}
			}
		}
		// MUSIC
		textY+=gp.tileSize;
		g2.drawString("Music",textX,textY);
		if (commandNum==1) {
			g2.drawString(">",textX-gp.tileSize/2,textY);
		}
		// SE
		textY+=gp.tileSize;
		g2.drawString("SE",textX,textY);
		if (commandNum==2) {
			g2.drawString(">",textX-gp.tileSize/2,textY);
		}
		// CONTROLS
		textY+=gp.tileSize;
		g2.drawString("Controls",textX,textY);
		if (commandNum==3) {
			g2.drawString(">",textX-gp.tileSize/2,textY);
		}
		// END GAME
		textY+=gp.tileSize;
		g2.drawString("End Game",textX,textY);
		if (commandNum==4) {
			g2.drawString(">",textX-gp.tileSize/2,textY);
			if (gp.keyH.ePressed==true) {
				subState=3;
				commandNum=0;
			}
		}
		// BACK
		textY+=gp.tileSize*2;
		g2.drawString("Back",textX,textY);
		if (commandNum==5) {
			g2.drawString(">",textX-gp.tileSize/2,textY);
			if(gp.keyH.ePressed==true) {
				gp.gameState=gp.playState;
				commandNum=0;
			}
				
		}
		
		// FULL SCREEN CHECKBOX
		textX = frameX + gp.tileSize*5;
		textY = frameY + gp.tileSize*2+40;
		g2.setStroke(new BasicStroke(3));
		g2.drawRect(textX,textY,24,24);
		if (gp.fullScreenOn==true) {
			g2.fillRect(textX,textY,24,24);
		}
		
		// MUSIC VOLUME
		textY+=gp.tileSize;
		g2.drawRect(textX, textY, 120,gp.tileSize/2-4);
		// SE VOLUME
		textY+=gp.tileSize;
		g2.drawRect(textX, textY, 120,gp.tileSize/2-4);
		
	}
	public void options_endGameConfirmation(int frameX,int frameY){
		int textX=frameX+gp.tileSize;
		int textY = frameY+gp.tileSize*3;
		
		currentDialogue = "Quit the game and \nreturn to the title screen?";
		
		for(String line:currentDialogue.split("\n")) {
			g2.drawString(line,textX,textY);
			textY+=40;
		}
		
		// YES 
		String text = "Yes";
		textX=getXForCenteredText(text);
		textY+=gp.tileSize*3;
		g2.drawString(text,textX,textY);
		if(commandNum==0) {
			g2.drawString(">",textX-25,textY);
			if (gp.keyH.ePressed==true) {
				subState=0;
				gp.gameState=gp.titleState;
				titleScreenState=0;
				gp.stopMusic();
			}
		}
		// NO 
		text = "NO";
		textX=getXForCenteredText(text);
		textY+=gp.tileSize;
		g2.drawString(text,textX,textY);
		if(commandNum==1) {
			g2.drawString(">",textX-25,textY);
			if (gp.keyH.ePressed==true) {
				subState=0;
				commandNum=4;
			}
		}
		
	}
	public void drawSubWindow(int x, int y, int width, int height) {
		
		Color c = new Color(0,0,0,210);
		g2.setColor(c);
		g2.fillRoundRect(x,y,width,height,35,35);
		
		c = new Color(255,255,255);
		g2.setColor(c);
		g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(x+5,y+5,width-10,height-10,25,25);
	}
	public int getXForCenteredText(String text) {
		
		int length = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
		int x=gp.screenWidth/2-length/2;
		return x;
	}
}
