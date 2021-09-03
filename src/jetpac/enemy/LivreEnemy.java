package jetpac.enemy;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

import jetpac.astro.Laser;
import jetpac.platform.Plataform;
import prof.jogos2D.ComponenteMultiAnimado;

public class LivreEnemy extends Enemy{

	
	private static final int LEFT = 1;
	private int ciclo;
	private Point ponto;
	private int x;
	private String e;
	public LivreEnemy(Point p, int vel, int score, int dir,
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
			die();
		ciclo++;
		if (ciclo > 1500){
				while (getWorld().getNumEnemies() <20){
					ponto= getPosition();
					x = getWorld().getRandomGen().nextInt(4);
					switch(x){
					case 0: getWorld().addEnemy(new RicocheteEnemy(ponto, 4, 20, 1,
							img));
						break;
					case 1: getWorld().addEnemy(new FloatEnemy(ponto, 4, 20, 1,
							img));
						break;
					case 2:
						getWorld().addEnemy(new LivreEnemy(ponto, 4, 20, 1,
								img));
						break;
					default:
						getWorld().addEnemy(new LinearEnemy(ponto, 4, 20, 1,
								img));
					
					}
					
				}
		}
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
