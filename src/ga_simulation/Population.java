package ga_simulation;

import exceptions.InvalidNodeIdException;
import exceptions.InvalidNumberOfFrequencySlotsException;
import exceptions.InvalidRoutesException;
/**
 * Descreve a popula��o usada no GA.
 * @author Andr� 
 */
public class Population {
	/**
	 * Tamanho da popula��o.
	 * @author Andr� 
	 */
	private transient final int populationSize;
	/**
	 * Vetor de indiv�duos.
	 * @author Andr� 
	 */
	private transient Individual[] individuals;
	/**
	 * Construtor da classe.
	 * @throws InvalidListOfCoefficientsException 
	 * @throws InvalidNodeIdException 
	 * @throws InvalidRoutesException 
	 * @throws InvalidNumberOfFrequencySlotsException
	 * @author Andr� 
	 */
	public Population(final int numbOfNodes, final int populationSize) throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException, InvalidNodeIdException {
		this.populationSize = populationSize;
        this.individuals = new Individual[populationSize];
        for (int i=0; i<individuals.length; i++) { 
        	individuals[i] = new Individual(numbOfNodes); // NOPMD by Andr� on 11/07/17 14:53
        }
    }
	
	/**
	 * Construtor da classe.
	 * @author Andr� 
	 */
	public Population(final int populationSize) {
		this.populationSize = populationSize;
        this.individuals = new Individual[populationSize];
    }
	
	/**
	 * M�todo para configurar um indiv�duo em uma determinada posi��o do vetor.
	 * @param individual
	 * @param position
	 * @author Andr�
	 */
    public void setIndividual(final Individual individual, final int position) {
        this.individuals[position] = individual;
    }
    /**
	 * M�todo para configurar um indiv�duo na pr�xima posi��o dispon�vel do vetor.
	 * @param individual
	 * @author Andr�
	 */
    public void setIndividual(final Individual individual) {
        for (int i=0; i<individuals.length; i++) {
            if (individuals[i]==null){
            	individuals[i]=individual;
                break;
            }
        }
    }
    /**
   	 * M�todo para verificar se algum indiv�duo atingiu o objetivo.
   	 * @param fitness
   	 * @return O atributo true or false
   	 * @author Andr�
   	 */
    public boolean hasSolution(double fitness){
        Individual i = null;
        for (int j = 0; j < individuals.length; j++) {
            if (individuals[j].getFitness() == fitness) {
                i = individuals[j];
                break;
            }
        }
        if (i == null) {
            return false;
        }
        return true;
    }
    /**
   	 * M�todo para ordenar a popula��o pelo valor do fitness.
   	 * @author Andr�
   	 */
    public void sortPopulation() {
        boolean change = true;
        while (change) {
        	change = false; // NOPMD by Andr� on 11/07/17 14:53
            for (int i=0; i<individuals.length-1; i++) {
                if (individuals[i].getFitness() > individuals[i+1].getFitness()) { // NOPMD by Andr� on 11/07/17 14:53
                    final Individual temp = individuals[i];
                    individuals[i] = individuals[i + 1];
                    individuals[i+1] = temp;
                    change = true; // NOPMD by Andr� on 11/07/17 14:53
                }
            }
        }
    }
    /**
   	 * M�todo para retornar o n�mero de indiv�duos na popula��o.
   	 * @return O n�mero de indiv�duos
   	 * @author Andr�
   	 */    
    public int getNumOfIndividuals(){
        int count = 0; // NOPMD by Andr� on 11/07/17 14:53
        for (int i=0; i<individuals.length;i++){
            if (individuals[i]!=null){
            	count++; // NOPMD by Andr� on 11/07/17 14:53
            }
        }
        return count;
    }
    /**
   	 * M�todo para retornar o tamanho da popula��o.
   	 * @return O atributo populationSize
   	 * @author Andr�
   	 */ 
    public int getPopulationSize(){
        return this.populationSize;
    }
    /**
   	 * M�todo para retornar o indiv�duo de uma determinada
   	 * posi��o.
   	 * @return O atributo populationSize
   	 * @author Andr�
   	 */ 
    public Individual getIndividual(final int position) {
        return individuals[position];
    }
}
