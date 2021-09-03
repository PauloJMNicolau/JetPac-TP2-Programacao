package jetpac.generator;

import java.awt.Point;

import jetpac.astro.Astronaut;
import jetpac.enemy.Enemy;
import jetpac.enemy.FloatEnemy;
import jetpac.enemy.FollowEnemy;
import jetpac.enemy.LinearEnemy;
import jetpac.enemy.LivreEnemy;
import jetpac.enemy.RicocheteEnemy;
import jetpac.world.World;
import prof.jogos2D.ComponenteMultiAnimado;

/**
 * Esta classe é responsável por gerar os inimigos de cada nível. 
 * 
 * This class is responsible for the creation of the enemies in each level. 
 * 
 * @author F. Sergio
 *
 */
public class EnemyGenerator {

	private static final int creationCicle = 20;  // a criação é de 20 em 20 ciclos 
	
	private String type;					// o tipo de inimigo a criar
	private int maxEnemys;                  // número máximo de inimigos que podem existir simultaneamente 
	private int enemyVel;                   // a velocidade de cada inimigo                     
	private ComponenteMultiAnimado img;     // a imagem que representa o inimigo
	private World world;          			// o mundo onde serão adicionados os inimigos
	private int nextCreation = creationCicle;  // o ciclo de criação é de 20 ciclos
	private int enemyScore;                 // a pontuação de cada inimigo

	/**
	 * Cria um gerador de inimigos 
	 * @param type tipo dos inimigos a criar. Type of the enemies to create
	 * @param maxEnemys número máximo de inimigos simultaneos. Maximum number of enemies at any given time
	 * @param enemyVel velocidade dos inimigos. Enemy velocity.
	 * @param enemyScore pontuação de cada inimigos. Enemy scoring points
	 * @param img imagem do inimigo. Enemy image.
	 * @param world mundo onde colocar os inimigos. World where the enemies are placed.
	 */
	public EnemyGenerator(String type, int maxEnemys, int enemyVel, int enemyScore, ComponenteMultiAnimado img, World world) {
		this.maxEnemys = maxEnemys;
		this.enemyVel = enemyVel;
		this.img = img;
		this.world = world;
		this.enemyScore = enemyScore;
		this.type = type;
	}

	/**
	 * Método que trata da criação dos inimigos, se for altura de os criar.
	 * Method that handles enemy creation, when it is necessary.
	 */
	public void update(){
		// verificar se é preciso criar inimigos
		// check if it is necessary to create enemies
		if( world.getNumEnemies() >= maxEnemys )
			return;
			
		// verificar se já é tempo de os criar
		// check if it is time to create
		if( nextCreation <= 0 ){
			// criar sempre metade dos inimigos mais 1, para não criar todos de uma vez
			// always create half + 1 of the required enemies, so they are not created all at the same time
			for( int i = 0; i <= (maxEnemys - world.getNumEnemies()) / 2; i++ ){
				// escolher aleatoriamente a coordenadda y onde vai aparecer o inimigo 
				// randomly pick the y coordinate of the enemy 
				int y = world.getRandomGen().nextInt( world.getHeight() - img.getAltura() );
				
				// escolher se aparece do lado esquerdo ou direito (1= direito, 0 = esquerdo)
				// choosing between the left or the right side (1= right, 0 = left)
				int r = world.getRandomGen().nextInt( 2 );				
				int dir;
				Point pos;
				if( r == 0 ){
					dir = Astronaut.RIGHT;   // move-se para a direita
					pos =  new Point( 0, y); // aparece do aldo esquerdo
				}
				else {
					dir = Astronaut.LEFT;                   // moves to the left
					pos = new Point( world.getWidth(), y ); // appears in the right side 
				}
				
				// verificar qual o tipo de iinimigo a criar
				// check which type of enemy to create
				Enemy e;				
				if( type.equals("linear") )
					e = new LinearEnemy( pos, enemyVel, enemyScore, dir, img );
				else if( type.equals("ricochete") )
					e = new RicocheteEnemy( pos, enemyVel, enemyScore, dir, img );
				else if(type.equals("flutuantes"))
					e = new FloatEnemy( pos, enemyVel, enemyScore, dir, img );
				else if(type.equals("perseguidor"))
					e = new FollowEnemy( pos, enemyVel, enemyScore, dir, img );
				else if(type.equals("livre"))
					e = new LivreEnemy( pos, enemyVel, enemyScore, dir, img );
				else
					e = new Enemy( pos, enemyVel, enemyScore, dir, img );
				
				
				
				// adicionar o inimigo ao mundo
				// add the enemy to the world
				world.addEnemy( e );
				
				// reiniciar o contador de criação
				// reset the creation timer
				nextCreation = creationCicle;
			}
		}
		// se não for ainda altura de criar decrementa o temporizador
		// is its not time to create, then decrement the timer
		else
			nextCreation--;
	}

}
