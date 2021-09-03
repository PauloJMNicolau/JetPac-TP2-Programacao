package jetpac.world;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import prof.jogos2D.ComponenteVisual;

/**
 * Esta interface define um elemento que est� no mundo.
 * Esta interface N�O DEVE SER ALTERADA
 * Esta interface N�O DEVE SER ALTERADA
 * 
 * This interface defines an element present in the world
 * This interface CANNOT BE CHANGED
 * This interface CANNOT BE CHANGED
 * 
 * @author F. S�rgio Barbosa
 */
public interface WorldElement {

	/** define o mundo em que vai estar metido o elemento <br>
	 * define the world where this element is living
	 * @param w o mundo
	 */
	public void setWorld( World w );
	
	/** retorna o mundo onde est� o elemento
	 * @return the world where the element is
	 */
	public World getWorld( );
	
	/** devolve a imagem do elemento 
	 * @return the image of the element
	 */
	public ComponenteVisual getImage();
	
	/** define a imagem do elemento
	 * define the element of the image
	 * @param img the image
	 */
	public void setImage(ComponenteVisual img);

	/**
	 * retorna a posi��o do elemento
	 * @return the position of the element
	 */
	public Point getPosition();
	
	/** define a posi��o do elemento
	 * @param pos the position of the element
	 */
	public void setPosition(Point pos);

	/**
	 * retorna o rect�ngulo envolvente do elemento. �til para detetar as colis�es.<br>
	 * returns the bounding rectangle of the element. Useful to detect collisions.
	 * @return
	 */
	public Rectangle getBounds();
	
	/** desloca o elemento. <br>
	 * moves the element.
	 * @param dx deslocamento em x, x movement
	 * @param dy deslocamento em y, y movement
	 */
	public void move(int dx, int dy );
	
	/** desenha o elemento. <br>
	 * draws the element.
	 * @param g onde desenhar. Where to draw
	 */
	public void draw(Graphics2D g);
	
	/** atualiza o elemento. M�todo chamado a cada ciclo de processamento.<br>
	 * updates the element. Method called at each processing cicle
	 */
	public void update( );
}
