package jetpac.generator;

import jetpac.drag.Treasure;
import jetpac.world.World;

import java.awt.Point;

/**
 * Classe respons�vel pela cria��o de tesouros.
 * Cada tesouro s� pode ser criado se n�o huve mais tesouros no mundo.
  * O tempo entre um tesouro ser colocado na nave e a cria��o do pr�ximo � aleat�rio, mas existe
 * um tempo m�nimo e um tempo m�ximo para esse tempo.
 * 
 * 
 * Class responsible for creating treasures.
 * There cannot be a treasure in the world before another can be created. The time between
 * it is stored and another appears is random, but there is a minimum and maximum
 * amount of time to wait.
 *  
 * @author F. S�rgio
 */
public class TreasureGenerator{

	private int minTime;            // tempo m�nimo entre cria��o de tesouros
	private int range;              // tempo durante o qual podem ser criados tesouros
	private TreasureInfo []tInfo;    // informa��o sobre os tesouros que podem ser criados
	private World world;            // mundo onde se adicionam os tesouros
	private int proxTreasureCreation; // temporizador de ria��o de tesouros
	
	/**
	 * cria o gerador de tesouros
	 * creates the treasure generator
	 * @param min tempo m�nimo entre cria��o de tesouros. Minimum time between treasure creation
	 * @param max tempo m�ximo entre cria��o de tesouros. Maximum time between treasure creation
	 * @param tInfo information for the treasures to be created
	 * @param w mundo onde os tesouros s�o adicionados. World where the treasures are added
	 */
	public TreasureGenerator( int min, int max, TreasureInfo []tInfo, World w ){
		world = w;
		this.tInfo = tInfo;
		minTime = min;
		range = max - min;
		proxTreasureCreation = nextCreationTime();
	}
	
	/**
	 * m�todo que trata da cri��o dos tesouros
	 * method that handles treasure creation
	 */
	public void update(){	
		// se o mundo tem tesouro n�o se pode criar
		// if the world has treasure then it cannot be created
		if(  world.getTreasure() != null )
			return;
		
		// se o temporizador � 0 chegou a altura de criar
		// if timer is 0 then it time to create a treasure
		if( proxTreasureCreation <= 0 ){
			// escolher aleatoriamente qual o tesouro a criar
			int prob = world.getRandomGen().nextInt( 100 );
			
			// ver qal os tesouros tem a probabilidade escolhida
			int total = 0;
			Treasure t = null;
			for( int i = 0; i < tInfo.length; i++ ){
				total += tInfo[i].getProbability();
				if( prob < total ){
					// escolher a coordenada x e criar o tesouro
					// pick the x coordinate of the treasure and create the treasure
					int x = world.getRandomGen().nextInt( world.getWidth() - tInfo[i].getImg().getComprimento() );
					t = tInfo[i].createTresure( new Point( x, 0) );		
					break;
				}
			}
			// adicionar o tesouro ao mundo e reiniciar o temporizador
			// add the treasure to the world and reset the creation timer
			world.setTreasure( t );			
			proxTreasureCreation = nextCreationTime();			
		}
		// decrementar o temporizador
		// decrement the timer
		else
			proxTreasureCreation--;		
	}

	/** estabelece o tempo de cria��o do pr�ximo tesouro
	 * defines the creation time until the next creation
	 * @return o n�mero de ciclos at� criar o pr�ximo tesouro. number of cicles until next creation
	 */
	private int nextCreationTime() {
		return minTime + world.getRandomGen().nextInt( range );
	}
}
