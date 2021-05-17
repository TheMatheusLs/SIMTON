package report_save;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

import parameters.SimulationParameters;
import types.MetricMethodType;
import types.ModulationLevelType;
import types.PSOType;
import types.ParticlePSOType;
import types.RoutingAlgorithmType;
import types.TopologyType;

public class CreateFolder {
    String simulationType;
    String topologyType;
    String dateForm;
    String folderName;
    String folderPath;
    String mainFolderName = "D:\\ProgrammingFiles\\ReportsSIMTON\\"; // DANILO

    public CreateFolder(String simulationType, String topologyType){
        this.simulationType = simulationType;
        this.topologyType = topologyType;

        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yy_HH-mm-ss");  
        LocalDateTime currentDateTime = LocalDateTime.now();  
        this.dateForm = dateTimeFormat.format(currentDateTime);
        
        this.folderName = this.dateForm + "_" + this.simulationType + "_" + this.topologyType;

        boolean statusFolder = new java.io.File(this.mainFolderName, this.folderName).mkdirs();

        if (statusFolder){
            System.out.println("Pasta criada com o nome: " + this.folderName);  
        }
        else{
            System.out.println("ERRO: Pasta não foi criada");  

        }
    }

    public String getFolderName(){
        return this.folderName;
    }

    public String getMainFolderName(){
        return this.mainFolderName;
    }

    public void writeInResults(String text){
        try {
            final FileWriter file = new FileWriter(this.mainFolderName + "\\" + this.folderName + "\\results.csv", true); 
            final PrintWriter saveFile = new PrintWriter(file);

            saveFile.printf(text + "\n");
            saveFile.close();
            
        } catch (Exception e) {
            //TODO: handle exception
        }
    }

    public void writeDone(double executionTime){
        try {
            final File file = new File(this.mainFolderName + "\\" + this.folderName + "\\DONE.md"); 
            final PrintWriter saveFile2 = new PrintWriter(file);

            saveFile2.printf("Simulação finalizada com sucesso" + "\n");
            saveFile2.printf("Tempo total de: " + Double.toString(executionTime) + " segundos \n");
            saveFile2.close();

            // Renomeia a pasta para faciliar a visualização

            final File oldNameFile = new File(this.mainFolderName + "\\" + this.folderName);
            final File newNameFile = new File(this.mainFolderName + "\\" + this.folderName + "_OK");
            oldNameFile.renameTo(newNameFile);

            System.out.println(this.getFolderName() + "_OK");
            
        } catch (Exception e) {
            //TODO: handle exception
        }
    }

    public void writeFile(String filename, String txt){
        try {
            final FileWriter file = new FileWriter(this.mainFolderName + "\\" + this.folderName + "\\" + filename, true); 
            final PrintWriter saveFile2 = new PrintWriter(file);

            saveFile2.printf(txt + "\n");
            saveFile2.close();
            
        } catch (Exception e) {
            //TODO: handle exception
        }
    }

