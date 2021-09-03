package jetpac.astro;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import jetpac.drag.DraggableDefault;
import jetpac.drag.Fuel;
import jetpac.drag.Spaceship;
import jetpac.drag.Treasure;
import jetpac.world.WorldElement;
import jetpac.world.WorldElementDefault;
import prof.jogos2D.ComponenteMultiAnimado;

/**
 * Esta classe representa o astronauta do jogo. Pode andar, disparar, carregar items, etc
 * 
 * This class represents the game astronaut. It can walk, shoot, drag items, etc
 * 
 * @author F. Sergio Barbosa

 */
public class Astronaut extends WorldElementDefault {

	// as direcções do jogo (podiam estar numa enumeração)
	// the game directions (could be in an enum)
	public static final int RIGHT = 0;
	public static final int LEFT = 1;
	
	//public Tecla t = new Tecla(1);
		
	private Point initialPos;           // posição inicial do astronauta, initial position of the astronaut
	private int dir;            		// direção atual, actual direction
	private boolean jetPacOn = false;   // tem o jetpac ligado?
	private boolean rising = false;     // está a subir?
	private boolean shooting = false;   // está a disparar?
	private boolean walking = false;    // esta a andar?
	private boolean dead = false;       // está morto?
	
	private final int shootSpeed = 4;    // velocidade de disparo
	private int nextShot = 0;			 // temporizador de disparo
	private int rangeIdx = 0;            // indíce do alcance do laser, laser range index 
	private int offsetDispY;             // ponta da arma relativa à imagem do astronauta
	                                     // gun position relative to the astronaut image
	// alcances do laser para dar um "efeito especial"
	// ranges of the laser gun, to give a "special effect"
	private int ranges[] = { 150, 300, 450, 600, 650, 700 };
	
	// indicação do que está a carregar
	private DraggableDefault dragged;

	/**
	 * cria o astronauta
	 * Creates the astronaut
	 * 
	 * @param image imagem representativa do astronauta. Image of the astronaut
	 * @param pos posição inicial do astronauta. Its initial prosition
	 * @param dir direção inical do astronauta. Its initial direction
	 * @param offShoot localização da arma relativa à imagem. Gun point position relative to the image
	 */
	public Astronaut(ComponenteMultiAnimado image, Point pos, int dir, int offShoot ) {
		super( image );
		initialPos = (Point) pos.clone();
		image.setPosicao( pos );
		this.dir = dir;
		offsetDispY = offShoot;
	}
	
	public Astronaut(ComponenteMultiAnimado image, Point pos, int offShoot ) {
		this( image,  pos, LEFT, offShoot );
	}

	
	public int getDirection() {
		return dir;
	}

	public void setDirection(int dir) {
		this.dir = dir;
	}

	public void setJetPacOn( boolean v ){
		jetPacOn = v;
	}

	public boolean isJetPacOn() {
		return jetPacOn;
	}

	public void setRising( boolean s ){
		rising = s;
	}
	
	public boolean isRising() {
		return rising;
	}
	
	public boolean isShooting() {
		return shooting;
	}
	
	public void setShooting(boolean s) {
		shooting = s;
		if( !shooting )
			rangeIdx = 0;
	}
	
	public void setWalking( boolean andar ){
		this.walking  = andar;
	}

	/**
	 * desenha o astronauta
	 * draws the astronaut
	 */
	public void draw(Graphics2D g) {
		ComponenteMultiAnimado img = (ComponenteMultiAnimado)getImage();
		if ( img.getAnim() != 4 ) {
			int anim = getDirection() == LEFT? 0 : 1;
			if( !jetPacOn ) anim += 2;
			img.setAnim( anim );
			if( !walking && !jetPacOn )
				img.setFrameNum( 0 );
		}
		super.draw(g);
	}
	
	/** indica se está a arrastar qualquer coisa
	 * @return true if it is dragging something
	 */
	public boolean isDragging(){
		return dragged !=null;
		//return draggedFuel != null || draggedTreasure != null || draggedShip != null;
	}
	
