package jetpac.drag;

import java.awt.Point;
import java.awt.Rectangle;

import jetpac.platform.Plataform;
import jetpac.world.WorldElementDefault;

import prof.jogos2D.ComponenteVisual;

/** classe que representa o combustível a ser carregado para a nave
 * Class that represents the fuel to supply the ship
 * 
 * @author F. Sérgio Barbosa
 */
public class Fuel extends DraggableDefault {

	/**
	 * cria um fuel
	 * @param p
	 * @param img
	 */
	public Fuel( Point p, ComponenteVisual img ){
		super( img );
		img.setPosicao( p );
	}
	
	public void update(){
		super.update();
		// já chegou à nave?
		// has it arrived to the ship?
		if( getWorld().getMainSpaceship().getBounds().intersects( getBounds() )){
			setFalling( false );
			getWorld().fuelDelivered();
		}
	}

}
