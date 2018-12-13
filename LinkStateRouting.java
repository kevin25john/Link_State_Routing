import java.io.*;
import java.util.*;


import java.lang.*;

class LinkStateRouting{

    public static LinkedList<Router> routerList = new LinkedList<Router>();
    public static HashMap<Integer,Router> routerListMapActualID = new HashMap<Integer,Router>();
    public static int totalVertex;
    public static void main(String[] args) throws Exception{

        LinkStateRouting l = new LinkStateRouting();
        l.readFile();
        
        totalVertex = routerList.size();


        System.out.println("Enter your choice: ");
        while(true){

            System.out.println();
            System.out.println("Enter 'C' to continue\n      'Q' to quit\n      'P' followed by a space and then the router\'s id number to print the routing table of a router\n      'S' followed by a space and then the id number to shut down a router\n      'T' followed by a space and then the id to start up a router ");
            Scanner scan = new Scanner(System.in);
            String[] readChoice = scan.next().split(" ");
            try {
                switch(readChoice[0].toUpperCase()){
                    case "C":
                        for(Router temp : routerList){
                            temp.originatePacket();
                        }
                        break;
                    case "Q":
                        System.exit(0);
                    case "P":
                        int printInput = scan.nextInt();
                        routerListMapActualID.get(printInput).printRoutingTable();
                        break;
                    case "T":
                        int startInput = scan.nextInt();
                        routerListMapActualID.get(startInput).startRouter();
                        for(Router temp : routerList){
                            temp.originatePacket();
                        }
                        break;
                    case "S":
                        int stopInput = scan.nextInt();
                        routerListMapActualID.get(stopInput).stopRouter();
                        for(Router temp : routerList){
                            temp.originatePacket();
                        }
                        break;
                    default:
                        System.out.println("invalid input");
                        break;
                }
                
            } catch (Exception e) {
                System.out.println("Invalid Input");
            }
            

        }

    }

    public void readFile() throws Exception{
        File f = new File("infile.dat");
        Scanner sc = new Scanner(f);
        Scanner scanConn = new Scanner(f);
        int currentMainRouter =0;
        String temp ="";
        int count = 0;

        while(sc.hasNextLine()){ //read network id and name

            String[] read = sc.nextLine().split("");
            if(!read[0].equals(" ")){
                String tmp = "";
                int j=0;
                for(int i=0;i<read.length;i++){

                    if(read[i].equals(" ")){
                        j = i;
                        for(int k=0;k<i;k++){
                            tmp = tmp + read[k];
                        }

                        currentMainRouter = Integer.parseInt(tmp);
                        tmp = "";
                    }
                    
                    else if(i>j && currentMainRouter!=0){
                        if(read[i]!=" "){
                            temp = temp + read[i];
                        }
                    }
                }
                
                routerList.add(new Router(count, temp));

                routerList.get(count).actualID = currentMainRouter;
                routerListMapActualID.put(currentMainRouter,routerList.get(count));
                temp = "";
                count++;
            }
            

            
        }sc.close();

        count = 0;

        while(scanConn.hasNextLine()){  // read connections and cost

            String[] read = scanConn.nextLine().split("");
            if(!read[0].equals(" ")){
                String tmp = "";
                int j=0;
                for(int i=0;i<read.length;i++){

                    if(read[i].equals(" ")){
                        j = i;
                        for(int k=0;k<i;k++){
                            tmp = tmp + read[k];
                        }

                        currentMainRouter = Integer.parseInt(tmp);
                        tmp = "";
                    }
                    
                    
                }

            }


            if(read[0].equals(" ")){
                int destRouter = 0;
                if(read.length>2 && read.length>3){
                    
                    Iterator it = routerListMapActualID.entrySet().iterator();
                    while (it.hasNext()) {
                        String connRead= "";

                        Map.Entry pair = (Map.Entry)it.next();
                        for(int i=1;i<read.length;i++){
                            if(read[i].equals(" ")){
                                break;
                            }
                            else{
                                connRead = connRead + read[i];
                            }
                        }

                        if((Integer)pair.getKey() == Integer.parseInt(connRead)){
                            destRouter = (Integer) pair.getKey();
                        }
                        //it.remove(); // avoids a ConcurrentModificationException
                    }
                    
                    String costRead = "";
                    for(int i=1;i<read.length;i++){
                        if(read[i].equals(" ")){
                            for(int j=i+1;j<read.length;j++){
                                costRead = costRead+1;
                            }
                        }
                    }

                    routerListMapActualID.get(currentMainRouter).connections(routerListMapActualID.get(destRouter), Integer.parseInt(costRead));
                    
                }
                else{
                    
                    Iterator it = routerListMapActualID.entrySet().iterator();
                    while (it.hasNext()) {
                        String connRead = "";

                        Map.Entry pair = (Map.Entry)it.next();
                        for(int i=1;i<read.length;i++){
                            if(read[i].equals(" ")){
                                break;
                            }
                            else{
                                connRead = connRead + read[i];
                            }
                        }

                        if((Integer)pair.getKey() == Integer.parseInt(connRead)){
                            destRouter = (Integer) pair.getKey();
                        }
                        //it.remove(); // avoids a ConcurrentModificationException
                    }
                    routerListMapActualID.get(currentMainRouter).connections(routerListMapActualID.get(destRouter), 1);
                }
                
                count ++;
            }
            
        }scanConn.close();

        count =0;
    }

}


