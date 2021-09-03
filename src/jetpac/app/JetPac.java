package jetpac.app;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import jetpac.astro.Astronaut;
import jetpac.world.World;
import prof.jogos2D.*;

/**
 * � a classe que controla todo o jogo
 * 
 * Class that controls the game
 */
public class JetPac extends JFrame {
	

	// elementos do jogo
	// game elements
	private World world;  		// representa o mundo onde se joga
	private int level; 			// n�vel actual do jogo
	private int score;			// score atual
	private int lives;			// n� de vidas
	private int time;// tempo na plataforma
	// leitor do teclado
	// keyboard reader
	private SKeyboard teclado;
	
	// vari�veis para os v�rios elementos visuais do jogo
	// the several visual elements of the game
	private JPanel jContentPane = null;
	private JPanel gameArea = null;
	private JPanel statusPane = null;
	
	// imagens usadas para melhorar as anima��es
	// images used to improve animations
	private Image ecran;         // o ecran	onde se desenha o mundo
	private Image statusBarImg;  // a barra de status
	
	// fontes para escrever a pontua��o, n�vel e vidas
	private Font livesFont = new Font("Roman",Font.BOLD, 20);
	private Font levelFont = new Font("Roman",Font.BOLD, 28);
	
	// isto � para n�o dar warnings
	// just to avoid warnings
	private static final long serialVersionUID = 1L;

	/**
	 * construtor da aplica��o
	 */
	public JetPac( ) {
		setTitle("ConquEST");
		initialize();  // defini��es da janela. Initializing the window
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		
		// configurar o teclado
		// setting up the keyboard
		teclado = new SKeyboard( this );
		
		startGame(); 		// come�ar jogo
	}	


	/** M�todo que come�a o jogo
	 */
	private void startGame(){
		// configura��o inicial
		// initial configuration
		level = 1;
		score = 0;
		lives = 5;
		teclado.setOwner( this );
		
		// jogar o n�vel
		playLevel();
	}
	

	/**
	 * Jogar um dado n�vel
	 */
	private void playLevel(){		
		world = readLevel( level );  // ler os ficheiro do n�vel
		if( world == null )
			return;
		
		// fazer o reset do n�vel, para ficar tudo reiniciado
		// do the reset so all becomes initialized
		resetLevel();
	}
	
	/**
	 * inicia/reinicia um n�vel
	 * starts/restarts a level
	 */
	private void resetLevel(){
		// iniciar o mundo
		// initialize the world
		world.play();
		
		// arrancar com o actualizador que vai actualizar o mundo em ciclos de 30 vezes por segundo		
		Actualizador actualiza = new Actualizador();
		actualiza.start();
	}
	
	/**
	 * L� as informa��es do n�vel no respectivo ficheiro
	 * O ficheiro dos n�veis est� no direct�rio levels e tem a termina��o txt.
	 * 
	 * reads the information about the level from the respective file.
	 * the level file is in the directory levels e tem a termina��o txt.
	 * 
	 * @param nivel o n�vel a ler
	 * @return o mundo representado neste n�vel
	 */
	private World readLevel( int level ){
		String file = "data/levels/level"+level+".txt";  // ficehiro onde est� o n�vel
		String dirArt = "data/art/";               // direct�rio onde est�o as imagens dos edif�cios
		
		// criar o leitor de mundos para ler o mundo
		// create the world reader to read the world
		WorldReader wr = new WorldReader( dirArt );		
		return wr.readWorld( file );	
	}
	
