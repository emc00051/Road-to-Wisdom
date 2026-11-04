package main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseHandler implements MouseListener{
	GamePanel gp;
	public boolean leftClicked,rightClicked;
	
	// DEBUG
	boolean checkDrawTime=false;
	
	public MouseHandler(GamePanel gp) {
		this.gp=gp;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int code = e.getButton();
		// PLAY STATE
		if (gp.gameState==gp.playState) {
			if (code==MouseEvent.BUTTON1) {
				leftClicked=true;
			}
			if (code==MouseEvent.BUTTON2) {
				rightClicked=true;
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		int code = e.getButton();
		if (code == MouseEvent.BUTTON1) {
            leftClicked = false;
        }
		if (code == MouseEvent.BUTTON2) {
            rightClicked = false;
        }
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
