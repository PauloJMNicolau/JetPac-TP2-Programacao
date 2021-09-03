package jetpac.enemy;

import jetpac.astro.Astronaut;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

import jetpac.platform.Plataform;
import prof.jogos2D.ComponenteMultiAnimado;

public class FollowEnemy extends Enemy {

	private Random gerarNum = new Random();
	private Point astroPos;
	private Point enemyPos;
	private int mov=0;
	//private int dir;
	
	public FollowEnemy(Point p, int vel, int score, int dir,
			ComponenteMultiAnimado img) {
		super(p, vel, score, dir, img);
	}

	/**
	 * desloca o inimigo
	 * moves the enemy
	 * @param dx distância a mover em x. x distance to move
	 * @param dy distância a mover em y. y distance to move
	 */
	public void move(int dx, int dy ) {
		ComponenteMultiAnimado img = (ComponenteMultiAnimado) getImage();
		Astronaut astro = getWorld().getAstronaut();
		if(dy != 0 && dy > mov)
			dy=0;

		
		if(gerarNum.nextInt(10 - (-10) + 1) -10 == 0){
			astroPos = astro.getPosition();
			enemyPos = getPosition();
		}
		if (astroPos != null && enemyPos != null){
			dx=0;
	//		dir = enemyPos.y - astroPos.y;
			if(astroPos.x < enemyPos.x ){			
				dx--;
			}
			else{
				dx++;
				}
			if( astroPos.y > enemyPos.y ){
				dy++;				
			}
			else{
				 dy--;
			}
			super.move(dx,dy);

		}
		else{
			dx=2;
			dy=0;
		}
		super.move(dx,dy);
		
	}
	
	public void update(){
		super.update();
		// ver se bate nas plataformas
		// check if it hits a platform
		for( Plataform f : getWorld().getPlatforms() ){
			Rectangle enemyRect = getBounds();
			Rectangle platRect = f.getBounds();		
			Rectangle r = platRect.intersection(enemyRect);
			if(!r.isEmpty()){
				if(r.height <= 6 && r.y == platRect.y || r.height <= 6 && r.y== enemyRect.y){
					velY=-velY;
				} 
				else if (r.x == platRect.x){
					ComponenteMultiAnimado image = (ComponenteMultiAnimado) getImage();
					image.setAnim(1);
				}else{

					ComponenteMultiAnimado image = (ComponenteMultiAnimado) getImage();
					image.setAnim(0);
				}
			}				
		}
	
	}
}
