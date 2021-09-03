package jetpac.drag;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import jetpac.platform.Plataform;
import jetpac.world.WorldElementDefault;

import prof.jogos2D.ComponenteVisual;

/**
 * Classe que representa a nave a as suas partes.
 * Em caso de haver v�rias partes a nave � considerada a parte com o �ndice 0
 * 
 * Class that represents the spaceship and its parts.
 * If there are several parts the ship is considered the one with part index 0 
 * 
 * @author F. S�rgio Barbosa
 *
 */
public class Spaceship extends DraggableDefault {

	private int partIdx;			// ind�ce da parte da nave. Part number index
	private int nParts;				// n�mero de partes da nave. Total number of parts
	private int nextPartDue;        // indica qual a pr�xima parte a ser precisa    
	private Rectangle dropArea;		// zona de descarga da nave. dropArea of the ship
	private int nextPartBottom;		// zona onde liga a pr�xima parte. where the next part fits
	
	/** Cria uma nave
	 *  Creates a spaceship 
	 * @param nParts quantas partes constituem a nave, the ship number of parts
	 * @param partIdx o �ndice desta parte, this part index
	 * @param img a imagem da nave. The ship image
	 */
	public Spaceship(int nParts, int partIdx, ComponenteVisual img) {
		super( img );
		this.partIdx = partIdx;
		this.nParts = nParts;
		nextPartBottom = img.getPosicao().y;
		nextPartDue = nParts > 0? 1: 0; 
		
		// se for a parte com �ndice 1 ent�o fica arrast�vel
		// only the part with index 1 is draggable in the beggining 
		setDraggable( partIdx == 1 );
	}
	
	/** Indica se a nave est� completa
	 * Check if the ship is complete
	 * @return
	 */
	public boolean isComplete(){
		return nParts == nextPartDue;
	}

	/** indica o n�mero da parte da nave
	 * Returns the part index
	 * @return o n�mero da parte da nave
	 */
	public int getPartIdx() {
		return partIdx;
	}
	
	/** indica qual a pr�xima parte a ser precisa para a nave
	 * indicates which is the next part to complete the ship 
	 * @return the next part to complete the ship
	 */
	public int getNextPartDue(){
		return nextPartDue;
	}
	

	/** devolve a zona de descarga da nave.
	 * return the ship drop area
	 * @return the ship drop area
	 */
	public Rectangle getDropArea(){
		if( dropArea == null ){
			Rectangle r = getBounds();
			// a zona de descarga tem metade da alrgura da nave
			// the drop area is half the width of the ship
			int width = r.width / 2;
			dropArea = new Rectangle( r.x + width/2, 0, width, r.y + r.height);
			
			// ver se a drop area est� coberta or alguma plataforma
			// check if the drop area is covered by some platform
			for( Plataform p : getWorld().getPlatforms() ){
				Rectangle rp = p.getBounds(); 
				if( p.getBounds().intersects( dropArea ) )
					dropArea.y = rp.y + rp.height;
			}
		}
		return dropArea;
	}
	

	/**
	 * indica se � arrast�vel ou n�o
	 * indicates if the ship is draggable
	 * @return true se a parte for careg�vel
	 */
	public boolean isDraggable() {		
		return partIdx == getWorld().getMainSpaceship().getNextPartDue() && super.isDraggable();
	}
	
	
	/** atualiza a nave
	 * updates the ship
	 */
	public void update( ){
		super.update();
		// ver se est� na drop area
		// is it over the srop area?
		if( nParts > 1 && getWorld().getMainSpaceship().getBounds().intersects( getBounds() )){
			setFalling( false );
			getWorld().getMainSpaceship().addParte( this );
		}
	}
	
	/** adiciona uma parte � nave. Este m�todo s� deve ser chamado para a nave e n�o
	 * para as partes.
	 * 
	 * Adds a part to the ship. This method should be called only for the ship and not for the parts
	 * 
	 * @param p the part to add
	 */
	private void addParte( Spaceship p ){
		p.setDraggable( false );
		if( this == p ) return;
		nextPartBottom -= p.getImage().getAltura(); 
		p.setPosition( new Point( p.getPosition().x, nextPartBottom ) );

		nextPartDue++;
		if( !isComplete() )
			getWorld().getSpaceships()[ p.getPartIdx()+1 ].setDraggable( true );
	}
}
