import java.io.*;
import java.util.*;
import java.lang.*;


class LinkStateRouting{

    LinkedList<Object> routerList;
    public static void main(String[] args) {


        
    }

    public void readFile(){
        File f = new File("infile.dat");
        Scanner sc = new Scanner(f);
        int currentMainRouter;
        String temp ="";

        while(sc.hasNextLine()){

            String[] read = sc.nextLine().split("");
            
            if(read[0]==" "){

            }
            else{
                for(int i=0;i<read.length;i++){
                    if(i==0){
                        currentMainRouter = read[i];
                    }
                    else{
                        if(read[i]!=" "){
                            temp = temp + read[i];
                        }
                    }
                }
                
                routerList = new LinkedList<Object>(); // work on this

            }
            
            


        }


    }

}


class Router{
    
    int ID;
    String networkName;
    Boolean status;
    ArrayList<String> routingTable;

    Router(int ID, String networkName){

        this.ID = ID;
        this.networkName = networkName;
        this.routingTable = new ArrayList<String>();
        this.status = true;

    }



}