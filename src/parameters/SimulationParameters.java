package parameters;

import types.CallRequestType;
import types.ModulationLevelType;

/**
 * Descreve a classe que armazena os principais param�tros da rede.
 * @author Andr� 
 */
public class SimulationParameters {
	
	private static final CallRequestType CALL_REQUEST_TYPE = CallRequestType.BIDIRECTIONAL;

	/**
	 * Constante da luz.
	 * @author Andr� 
	 */
	private static final double LIGHT_CONSTANT = 2.99792458E8;  // Unidade: m/s (metro/segundo)
	/**
	 * Comprimento de onda inicial.
	 * @author Andr� 
	 */
	private static final double INITIAL_LAMBDA = 1528.77E-9; // Unidade: m (metro)
	/**
	 * Frequencia final.
	 * @author Andr� 
	 */
	private static final double FINAL_FREQUENCY = (LIGHT_CONSTANT)/INITIAL_LAMBDA; // Unidade: Hz (hertz)
	/**
	 * Espa�amento do canal.
	 * @author Andr� 
	 */
	private static final double SPACING = 12.5E9; 		// Unidade: bits
	/**
	 * Constante de Planck.
	 * @author Andr� 
	 */
	private static final double PLANCK = 6.626068E-34;  // Unidade: J * s
	
	//Amplifier parameters
	/**
	 * Figura de ru�do.
	 * @author Andr� 
	 */
	private static final double NOISE_FIGURE = 5.5;
	//Fiber parameters
	/**
	 * Coeficiente de atenua��o da fibra.
	 * @author Andr� 
	 */
	private static final double ATENU_COEFFIC = -0.23;
	/**
	 * N�mero de canais na rede.
	 * @author Andr� 
	 */
	private static final int NUMBER_SLOTS = 64;
	//Span parameters
	/**
	 * Tamanho do span.
	 * @author Andr� 
	 */
	private static final double SPAN_SIZE = 100.0;
	
	//Node parameters
	/**
	 * Perda do mux/demux.
	 * @author Andr� 
	 */
	private static final double MUX_LOSS = -3.0;
	/**
	 * Perda do conector.
	 * @author Andr� 
	 */
	private static final double DIO_LOSS = -3.0;
	/**
	 * Perda do comutador.
	 * @author Andr� 
	 */
	private static final double SWITCH_LOSS = -10.0;
	/**
	 * OSNR de entrada.
	 * @author Andr� 
	 */
	private static final double OSNR_IN = 40.0;
	/**
	 * Pot�ncia do laser.
	 * @author Andr� 
	 */
	private static final double LASER_POWER = 0.0;
	//Network parameters
	/**
	 * Lista de Taxas de transmiss�o consideradas na simula��o.
	 * @author Andr� 
	 */
	//private static final int[] BIT_RATE = {40,100,150,200,400}; //in Gb/s //Removi a taxa de 10 Gbits
	private static final int[] BIT_RATE = {50,75,100,200,300}; //in Gb/s //Removi a taxa de 10 Gbits
	/**
	 * Tempo m�ximo.
	 * @author Andr� 
	 */
	private static final double MAX_TIME = 100000.0;
	/**
	 * Taxa m�dia de dura��o da chamada.
	 * @author Andr� 
	 */
	private static final double MEAN_RATE = 1.0;
	/**
	 * Lista dos tipos de modula��o considerados na simula��o.
	 * @author Andr� 
	 */
	private static final ModulationLevelType[] MODUL_LEVEL ={
		ModulationLevelType.SIXTYFOUR_QAM,
		ModulationLevelType.THIRTYTWO_QAM,
		ModulationLevelType.SIXTEEN_QAM,
		ModulationLevelType.EIGHT_QAM,
		ModulationLevelType.FOUR_QAM};
	// private static final ModulationLevelType[] MODUL_LEVEL ={
	// 	ModulationLevelType.SIXTYFOUR_QAM};
	/**
	 * Comprimento do maior enlace da rede.
	 * @author Andr� 
	 */
	private static final double MAX_LENGTH = 3400.0;
	//PSR parameters
	/**
	 * N�mero de canais na rede.
	 * @author Andr� 
	 */
	private static final int DIMENSION = 31;
	//ACO parameters
	/**
	 * N�mero de ciclos.
	 * @author Andr� 
	 */
	private static final int CREATION_FREQ = 2;		// cycles
	/**
	 * Taxa de evapora��o.
	 * @author Andr� 
	 */
	private static final double EVAPOR_RATE = 1.0;		// evaporation rate (unit/cycle)
	/**
	 * Ferom�nio.
	 * @author Andr� 
	 */
	private static final double PHK = 10.0;		// pheromone laid units
	/**
	 * Contante Theta.
	 * @author Andr� 
	 */
	private static final float THETA = 0.9f;	// allocator threshold
	/**
	 * Contante Alfa.
	 * @author Andr� 
	 */
	private static final double ALFA = 4.0;		// pheromone sensitivity
	/**
	 * Contante Beta.
	 * @author Andr� 
	 */
	private static final double BETA = 2.0;		// cost sensitivity
	/**
	 * Contante Gamma.
	 * @author Andr� 
	 */
	private static final double GAMMA = 0.0;		// availability sensitivity
	/**
	 * Custo m�ximo da rota.
	 * @author Andr� 
	 */
	private static final int R_MAX = 2;		// max allowed cost of a route
	/**
	 * Tempo m�ximo de busca.
	 * @author Andr� 
	 */
	private static final int T_MAX = 100;		// max search time
	/**
	 * N�mero de agentes.
	 * @author Andr� 
	 */
	private static final int NUMBODAGENTS = 5;		// number of agents created 
	/**
	 * N�mero de rotas.
	 * @author Andr� 
	 */
	private static final int NUMBOFPATHS = 40;		// number of paths 

