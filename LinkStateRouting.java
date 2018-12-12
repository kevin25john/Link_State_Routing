import java.io.*;
import java.util.*;

//import com.sun.tools.jdi.Packet;

import java.lang.*;
//import java.util.HashMap;

class LinkStateRouting{

    public static LinkedList<Router> routerList = new LinkedList<Router>();
    public static HashMap<Integer,Router> routerListMapActualID = new HashMap<Integer,Router>();
    //public static HashMap<Integer,Object> connectionList = new HashMap<>();
    public static int totalVertex;
    public static void main(String[] args) throws Exception{

        LinkStateRouting l = new LinkStateRouting();
        l.readFile();
        // for(Router temp : routerList){
        //     System.out.println(temp.ID + temp.networkName);
        // }

        // for(Router temp : routerList){
        //     System.out.print(temp.ID + "  ");
        //     System.out.println(temp.connectionList);
        // }

        totalVertex = routerList.size();


        System.out.println("Enter your choice: ");
        while(true){

            System.out.println();
            System.out.println("Enter 'C' to continue\n      'Q' to quit\n      'P' followed by the router\'s id number to print the routing table of a router\n      'S' followed by the id number to shut down a router\n      'T' followed by the id to start up a router ");
            Scanner scan = new Scanner(System.in);
            String[] readChoice = scan.nextLine().split(" ");
            switch(readChoice[0].toUpperCase()){
                case "C":
                    for(Router temp : routerList){
                        //System.out.print(temp.ID + "  ");
                        temp.originatePacket();
                    }
                    break;
                case "Q":
                    System.exit(0);
                case "P":
                    int printInput = scan.nextInt();
                    //routerListMapActualID.get(printInput).connectionList.size();
                    routerListMapActualID.get(printInput).printRoutingTable();
                    break;
                case "S":
                    int startInput = scan.nextInt();
                    routerListMapActualID.get(startInput).startRouter();
                    break;
                case "T":
                    int stopInput = scan.nextInt();
                    routerListMapActualID.get(stopInput).startRouter();
                    break;
                default:
                    System.out.println("invalid input");
                    break;
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
            //System.out.println(read[0]);
            if(!read[0].equals(" ")){
                //System.out.println(read.length);
                String tmp = "";
                int j=0;
                for(int i=0;i<read.length;i++){
                    //System.out.println();

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
                //System.out.println(count);

                //routerList.put(currentMainRouter, new Router(currentMainRouter, temp));
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
                //System.out.println(read.length);
                String tmp = "";
                int j=0;
                for(int i=0;i<read.length;i++){
                    //System.out.println();

                    if(read[i].equals(" ")){
                        j = i;
                        for(int k=0;k<i;k++){
                            tmp = tmp + read[k];
                        }

                        currentMainRouter = Integer.parseInt(tmp);
                        tmp = "";
                    }
                    
                    // else if(i>j && currentMainRouter!=0){
                    //     if(read[i]!=" "){
                    //         temp = temp + read[i];
                    //     }
                    // }
                }
                //System.out.println(currentMainRouter);

            }


            if(read[0].equals(" ")){
                int destRouter = 0;
            //System.out.println(read[1]);
                if(read.length>2 && read.length>3){
                    //connectionList.put(currentMainRouter, Integer.parseInt(read[4]));
                    
                    // for(Router routs : routerList){
                    //         //System.out.println(temp.ID + temp.networkName);
                    //         if(routs.actualID == Integer.parseInt(read[1])){
                    //             destRouter = routs.ID;
                    //         }

                    //     }

                    //System.out.println("test");
                    Iterator it = routerListMapActualID.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry)it.next();
                        //System.out.print(pair.getKey());
                        if((Integer)pair.getKey() == Integer.parseInt(read[1])){
                            destRouter = (Integer) pair.getKey();
                            //System.out.println(destRouter);
                        }
                        //it.remove(); // avoids a ConcurrentModificationException
                    }
                    //System.out.println(destRouter);
                    //routerList.get(count).connections(routerList.get(destRouter), Integer.parseInt(read[3]));
                    routerListMapActualID.get(currentMainRouter).connections(routerListMapActualID.get(destRouter), Integer.parseInt(read[3]));
                    //routerList.get(count).connections(destRouter, Integer.parseInt(read[3]));
                    //System.out.println("test");
                }
                else{
                    //connectionList.put(currentMainRouter, 1);
                    //routerList.get(count).connections(routerList.get(destRouter), 1);
                    //System.out.println(routerListMapActualID.get(currentMainRouter).actualID);
                    Iterator it = routerListMapActualID.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry)it.next();
                        //System.out.print(pair.getKey());
                        if((Integer)pair.getKey() == Integer.parseInt(read[1])){
                            destRouter = (Integer) pair.getKey();
                            //System.out.println(destRouter);
                        }
                        //it.remove(); // avoids a ConcurrentModificationException
                    }
                    //System.out.println(destRouter);
                    routerListMapActualID.get(currentMainRouter).connections(routerListMapActualID.get(destRouter), 1);
                    //System.out.println(routerListMapActualID.size());
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
    //ArrayList<Triplet> routingTable;
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
        //this.createGraph();

    }

    public void connections(Router router, int cost){
        //this.router = router;
        //System.out.println(router.actualID);
        this.connectionList.put(router.ID, cost);
        //System.out.println(this.connectionList);
        //System.out.println(this.ID);
        Iterator it = this.connectionList.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                //System.out.print(pair.getKey());
                this.tickCounter.put((Integer)pair.getKey(), 0);
                this.tickCheck.put((Integer)pair.getKey(), false);
                //it.remove(); // avoids a ConcurrentModificationException
            }
        

        //this.routingTableList.add(new routingTable(router,router,cost,0,0));
        //this.routingTableMap.put(router.ID, new routingTable(router,router,cost,0,0));
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

        //System.out.println( count);

        graph.djiktra(this.ID);

        // for(Router rs : ls.routerList){
        //     //count++;
        //     //System.out.println("in graph loop");
        //     Iterator it = rs.connectionList.entrySet().iterator();
        //     while (it.hasNext()) {
        //         Map.Entry pair = (Map.Entry)it.next();
        //         //System.out.print(pair.getKey());
                
        //         //graph.addEdge(rs.ID,(Integer) pair.getKey(),(Integer) pair.getValue());
        //         //System.out.println(rs.ID +" "+ (Integer) pair.getKey()+ " "+(Integer) pair.getValue());
        //         //System.out.println(rs.ID);
        //         graph.djiktra(rs.ID);

                
        //         //it.remove(); // avoids a ConcurrentModificationException
        //     }  
        // }


        //System.out.println(); 


    }