	/**
	 * m�todo que vai ser usado para desenhar os componentes do jogo
	 * QUALQUER DESENHO DEVE SER FEITO AQUI OU NA STATUS BAR
	 * 
	 * method to draw all coponents in the game
	 * EVERY DRAWING SHOULD BE DONE HERE OR IN THE STATUS BAR
	 * 
	 * @param g elemento onde se vai desenhar. g is the drawing element
	 */
	private void drawGameArea( Graphics2D g ){
		// passar para graphics2D pois este � mais avan�ado
		// use the more advanced Graphics2D
		Graphics2D ge = (Graphics2D )ecran.getGraphics();

		// desenhar o mundo
		world.draw( ge );   
				
		// agora que est� tudo desenhado na imagem auxiliar, desenhar no ecr�n essa imagem
		// now that everithing is drawn in the aux image draw that image on the screen
		g.drawImage( ecran, 0, 0, null );		
	}
	
	/**
	 * m�todo que vai ser usado para desenhar a barra de estados
	 * method to draw the status bar
	 * @param g elemento onde se vai desenhar. g is the drawing element
	 */
	private void drawStatusBar( Graphics2D g ){
		// desenhar o fundo da imagem
		g.drawImage( statusBarImg, 0, 0, null );
		
		// desenhar a percentagem de fuel
		// draw the fuel percentage
		int perc = world.getFuelPercentage();
		if( perc < 60 )
			g.setColor( Color.RED );
		else if( perc < 90 )
			g.setColor( Color.YELLOW );
		else
			g.setColor( Color.GREEN );
		g.fillRect( 180, 11, perc*2, 20);
		
		// desenhar as vidas, n�vel e pontua��o
		// draw lives, level and score
		g.setColor( Color.white );
		g.setFont( livesFont );
		g.drawString( ""+lives, 55, 25);
		
		g.setFont( levelFont );
		g.drawString( ""+level, 472, 32);
		g.drawString( ""+score, 618, 32);
	}

	
	/** 
	 * m�todo chamado a cada ciclo de processamento para actualizar os elemntos do jogo.
	 * Aten��o! Este m�todo N�O desenha nada. Usar o m�todo drawGameArea para isso
	 * 
	 * method that updates the elements in the game in each cicle
	 * Warning! This does not draw anything. Use the drawGameArea for that.
	 */
	private void updateGame() {
		Astronaut astro = world.getAstronaut(); 

		// ver as teclas premidas
		// cheack for pressed keys
		
		// subir? going up?
		astro.setRising( teclado.estaPremida( KeyEvent.VK_UP ) );
		// disparar? shooting?
		astro.setShooting( teclado.estaPremida( KeyEvent.VK_SPACE) );
		// a andar? se sim para que lado? 
		// walking? in which direction?
		if( teclado.estaPremida( KeyEvent.VK_LEFT ) ){
			astro.setDirection( Astronaut.LEFT );
			astro.setWalking( true );
		}
		else if( teclado.estaPremida( KeyEvent.VK_RIGHT ) ){
			astro.setDirection( Astronaut.RIGHT );
			astro.setWalking( true );
		}
		else
			astro.setWalking( false );
		
		if( teclado.estaPremida( KeyEvent.VK_DOWN ) )
			astro.drop();

		// actualizar mundo e ver quanto se pontuou neste ciclo
		// update world and add the score of this cicle
		score += world.update();
	}
	
