package ga_simulation;

import exceptions.InvalidNodeIdException;
import exceptions.InvalidNumberOfFrequencySlotsException;
import exceptions.InvalidRoutesException;
/**
 * Descreve a população usada no GA.
 * @author André 
 */
public class Population {
	/**
	 * Tamanho da população.
	 * @author André 
	 */
	private transient final int populationSize;
	/**
	 * Vetor de indivíduos.
	 * @author André 
	 */
	private transient Individual[] individuals;
	/**
	 * Construtor da classe.
	 * @throws InvalidListOfCoefficientsException 
	 * @throws InvalidNodeIdException 
	 * @throws InvalidRoutesException 
	 * @throws InvalidNumberOfFrequencySlotsException
	 * @author André 
	 */
	public Population(final int numbOfNodes, final int populationSize) throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException, InvalidNodeIdException {
		this.populationSize = populationSize;
        this.individuals = new Individual[populationSize];
        for (int i=0; i<individuals.length; i++) { 
        	individuals[i] = new Individual(numbOfNodes); // NOPMD by André on 11/07/17 14:53
        }
    }
	
	/**
	 * Construtor da classe.
	 * @author André 
	 */
	public Population(final int populationSize) {
		this.populationSize = populationSize;
        this.individuals = new Individual[populationSize];
    }
	
	/**
	 * Método para configurar um indivíduo em uma determinada posição do vetor.
	 * @param individual
	 * @param position
	 * @author André
	 */
    public void setIndividual(final Individual individual, final int position) {
        this.individuals[position] = individual;
    }
    /**
	 * Método para configurar um indivíduo na próxima posição disponível do vetor.
	 * @param individual
	 * @author André
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
   	 * Método para verificar se algum indivíduo atingiu o objetivo.
   	 * @param fitness
   	 * @return O atributo true or false
   	 * @author André
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
   	 * Método para ordenar a população pelo valor do fitness.
   	 * @author André
   	 */
    public void sortPopulation() {
        boolean change = true;
        while (change) {
        	change = false; // NOPMD by André on 11/07/17 14:53
            for (int i=0; i<individuals.length-1; i++) {
                if (individuals[i].getFitness() > individuals[i+1].getFitness()) { // NOPMD by André on 11/07/17 14:53
                    final Individual temp = individuals[i];
                    individuals[i] = individuals[i + 1];
                    individuals[i+1] = temp;
                    change = true; // NOPMD by André on 11/07/17 14:53
                }
            }
        }
    }
    /**
   	 * Método para retornar o número de indivíduos na população.
   	 * @return O número de indivíduos
   	 * @author André
   	 */    
    public int getNumOfIndividuals(){
        int count = 0; // NOPMD by André on 11/07/17 14:53
        for (int i=0; i<individuals.length;i++){
            if (individuals[i]!=null){
            	count++; // NOPMD by André on 11/07/17 14:53
            }
        }
        return count;
    }
    /**
   	 * Método para retornar o tamanho da população.
   	 * @return O atributo populationSize
   	 * @author André
   	 */ 
    public int getPopulationSize(){
        return this.populationSize;
    }
    /**
   	 * Método para retornar o indivíduo de uma determinada
   	 * posição.
   	 * @return O atributo populationSize
   	 * @author André
   	 */ 
    public Individual getIndividual(final int position) {
        return individuals[position];
    }
}
