package jetpac.enemy;

import java.awt.Point;
import java.awt.Rectangle;

import jetpac.platform.Plataform;
import prof.jogos2D.ComponenteMultiAnimado;

public class LinearEnemy extends Enemy {

	public LinearEnemy(Point p, int vel, int score, int dir,
		ComponenteMultiAnimado img) {
		super(p, vel, score, dir, img);
	}
	
	/**
	 * desloca o inimigo
	 * moves the enemy
	 * @param dx dist�ncia a mover em x. x distance to move
	 * @param dy dist�ncia a mover em y. y distance to move
	 */
	public void move(int dx, int dy ) {
		ComponenteMultiAnimado img = (ComponenteMultiAnimado) getImage();

		// se bater na parte superior morre
		// if it hits the top it dies
		if( img.getPosicao().y + dy < 0 )
			die();
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
			Rectangle r = f.getBounds();
			if( r.intersects( getBounds() )){
				die();
			}				
		}
	
	}

}
