package jetpac.generator;

import java.awt.Point;

import prof.jogos2D.ComponenteVisual;

import jetpac.drag.Fuel;
import jetpac.world.World;

/**
 * Classe respons�vel pela cria��o do combust�vel para a nave.
 * Para criar um fuel n�o pode haver fuels no mundo.
 * O tempo entre um fuel ser colocado na nave e a cria��o do pr�ximo � aleat�rio, mas existe
 * um tempo m�nimo e um tempo m�ximo para esse tempo.
 * 
 * Class responsible for creating fuel for the ship.
 * There cannot be a fuel in the world before another fuel can be created. The time between
 * a fuel is stored and another appears is random, but there is a minimum and maximum
 * amount of time to wait. 
 * 
 * @author F. Sergio
 *
 */
public class FuelGenerator {
	
	private int nFuels;    			// n�mero de fuels j� criados
	private int maxFuel;            // m�ximo de fuels a criar
	private ComponenteVisual img;   // imagem de cada fuel
	private World world;			// mundo ao qual se adiciona fuel
	private int minTime;            // tempo m�nimo entre cria��o de fuels
	private int range;              // tempo durante o qual podem ser criados fuels
	private int proxFuel;           // temporizador de cria��o do pr�ximo fuel

	/**
	 * Cria o gerador de fuel
	 * Creates the fuel generator
	 * @param maxFuel m�ximo n�mero de fuela a criar. Maximum number of fuels to create
	 * @param min tempo m�nimo entre cria��o de fuels. Minimum time between fuel creation.
	 * @param max tempo m�ximo entre cria��o de fuels. Maximum time between fuel creation
	 * @param img
	 * @param m
	 */
	public FuelGenerator( int maxFuel, int min, int max, ComponenteVisual img, World w ){
		this.nFuels = 0;
		this.maxFuel = maxFuel;
		this.img = img;
		world = w;
		minTime = min;
		range = max - min;
		proxFuel = nextFuelTime();
	}
	
	/**
	 * m�todo usado para criar o fuel, se for caso disso
	 * method that creates the fuel, when it is necessary
	 */
	public void update(){
		// s� pode criar se n�o houver fuel no mundo e se a nave estiver completa
		// checks if there is fuel in the world and the ship is complete, otherwise ir cannot create a fuel
		if( nFuels >= maxFuel || !world.getMainSpaceship().isComplete() || world.hasFuel() )
			return;
			
		// quando o temporizador chega a zero � altura de criar fuel
		// if the creastion timer reaches 0 its time to create
		if( proxFuel == 0 ){
			// gerar aleatoriamente a coordenada x onde aparece o fuel
			// generate the xx coordinate where the fuel appears
			int x = world.getRandomGen().nextInt( world.getWidth() - img.getComprimento() );
			
			// criar e adicionar o fuel ao mundo
			// create and add the fuel to the world
			Fuel f = new Fuel( new Point( x, 0), img );						
			world.setFuel( f );
			
			// incrementar o n�mero de fuels j� criados e reiniciar o temporizador
			// increment the fuel count and reset the creation timer
			nFuels++;
			proxFuel = nextFuelTime();
		}
		// sen�o decrementa-se o temporizador
		// otherwise the timer is decremented
		else
			proxFuel--;
	}
	
	/** indica se ainda tem mais fuel para criar
	 * returns if there are more fuels to create
	 * @return true se ainda vai criar mais fuel. return true if it has more fuel to create
	 */
	public boolean hasMoreFuel(){
		return nFuels < maxFuel;
	}
	
	/** estabelece o tempo de cria��o do pr�ximo fuel
	 * defines the creation time until the next creation
	 * @return o n�mero de ciclos at� crir o pr�ximo fuel. number of cicles until next creation
	 */
	private int nextFuelTime(){
		return minTime + world.getRandomGen().nextInt( range );
	}

	/** retorna o n�mero m�ximo de fuels a criar
	 * return the mamimum number of fuels to create
	 * @return retorna o n�mero m�ximo de fuels a criar
	 */
	public int getMaxFuel() {
		return maxFuel;
	}
	
	/** retorna o n�mero de fuels j� criados
	 * return the number of fuels already created
	 * @return the number of fuels already created
	 */
	public int getNumFuels(){
		return nFuels;
	}
}
