package routing;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import types.ModulationLevelType;
import types.RoutingAlgorithmType;
import utility.Function;
import arquitecture.OpticalSwitch;
import arquitecture.OpticalLink;
import parameters.SimulationParameters;
import exceptions.ExceptionsByMTHS;
import exceptions.InvalidNodeIdException;
import exceptions.InvalidRoutesException;

public class Routing {

    private ArrayList<List<RoutingAlgorithmSolution>> allRoutes;
    public final int KYEN;

    public Routing(final OpticalLink[][] network, final List<OpticalSwitch> listOfNodes, final RoutingAlgorithmType routAlgorType, final int KYEN) throws InvalidNodeIdException, InvalidRoutesException, ExceptionsByMTHS{

        this.allRoutes = new ArrayList<List<RoutingAlgorithmSolution>>(listOfNodes.size() * listOfNodes.size());
        this.KYEN = KYEN;

        int routeID = 0;

        final YenAlgorithm yenInstance = YenAlgorithm.getYenInstance();
        final IRoutingAlgorithm spInstance = ShortestPath.getShortestPathInstance();
        final IRoutingAlgorithm mhInstance = MinimumHops.getMinimumHopsInstance();

        if ((RoutingAlgorithmType.YEN == routAlgorType) || (RoutingAlgorithmType.FUZZY == routAlgorType) || (RoutingAlgorithmType.ALTERNATIVE == routAlgorType)){
            for (int originNode = 0; originNode < listOfNodes.size(); originNode++){
                for (int destinationNode = 0; destinationNode < listOfNodes.size(); destinationNode++){
                    List<RoutingAlgorithmSolution> routes = null;
                    if (originNode != destinationNode){
                        routes = yenInstance.findRoutes(network, originNode, destinationNode, listOfNodes, this.KYEN, routeID);

                        routeID += routes.size();
                    }
                    setRoute(originNode, destinationNode, routes);
                }//End for destinationNode
            }//End for originNode
        }//End if YEN

        if ((RoutingAlgorithmType.SP == routAlgorType) || (RoutingAlgorithmType.SCCSP == routAlgorType) || (RoutingAlgorithmType.CASP == routAlgorType)){
            for (int originNode = 0; originNode < listOfNodes.size(); originNode++){
                for (int destinationNode = 0; destinationNode < listOfNodes.size(); destinationNode++){
                    List<RoutingAlgorithmSolution> routes = null;
                    if (originNode != destinationNode){
                        routes = spInstance.findRoute(network, originNode, destinationNode, listOfNodes);
                    }
                    setRoute(originNode, destinationNode, routes);
                }//End for destinationNode
            }//End for originNode
        }//End for SP

        if (RoutingAlgorithmType.MH == routAlgorType){
            for (int originNode = 0; originNode < listOfNodes.size(); originNode++){
                for (int destinationNode = 0; destinationNode < listOfNodes.size(); destinationNode++){
                    List<RoutingAlgorithmSolution> routes = null;
                    if (originNode != destinationNode){
                        routes = mhInstance.findRoute(network, originNode, destinationNode, listOfNodes);
                    }
                    setRoute(originNode, destinationNode, routes);
                }//End for destinationNode
            }//End for originNode
        }//End for MH
    }//End Routing

    private void setRoute(int originNode, int destinationNode, List<RoutingAlgorithmSolution>  route){
        this.allRoutes.add(route);
    }

    public ArrayList<List<RoutingAlgorithmSolution>> getAllRoutes(){
        return this.allRoutes;
    }

