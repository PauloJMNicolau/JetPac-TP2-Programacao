package jetpac.app;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JOptionPane;

import jetpac.astro.Astronaut;
import jetpac.drag.Spaceship;
import jetpac.generator.EnemyGenerator;
import jetpac.generator.FuelGenerator;
import jetpac.generator.TreasureGenerator;
import jetpac.generator.TreasureInfo;
import jetpac.platform.Eletrica;
import jetpac.platform.Livre;
import jetpac.platform.Plataform;
import jetpac.platform.RolanteDir;
import jetpac.platform.RolanteEsq;
import jetpac.platform.StandardPlat;
import jetpac.world.World;
import prof.jogos2D.ComponenteAnimado;
import prof.jogos2D.ComponenteMultiAnimado;
import prof.jogos2D.ComponenteSimples;
import prof.jogos2D.ComponenteVisual;


/**
 * Classe que faz a leitura dos ficheiros dos n�veis
 * 
 * Class that read the level files
 * 
 * @author F. S�rgio Barbosa
 */
public class WorldReader {

	// o direct�rio onde est�o os ficheiros com as imagens
	// folder with the art files
	private String artDir;   
	
	// o mundo a ser criado
	// the world being created
	private World world;
	
	/**
	 * Cria a WorldReader com que vai ler as imagens no direct�rio especificado
	 * Creates a WorldReader with the specified art directory 
	 * @param artDir the folder where the art files are
	 */
	public WorldReader(String artDir) {
		this.artDir = artDir;
	}

	/** L� o mundo especificado no ficheiro indicado. Para saber o formato do ficheiro 
	 * ler o ficheiro do n�vel 1
	 * 
	 * reads the world in the given file. To learn about the file format read the file
	 * for level 1
	 * 
	 * @param file o fichero com o n�vel. The file with the level specifications
	 * @return o mundo correspondente ao n�vel lido. The world described by the level file
	 */
	public World readWorld( String file ){
		// cria um mundo novo, vazio
		// creates a new, empty world
		world = new World();
		
		try {
			// abrir o ficheiro do n�vel
			// open the file
			BufferedReader in = new BufferedReader( new FileReader( file ));
			
			// ler uma linha 
			// read a line
			String line = getNextLine( in );			
			while( line != null ){
				// separar o = do resto da informa��o da linha
				// remove the = in the line
				String cmd[] = line.split("=");
				
				String info = cmd[0]; // info � o comando (lado esquerdo do =)
				                      // info is the command (left side of =)
				String data = cmd[1]; // data � a informa��o a ler 
				                      // data is the information to read
				
				// ver qual a informa��o e  process�-la
				// determine which command it is and process it
				if( info.startsWith("mundo") )
					readWorldConfig( data );
				else if( info.startsWith("nave") )
					readShip( data, in );
				else if( info.startsWith("astronauta") )
					readAstronaut( data );
				else if( info.startsWith("fuel") )
					readFuel( data );
				else if( info.startsWith("plataformas") )
					readPlatforms( data, in );
				else if( info.startsWith("inimigos") )
					readEnemies( data );
				else if( info.startsWith("tesouros") )
					readTreasures( data, in );
				line = getNextLine( in );
			}				
		} catch( Exception e ){
			// caso tenha acontecido algo errado ao ler o ficheiro de n�vel
			// in case something goes wrong while reading the file
			e.printStackTrace();
			JOptionPane.showMessageDialog( null, "Erro na leitura do ficheiro " + file, "ERRO", JOptionPane.ERROR_MESSAGE );
			System.exit( 1 );
		}
		return world;	
	}				

	/**
	 * le a informa��o sobre um ficheiro de imagem
	 * reads the information of a image file
	 * @param info a informa��o a ler. The information to read from.
	 * @param offset onde come�ar a ler a informa��o. Where to start to read from.
	 * @return a imagem correspondente � info. The image described in the info
	 * @throws IOException
	 */
	private ComponenteVisual readImage( String info [], int offset, Point p ) throws IOException{
		String imgFile = artDir + info[offset];
		int nFrames = Integer.parseInt( info[offset+1] );
		int delay = Integer.parseInt( info[offset+2] );
		if( nFrames == 0 )
			return new ComponenteSimples( p, imgFile );
		else 
			return new ComponenteAnimado( p, imgFile, nFrames, delay);
	}
	
	/** l� a configura��o do mundo
	 * read the world configuration
	 * @param data a linha com a informa��o sobre o mundo
	 * @throws IOException
	 */
	private void readWorldConfig(String data ) throws IOException {
		String info [] = data.split(",");
		int w= Integer.parseInt( info[0] );   // comprimento e altura
		int h = Integer.parseInt( info[1] );  // width and height
		world.setDimensions( w, h );
		world.setBackground( readImage(info, 2, new Point()) );
	}
	
	/** l� a configura��o da nave
	 * read the ship configuration
	 * @param data linha com a informa��o, line with the information
	 * @param in resto das linhas com a info. Remainder lines with the information
	 * @throws IOException
	 */
	private void readShip(String data, BufferedReader in ) throws IOException {
		int nParts = Integer.parseInt( data );
		for( int i = 0; i < nParts ; i ++){
			String info[] = getNextLine( in ).split(",");
			int x = Integer.parseInt( info[0] );
			int y = Integer.parseInt( info[1] );
			ComponenteVisual img = readImage(info, 2, new Point(x,y) );
			Spaceship n = new Spaceship( nParts, i, img );	
			world.addSpaceship( n );
		}
	}

