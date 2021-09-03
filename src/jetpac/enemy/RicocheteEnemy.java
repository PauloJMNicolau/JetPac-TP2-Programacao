package jetpac.enemy;

import java.awt.Point;
import java.awt.Rectangle;

import jetpac.platform.Plataform;
import prof.jogos2D.ComponenteMultiAnimado;

public class RicocheteEnemy extends Enemy {

	public RicocheteEnemy(Point p, int vel, int score, int dir,
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

		// se bater na parte superior morre
		// if it hits the top it dies
		if( img.getPosicao().y + dy < 0 )
			velY=-velY;
		super.move(dx, dy);
	}
	/** atualiza o inimigo
	 * updates the enemy
	 */
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
					image.setAnim(0);
					velX=-velX;
					
				}else{

					ComponenteMultiAnimado image = (ComponenteMultiAnimado) getImage();
					image.setAnim(1);
					velX=-velX;
				}
			}				
		}
	
	}

}