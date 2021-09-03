package jetpac.drag;

import java.awt.Point;
import java.awt.Rectangle;

import jetpac.platform.Plataform;
import jetpac.world.WorldElementDefault;

import prof.jogos2D.ComponenteVisual;

/**
 * classe que prepresenta um tesouro que aparece no mundo.
 * Os tesouros aumentam a pontua��o do jogador, mas, se n�o forem
 * apanhados, desaparecem ap�s algum tempo
 * 
 * Class that represents a treasure in the world.
 * Treasures improve player score but they disappear after a short time
 * if they are not grabbed
 * 
 * @author F. S�rgio Barbosa
 *
 */
public class Treasure extends DraggableDefault {
	
	private int lifeTime;              // tempo de vida
	private int score;    			   // pontua��o	
	
	/**
	 * Cria um tesouro
	 * @param p posi��o onde vai ser criado
	 * @param lifeTime tempo de vida
	 * @param score pontua��o
	 * @param img imagem
	 */
	public Treasure(Point p, int lifeTime, int score, ComponenteVisual img) {
		super( img.clone() );
		this.lifeTime = lifeTime;
		this.score = score;
		setPosition( p );
	}

	


	

	/** atualiza o tesouro
	 * update the treasure
	 */
	public void update(){
		// o tempo de vida s� conta enuqanto n�o for apanhado
		// life time only decreases if it is not picked up 
		if( isDraggable() ){
			lifeTime--;
			if( lifeTime <= 0 )
				getWorld().setTreasure( null );
		}
		super.update();
		// ver se j� chegou � nave
		// has it reached the ship?
		if( getWorld().getMainSpaceship().getBounds().intersects( getBounds() )){
			setFalling( false );
			getWorld().addCiclePoints( score );
			getWorld().setTreasure( null );
		}
	}
	
	/**
	 * devolve a pontua��o do tesouro
	 * @return the treasure score
	 */
	public int getScore(){
		return score;
	}
}
