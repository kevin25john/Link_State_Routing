import java.io.*;
import java.util.*;
import java.lang.*;


class LinkStateRouting{

    public static LinkedList<Router> routerList = new LinkedList<Router>();
    //public static HashMap<Integer,Object> routerList = new HashMap<Integer,Object>();
    //public static HashMap<Integer,Object> connectionList = new HashMap<>();
    
    public static void main(String[] args) throws Exception{

        LinkStateRouting l = new LinkStateRouting();
        l.readFile();
        for(Router temp : routerList){
            System.out.println(temp.ID + temp.networkName);


        }

        for(Router temp : routerList){
            System.out.print(temp.ID + "  ");
            System.out.println(temp.connectionList);
        }

    }

    public void readFile() throws Exception{
        File f = new File("infile.dat");
        Scanner sc = new Scanner(f);
        Scanner scanConn = new Scanner(f);
        int currentMainRouter =0;
        String temp ="";

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
                System.out.println(currentMainRouter);

                //routerList.put(currentMainRouter, new Router(currentMainRouter, temp));
                routerList.add(new Router(currentMainRouter, temp));
                temp = "";

            }sc.close();

            
        }

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
                System.out.println(currentMainRouter);

            }


            if(read[0].equals(" ")){
            //System.out.println(read[1]);
            if(read.length>2 && read.length>3){
                //connectionList.put(currentMainRouter, Integer.parseInt(read[4]));
                routerList.get(currentMainRouter).connections(routerList.get(Integer.parseInt(read[1])), Integer.parseInt(read[3]));

            }
            else{
                //connectionList.put(currentMainRouter, 1);
                routerList.get(currentMainRouter).connections(routerList.get(Integer.parseInt(read[1])), 1);
            }


        }
        }scanConn.close();


    }

}


class Router{
    
    int ID;
    String networkName;
    Boolean status;
    ArrayList<String> routingTable;
    HashMap<Integer,Integer> connectionList;

    Router(int ID, String networkName){

        this.ID = ID;
        this.networkName = networkName;
        this.routingTable = new ArrayList<String>();
        this.status = true;
        this.connectionList = new HashMap<Integer, Integer>();

    }

    public void connections(Router router, int cost){
        //this.router = router;
        this.connectionList.put(router.ID, cost);
    }

    public void printTest(){
        System.out.println(this.connectionList);
    }






    static class Graph{

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
    int cost =0;
    int TTL=10;
    int originID;
    HashMap<Integer, Router> reachablRouters;

    packet(Router originRouter, int SN,int cost, int TTL){
        this.originRouter = originRouter;
        this.originID = originRouter.ID;
        this.SN = SN;
        this.TTL = TTL;
        this.totalCost = cost;
        this.reachableRouters = new HashMap<>();


    }
}