    public void updateGraph(){

    }


    public void originatePacket(){
        

        if(this.status){
            //System.out.println(this.connectionList);
            //System.out.println("in Originate");
            //Router originrouter = this.router;
            //System.out.println(originrouter.ID);
            Iterator it = this.connectionList.entrySet().iterator();
            //System.out.println(connectionList.size());
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                //System.out.print(pair.getKey());
                // routingTable router = routingTableMap.get(pair.getKey());
                // routingTable outGoingLink = routingTableMap.get(pair.getKey());
                // routingTable cost = routingTableMap.get(pair.getKey());
                // routingTable TTL = routingTableMap.get(pair.getKey());
                int tick = this.tickCounter.get((Integer) pair.getKey());

                if(!tickCheck.get((Integer) pair.getKey())){
                    tick = tick +1;
                    this.tickCounter.replace((Integer) pair.getKey(), tick);
                }

                if(this.tickCounter.get(pair.getKey()) >1){
                    this.connectionList.replace((Integer) pair.getKey(), 588);
                    //System.out.println("test");
                }
                else{

                    LinkStateRouting l = new LinkStateRouting();
                    Router connectedRouter = l.routerList.get((Integer)pair.getKey());
                    Router originRouter  = l.routerList.get(this.ID);
                    //System.out.print(connectedRouter.ID);
                    //System.out.println(originRouter.ID);
                    //this.createGraph();

                    connectedRouter.receivePacket(new packet(originRouter, this.SN,(Integer) pair.getValue()), this.ID, this.ID);
                    //System.out.println("test");
                    connectedRouter.tickCheck.replace(this.ID, true);
                    connectedRouter.tickCounter.replace(this.ID, 0);
                    this.SN++;

                }

                //router.router.receivePacket(new packet(originrouter, 1, cost, --TTL), originrouter.ID);

                //it.remove(); // avoids a ConcurrentModificationException
            }
            //this.createGraph();
        }

    }


   

    public void receivePacket(packet packet, int originRouterID, int receiverRouterID) {
        
        if(this.status = true){

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
                    //System.out.print(pair.getKey());
                    // routingTable router = routingTableMap.get(pair.getKey());
                    // routingTable outGoingLink = routingTableMap.get(pair.getKey());
                    // routingTable cost = routingTableMap.get(pair.getKey());
                    // routingTable TTL = routingTableMap.get(pair.getKey());
                

                    if((Integer) pair.getKey() != receiverRouterID){

                        Router forwardRouter = lst.routerList.get((Integer) pair.getKey());

                        forwardRouter.receivePacket(packet, originRouterID, this.ID);
                    }
                    
                    //it.remove(); // avoids a ConcurrentModificationException
                }
                //this.createGraph();

            }
            
        }


    }


    public void updateRoutingTable(){

    }

    public void startRouter(){
        this.status = true;
    }

    public void stopRouter(){
        this.status = false;
    
    }

    public void printRoutingTable(){
        this.createGraph();
        //System.out.println(this.status);
        System.out.println("Network \t Outgoing Link \t \t \tcost");                           ////
        System.out.println();
        //System.out.println(routingTableMap.size());
        System.out.println(this.actualID + "\t \t \t"+ this.actualID + "\t \t \t" + "0");
        for(routingTable rs : routingTableList){
            System.out.println(rs.router.actualID + "\t \t \t" + rs.outGoingLink.actualID + "\t \t \t" + rs.cost);
        }


        // Iterator it = this.routingTableMap.entrySet().iterator();
        //     while (it.hasNext()) {
        //         Map.Entry pair = (Map.Entry)it.next();
        //         //System.out.print(pair.getKey());

        //         routingTable actualID = (routingTable)pair.getKey(); 
        //         //routingTableMap.get(pair.getKey());
        //         routingTable cost = (routingTable)pair.getKey();
        //         //routingTableMap.get(pair.getKey());

                
        //         System.out.print(pair.getValue() + "\t" + actualID.router.actualID + "\t" + cost.cost); ////////////
        //         //System.out.print();
        //         System.out.println(); ///////////////////////////////////
                
        //         //it.remove(); // avoids a ConcurrentModificationException
        //     }  
        //System.out.println(this.connectionList);
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
        //LinkedList<Integer> adjlistArr[];
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
                    //System.out.println("testVe");
                }
            }
            //System.out.println(vertex);
            return vertex;
        }

        public void djiktra(int source){
            boolean[] spt = new boolean[vertices];
            int [] dist = new int[vertices];
            int INFINITY = Integer.MAX_VALUE;
        
            
            //System.out.println("dijiktra called");
            for(int i =0;i<vertices;i++){
                dist[i] = INFINITY;
            }

            dist[source] = 0;
            int outgoingVertex = source;

            for(int i=0; i<vertices;i++){

                int vertex_u = getMinimum(spt, dist);
                //int vertex_U = getMinimumVertex(spt, distance);
                //System.out.print(vertex_U + ". ");
                if(vertex_u != source && this.ls.routerList.get(source).connectionList.containsKey(vertex_u)){
                    outgoingVertex = vertex_u;
                    //System.out.print(outgoingVertex + ". ");   
                }
                //System.out.println(vertex_u);

                spt[vertex_u] = true;
                
                for(int vertex_v = 0; vertex_v<vertices;vertex_v++){
                    if(graphMatrix[vertex_u][vertex_v]>0){


                        if(spt[vertex_v]==false && graphMatrix[vertex_u][vertex_v]!=INFINITY){

                            int newKey = graphMatrix[vertex_u][vertex_v] + dist[vertex_u];
                            if(newKey<dist[vertex_v]){
                                dist[vertex_v] = newKey;
                                
                                //System.out.print(vertex_v + " ");
                                if(outgoingVertex != source){
                                    //System.out.print(outgoingVertex + ". ");
                                    this.ls.routerList.get(source).routingTableList.add(new routingTable(this.ls.routerList.get(vertex_v), this.ls.routerList.get(outgoingVertex), newKey));

                                }
                                else{
                                    //System.out.print(vertex_v + ". ");
                                    //System.out.println("test");
                                    this.ls.routerList.get(source).routingTableList.add(new routingTable(this.ls.routerList.get(vertex_v), this.ls.routerList.get(vertex_v), newKey));

                                }
                                //System.out.print(newKey);
                                //System.out.println();
                                // if(this.ls.routerList.get(source).routingTableMap.containsKey(this.ls.routerList.get(outgoingVertex).actualID)){
                                //     //System.out.println("test1");
                                //     this.ls.routerList.get(source).routingTableMap.replace(this.ls.routerList.get(outgoingVertex).actualID, new routingTable(this.ls.routerList.get(outgoingVertex), this.ls.routerList.get(vertex_v), newKey));
                                // }
                                // else{
                                    //System.out.println("test");
                                    //this.ls.routerList.get(source).routingTableMap.put(new routingTable(this.ls.routerList.get(outgoingVertex), this.ls.routerList.get(vertex_v), newKey), this.ls.routerList.get(outgoingVertex).actualID);
                                //}

                                
                            }
                        }

                    }
                }





            }

            ///
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


