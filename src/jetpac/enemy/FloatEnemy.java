package jetpac.enemy;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

import jetpac.platform.Plataform;
import prof.jogos2D.ComponenteMultiAnimado;

public class FloatEnemy extends Enemy {
	private Random gerarNum = new Random();
	private int ciclo;

	private int mov;
	private int auxDy;
	private int dir;
	
	
	public FloatEnemy(Point p, int vel, int score, int dir,
			ComponenteMultiAnimado img) {
		super(p, vel, score, dir, img);
		ciclo = 0;
		mov = 0;
		dir=0;
	}
	
	/**
	 * desloca o inimigo
	 * moves the enemy
	 * @param dx distância a mover em x. x distance to move
	 * @param dy distância a mover em y. y distance to move
	 */
	public void move(int dx, int dy ) {
		ComponenteMultiAnimado img = (ComponenteMultiAnimado) getImage();
		if(mov==0){
			mov = gerarNum.nextInt(15)+10;
			dir = gerarNum.nextInt(2);
		}
		
		if(dy != 0)
			dy=0;
		
		ciclo++;
		if(ciclo>100){
			dx=0;
			if(dir ==1){
				dy++;
				if( img.getPosicao().y + dy < 0 || img.getPosicao().y + dy > getWorld().getHeight())
					dy = -dy;
		
			}else{
				dy--;
				if( img.getPosicao().y + dy < 0 || img.getPosicao().y + dy > getWorld().getHeight())
					dy = -dy;
			}
			auxDy++;
				
		}
		if(auxDy > mov){
			ciclo=0;
			mov=0;
			dir=0;
			auxDy=0;
		}		
		super.move(dx, dy);
	}
	
	public void update(){
		super.update();
		// ver se bate nas plataformas
		// check if it hits a platform
		for( Plataform f : getWorld().getPlatforms() ){
			Rectangle enemyRect = getBounds();
			Rectangle platRect = f.getBounds();		
			Rectangle r = platRect.intersection(enemyRect);
			if(!r.isEmpty()){
				if(r.height <= 6 && r.y == platRect.y || r.height <= 6 && r.y== enemyRect.y){
					velY=-velY;
				} 
				else if (r.x == platRect.x){
					ComponenteMultiAnimado image = (ComponenteMultiAnimado) getImage();
					image.setAnim(1);
					mov = gerarNum.nextInt(15)+10;
					move(0,-mov);
				}else{

					ComponenteMultiAnimado image = (ComponenteMultiAnimado) getImage();
					image.setAnim(0);
					mov = gerarNum.nextInt(15)+10;
					move(0,-mov);
				}
			}				
		}
	
	}
}
