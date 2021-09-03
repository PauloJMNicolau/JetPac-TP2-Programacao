package jetpac.world;

import java.awt.Graphics2D;
import java.util.Vector;
import java.util.Random;

import jetpac.astro.*;
import jetpac.drag.*;
import jetpac.enemy.*;
import jetpac.generator.*;
import jetpac.platform.*;

import prof.jogos2D.ComponenteVisual;

/**
 * Esta classe é responsável por manter toda a informação acerca do mundo
 * O único método desta classe que pode ser alterado é o getDraggables.
 * Todo o resto da classe, variáveis incluídas, NÃO PODE SER ALTERADA 
 * O único método desta classe que pode ser alterado é o getDraggables.
 * Todo o resto da classe, variáveis incluídas, NÃO PODE SER ALTERADA
 * 
 * class responsible for all world information
 * The only method in this class that can be changed is getDrabbagles.
 * The remainder of the class, fields included, CANNOT BE CHANGED 
 * The only method in this class that can be changed is getDrabbagles.
 * The remainder of the class, fields included, CANNOT BE CHANGED 
 */
public class World {

	private ComponenteVisual background;  // imagem de fundo do nível
	
	private Astronaut astronauta;         // o astronauta 
	
	// vectores com os vários elementos presentes no jogo
	// vector with all the game elements
	private Vector<Spaceship> ships = new Vector<Spaceship>();
	private Vector<Plataform> platforms = new Vector<Plataform>();
	private Vector<Laser> lasers = new Vector<Laser>();
	private Vector<Enemy> enemies = new Vector<Enemy>();

	// os vários geradores de elementos
	// the several element generators
	private TreasureGenerator treasureGen;
	private FuelGenerator fuelGen;
	private EnemyGenerator enemyGen;
	private Random randomGen = new Random();

	// dimensões do mundo
	// world dimensions
	private int width;
	private int height;
	
	// mundo completo?
	// completed world?
	private boolean completed;
	
	// pontuação em cada ciclo
	// score in each cicle
	private int ciclePoints;    
					
	// informação sobre o fuel
	// fuel stuff
	private int nFuels = 0;
	private Fuel fuel;
	
	// o tesouro presente no mundo
	// the treasure in the world
	private Treasure treasure;
	

	// os estados possíveis para o mundo
	// world possible states
	private final static int STARTING = 0;    // está a começar
	private final static int PLAYING = 1;     // está a jogar
	private final static int ENDING = 2;      // está a acabar por morte
	private final static int COMPLETING = 3;  // está a acabar por vitória
	
	// estado atual, inicializado com a começar
	// currents world state
	private int state = STARTING; 

	/** construtor do mundo
	 * world constructor
	 */
	public World( ){
		background = null;
	}
		
	/** construtor do mundo
	 * world constructor
	 * @param img imagem de fundo do mundo, background image
	 */
	public World( ComponenteVisual img ){
		background = img;
	}
	
	/** começar a jogar
	 * start playing
	 */
	public void play(){
		state = STARTING;
		astronauta.reset();
	}
	
	/** vai desenhar todos os elementos do mundo
	 * draws every element in the world
	 * @param g onde vai desenhar. g is the drawing system
	 */
	public synchronized void draw( Graphics2D g ){
		if( background != null )
			background.desenhar( g );
		
		if( state != COMPLETING )
			astronauta.draw( g );
		
		for( Plataform p : platforms )
			p.draw( g );
		
		for( Laser d : lasers )
			d.draw( g );

		for( Enemy e : enemies )
			e.draw( g );

		for( Spaceship n : ships )
			n.draw( g );
		
		if( treasure != null )
			treasure.draw( g );

		if( fuel != null )
			fuel.draw( g );
	}

