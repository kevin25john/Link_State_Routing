import java.io.*;
import java.util.*;

//import com.sun.tools.jdi.Packet;

import java.lang.*;
//import java.util.HashMap;

class LinkStateRouting{

    public static LinkedList<Router> routerList = new LinkedList<Router>();
    //public static HashMap<Integer,Object> routerList = new HashMap<Integer,Object>();
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
                        System.out.print(temp.ID + "  ");
                        temp.originatePacket();
                    }
                    break;
                case "Q":
                    System.exit(0);
                case "P":
                    break;
                case "S":
                    break;
                case "T":
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
                temp = "";

            }
            count++;

            
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
                    
                    for(Router routs : routerList){
                            //System.out.println(temp.ID + temp.networkName);
                            if(routs.actualID == Integer.parseInt(read[1])){
                                destRouter = routs.ID;
                            }

                        }
                    routerList.get(count).connections(routerList.get(destRouter), Integer.parseInt(read[3]));
                    //routerList.get(count).connections(destRouter, Integer.parseInt(read[3]));
                }
                else{
                    //connectionList.put(currentMainRouter, 1);
                    routerList.get(count).connections(routerList.get(destRouter), 1);
                }


            }
            count ++;
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
    HashMap<Integer, routingTable> routingTableMap;
    HashMap<Integer, Integer> highestSeqNumber;
    //LinkedList<routingTable> routingTableList;
    int SN;
    int tick;
    Boolean receivedLSP;
    HashMap <Integer, Integer> lspReceived;


    Router(int ID, String networkName){

        this.ID = ID;
        this.actualID = actualID;
        this.networkName = networkName;
        this.routingTableMap = new HashMap<Integer, routingTable>();
        //this.routingTableList = new LinkedList<routingTable>();
        this.status = true;
        this.connectionList = new HashMap<Integer, Integer>();
        this.highestSeqNumber = new HashMap<Integer, Integer>();
        this.receivedLSP = new HashMap<Integer, Integer>();     //1 for true, 0 for false
        this.SN = 1;
        this.receivedLSP = false;
        this.tick = 0;
        this.createGraph();

    }

    public void connections(Router router, int cost){
        //this.router = router;
        this.connectionList.put(router.ID, cost);
        //this.routingTableList.add(new routingTable(router,router,cost,0,0));
        this.routingTableMap.put(router.ID, new routingTable(router,router,cost,0,0));
    }

    public void printTest(){
        System.out.println(this.connectionList);
    }


    public void createGraph(){
        LinkStateRouting ls = new LinkStateRouting();
        int vertices = ls.routerList.size();
        
        Graph graph = new Graph(vertices);

        for(Router rs : ls.routerList){

            Iterator it = rs.connectionList.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                //System.out.print(pair.getKey());

                graph.addEdge(rs.ID,(Integer) pair.getKey(),(Integer) pair.getValue());

                
                it.remove(); // avoids a ConcurrentModificationException
            }  
        } 


    }


    public void updateGraph(){
        
    }


    public void originatePacket(){
        // Iterator it = routingTableMap.entrySet().iterator();
        // while (it.hasNext()) {
        //     Map.Entry pair = (Map.Entry)it.next();
        //     System.out.print(pair.getKey());
        //     routingTable router = routingTableMap.get(pair.getKey());
        //     routingTable outGoingLink = routingTableMap.get(pair.getKey());
        //     routingTable cost = routingTableMap.get(pair.getKey());
        //     System.out.println(" = " + router.router.ID + ", " + outGoingLink.router.ID + ", " + cost.cost);
        //     System.out.println();
        //     it.remove(); // avoids a ConcurrentModificationException
        // }

        if(this.status){

            Router originrouter = this.router;
            System.out.println(originrouter.ID);
            Iterator it = routingTableMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                //System.out.print(pair.getKey());
                routingTable router = routingTableMap.get(pair.getKey());
                routingTable outGoingLink = routingTableMap.get(pair.getKey());
                routingTable cost = routingTableMap.get(pair.getKey());
                routingTable TTL = routingTableMap.get(pair.getKey());
                
                router.router.receivePacket(new packet(originrouter, 1, cost, --TTL), originrouter.ID);

                // System.out.println(" = " + router.router.ID + ", " + outGoingLink.router.ID + ", " + cost.cost);
                // System.out.println();
                it.remove(); // avoids a ConcurrentModificationException
            }
        }



        //LinkStateRouting lst = new LinkStateRouting();
        
        //System.out.println(lst.totalVertex);
        


        // for(routingTable temp : routingTableList){
        //         System.out.println(temp.router.ID+ ", " + temp.outGoingLink.ID + ", " + temp.cost);
        //     }



        //routingTableMap.get(key);

    }


    
    public boolean tickCheck(){

        if(this.lspReceived.get(key)){

        }


        if(tick>2){

            return false;
        }
        else{
            return true;
        }

    }

    // public int getReceiveID(int receicerRouterID){
    //     return receicerRouterID;
    // }



    public void receivePacket(packet packet, int originRouterID, int receiverRouterID) {
        
        if(this.status = true){

            LinkStateRouting lst = new LinkStateRouting();
            Router originRouter = lst.routerList.get(originRouterID);
            int currentRouterrID = this.ID;

            packet.TTL = packet.TTL - 1;
            if(!originRouter.highestSeqNumber.containsKey(originRouter.ID)){
                originRouter.highestSeqNumber.put(originRouter.ID, packet.SN);
            }

            if(packet.TTL >0 || originRouter.highestSeqNumber.get(originRouter.ID) < packet.SN){
            
                originRouter.highestSeqNumber.replace(originRouter.ID, packet.SN);

                Iterator it = routingTableMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    //System.out.print(pair.getKey());
                    routingTable router = routingTableMap.get(pair.getKey());
                    routingTable outGoingLink = routingTableMap.get(pair.getKey());
                    routingTable cost = routingTableMap.get(pair.getKey());
                    routingTable TTL = routingTableMap.get(pair.getKey());
                

                    if(router.router.ID != receiverRouterID){
                        router.router.receivePacket(packet, originRouterID, currentRouterrID);
                    }
                    // System.out.println(" = " + router.router.ID + ", " + outGoingLink.router.ID + ", " + cost.cost);
                    // System.out.println();
                    it.remove(); // avoids a ConcurrentModificationException
                }

            }
            
        }


    }


    static class routingTable{
        Router router;
        Router outGoingLink;
        int cost;
        int SN = 0;
        int Tick = 0;
        routingTable(Router router, Router outGoingLink, int cost, int SN, int Tick){
            this.router = router;
            this.outGoingLink = outGoingLink;
            this.cost = cost;
            this.SN = SN;
            this.Tick = Tick;
        }
        
    }

    static class Graph{ //https://algorithms.tutorialhorizon.com/djkstras-shortest-path-algorithm-adjacency-matrix-java-code/

        int vertices;
        int graphMatrix[][];
        //LinkedList<Integer> adjlistArr[];
        public Graph(int vertices){
            this.vertices = vertices;
            //adjlistArr = new LinkedList[vertices];
            graphMatrix = new int[vertices][vertices];

            // for(int i=0;i<vertices;i++){
            //     adjlistArr[i] = new LinkedList<>();
            // }
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

            for(int i=0; i<vertices;i++){
                int vertex_u = getMinimum(spt, dist);

                spt[vertex_u] = true;
                
                for(int vertex_v = 0; vertex_v<vertices;vertex_v++){
                    if(graphMatrix[vertex_u][vertex_v]>0){


                        if(spt[vertex_v]==false && graphMatrix[vertex_u][vertex_v]!=INFINITY){

                            int newKey = graphMatrix[vertex_u][vertex_v] + dist[vertex_u];
                            if(newKey<dist[vertex_v]){
                                dist[vertex_v] = newKey;
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

    packet(Router originRouter, int SN,int cost, int TTL){
        this.originRouter = originRouter;
        this.originID = originRouter.ID;
        this.SN = SN;
        this.TTL = TTL;
        this.totalCost = cost;
        this.reachableRouters = new HashMap<>();


    }
}


