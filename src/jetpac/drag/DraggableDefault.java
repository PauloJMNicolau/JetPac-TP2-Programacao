package jetpac.drag;

import java.awt.Rectangle;

import prof.jogos2D.ComponenteVisual;
import jetpac.platform.Plataform;
import jetpac.world.WorldElementDefault;

public class DraggableDefault extends WorldElementDefault implements Draggable{
	
	protected boolean falling = true;    // está a cair?
	protected boolean draggable = true;  // está a ser carregado?

	public DraggableDefault(ComponenteVisual image, boolean falling,
		boolean draggable) {
		super(image);
		this.falling = falling;
		this.draggable = draggable;
	}
	public DraggableDefault(ComponenteVisual img) {
		super(img);// TODO Apêndice de construtor gerado automaticamente
	}
	/** este elemento foi apanhado
	 * this was pickedup
	 */
	public void pickUp() {
		setDraggable( false );
		setFalling( false );
	}
	
	/** elemento foi libertado
	 * element was released
	 */
	public void release() {
		setFalling( true );
	}

	/** define se o elemento pode ser, ou não, arrastado
	 * defines if the element can, or not, be dragged
	 * @param d draggable new state
	 */
	public void setDraggable(boolean d) {
		draggable = d;
	}

	
	/** testa se o elemento pode ser arrastado
	 * tests if the element can be dragged
	 * @return true se o elemento pode ser arrastado
	 */
	public boolean isDraggable() {
		return draggable;		
	}
	
	/** define se o elemento está a cair
	 * defines if the element is falling
	 * @param f o novo estado da queda
	 */
	public void setFalling( boolean f ){
		falling = f;
	}
	
	/** indica se está a cair
	 * tells if it is falling
	 * @return true se está a cair
	 */
	public boolean isFalling() {		
		return falling;
	}
	
	/** atualiza o objeto
	 */
	public void update(){
		if( !isFalling() )
			return;
	
		// cai e deteta se bateu nas plataformas
		// falls and checks hits on platforms
		move( 0, 2 );
		for( Plataform p : getWorld().getPlatforms() ){
			Rectangle inter = p.getBounds().intersection( getBounds() ); 
			if( !inter.isEmpty() ){
				move( 0, -inter.height );
				falling = false;
				draggable = true;
			}
		}

	}	
	
}