	/**
	 * Actualiza todos os elementos do mundo e remove os elementos que já não são necessários
	 * Cada chamada a este método conta como um ciclo de processamento.
	 * 
	 * Updates all elements in the world and removes the dead ones
	 * Each call to this methos is a processing cicle
	 */
	public synchronized int update(){
		// reiniciar a pontuação do ciclo
		// reset cicle scoring
		ciclePoints = 0;

		if( state == COMPLETING ){
			// se está a completar apenas move as naves
			// if it is competing only moves the ships
			for( Spaceship n : ships ){
				n.move(0, -3 );
				if( n.getPosition().y <= 0 ){
					completed = true;
					return -1;
				}
			}			
		}
		else {
			for( Spaceship n : ships )
				n.update( );			
		}
		if( state == PLAYING ){
			// se está a jogar precisa de usar os geradores
			// when playing it needs to use the element generators
			treasureGen.update();
			fuelGen.update();
			enemyGen.update();
			
			astronauta.update( );
			
			for( Laser d : lasers )
				d.update( );			
		}

		for( Plataform f : platforms )
			f.update( );

		for( int i = 0; i < enemies.size(); i++ )
			enemies.get(i).update();
		
		if( treasure != null )
			treasure.update();

		if( fuel != null )
			fuel.update();
				
		// retirar os disparos que já estão desativos 
		for( int i= lasers.size()-1; i >= 0; i-- ){
			if( lasers.get(i).isDead() )
				lasers.remove( i );				
		}
		
		// retirar os inimigos que já estão desativos 
		for( int i= enemies.size()-1; i >= 0; i-- ){
			if( enemies.get(i).isDead() )
				enemies.remove( i );				
		}
		
		if( state == STARTING ){
			// não pode começar enquanto houver coisas a cair
			// it cannot start when things are still falling
			boolean start = true;
			if( treasure != null && treasure.isFalling() )
				start = false;
			if( fuel != null && fuel.isFalling() )
				start = false;
			for( Spaceship n : ships )
				if( n.isFalling() ){
					start = false;
					break;
				}
			if( start )
				start();
		}		
		if( state == ENDING ){
			return 0;
		}
		else
			return ciclePoints;
	}
	
	/** começa a jogar o mundo
	 * starts the world playing
	 */
	private void start() {
		state = PLAYING; 		
	}

	/**
	 * adicionar à pontuação
	 * add points to the cicle score 
	 * @param p número de pontos a adicionar. Amount of points to add
	 */
	public void addCiclePoints( int p ){
		ciclePoints += p;
	}

	/** retorna a imagem de fundo.<br>
	 * returns the background image.
	 * @return the background image
	 */
	public ComponenteVisual getBackground() {
		return background;
	}
	
	/**
	 * define a imagem de fundo <br>
	 * defines the background image
	 * @param fundo a nova imagem 
	 */
	public void setBackground(ComponenteVisual fundo) {
		this.background = fundo;
	}
	
	/** retorna o astronauta <br>
	 * returns the astronaut
	 * @return o astronauta
	 */
	public Astronaut getAstronaut() {
		return astronauta;
	}
	
	/** define o astronauta <br>
	 * defines the astronaut
	 * @param astronauta
	 */
	public void setAstronauta(Astronaut astronauta) {
		this.astronauta = astronauta;
		astronauta.setWorld( this );
	}

	/**
	 * adiciona uma nave ao mundo <br>
	 * adds a ship to the world
	 * @param s a nave a adicionar
	 */
	public void addSpaceship(Spaceship s) {
		ships.add( s );	
		s.setWorld( this );
	}

	/** retorna as naves  <br>
	 * returns the ships
	 * @return the ships
	 */
	public Spaceship[] getSpaceships() {		
		return ships.toArray( new Spaceship[ ships.size() ] );
	}
	
	/** retorna a nave principal <br>
	 * returns the main spaceship
	 * @return a nave principal
	 */
	public Spaceship getMainSpaceship() {
		return ships.get( 0 );
	}

	/**
	 * adiciona uma plataforma  <br>
	 * adds a platform
	 * @param p the platform to add
	 */
	public void addPlatform(Plataform p) {		
		platforms.add( p );
		p.setWorld( this );
	}

	/** retorna as plataformas  <br>
	 * returns the platforms
	 * @return the platforms
	 */
	public Vector<Plataform> getPlatforms(){
		return platforms;
	}

	/** adiciona um laser <br>
	 * adds a laser
	 * @param l o laser a adicionar
	 */
	public void addLaser(Laser l) {
		lasers.add( l );
		l.setWorld( this );
	}

	/** retorna todos os elementos que se podem arrastar <br>
	 * returns all elements that can be dragged
	 * @return todos os elementos que se podem arrastar
	 */
	public WorldElement[] getDraggables() {
		/**
		 * ESTE É O ÚNICO MÉTODO DESTA CLASSE QUE PODE SER ALTERADO
		 * TODOS OS OUTROS NÃO PODEM...
		 * 
		 * THIS IS THE ONLY METHOD THAT CAN BE CHANGED
		 * ANY OTHER METHOD CANNOT BE CHANGED
		 */
		Vector<WorldElement> drags = new Vector<WorldElement>();
		for( Spaceship n : ships ){
			if( n.isDraggable() )
				drags.add( n );			
		}
		if( hasTreasure() && getTreasure().isDraggable() )
			drags.add( getTreasure() );
		if( hasFuel() && getFuel().isDraggable() )
			drags.add( getFuel() );
		return drags.toArray( new WorldElement[drags.size()] );
	}

