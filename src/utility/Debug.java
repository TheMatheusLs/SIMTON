package utility;

import report_save.CreateFolder;

public class Debug {

    final boolean isDebugReqON;
    final CreateFolder createFolder;
    final boolean isAlternativeKYEN;
    
    // * Runner
    int networkLoad;
    int numSimulationID;
    String logReqSimulationFilename;
    String logReqSimulationHeader;

    String folderToReadReqs;

    // * RMLSASimulation > simulation
    double OSNRTh;
    double OSNRInbond;
    int routeKYEN;
    int routeToFind;
    int reqNumbOfSlots;
    boolean hasQoT;
    boolean hasSlots;

    public Debug(boolean isDebugReqON, CreateFolder createFolder, boolean isAlternativeKYEN){
        this.isDebugReqON = isDebugReqON;
        this.createFolder = createFolder;
        this.isAlternativeKYEN = isAlternativeKYEN;

        this.folderToReadReqs = null;

        this.networkLoad = -1;
        this.numSimulationID = -1;
        this.logReqSimulationFilename = null;
        this.logReqSimulationHeader = null;

        this.OSNRTh = -1.0;
        this.OSNRInbond = -1.0;
        this.routeKYEN = -1;
        this.routeToFind = -1;

        this.reqNumbOfSlots = -1;

        this.hasQoT = false;
        this.hasSlots = false;
    }

    public boolean isDebugReqON() {
        return this.isDebugReqON;
    }

    public boolean isAlternativeKYEN() {
        return this.isAlternativeKYEN;
    }
    
    // *** Getters
    public CreateFolder getCreateFolder(){
        return this.createFolder;
    }

    public int getNetworkLoad(){
        return this.networkLoad;
    }

    public int getNumSimulationID(){
        return this.numSimulationID;
    }

    public String getLogReqSimulationHeader(){
        return this.logReqSimulationHeader;
    }

    public String getLogReqSimulationFilename(){
        return this.logReqSimulationFilename;
    }

    public double getOSNRTh(){
        return this.OSNRTh;
    }

    public double getOSNRInbond(){
        return this.OSNRInbond;
    }

    public int getRouteKYEN(){
        return this.routeKYEN;
    }

    public int getRouteToFind(){
        return this.routeToFind;
    }
    
    public int getReqNumbOfSlots(){
        return this.reqNumbOfSlots;
    }

    public boolean getHasQoT(){
        return this.hasQoT;
    }

    public boolean getHasSlots(){
        return this.hasSlots;
    }

    public String getFolderToReadReqs(){
        return this.folderToReadReqs;
    }

    // *** Setters
    public void setNetworkLoad(int networkLoad){
        this.networkLoad = networkLoad;
    }

    public void setNumSimulationID(int numSimulationID){
        this.numSimulationID = numSimulationID;
    }

    public void setLogReqSimulationHeader(String logReqSimulationHeader){
        this.logReqSimulationHeader = logReqSimulationHeader;
    }

    public void setLogReqSimulationFilename(String logReqSimulationFilename){
        this.logReqSimulationFilename = logReqSimulationFilename;
    }

    public void setOSNRTh(double OSNRTh){
        this.OSNRTh = OSNRTh;
    }

    public void setOSNRInbond(double OSNRInbond){
        this.OSNRInbond = OSNRInbond;
    }

    public void setRouteKYEN(int routeKYEN){
        this.routeKYEN = routeKYEN;
    }

    public void setrouteToFind(int routeToFind){
        this.routeToFind = routeToFind;
    }

    public void setReqNumbOfSlots(int reqNumbOfSlots){
        this.reqNumbOfSlots = reqNumbOfSlots;
    }

    public void setHasQoT(boolean hasQoT){
        this.hasQoT = hasQoT;
    }

    public void setHasSlots(boolean hasSlots){
        this.hasSlots = hasSlots;
    }

    public void setFolderToReadReqs(String folder){
        this.folderToReadReqs = folder;
    }
}  