	/** atualiza o astronauta num ciclo de processamento
	 * updates the astronaut in a processing cicle
	 */
	public void update( ){
		// se está morto não faz nada
		// is its dead it does nothing
		if( dead ) return;
		
		// ver que movimento está a fazer
		// see what movement it is doing
		int dy = rising? -6: 6;
		if( walking && rising )
			move( (dir==LEFT? -6: 6), dy);
		else if( walking )
			move( (dir==LEFT? -4: 4), dy);
		else
			move( 0, dy );
		
		if( shooting )
			fire();
		
		// ver o rectangulo envolvente para ver se bate em alguma coisa
		// see the bouding rectangle to check for colisions
		Rectangle ra = getBounds(); 
		
		// se não está a carregar ver se toca em algum carragável
		// if it is not draggin, see if it touches any draggable
		if( !isDragging() ){
			for( WorldElement t : getWorld().getDraggables() ){				
				Rectangle rt = t.getBounds();
				if( rt.intersects( ra ) ){	
					DraggableDefault d = (DraggableDefault) t;
					dragged = d;
					d.pickUp();
					int dtx = (ra.x - rt.x) + (ra.width - rt.width)/2;
					int dty = (ra.y - rt.y) + (ra.height - rt.height);
					t.move(dtx, dty );
					break;
				}
			}
		}
		
		// se está a arrastar, verificar se está sobre a drop area
		// if it is dragging then check if its over the drop area
		if( isDragging() ){
			Rectangle drop = getWorld().getMainSpaceship().getDropArea();
			Rectangle rt = dragged.getBounds(); 
			if( drop.intersects( rt ) ){
				dragged.release();
				int dtx = (drop.x - rt.x) + (drop.width - rt.width)/2;
				dragged.move(dtx, 0 );
				dragged = null;				
			}		
		}		
		
		// se a nave está cheia verificar se bate na nave para passar o nível
		// if the ship is full then test if it hits the ship to win the level
		if( getWorld().getFuelPercentage() == 100 ){
			Spaceship n = getWorld().getMainSpaceship();
			if( n.getBounds().intersects( getBounds() ) ){
				getWorld().completed();
				setPosition( new Point(-100,-100) );
			}
		}
		setJetPacOn( true );
	}

	/** move o astronauta
	 * move the astronaut
	 * @param dx a distância a mover em x. The x distance to move
	 * @param dy a distância a mover em y. The y distance to move
	 */
	public void move(int dx, int dy ) {
		if( getImage().getPosicao().y + dy < 0 )
			dy = -getImage().getPosicao().y;
		
		if( getImage().getPosicao().x < 0 )
			dx += getWorld().getWidth();
		else if( getImage().getPosicao().x > getWorld().getWidth() )
			dx -= getWorld().getWidth();
		
		super.move( dx, dy );	
		
		// mover também o arrastado
		// also move the dragged
		if( isDragging() ){
			
			if( dragged != null )
				dragged.move( dx, dy );
		}
	}
	
	/** larga o que está a carregar
	 * drops what it is dragging
	 */
	public void drop(){
		if( !isDragging() )
			return;
		if( dragged != null ){
			dragged.release();
			dragged = null;
		}
	}

	/** dispara
	 * fires
	 */
	private void fire(){
		// se já chegou a altura de disparar
		// if its time to shoot
		if( nextShot <= 0 ){
			// definir o alcance deste tiro
			// define the shot range
			rangeIdx++;
			if( rangeIdx >= ranges.length )
				rangeIdx = 0;
			int x = dir == LEFT? getPosition().x: getPosition().x + getImage().getComprimento();   
			int y = getPosition().y + offsetDispY;
			
			// criar e adicionar o laser ao mundo
			// create and add the laser to the world
			getWorld().addLaser( new Laser( new Point(x,y),  dir==LEFT? -ranges[rangeIdx]: ranges[rangeIdx] ) );

			// reinicializar o contador
			// reset the timer
			nextShot = shootSpeed;			
		}
		else
			nextShot--;
	}

	/** "mata" o astronauta
	 * "kills" the astronaut
	 */
	public void die() {
		if( dead )
			return;

		// selecionar a animação de morte
		// select the dying animation
		ComponenteMultiAnimado img = (ComponenteMultiAnimado)getImage();
		img.setAnim( 4 );
		img.setFrameNum( 0 );
		img.setRepeat( false );
		dead = true;
		
		// avisar o mundo da "morte do artista"
		// tell the world that it is ending
		getWorld().dying();
		
		// se está a carregar tem de soltar a carga
		// is its dragging then it needs to release the cargo
		drop();
	}
	
	/** informa se está completamente morto. Só está completamente morto se
	 * a animação de morte estiver concluida
	 * indicates if the astonaut is dead. It is only dead if the dying
	 * animation is completly over
	 * @return true se o astronauta está morto
	 */
	public boolean isDead() {
		ComponenteMultiAnimado img = (ComponenteMultiAnimado)getImage();
		return dead	&& img.jaAcabou();
	}

	/** "revive" o astronauta
	 * revives the astronaut 
	 */
	public void reset() {
		setPosition( (Point)initialPos.clone() );
		dead = false;
		// coloca a animação no início
		// resets the animation
		ComponenteMultiAnimado img = (ComponenteMultiAnimado)getImage();
		img.setAnim(0);
		img.setRepeat( true );
	}
}