    public void writeParameters(TopologyType TOPOLOGY_TYPE, RoutingAlgorithmType ROUTING_ALGORITHM_TYPE, int networkLoad, int NUMBER_OF_SIMULATIONS, int NETWORK_LOAD_STEP, short NUMBER_NETWORK_LOAD, int NUMBER_REQUEST_MAX, int KYEN, MetricMethodType fuzzyLogicType, double[] listOfCoefficients){

        final SimulationParameters parameters = new SimulationParameters();

        try {
            final FileWriter file = new FileWriter(this.mainFolderName + "\\" + this.folderName + "\\parameters.txt", true); 
            final PrintWriter saveFile3 = new PrintWriter(file);

            saveFile3.printf(String.format("TOPOLOGY_TYPE: %s%n", TOPOLOGY_TYPE.getDescription())); 
            saveFile3.printf(String.format("ROUTING_ALGORITHM_TYPE: %s%n", ROUTING_ALGORITHM_TYPE.getName())); 
            saveFile3.printf(String.format("networkLoad: %d%n", networkLoad)); 
            saveFile3.printf(String.format("NUMBER_OF_SIMULATIONS: %d%n", NUMBER_OF_SIMULATIONS)); 
            saveFile3.printf(String.format("NETWORK_LOAD_STEP: %d%n", NETWORK_LOAD_STEP)); 
            saveFile3.printf(String.format("NUMBER_NETWORK_LOAD: %d%n", NUMBER_NETWORK_LOAD)); 
            saveFile3.printf(String.format("NUMBER_REQUEST_MAX: %d%n", NUMBER_REQUEST_MAX)); 
            saveFile3.printf(String.format("KYEN: %d%n", KYEN)); 
            saveFile3.printf(String.format("fuzzyLogicType: %s%n", fuzzyLogicType.getDescription())); 

            saveFile3.printf("NOISE_FIGURE: "+ Double.toString(parameters.getNoiseFigureIndB()) + "\n");
            saveFile3.printf("ATENU_COEFFIC: "+ Double.toString(parameters.getFiberAtenuationCoefficient()) + "\n");
            saveFile3.printf("SPAN_SIZE :"+ Double.toString(parameters.getSpanSize()) + "\n");
            saveFile3.printf("MUX_LOSS :"+ Double.toString(parameters.getMuxLoss()) + "\n");
            saveFile3.printf("SWITCH_LOSS: "+ Double.toString(parameters.getSwitchLoss()) + "\n");
            saveFile3.printf("LASER_POWER: "+ Double.toString(parameters.getLaserPower()) + "\n");
            saveFile3.printf("OSNR_IN: "+ Double.toString(parameters.getOSNRIn()) + "\n");
            saveFile3.printf("DIO_LOSS: "+ Double.toString(parameters.getDioLoss()) + "\n");
            saveFile3.printf("LIGHT_CONSTANT: "+ Double.toString(parameters.getC()) + "\n");
            saveFile3.printf("INITIAL_LAMBDA: "+ Double.toString(parameters.getInitialLambda()) + "\n");
            saveFile3.printf("NUMBER_SLOTS: "+ Double.toString(parameters.getNumberOfSlots()) + "\n");

            saveFile3.printf("BIT_RATE: [");
            for (int i = 0; i < parameters.getBitRate().length; i++){
                saveFile3.printf(parameters.getBitRate()[i] + ", ");
            }
            saveFile3.printf("] \n");
            
            saveFile3.printf("MAX_TIME: "+ Double.toString(parameters.getMaxTime()) + "\n");
            saveFile3.printf("MEAN_RATE: "+ Double.toString(parameters.getMeanRateOfCallsDuration()) + "\n");
            // saveFile3.printf("MODUL_LEVEL: "+ Double.toString(parameters.getModulationLevelType()) + "\n");
            
            saveFile3.printf("MODUL_LEVEL: [");
            for (ModulationLevelType modulation : parameters.getModulationLevelType()) 
            {   
                saveFile3.printf(modulation.getDescription() + ", ");
            }
            saveFile3.printf("] \n");

            if (listOfCoefficients != null){
                saveFile3.printf("listOfCoefficients: [");
                for (double value : listOfCoefficients){   
                    saveFile3.printf(Double.toString(value) + ", ");
                }
                saveFile3.printf("] \n");
            }else{
                saveFile3.printf("listOfCoefficients: null %n");
            }

            saveFile3.printf("CREATION_FREQ: "+ Double.toString(parameters.getCreationfrequency()) + "\n");
            
            saveFile3.printf("EVAPOR_RATE: %f %n", parameters.getSpacing());
            saveFile3.printf("R_MAX: %d %n", parameters.getrMax());
            saveFile3.printf("DIMENSION: %d %n", parameters.getDimension());

            saveFile3.close();
            
        } catch (Exception e) {
            //TODO: handle exception
        }
    }

    public void writeParametersPSO(TopologyType TOPOLOGY_TYPE, RoutingAlgorithmType ROUTING_ALGORITHM_TYPE, int networkLoad, int NUMBER_OF_SIMULATIONS, int NETWORK_LOAD_STEP, short NUMBER_NETWORK_LOAD, int NUMBER_REQUEST_MAX, int KYEN, MetricMethodType fuzzyLogicType, double[] listOfCoefficients, PSOType PSO_TYPE, int PSO_NUMBER_OF_PARTICLES, int PSO_NUMBER_OF_INTERATIONS, boolean PSO_SIDESTEP, ParticlePSOType PSO_PARTICLE_TYPE, double[] PSO_COEFF_SEED){
    
        try {
            writeParameters(TOPOLOGY_TYPE, ROUTING_ALGORITHM_TYPE, networkLoad, NUMBER_OF_SIMULATIONS, NETWORK_LOAD_STEP, NUMBER_NETWORK_LOAD, NUMBER_REQUEST_MAX, KYEN, fuzzyLogicType, listOfCoefficients);
            
            final FileWriter file = new FileWriter(this.mainFolderName + "\\" + this.folderName + "\\parameters.txt", true); 
            final PrintWriter saveFile3 = new PrintWriter(file);

            saveFile3.printf(String.format("PSO_TYPE: %s%n", PSO_TYPE.getDescription())); 
            saveFile3.printf(String.format("PSO_NUMBER_OF_PARTICLES: %d%n", PSO_NUMBER_OF_PARTICLES)); 
            saveFile3.printf(String.format("PSO_NUMBER_OF_INTERATIONS: %d%n", PSO_NUMBER_OF_INTERATIONS)); 
            saveFile3.printf(String.format("PSO_SIDESTEP: %b%n", PSO_SIDESTEP)); 
            saveFile3.printf(String.format("PSO_PARTICLE_TYPE: %s%n", PSO_PARTICLE_TYPE.getName()));  

            if (PSO_COEFF_SEED != null){
                saveFile3.printf("PSO_COEFF_SEED: [");
                for (double value : PSO_COEFF_SEED){   
                    saveFile3.printf(Double.toString(value) + ", ");
                }
                saveFile3.printf("] \n");
            }else{
                saveFile3.printf("PSO_COEFF_SEED: null %n");
            }
            saveFile3.printf("] \n");

            saveFile3.close();
            
        } catch (Exception e) {
            //TODO: handle exception
        }
    }
}