	public CallRequestType getCallRequestType() {
		return CALL_REQUEST_TYPE;
	}

	/**
	 * M�todo para retornar a figura de ru�do.
	 * @author Andr� 
	 */
	public double getNoiseFigureIndB() {
		return NOISE_FIGURE;
	}
	/**
	 * M�todo para retornar o coeficiente de atenua��o.
	 * @author Andr� 
	 */
	public double getFiberAtenuationCoefficient() {
		return ATENU_COEFFIC;
	}
	/**
	 * M�todo para retornar o tamanho do span.
	 * @author Andr� 
	 */
	public double getSpanSize() {
		return SPAN_SIZE;
	}
	/**
	 * M�todo para retornar a perda do mux.
	 * @author Andr� 
	 */
	public double getMuxLoss() {
		return MUX_LOSS;
	}
	/**
	 * M�todo para retornar a perda do comutador.
	 * @author Andr� 
	 */
	public double getSwitchLoss() {
		return SWITCH_LOSS;
	}
	/**
	 * M�todo para retornar a pot�ncia do laser.
	 * @author Andr� 
	 */
	public double getLaserPower() {
		return LASER_POWER;
	}
	/**
	 * M�todo para retornar a frequencia final.
	 * @author Andr� 
	 */
	public static double getFinalFrequency() {
		return FINAL_FREQUENCY;
	}
	/**
	 * M�todo para retornar a OSNR de entrada.
	 * @author Andr� 
	 */
	public double getOSNRIn() {
		return OSNR_IN;
	}
	/**
	 * M�todo para retornar a perda do conector.
	 * @author Andr� 
	 */
	public double getDioLoss() {
		return DIO_LOSS;
	}
	/**
	 * M�todo para retornar o valor da constante C.
	 * @author Andr� 
	 */
	public static double getC() {
		return LIGHT_CONSTANT;
	}
	/**
	 * M�todo para retornar o comprimento de
	 * onda inicial.
	 * @author Andr� 
	 */
	public static double getInitialLambda() {
		return INITIAL_LAMBDA;
	}
	/**
	 * M�todo para retornar o espa�amento do canal.
	 * @author Andr� 
	 */
	public static double getSpacing() {
		return SPACING;
	}
	/**
	 * M�todo para retornar o valor da constante Plank.
	 * @author Andr� 
	 */
	public static double getPlanck() {
		return PLANCK;
	}
	/**
	 * M�todo para retornar o n�mero de slots.
	 * @author Andr� 
	 */
	public int getNumberOfSlots() {
		return NUMBER_SLOTS;
	}
	/**
	 * M�todo para retornar a lista de taxas de 
	 * transmiss�o.
	 * @author Andr� 
	 */
	public int[] getBitRate() {
		return BIT_RATE; // NOPMD by Andr� on 22/06/17 15:38
	}
	/**
	 * M�todo para retornar o tempo m�ximo.
	 * @author Andr� 
	 */
	public double getMaxTime() {
		return MAX_TIME;
	}
	/**
	 * M�todo para retornar a taxa m�dia de dura��o da chamada.
	 * @author Andr� 
	 */
	public double getMeanRateOfCallsDuration() {
		return MEAN_RATE;
	}
	/**
	 * M�todo para retornar a lista de formatos de modula��o.
	 * @author Andr� 
	 */
	public ModulationLevelType[] getModulationLevelType() {
		return MODUL_LEVEL; // NOPMD by Andr� on 22/06/17 15:39
	}
	/**
	 * M�todo para retornar a frequ�ncia de cria��o.
	 * @author Andr� 
	 */
	public static int getCreationfrequency() {
		return CREATION_FREQ;
	}
	/**
	 * M�todo para retornar a taxa de evapora��o.
	 * @author Andr� 
	 */
	public static double getEr() {
		return EVAPOR_RATE;
	}
	/**
	 * M�todo para retornar o fer�monio.
	 * @author Andr� 
	 */
	public static double getPhk() {
		return PHK;
	}
	/**
	 * M�todo para retornar o par�metro Theta.
	 * @author Andr� 
	 */
	public static float getTheta() {
		return THETA;
	}
	/**
	 * M�todo para retornar o par�metro Alfa.
	 * @author Andr� 
	 */
	public static double getAlfa() {
		return ALFA;
	}
	/**
	 * M�todo para retornar o par�metro Beta.
	 * @author Andr� 
	 */
	public static double getBeta() {
		return BETA;
	}
	/**
	 * M�todo para retornar o par�metro Gamma.
	 * @author Andr� 
	 */
	public static double getGamma() {
		return GAMMA;
	}
	/**
	 * M�todo para retornar o custo m�ximo da rota.
	 * @author Andr� 
	 */
	public static int getrMax() {
		return R_MAX;
	}
	/**
	 * M�todo para retornar o tempo m�ximo de busca.
	 * @author Andr� 
	 */
	public static int gettMax() {
		return T_MAX;
	}
	/**
	 * M�todo para retornar o n�mero de agentes.
	 * @author Andr� 
	 */
	public static int getNumberofagents() {
		return NUMBODAGENTS;
	}
	/**
	 * M�todo para retornar o n�mero de rotas.
	 * @author Andr� 
	 */
	public static int getNumberofpaths() {
		return NUMBOFPATHS;
	}
	/**
	 * M�todo para retornar o comprimento do maior enlace da rede.
	 * @author Andr� 
	 */
	public double getMaxLength() {
		return MAX_LENGTH;
	}
	/**
	 * M�todo para retornar o n�mero de dimens�es.
	 * @author Andr� 
	 */
	public int getDimension() {
		return DIMENSION;
	}	
}
