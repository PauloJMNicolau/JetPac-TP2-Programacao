package jetpac.platform;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import jetpac.astro.Astronaut;
import jetpac.world.WorldElementDefault;
import prof.jogos2D.ComponenteVisual;

/**
 * Esta classe é responsável pelas plataformas do jogo
 * 
 * Class responsible for the game platforms
 *  
 * @author F. Sérgio Barbosa
 *
 */
public class Plataform extends WorldElementDefault {

	 /*
	 * * cria uma plataforma
	 * @param img imagem da plataforma
	 * @param comp comprimento da plataforma
	 */
	public Plataform(ComponenteVisual img) {
		super( img );
	}
	
	/**
	 * atualiza a plataforma
	 * updates the platform
	 */
	public void update(){
		Astronaut astro = getWorld().getAstronaut();
		Rectangle astroRect = astro.getBounds();
		Rectangle platRect = getBounds();		
		Rectangle r = platRect.intersection( astroRect );
		
		// ver de que lado bateu o astronauta
		// from which side was the hit?
		if( !r.isEmpty() ){
			// bateu na parte superior
			// top 
			if( r.height <= 6 && r.y == platRect.y ){
				astro.move( 0, -r.height );
				astro.setJetPacOn( false );
			}
			// bateu na parte inferior
			// bottom
			else if( r.height <= 6 && r.y == astroRect.y ){
				astro.move( 0, r.height );
			} 
			// bateu no lado esquerdo
			// left
			else if( r.x == platRect.x ){
				astro.move( -r.width, 0 );
			}
			// bateu no lado direito
			// right
			else {
				astro.move( r.width, 0 );
			}
			
		}
		
	}
	
}
