package jetpac.platform;

import java.awt.Rectangle;

import jetpac.astro.Astronaut;
import prof.jogos2D.ComponenteVisual;

public class Eletrica extends Plataform{

	private int ciclo=0;

	public Eletrica(ComponenteVisual img) {
		super(img);
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
				ciclo++;
				if (ciclo>=90){
					astro.die();
					ciclo=0;
				}
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
			
		}else
			ciclo=0;
		
	}

}
