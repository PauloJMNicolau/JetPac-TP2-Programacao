package jetpac.enemy;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

import prof.jogos2D.ComponenteMultiAnimado;
import jetpac.astro.Astronaut;
import jetpac.platform.Plataform;
import jetpac.world.WorldElementDefault;

/**
 * classe que representa um inimigo
 * 
 * Class that represents an enemy
 * 
 * @author F. S�rgio Barbosa
 */
public class Enemy extends WorldElementDefault {

	private boolean dead = false;  // est� morto?
	protected int velX, velY;        // velocidade do inimigo em X e em Y  
	private int score;             // pontua��o do inimigo
	/**
	 * cria um inimigo
	 * 
	 * @param p posi��o inicial
	 * @param vel velocidade inicial
	 * @param score pontua��o
	 * @param dir direc��o en que est� virado
	 * @param img imagem
	 */
	public Enemy( Point p, int vel, int score, int dir, ComponenteMultiAnimado img ){
		super( img.clone() );
		ComponenteMultiAnimado image = (ComponenteMultiAnimado) getImage();

		// ver qual anima��o a usar
		// compute the animation to use
		if( dir == Astronaut.LEFT ){
			image.setAnim( 0 );
			velX = -vel;  
		}
		else {
			image.setAnim( 1 );
			velX = vel;
		}
		image.setPosicao( p );
		this.score = score;
		Random r = new Random();
		// a velocidade em Y � aleta�ria
		// Y speed is random
		velY = vel/2 - r.nextInt( vel );
	}

	/** atualiza o inimigo
	 * updates the enemy
	 */
	public void update(){
		// se est� morto n�o faz nada
		if( dead ) return;		
		
		move( velX, velY);	
	
		// ver se bate no astronauta
		// check if it hits the astronaut
		Rectangle ra = getWorld().getAstronaut().getBounds();
		if( ra.intersects( getBounds() )){
			die();
			getWorld().getAstronaut().die();
		}		
	}
	
	/**
	 * indica se o inimigo est� morto.
	 * S� est� morto se a anima��o de morte j� acabou.
	 * 
	 * indicates if the enemy is dead.
	 * it's only dead when the death animation is over
	 * @return true if it's dead
	 */
	public boolean isDead() {
		ComponenteMultiAnimado img = (ComponenteMultiAnimado) getImage();
		return dead && img.jaAcabou();
	}
	
	/** indica se o inimigo est� a morrer
	 * tests if the enemy is dying
	 * @return true se estiver a morrer
	 */
	public boolean isDying(){
		return dead;
	}
	
	/**
	 * desloca o inimigo
	 * moves the enemy
	 * @param dx dist�ncia a mover em x. x distance to move
	 * @param dy dist�ncia a mover em y. y distance to move
	 */
	public void move(int dx, int dy ) {
		ComponenteMultiAnimado img = (ComponenteMultiAnimado) getImage();
		int y= img.getPosicao().y;
		// se chegar a uma das extremidades passa para a outra
		// if it reaches a side it goes to the other
		if( img.getPosicao().x < 0 )
			img.setPosicao( new Point(getWorld().getWidth(), y) );
		else if( img.getPosicao().x > getWorld().getWidth() )
			img.setPosicao( new Point(0, y) );
		
		super.move( dx, dy );
		
	}

	/** mata o inimigo
	 * kills the enemy
	 */
	public void die(){
		if( dead ) return;
		dead = true;
		
		// passar para a anima��o de morte
		// sets the death animation
		ComponenteMultiAnimado img = (ComponenteMultiAnimado) getImage();
		img.setAnim( 2 );
		img.setFrameNum( 0 );
		img.setRepeat( false );
	}
	
	/** retorna a pontua��o do inimigo
	 * return the enemy score
	 * @return the enemy score
	 */
	public int getScore() {
		return score;
	}
}
