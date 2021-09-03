package jetpac.generator;

import java.awt.Point;

import jetpac.drag.Treasure;

import prof.jogos2D.ComponenteVisual;


/**
 * Esta classe armazena a informação sobre cada um dois tipos de tesouro a criar
 * num dado nível e que permitirá ao gerador de tesouros crfiar os tesouros corretos
 * 
 * This class stores information about each kind of treasure allowed in each level
 * and that will allow the treasure generator to create the correct kind of treasures
 *  
 * @author Sergio
 */
public class TreasureInfo {

	private int probability;   		// probaabilidade de sair este tipo tesouro
	private int lifeTime;           // duraçao do tesouro (em ciclos)
	private int score;              // pontuação de cada tesouro
	private ComponenteVisual img;  // imagem de cada tesouro
	
	/**
	 * Cria a informação para um tesouro
	 * @param probability probabilidade de sair o tesouro. probability of a treasure
	 * @param lifeTime tempo de vida do tesouro. Treasure life time
	 * @param score pontuação do tesouro. Score of the treasure
	 * @param img imagem do tesouro. Treasure image.
	 */
	public TreasureInfo(int probability, int lifeTime, int score,
			ComponenteVisual img) {
		this.probability = probability;
		this.lifeTime = lifeTime;
		this.score = score;
		this.img = img;
	}
	
	/**
	 * Cria um tesouro de acordo com estas especificações
	 * Create a treasure with the given specifications 
	 * @param p coordenada onde colocar o tesouro. Point where to place the treasure
	 * @return o tesouro criado. The created treasure
	 */
	public Treasure createTresure( Point p ){
		return new Treasure( p, lifeTime, score, img );
	}

	/** retorna a probabilidade deste tipo de tesouro
	 * returns the probability of this kind of treasure 
	 * @return the probability of this kind of treasure
	 */
	public int getProbability() {
		return probability;
	}

	/** define a probabildiade deste tipo de tesouro
	 * defines the probability of this kid of treasure
	 * @param probability
	 */
	public void setProbabilidade(int probability) {
		this.probability = probability;
	}

	/** retorna o tempo de vida do tesouro
	 * return the treasure life time
	 * @return o tempo de vida do tesouro
	 */
	public int getLifeTime() {
		return lifeTime;
	}

	/** define o tempo de vida do tipo de tesouro 
	 * define this kind of treasure life time
	 * @param lifeTime
	 */
	public void setLifeTime(int lifeTime) {
		this.lifeTime = lifeTime;
	}

	/** retorna a pontuação deste tipo de tesouro
	 * return the score of this kind of treasure
	 * @return a pontuação deste tipo de tesouro
	 */
	public int getScore() {
		return score;
	}

	/** define a pontuação para este tipo de tesouro
	 * defines the score for this kind of treasure
	 * @param score a pontuação a usar. The socre to use 
	 */
	public void setPontos(int score) {
		this.score = score;
	}		
	
	/** retorna a imagem a usar para este tipo de tesouro 
	 * return the image to use for this kind of treasure
	 * @return the image to use for this kind of treasure
	 */
	public ComponenteVisual getImg() {
		return img;
	}
}