    public void generateConflictList(){
        System.out.println("Criando a lista de rotas conflitantes");

        for (List<RoutingAlgorithmSolution> routesOD : this.allRoutes){
            if ((routesOD != null) && (routesOD.size() > 0)){
                for (RoutingAlgorithmSolution mainRoutes : routesOD){

                    List<RoutingAlgorithmSolution> conflictRoutes = new ArrayList<RoutingAlgorithmSolution>();
                    int currentRouteID = mainRoutes.getRouteID();

                    //Se pelo menos um link for compartilhado pelas rotas há um conflito
                    for (OpticalLink mainLink: mainRoutes.getUpLink()){
                        //System.out.println("");

                        // Percorre todas as Rotas
                        for (List<RoutingAlgorithmSolution> routesODAux : this.allRoutes){
                            if ((routesODAux != null) && (routesODAux.size() > 0)){
                                for (RoutingAlgorithmSolution mainRoutesAux : routesODAux){
                                    BREAK_LINK:for (OpticalLink link: mainRoutesAux.getUpLink()){
                                        if ((currentRouteID != mainRoutesAux.getRouteID()) && (mainLink == link)){
                                            conflictRoutes.add(mainRoutesAux);
                                            break BREAK_LINK;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    mainRoutes.setConflitList(conflictRoutes);
                }
            }
        }
    }

    public void generateConflictListBidirectional() throws ExceptionsByMTHS{
        System.out.println("Criando a lista de rotas conflitantes. Bidirecional");

        List<RoutingAlgorithmSolution> allRoutesAux = new ArrayList<RoutingAlgorithmSolution>();


        for (List<RoutingAlgorithmSolution> routesOD : this.allRoutes){
            if ((routesOD != null) && (routesOD.size() > 0)){
                for (RoutingAlgorithmSolution mainRoutes : routesOD){
                    allRoutesAux.add(mainRoutes);
                }
            }
        }

        // Percorre todas as Rotas
        for (RoutingAlgorithmSolution mainRoutes : allRoutesAux){

            // Cria a estrutura para armazenar a lista de conflitos
            List<RoutingAlgorithmSolution> conflictRoutes = new ArrayList<RoutingAlgorithmSolution>();

            // Armazena o ID da rota principal
            int currentRouteID = mainRoutes.getRouteID();

            List<OpticalLink> mainRoutesUplinks = mainRoutes.getUpLink();
            List<OpticalLink> mainRoutesDownlinks = mainRoutes.getDownLink();

            if (mainRoutesUplinks.size() != mainRoutesDownlinks.size()){
                throw new ExceptionsByMTHS("Os tamanhos de Uplink e Downlink são diferentes", 1);
            }

            // Procura por conflito dentro com os links presente no Uplink da rota principal
            for (int mainl = 0; mainl < mainRoutesUplinks.size(); mainl++) {

                OpticalLink mainRoutesUplink = mainRoutesUplinks.get(mainl);
                OpticalLink mainRoutesDownlink = mainRoutesDownlinks.get(mainl);

                // Percorre todas as Rotas
                for (RoutingAlgorithmSolution auxConflictRoutes : allRoutesAux){
                
                    List<OpticalLink> auxConflictRoutesUplinks = auxConflictRoutes.getUpLink();
                    List<OpticalLink> auxConflictRoutesDownlinks = auxConflictRoutes.getDownLink();

                    // Percorre os uplinks e downlinks da rota auxConflictRoutes
                    BREAK:for (int auxl = 0; auxl < auxConflictRoutesUplinks.size(); auxl++){

                        OpticalLink auxConflictRoutesUplink = auxConflictRoutesUplinks.get(auxl);
                        OpticalLink auxConflictRoutesDownlink = auxConflictRoutesDownlinks.get(auxl);

                        // Verifica se a rota auxiliar não é a rota principal.
                        boolean isMainRoute = (currentRouteID == auxConflictRoutes.getRouteID());

                        // Verifica se o mainUp é diferente do auxUp
                        boolean isMainUpAuxUp = (mainRoutesUplink == auxConflictRoutesUplink);
                        // Verifica se o mainUp é diferente do auxDown
                        boolean isMainUpAuxDown = (mainRoutesUplink == auxConflictRoutesDownlink);
                        // Verifica se o mainDown é diferente do auxUp
                        boolean isMainDownAuxUp = (mainRoutesDownlink == auxConflictRoutesUplink);
                        // Verifica se o mainDown é diferente do auxDown
                        boolean isMainDownAuxDown = (mainRoutesDownlink == auxConflictRoutesDownlink);

                        boolean isAlreadyAdd = false;
                        BREAK_ADD: for (RoutingAlgorithmSolution RouteInConflict: conflictRoutes){
                            if (RouteInConflict == auxConflictRoutes){
                                isAlreadyAdd = true;
                                break BREAK_ADD;
                            }
                        }

                        if (!isMainRoute && (isMainUpAuxUp || isMainUpAuxDown || isMainDownAuxUp || isMainDownAuxDown) && !isAlreadyAdd){
                            conflictRoutes.add(auxConflictRoutes);
                            break BREAK;
                        }
                    }
                } // end for auxConflictRoutes

                mainRoutes.setConflitList(conflictRoutes);
            } // end for mainRoutesUplink
        }// end for mainRoutes
    }// end function


    public void cleanRoutes(){

        for (List<RoutingAlgorithmSolution> routesOD : this.allRoutes){
            if ((routesOD != null) && (routesOD.size() > 0)){
                for (RoutingAlgorithmSolution mainRoutes : routesOD){
                    mainRoutes.resetSlotValue();
                }
            }
        }
    }

    public void OrderConflictRoutes(){
        System.out.println("Ordenando a lista de rotas conflitantes");

        for (List<RoutingAlgorithmSolution> routesOD : this.allRoutes){
            if ((routesOD != null) && (routesOD.size() > 0)){
                for (RoutingAlgorithmSolution mainRoutes : routesOD){

                    List<RoutingAlgorithmSolution> conflictRoutes = mainRoutes.getConflictRoute();
                    int currentRouteID = mainRoutes.getRouteID();

                    List<RoutingAlgorithmSolution> orderConflictRoutes = new ArrayList<RoutingAlgorithmSolution>();

                    final int numRoutes = conflictRoutes.size();
		            for (int cRoute = 0; cRoute < numRoutes; cRoute++){
                        //Percorre as soluções Fuzzy
                        int worseRouteIndex = -1;
                        double maxHops = -1;
                        double maxDistance = -1;
                        for (int nSolution = 0; nSolution < conflictRoutes.size(); nSolution++){
                            // Procura a rota com o menor CTR
                            if(conflictRoutes.get(nSolution).getNumHops() > maxHops){
                                //if (conflictRoutes.get(nSolution).getSizeInBound() > maxDistance){
                                maxHops = conflictRoutes.get(nSolution).getNumHops();
                                maxDistance = conflictRoutes.get(nSolution).getSizeInBound();
                                worseRouteIndex = nSolution;	//Salva a posição da rota
                                //}
                            }
                        }

                        orderConflictRoutes.add(conflictRoutes.get(worseRouteIndex));

                        conflictRoutes.remove(worseRouteIndex);
                    }

                    mainRoutes.setConflitList(orderConflictRoutes);
                }
            }
        }
    }

    public void findAllModulationsByRoute() throws Exception {
        System.out.println("Encontrando as modulações para as rotas");

        SimulationParameters parameters = new SimulationParameters();
        Function function = new Function();

        for (List<RoutingAlgorithmSolution> routesOD : this.allRoutes){
            if ((routesOD != null) && (routesOD.size() > 0)){
                for (RoutingAlgorithmSolution mainRoutes : routesOD){

                    List<ModulationLevelType> modulationLevelByBitrate = new ArrayList<ModulationLevelType>();

                    POINT_BITRATE:for (int bitRate: parameters.getBitRate()){

                        for (ModulationLevelType modLevel: parameters.getModulationLevelType()){

                            final double snrLinear = Math.pow(10, modLevel.getSNRIndB()/10);
                            final double osnrLinear = (((double)bitRate*1E9)/(2*SimulationParameters.getSpacing()))*snrLinear;

                            final double inBoundQot = function.evaluateOSNR(mainRoutes.getUpLink(), 159);

                            if(inBoundQot>=osnrLinear){
                                modulationLevelByBitrate.add(modLevel);
                                continue POINT_BITRATE;
                            }
                        }
                    }

                    if (parameters.getBitRate().length == modulationLevelByBitrate.size()){
                        mainRoutes.setModulationsTypeByBitrate(modulationLevelByBitrate);
                    } else {
                        System.out.println("ERRRo");
                    }
                }
            }
        }
    }

    public void findAllSizeSlotsAndModulationByRoute() throws Exception {
        System.out.println("Encontrando as modulações para as rotas");

        SimulationParameters parameters = new SimulationParameters();
        Function function = new Function();

        for (List<RoutingAlgorithmSolution> routesOD : this.allRoutes){
            if ((routesOD != null) && (routesOD.size() > 0)){
                for (RoutingAlgorithmSolution mainRoutes : routesOD){

                    List<ModulationLevelType> modulationLevelByBitrate = new ArrayList<ModulationLevelType>();
                    List<Integer> sizeSlotsByBitrate = new ArrayList<Integer>();

                    POINT_BITRATE:for (int bitRate: parameters.getBitRate()){

                        for (ModulationLevelType modLevel: parameters.getModulationLevelType()){

                            final double snrLinear = Math.pow(10, modLevel.getSNRIndB()/10);
                            final double osnrLinear = (((double)bitRate*1E9)/(2*SimulationParameters.getSpacing()))*snrLinear;

                            final double inBoundQot = function.evaluateOSNR(mainRoutes.getUpLink(), 159);

                            if(inBoundQot>=osnrLinear){
                                modulationLevelByBitrate.add(modLevel);

                                // Encontra o tamanho do slot
                                final int reqNumbOfSlots = function.calculateNumberOfSlots(modLevel, bitRate);

                                sizeSlotsByBitrate.add(reqNumbOfSlots);

                                continue POINT_BITRATE;
                            }
                        }
                    }

                    if (parameters.getBitRate().length == modulationLevelByBitrate.size()){
                        mainRoutes.setModulationsTypeByBitrate(modulationLevelByBitrate);
                        mainRoutes.setSizeSlotTypeByBitrate(sizeSlotsByBitrate);
                    } else {
                        System.out.println("ERRRo");
                    }
                }
            }
        }
    }



}//END class Routing
