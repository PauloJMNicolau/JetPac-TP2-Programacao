package jetpac.world;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import prof.jogos2D.ComponenteVisual;

/**
 * Classe que define o comportamento normal de um elemento do mundo.
 * Esta clase não deve ser alterada.
 * Esta clase não deve ser alterada.
 * 
 * Class that defines the commom behaviour of a world element
 * This class should not be changed
 * This class should not be changed
 * @author F. Sérgio Barbosa
 */
public abstract class WorldElementDefault implements WorldElement {

	// a imagem do elemento do mundo
	// the image of the element
	private ComponenteVisual image; 
	
	// o mundo em que o elemento se move
	// the world where the element is at
	private World world;

	/** construtor do elemento, que define a imagem do elemento
	 * Element constructor that defines the element image
	 * @param image a imagem
	 */
	public WorldElementDefault( ComponenteVisual image ) {
		this.image = image;
	}
			
	/* (non-Javadoc)
	 * @see jetpac.world.WorldElement#getBounds()
	 */
	public Rectangle getBounds() {		
		return image.getBounds();
	}

	/* (non-Javadoc)
	 * @see jetpac.world.WorldElement#getImage()
	 */
	public ComponenteVisual getImage() {
		return image;
	}

	/* (non-Javadoc)
	 * @see jetpac.world.WorldElement#getPosition()
	 */
	public Point getPosition() {
		return image.getPosicao();
	}

	/* (non-Javadoc)
	 * @see jetpac.world.WorldElement#getWorld()
	 */
	public World getWorld() {
		return world;
	}

	/* (non-Javadoc)
	 * @see jetpac.world.WorldElement#setImage(prof.jogos2D.ComponenteVisual)
	 */
	public void setImage(ComponenteVisual img) {
		this.image = img;
	}

	/* (non-Javadoc)
	 * @see jetpac.world.WorldElement#setPosition(java.awt.Point)
	 */
	public void setPosition(Point pos) {
		image.setPosicao( pos );
	}

	/* (non-Javadoc)
	 * @see jetpac.world.WorldElement#setWorld(jetpac.world.World)
	 */
	public void setWorld(World w) {
		world = w;
	}
	
	/* (non-Javadoc)
	 * @see jetpac.world.WorldElement#draw(java.awt.Graphics2D)
	 */
	public void draw(Graphics2D g){
		image.desenhar( g ); 
	}
	
	/* (non-Javadoc)
	 * @see jetpac.world.WorldElement#move(int, int)
	 */
	public void move(int dx, int dy ){
		image.desloca(dx, dy);
	}
}