	/**
	 * retorna o gerador de números aleatórios
	 * return the random number generator
	 * @return o gerador de números aleatórios
	 */
	public Random getRandomGen() {
		return randomGen;
	}
	
	/** retorna o tesouro <br>
	 * returns the treasure
	 * @return o tesouro
	 */
	public Treasure getTreasure() {
		return treasure;
	}
	
	/**
	 * define o tesouro <br>
	 * defines the treasure
	 * @param treasure
	 */
	public void setTreasure(Treasure treasure) {
		this.treasure = treasure;
		if( treasure != null )
			treasure.setWorld( this );
	}

	/** ainda tem tesouro?
	 * has treasure?
	 * @return true se tem tesouro
	 */
	private boolean hasTreasure() {
		return treasure != null;
	}

	/** retorna o gerador de tesouros <br>
	 * return the treasure generator
	 * @param tg o gerador de tesouros
	 */
	public void setTreasureGenerator(TreasureGenerator tg) {
		treasureGen = tg; 		
	}

	/**
	 * retorna o fuel <br>
	 * returns the fuel
	 * @return
	 */
	public Fuel getFuel() {
		return fuel;
	}
	
	/** define o fuel <br>
	 * defines the fuel
	 * @param fuel
	 */
	public void setFuel(Fuel fuel) {
		this.fuel = fuel;
		fuel.setWorld( this );
	}
	
	/** define o gerador de fuel <br>
	 * define the fuel generator
	 * @param fg fuel generator
	 */
	public void setFuelGen(FuelGenerator fg) {
		fuelGen = fg;		
	}

	/** tem fuel? <br>
	 * @return true if it has fuel
	 */
	public boolean hasFuel() {
		return fuel != null;
	}

	/**
	 * o fuel foi entregue <br>
	 * the fuel was delivered
	 */
	public void fuelDelivered() {
		fuel = null;
		nFuels++;
	}
	
	/** retorna a percentagem de fuel já entregue (0 a 100) <br>
	 * return the percentage of delibered fuel (0 to 100)
	 * @return a percentagem de fuel
	 */
	public int getFuelPercentage(){
		return nFuels*100 / fuelGen.getMaxFuel(); 
	}

	/**
	 * o mundo está completo <br>
	 * the world is complete
	 */
	public void completed() {			
		state = COMPLETING;
		lasers.clear();
	}
	
	/** indica se já está completo <br>
	 * return if it is completed
	 * @return
	 */
	public boolean isCompleted() {
		return completed;
	}

	/** indica se já perdeu <br>
	 * returns if it is over
	 * @return se já perdeu
	 */
	public boolean isOver() {		
		return astronauta.isDead();
	}

	/** o mundo está a acabar por morte <br>
	 * the world is ending due to game over
	 */
	public void dying() {
		state = ENDING;
		lasers.clear();
		enemies.clear();
	}

	/**
	 * define o gerador de inimigos.
	 * sets the enemy generator
	 * @param eg o novo gerador de inimigos
	 */
	public void setEnemyGen(EnemyGenerator eg) {
		enemyGen = eg;		
	}

	/**
	 * adiciona um inimigo ao mundo.
	 * adds an enemy to the world.
	 * @param e o inimigo a adicionar
	 */
	public void addEnemy( Enemy e ){
		enemies.add( e );
		e.setWorld( this );
	}

	/** retorna o número de inimigos
	 * returns the number of enemies
	 * @return the number of enemies
	 */
	public int getNumEnemies() {
		return enemies.size();
	}

	/** retorna os inimigos
	 * returns the enemies
	 * @return os inimigos
	 */
	public Vector<Enemy> getEnemies() {
		return enemies;
	}
	
	/**
	 * define as dimensões do mundo <br>
	 * defines the world dimensions
	 * @param width comprimento do mundo
	 * @param height altura do mundo
	 */
	public void setDimensions(int width, int height ) {
		this.width = width ;
		this.height = height ;
	}
	
	/**
	 * devolve o comprimento
	 * @return the world width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * devolve a altura 
	 * @return the world height
	 */
	public int getHeight() {
		return height;
	}
}