	/**
	 * Classe respons�vel pela cria��o da thread que vai actualizar o mundo de x em x tempo
	 * AQUI N�O DEVEM ALTERAR NADA
	 * 
	 * Class that creates the thread responsible for the game updating 
	 * DO NOT CHANGE ANYTHING 
	 * 
	 * @author F. Sergio Barbosa
	 */
	class Actualizador extends Thread {
		public void run(){
			long mili = System.currentTimeMillis();
			long target = mili + 30;
			do {
				updateGame();
				gameArea.repaint();
				statusPane.repaint();
				// esperar 30 milisegundos o que d� umas 32 frames por segundo
				// wait 30 miliscodens, this gives about 32 frames per second
				while (mili < target)
					mili = System.currentTimeMillis();
				target = mili + 30;
				// enquanto o mundo n�o estiver completo ou tiver acabado
				// while the world is not completed otr over
			} while ( !world.isCompleted() && !world.isOver() );
			
			// se o n�vel estiver completo pasa ao pr�ximo
			// if the level is complete then goes to the next level
			if( world.isCompleted() && level <7){
				level++;
				playLevel();
			}
			else {
				// se perdeu uma vida, atualizar as vidas e recome�ar o n�vel ou terminar o jogo
				// if a life is lost then restart level or end game
				lives--;
				if( lives == 0 ){
					// as escolhas s�o: recome�ar do 1� n�vel ou sair
					// choices are "Replay from 1st level" or "Quit game"
					String escolhas[] = {"Voltar ao 1� n�vel", "Terminar Jogo" };
					int resposta = JOptionPane.showOptionDialog(  JetPac.this, "Game Over! Que deseja fazer?", "GAME OVER", JOptionPane.YES_NO_OPTION,
							                                       JOptionPane.PLAIN_MESSAGE, null, escolhas, escolhas[0]  );
					switch( resposta ){
					case 0:
						startGame();
						break;
					case 1:
						System.exit( 0 );
					}
				}
				else 
					resetLevel();
			}
		}
	}	
	
	/**
	 * Este m�todo inicializa a zonaJogo, AQUI N�O DEVEM ALTERAR NADA
	 * This methos initializes the game area, DO NOT CHANGE IT 	
	 */
	private JPanel getGameArea() {
		if (gameArea == null) {
			gameArea = new JPanel(){
				public void paintComponent(Graphics g) {
					drawGameArea( (Graphics2D)g );
				}
			};
			Dimension d = new Dimension(1000, 700);			
			gameArea.setPreferredSize( d );
			gameArea.setSize( d );
			gameArea.setMinimumSize( d );
			gameArea.setBackground(Color.BLACK);
		}
		return gameArea;
	}

	/**
	 *  vai inicializar a aplica��o, AQUI N�O DEVEM ALTERAR NADA
	 *  initializing the application, DO NOT CHANGE IT
	 */
	private void initialize() {
		// criar a imagem para melhorar as anima��es e configur�-la para isso mesmo
		// create the image to improve animations and configure it
		ecran = new BufferedImage( 1000,700, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D ge = (Graphics2D )ecran.getGraphics();		
		ge.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    ge.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    
	    // ler a imagem para a barra de estado
	    // read the status bar image
	    try {
			statusBarImg = ImageIO.read( new File("data/art/statusbar.gif") );
		} catch (IOException e) {
			JOptionPane.showMessageDialog( null, "Falta ficheiro data/art/statusbar.gif", "ERRO", JOptionPane.ERROR_MESSAGE );
			System.exit( 1 );
		}
		
		// caracter�sticas da janela
	    // windows settings
		this.setLocationRelativeTo( null );
		this.setContentPane( getJContentPane() );
		this.setTitle("JetPac");
	    this.pack(); 	    
	    this.setResizable( false );
	    this.setLocationRelativeTo( null );		
	}
	
	/**
	 * m�todos auxiliares para configurar a janela, AQUI N�O DEVEM ALTERAR NADA
	 * aux methods to configure the window, DO NOT CHANGE THEM
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();			
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getGameArea(),BorderLayout.CENTER);
			jContentPane.add(getStatusBar(), BorderLayout.NORTH );
		}
		return jContentPane;
	}
	
	private JPanel getStatusBar() {
		if( statusPane == null ){
			statusPane = new JPanel(){
				public void paintComponent(Graphics g) {
					drawStatusBar( (Graphics2D)g );
				}
			};
			
			Dimension d = new Dimension(statusBarImg.getWidth( null ), statusBarImg.getHeight( null ));			
			statusPane.setPreferredSize( d );
			statusPane.setSize( d );
			statusPane.setMinimumSize( d );

		}
		return statusPane;
	}
	
	
	
	public static void main( String args[] ){
		JetPac ce = new JetPac();
		ce.setVisible( true );
	}
}