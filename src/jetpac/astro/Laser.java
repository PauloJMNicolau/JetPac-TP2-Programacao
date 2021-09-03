package jetpac.astro;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.Random;

import jetpac.enemy.Enemy;
import jetpac.platform.Plataform;
import jetpac.world.WorldElementDefault;

/**
 * Classe que representa raio laser
 * 
 * Class that represents a laser ray
 * 
 * @author F. Sérgio Barbosa
 */
public class Laser extends WorldElementDefault {

	private int lifeTime;  // duração do laser
	private int range;     // alcance do laser
	
	// estilo de linha do laser
	// laser line style
	private static Stroke laserStyle = new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND );
	private Color color;           // cor do laser 
	private Line2D.Double laser;   // linha do laser no écran
		
	private boolean dead = false;  // se o laser está desativo

	/** Cria um laser
	 * Creates a laser
	 * @param i posição inical do laser, initial laser position
	 * @param a alcance do laser. Laser range
	 */
	public Laser( Point i, int a ){
		super( null );
		lifeTime = 10;
		range = a / lifeTime;
		Random r = new Random();
		Color colors[] = { Color.CYAN, Color.YELLOW, Color.RED, Color.GREEN };
		color = colors[ r.nextInt( colors.length ) ];
		laser = new Line2D.Double( i.x, i.y, i.x + range, i.y );
	}
	
	/** desenha a linha do laser
	 * draws the laser line
	 */
	public void draw( Graphics2D g ){
		g.setColor( color );
		g.setStroke( laserStyle );									
		g.draw( laser );		
	}
	
	/** atualiza o laser em cada ciclo de processamento
	 * updates the laser in each processing cicle
	 */
	public void update( ) {
		// movimentar o laser
		// move the laser
		laser.x2 += range;
		laser.x1 += (range * (lifeTime>8? 0: 1));
		
		if( laser.x2 > getWorld().getWidth() )
			laser.x2 = getWorld().getWidth();
		if( laser.x1 > getWorld().getWidth() )
			laser.x1 = getWorld().getWidth();
		
		// ver se ainda está ativo
		// check lifetime
		lifeTime--;
		if( lifeTime <= 0 ){
			die();
			return;
		}
		
		// ver se está a bater nas plataformas
		// check hits on platforms
		for( Plataform f : getWorld().getPlatforms() ){
			Rectangle r = f.getBounds();
			if( r.intersectsLine( laser )){
				laser.x2 = laser.x1 <= r.x ? r.x : r.x + r.width;
				range = 0;
			}				
		}
		// ver se bate nos enimigos
		// check hits on enemies
		for( Enemy e : getWorld().getEnemies() ){
			Rectangle r = e.getBounds();
			if( !e.isDying() && r.intersectsLine( laser )){
				laser.x2 = laser.x1 <= r.x ? r.x : r.x + r.width;
				e.die();
				getWorld().addCiclePoints( e.getScore() );
			}				
		}
	}

	/** desativa o laser
	 * deactivates the laser
	 */
	public void die() {
		dead = true;		
	}
	
	/** testa se o laser está desativo
	 * test if the laser is inactive
	 * @return true if laser is inactive
	 */
	public boolean isDead(){
		return dead; 
	}

}