	/** l� a informa��o do astronauta
	 * reads astronaut information
	 * @param data linha com a informa��o, line with the information
	 * @throws IOException
	 */
	private void readAstronaut(String data) throws IOException {
		String info [] = data.split(",");
		int x = Integer.parseInt( info[0] );
		int y = Integer.parseInt( info[1] );
		String file = artDir + info[2];
		int dy = Integer.parseInt( info[3] );
		ComponenteMultiAnimado img = new ComponenteMultiAnimado( new Point(x,y), file, 5, 5, 4);
		Astronaut a = new Astronaut( img, new Point(x,y), dy );
		world.setAstronauta( a );
	}

	/** ler informa��o sobre fuel
	 * reads fuel information
	 * @param data linha com a informa��o, line with the information
	 * @throws IOException
	 */
	private void readFuel(String data) throws IOException {
		String info [] = data.split(",");	
		int nFuels = Integer.parseInt( info[0] );
		int minTime = Integer.parseInt( info[1] );
		int maxTime = Integer.parseInt( info[2] );
		ComponenteVisual img = readImage(info, 3, new Point());
		
		// criar o gerador de fuel e associ�-lo ao mundo
		// create the fuel generator and add it to the world
		FuelGenerator fg = new FuelGenerator( nFuels, minTime, maxTime, img, world);
		world.setFuelGen( fg );
	}

	/** ler informa��o sobre plataformas
	 * read information about platform
	 * @param data linha com a informa��o, line with the information
	 * @param in restantes linhas de informa��o, remainder lines with the info
	 * @throws IOException
	 */
	private void readPlatforms(String data, BufferedReader in) throws IOException {
		int nPlataformas = Integer.parseInt( data );
		for( int i = 0; i < nPlataformas; i++ ){
			String info[] = getNextLine( in ).split(",");
			String tipo = info[0];
			int x = Integer.parseInt( info[1] );
			int y = Integer.parseInt( info[2] );
			ComponenteVisual img = readImage(info, 3, new Point(x,y));
			Plataform p;
			if( tipo.equals("standard") )
				p= new StandardPlat(img);
			else if( tipo.equals("rolanteEsq") )
				p= new RolanteEsq(img);
			else if( tipo.equals("rolanteDir") )
				p= new RolanteDir(img);
			else if( tipo.equals("eletrificada") ){
				p= new Eletrica(img);
			}else if( tipo.equals("livre") ){
				p= new Livre(img);
				
			}else 
				p = new Plataform( img);
			world.addPlatform( p );
		}
	}

	/** ler informa��o sobre inimigos
	 * read enemy configuration
	 * @param data linha com a informa��o, line with the information
	 * @throws IOException
	 */
	private void readEnemies(String data) throws IOException {
		String info [] = data.split(",");
		
		int maxEnemys = Integer.parseInt( info[0] );
		int enemyVel = Integer.parseInt( info[1] );
		int enemyScore = Integer.parseInt( info[2] );
		ComponenteMultiAnimado img;
		String tipo = info[3];
		String file = artDir + info[4];
		int nFrames = Integer.parseInt( info[5] );
		int delay = Integer.parseInt( info[6] );
		img = new ComponenteMultiAnimado( new Point(), file, 3, nFrames, delay);
		
		// criar o gerador de inimigos e asscoi�-lo ao mundo
		// create the enemy generator and add it to the world
		EnemyGenerator eg = new EnemyGenerator( tipo, maxEnemys, enemyVel, enemyScore, img, world );
		world.setEnemyGen( eg );
	}

	/** ler informa��o sobre tesouros
	 * reads treasure configuration
	 * 
	 * @param data linha com a informa��o, line with the information
	 * @param in restante informa��o, remainder information
	 * @throws IOException
	 */
	private void readTreasures(String data, BufferedReader in) throws IOException {
		String info [] = data.split(",");
		int nTesouros = Integer.parseInt( info[0] );
		int minTime = Integer.parseInt( info[1] );
		int maxTime = Integer.parseInt( info[2] );
		
		TreasureInfo []tInfo = new TreasureInfo[ nTesouros ]; 
		for( int i = 0; i < nTesouros; i++ ){
			String tinf[] = getNextLine( in ).split(",");
			int prob = Integer.parseInt( tinf[0] );
			int dur = Integer.parseInt( tinf[1] );
			int pontos = Integer.parseInt( tinf[2] );
			ComponenteVisual img = readImage(tinf, 3, new Point());
			tInfo[i] = new TreasureInfo( prob, dur, pontos, img );
		}
		TreasureGenerator tg = new TreasureGenerator( minTime, maxTime, tInfo, world );
		world.setTreasureGenerator( tg );
	}
	
	/**
	 * L� uma linha de informa��o, ignorando coment�tios e linhas em branco
	 * reads a line from the file, ignoring comments and empty lines  
	 * @param in leitor do ficheiro
	 * @return a linha que cont�m informa��o v�lida, ou null se acabou a leitura do ficheiro
	 * @throws IOException
	 */
	private String getNextLine( BufferedReader in ) throws IOException{
		String res = null;
		do {
			res = in.readLine();
			if( res == null ) return null;
		} while( res.isEmpty() || res.startsWith("#") );
		char chs[] = new char[ res.length() ];
		int nchs = 0;
		for( int i = 0; i < res.length(); i++ ){
			char ch = res.charAt(i); 
			if( !Character.isSpaceChar(ch) ){
				chs[nchs] = ch;
				nchs++;
			}
		}
		res = String.copyValueOf( chs, 0, nchs);
		//System.out.println("next line: " + res);
		return res;
	}
}
