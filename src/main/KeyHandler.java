package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{
	
	GamePanel gp;
	public boolean upPressed,downPressed,leftPressed,rightPressed,ePressed;
	private StringBuilder keySequence = new StringBuilder();
	private final String SECRET_CODE = "WWADWAWWADWAWAWAWWS";
	private int secretProgress = 0;
	public boolean secretUnlocked = false;
	
	// DEBUG
	boolean showDebugText=false;
	
	public KeyHandler(GamePanel gp) {
		this.gp=gp;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		
		int code = e.getKeyCode();
		// TITLE STATE
		if (gp.gameState==gp.titleState) {
			titleState(code);
		}
		// PLAY STATE
		else if (gp.gameState==gp.playState) {
			playState(code);
		}
		// PAUSE STATE
		else if (gp.gameState==gp.pauseState) {
			pauseState(code);
		}
		// DIALOGUE STATE
		else if (gp.gameState==gp.dialogueState) {
			dialogueState(code);
		}
		// OPTIONS STATE
		else if (gp.gameState==gp.optionsState) {
			optionsState(code);
		}
		// GAME OVER STATE
		else if (gp.gameState==gp.gameOverState) {
			gameOverState(code);
		}
		// PIANO STATE
		else if (gp.gameState==gp.pianoState) {
			pianoState(code);
		}
		
	}
	public void pianoState(int code) {
		gp.pauseMusic();
	    char expectedKey = SECRET_CODE.charAt(secretProgress);
	    char keyChar = '\0';

	    if (code == KeyEvent.VK_W) {
	    	keyChar = 'W';
	    	gp.playSE(10);
	    }
	    else if (code == KeyEvent.VK_A) {
	    	keyChar = 'A';
	    	gp.playSE(9);
	    }
	    else if (code == KeyEvent.VK_S) {
	    	keyChar = 'S';
	    	gp.playSE(11);
	    }
	    else if (code == KeyEvent.VK_D) {
	    	keyChar = 'D';
	    	gp.playSE(12);
	    }

	    if (keyChar != '\0') {
	        if (keyChar == expectedKey) {
	            secretProgress++;
	            if (secretProgress == SECRET_CODE.length()) {
	                secretUnlocked = true;
	                gp.gameState=gp.playState;
	                gp.playMusic(0);
	            }
	        } else {
	            // Wrong key, reset progress
	            secretProgress = 0;
	        }
	    }
	}
	
	public void gameOverState(int code) {
		if (code==KeyEvent.VK_W) {
			gp.ui.commandNum--;
			if (gp.ui.commandNum<0) {
				gp.ui.commandNum=1;
			}
		}
		if (code==KeyEvent.VK_S) {
			gp.ui.commandNum++;
			if (gp.ui.commandNum>1) {
				gp.ui.commandNum=0;
			}
		}
		if(code==KeyEvent.VK_ENTER) {
			if(gp.ui.commandNum==0) {
				gp.gameState=gp.playState;
				gp.retry();
			}
			else if(gp.ui.commandNum==1) {
				gp.gameState=gp.titleState;
				gp.ui.titleScreenState=0;
				gp.restart();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int code = e.getKeyCode();
		if (code==KeyEvent.VK_W) {
			upPressed=false;
		}
		if (code==KeyEvent.VK_A) {
			leftPressed=false;
		}
		if (code==KeyEvent.VK_S) {
			downPressed=false;
		}
		if (code==KeyEvent.VK_D) {
			rightPressed=false;
		}
	}
	public void titleState(int code) {
		if (gp.ui.titleScreenState==0) {
			if (code==KeyEvent.VK_W) {
				gp.ui.commandNum--;
				if (gp.ui.commandNum<0) {
					gp.ui.commandNum=2;
				}
			}
			if (code==KeyEvent.VK_S) {
				gp.ui.commandNum++;
				if (gp.ui.commandNum>2) {
					gp.ui.commandNum=0;
				}
			}
			if (code==KeyEvent.VK_ENTER) {
				if (gp.ui.commandNum==0) {
					gp.ui.titleScreenState=1;
				}
				if (gp.ui.commandNum==1) {
					// add later
				}
				if (gp.ui.commandNum==2) {
					System.exit(0);
				}
			}
		}
		else if (gp.ui.titleScreenState==1) {
			if (code==KeyEvent.VK_ENTER) {
				gp.gameState=gp.playState;
				gp.playMusic(0);
				
			}
		}
	}
	public void playState(int code) {
		if (code==KeyEvent.VK_W) {
			upPressed=true;
		}
		if (code==KeyEvent.VK_A) {
			leftPressed=true;
		}
		if (code==KeyEvent.VK_S) {
			downPressed=true;
		}
		if (code==KeyEvent.VK_D) {
			rightPressed=true;
		}
		if (code==KeyEvent.VK_P) {
			gp.gameState=gp.pauseState;
		}
		if (code==KeyEvent.VK_E) {
			ePressed=true;
		}
		if (code==KeyEvent.VK_ESCAPE) {
			gp.gameState=gp.optionsState;
		}
		// DEBUG
		if (code==KeyEvent.VK_T) {
			if (showDebugText==false) {
				showDebugText=true;
			}
			else if (showDebugText==true) {
				showDebugText=false;
			}
		}
		if (code==KeyEvent.VK_R) {
			switch(gp.currentMap) {
			case 0: gp.tileM.loadMap("/maps/starterIsland.txt",0);break;
			case 1: gp.tileM.loadMap("/maps/trial.txt",1);break;
			}
			
		}
	}
	public void pauseState(int code) {
		if (code==KeyEvent.VK_P) {
			gp.gameState=gp.playState;
		}
	}
	public void dialogueState(int code) {
		if (code==KeyEvent.VK_E) {
			
			gp.gameState = gp.playState;
			
		}
	}
	public void optionsState(int code) {
		if (code==KeyEvent.VK_ESCAPE) {
			gp.gameState=gp.playState;
		}
		if(code==KeyEvent.VK_ENTER) {
			ePressed=true;
		}
		int maxCommandNum=0;
		switch(gp.ui.subState) {
		case 0: maxCommandNum=5; break;
		case 3: maxCommandNum=1; break;
		}
		if (code==KeyEvent.VK_W) {
			gp.ui.commandNum--;
			if (gp.ui.commandNum<0) {
				gp.ui.commandNum=maxCommandNum;
			}
		}
		if (code==KeyEvent.VK_S) {
			gp.ui.commandNum++;
			if (gp.ui.commandNum>maxCommandNum) {
				gp.ui.commandNum=0;
			}
		}
	}
}