class Router{
    
    int ID;
    int actualID;
    String networkName;
    Boolean status;
    HashMap<Integer,Integer> connectionList;
    HashMap<routingTable, Integer> routingTableMap;
    HashMap<Integer, Integer> highestSeqNumber;
    LinkedList<routingTable> routingTableList;
    int SN;
    int tick;
    Boolean receivedLSP;
    HashMap <Integer, Integer> lspReceived;
    HashMap <Integer, Integer> tickCounter;
    HashMap <Integer, Boolean> tickCheck;


    Router(int ID, String networkName){

        this.ID = ID;
        this.actualID = actualID;
        this.networkName = networkName;
        this.routingTableMap = new HashMap<routingTable, Integer>();
        this.routingTableList = new LinkedList<routingTable>();
        this.status = true;
        this.connectionList = new HashMap<Integer, Integer>();
        this.highestSeqNumber = new HashMap<Integer, Integer>();
        this.lspReceived = new HashMap<Integer, Integer>();     //1 for true, 0 for false
        this.tickCounter = new HashMap<Integer, Integer>();
        this.tickCheck = new HashMap<Integer, Boolean>();
        
        this.SN = 1;
        this.receivedLSP = false;
        this.tick = 0;

    }

    public void connections(Router router, int cost){
        
        this.connectionList.put(router.ID, cost);
        
        Iterator it = this.connectionList.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                this.tickCounter.put((Integer)pair.getKey(), 0);
                this.tickCheck.put((Integer)pair.getKey(), false);
                //it.remove(); // avoids a ConcurrentModificationException
            }
        

        
    }

   

    public void createGraph(){
        //System.out.println(this.ID+" "+this.connectionList);

        LinkStateRouting ls = new LinkStateRouting();
        int vertices = ls.routerList.size();
        //System.out.println("createGraph called");
        Graph graph = new Graph(vertices);
        //System.out.println(vertices);
        int count = 0;
        //System.out.println(ls.routerList.size());
        for(Router rs : ls.routerList){
            count++;
            //System.out.println("in graph loop");
            //System.out.println(rs.ID);
            //System.out.println(rs.connectionList.size());
            Iterator it = rs.connectionList.entrySet().iterator();
            //System.out.println(rs.ID);
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                //System.out.print(pair.getKey());
                //count++;
                graph.addEdge(rs.ID,(Integer) pair.getKey(),(Integer) pair.getValue());
                
                //System.out.println(rs.ID+" "+(Integer) pair.getKey()+ " "+(Integer) pair.getValue());
               // System.out.println(rs.ID);

                //it.remove(); // avoids a ConcurrentModificationException
            } 
            

        }


        graph.djiktra(this.ID);

        //this.routingTableList.clear();


    }


    public void updateGraph(){

    }


    public void originatePacket(){
        

        if(this.status == true){
            
            Iterator it = this.connectionList.entrySet().iterator();
            int tick = 0;
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                
                tick = this.tickCounter.get((Integer) pair.getKey());

                if(!tickCheck.get((Integer) pair.getKey())){
                    tick = tick +1;
                    this.tickCounter.replace((Integer) pair.getKey(), tick);
                }

                if(this.tickCounter.get(pair.getKey()) >1){
                    this.connectionList.replace((Integer) pair.getKey(), 585858222);
                    //System.out.println(this.connectionList);

                    LinkStateRouting lsst = new LinkStateRouting();
                    lsst.routerList.get((Integer) pair.getKey()).connectionList.replace(this.ID, 585858222);
                    //System.out.println(lsst.routerList.get(0).connectionList);
                    //System.out.println(this.ID);
                    //System.out.println(lsst.routerList.get((Integer) pair.getKey()).connectionList);
                    
                }
                else{

                    LinkStateRouting l = new LinkStateRouting();
                    Router connectedRouter = l.routerList.get((Integer)pair.getKey());
                    Router originRouter  = l.routerList.get(this.ID);
                    

                    connectedRouter.tickCheck.replace(this.ID, true);
                    connectedRouter.tickCounter.replace(this.ID, 0);


                    connectedRouter.receivePacket(new packet(originRouter, this.SN,(Integer) pair.getValue()), this.ID, this.ID);
                    
                    
                    
                    
                    this.SN++;

                }


                //it.remove(); // avoids a ConcurrentModificationException
            }
        }
        else{
            Iterator it = this.connectionList.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                LinkStateRouting l = new LinkStateRouting();
                Router connectedRouter = l.routerList.get((Integer)pair.getKey());
                Router originRouter  = l.routerList.get(this.ID);
                connectedRouter.tickCheck.replace(this.ID, false);

            }

        }

    }


   

    public void receivePacket(packet packet, int originRouterID, int receiverRouterID) {
        
        if(this.status == true){
            //System.out.println("packet received on" + this.actualID);
            LinkStateRouting lst = new LinkStateRouting();
            Router originRouter = lst.routerList.get(originRouterID);
            int currentRouterrID = this.ID;
            //System.out.println(currentRouterrID);
            packet.TTL = packet.TTL - 1;
            if(!originRouter.highestSeqNumber.containsKey(originRouter.ID)){
                originRouter.highestSeqNumber.put(originRouter.ID, packet.SN);
            }

            if(packet.TTL >0 || originRouter.highestSeqNumber.get(originRouter.ID) < packet.SN){
            
                originRouter.highestSeqNumber.replace(originRouter.ID, packet.SN);

                Iterator it = this.connectionList.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    
                    if((Integer) pair.getKey() != receiverRouterID){

                        Router forwardRouter = lst.routerList.get((Integer) pair.getKey());
                        forwardRouter.receivePacket(packet, originRouterID, this.ID);
                    }
                    
                    //it.remove(); // avoids a ConcurrentModificationException
                }

            }
            
        }
        else{
            LinkStateRouting ls = new LinkStateRouting();
            ls.routerList.get(receiverRouterID).tickCheck.replace(this.ID, false);
            
        }


    }


    

    public void startRouter(){
        this.status = true;
        System.out.println("Router "+this.actualID+ " is started");
    }

    public void stopRouter(){
        this.status = false;
        System.out.println("Router "+this.actualID+ " is stopped");
    
    }

    public void printRoutingTable(){
        if(this.status ==true){
        this.createGraph();
        System.out.println("Network \t Outgoing Link \t \t \tcost");                           
        System.out.println();
        System.out.println(this.actualID + "\t \t \t"+ this.actualID + "\t \t \t" + "0");
        for(routingTable rs : routingTableList){
            
            System.out.println(rs.router.actualID + "\t \t \t" + rs.outGoingLink.actualID + "\t \t \t" + rs.cost);

        }


        

        this.routingTableList.clear();
        }
        else{
            System.out.println("Router "+ this.actualID + " is currently shut down.");
        }
    }


    static class routingTable{
        Router router;
        Router outGoingLink;
        int cost;
        //int SN = 0;
        //int Tick = 0;
        routingTable(Router router, Router outGoingLink, int cost){
            this.router = router;
            this.outGoingLink = outGoingLink;
            this.cost = cost;
            //this.SN = SN;
            //this.Tick = Tick;
        }
        
    }

    static class Graph{ //https://algorithms.tutorialhorizon.com/djkstras-shortest-path-algorithm-adjacency-matrix-java-code/

        int vertices;
        int graphMatrix[][];
        routingTable routingTable;
        LinkStateRouting ls;
        public Graph(int vertex){
            this.vertices = vertex;
            graphMatrix = new int[vertex][vertex];
            this.ls = new LinkStateRouting();
           
        }

        public void addEdge(int source, int destination, int cost){
            graphMatrix[source][destination] = cost;
            graphMatrix[destination][source] = cost;
        }

        int getMinimum(boolean [] mst, int [] key){
            int minKey = Integer.MAX_VALUE;
            int vertex = -1;
            for(int i =0; i<vertices ; i++){
                if(mst[i]==false && minKey>key[i]){
                    minKey = key[i];
                    vertex = i;
                }
            }
            return vertex;
        }

        public void djiktra(int source){
            boolean[] spt = new boolean[vertices];
            int [] dist = new int[vertices];
            int INFINITY = Integer.MAX_VALUE;
        
            
            for(int i =0;i<vertices;i++){
                dist[i] = INFINITY;
            }

            dist[source] = 0;
            int outgoingVertex = source;

            for(int i=0; i<vertices;i++){

                int vertex_u = getMinimum(spt, dist);
                
                if(vertex_u != source && this.ls.routerList.get(source).connectionList.containsKey(vertex_u)){
                    outgoingVertex = vertex_u;
                }

                spt[vertex_u] = true;
                
                for(int vertex_v = 0; vertex_v<vertices;vertex_v++){
                    if(graphMatrix[vertex_u][vertex_v]>0){


                        if(spt[vertex_v]==false && graphMatrix[vertex_u][vertex_v]!=INFINITY){

                            int newKey = graphMatrix[vertex_u][vertex_v] + dist[vertex_u];
                            if(newKey<dist[vertex_v]){
                                dist[vertex_v] = newKey;
                                
                                if(outgoingVertex != source){
                                    this.ls.routerList.get(source).routingTableList.add(new routingTable(this.ls.routerList.get(vertex_v), this.ls.routerList.get(outgoingVertex), newKey));

                                }
                                else{
                                    
                                    this.ls.routerList.get(source).routingTableList.add(new routingTable(this.ls.routerList.get(vertex_v), this.ls.routerList.get(vertex_v), newKey));

                                }

                                
                            }
                        }

                    }
                }





            }

            
        }


    


        
    }

}


class packet{
    int SN;
    //Router router;
    int totalCost =0;
    int TTL=10;
    int originID;
    HashMap<Integer, Router> reachableRouters;
    Router originRouter;

    packet(Router originRouter, int SN,int cost){
        this.originRouter = originRouter;
        this.originID = originRouter.ID;
        this.SN = SN;
        this.TTL = TTL -1;
        this.totalCost = cost;
        this.reachableRouters = new HashMap<>();


    }
}


