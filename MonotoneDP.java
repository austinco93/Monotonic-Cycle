import java.util.*;
import java.io.*;
/* MonotoneDP.java 
 * Author: Austin Corotan
 * Date: May 21, 2017
 * Description: This program is able to use dynamic program to calculate the optimal montone cycle
 * path for a given set of vertices */

public class MonotoneDP {
    public static double[][] distances;
    public static Vertex[][] optimals;
    public static ArrayList<Vertex> verticies = new ArrayList<Vertex>();
    public static int size;
    public static ArrayList<Vertex> path = new ArrayList<Vertex>();
    public static ArrayList<Integer> pathFull = new ArrayList<Integer>();

    public static void main(String[] args) {
      if(args.length != 1){
        System.err.println("Usage: java MonotoneDP <input.txt>");
        System.exit(1);
      }

      /* store verticies */
      String fileName = args[0];
      loadVerticies(fileName);
      size = verticies.size();
      distances = new double[size][size];
      optimals = new Vertex[size][size];

      /* calculate distances */
      monotoneCycleDP();

      /* determine path */
      findOptimalPath();

      /* print results */
      printResults();
    }

    /* prints results in specified format */
    static void printResults(){
      System.out.format("Optimal DP monotone cycle length: %.4f\n", distances[size-1][size-1]);

      double distance = 0;
      for(int i = 0; i <= pathFull.size(); i++){
        if(i == 0){
          System.out.println(verticies.get(pathFull.get(i)).toString());
        } else if(i == pathFull.size()){
          distance += distance(pathFull.get(0), pathFull.get(i-1));
          System.out.format("%33.4f\n",distance);
        } else {
          distance += distance(pathFull.get(i), pathFull.get(i-1));
          System.out.print(verticies.get(pathFull.get(i)).toString());
          System.out.format("%15.4f\n",distance);
        }
      }
    }

    /* calculates distance between any two set of verticies */
    static double distance(int i, int j){
      return Math.sqrt(Math.pow(verticies.get(i).getX() - verticies.get(j).getX(),2) + Math.pow(verticies.get(i).getY() - verticies.get(j).getY(),2));
    }

    /* determines optimal path from parallel DP structure */
    static void findOptimalPath(){
      Vertex start = new Vertex(size-1, size-1);
      path.add(start);

      Vertex choice = optimals[size-1][size-1];
      path.add(choice);

      for(int i = 0; i < size - 2; i++){
        choice = optimals[(int)choice.getX()][(int)choice.getY()];
        path.add(choice);
      }

      choice = optimals[(int)choice.getX()][(int)choice.getY()];
      path.add(choice);
      
      boolean forward = false;
      pathFull.add(size-1);
      for(int ii = 0; ii < path.size() - 1; ii++){
        Vertex v = path.get(ii);
        Vertex v1 = path.get(ii+1);
        double i = v.getX();
        double j = v.getY();
        double i1 = v1.getX();
        double j1 = v1.getY();

        double vertexInd = (i == i1) ? j1 : i1;
        if(vertexInd != 0){
          if(forward){
            pathFull.add(0,(int)vertexInd);
          } else {
            pathFull.add((int)vertexInd);
          }
        }

        if(i == j-1 || i == j){
          forward = !forward;
        }
      }
      pathFull.add(0, 0);
    }

    /* stores verticies from file into verted arraylist */
    static void loadVerticies(String fileName) {
       Scanner fileScanner = null;
        try{
          File file = new File(fileName);
          fileScanner = new Scanner(file);
        } catch(FileNotFoundException e){
          System.exit(2);
        }
        /* Store verticies */
        while(fileScanner.hasNextLine()){
          String vertexLine = fileScanner.nextLine();
          Scanner vertexInfo = new Scanner(vertexLine);
          double x = vertexInfo.nextDouble();
          double y = vertexInfo.nextDouble();
          Vertex temp = new Vertex(x,y);
          verticies.add(temp);
        }

        /* sort ascending on x */
        Collections.sort(verticies);
    }

    /* Dynamic programming algorithm for calculating distances */
    static void monotoneCycleDP(){
      distances[0][0] = 0;
      optimals[0][0] = new Vertex(0,0);

      for(int i = 0; i < size; i++){
        for(int j = i; j < size; j++){
          if(i < j-1){
            distances[i][j] = distance(j-1,j) + distances[i][j-1];
            Vertex optimal = new Vertex(i,j-1);
            optimals[i][j] = optimal;
          } else if(i == j-1){
            minNot(i,j); 
          } else if (i == j){
            minEq(i,j);
          }
        }
      }
    }

    /* naive implementation (recursive) algorithm) */
    static double monotoneCycle(int i, int j){
        if(i == j){
          if(i == 0 && j == 0){
            distances[i][j] = 0;
          } else {
            double minEq = distance(0, j) + monotoneCycle(0, j);
            for(int ii = 0; ii < i; ii++){
              double checkEq = distance(ii, j) + monotoneCycle(ii, j);
              if(checkEq < minEq){
                minEq = checkEq;
              }
            }
            distances[i][j] = minEq;
          }

        } else if (i < j - 1){
            distances[i][j] = distance(j-1,j) + monotoneCycle(i, j-1);
        } else if (i == j - 1){
            double min = distance(0, j) + monotoneCycle(0, j-1);
            for(int ii = 0; ii < i; ii++){
              double check = distance(ii, j) + monotoneCycle(ii, j-1);
              if(check < min){
                min = check;
              }
            }
            distances[i][j] = min;
        }

        return distances[i][j];
    }

    /* helper function for finding the minimum subproblem for the i = j - 1 case */
    static void minNot(int i, int j){
      int iIndex = 0;
      int jIndex = j;
      double minCycle = distance(iIndex, jIndex) + distances[0][j-1];
      for(int ii = 0; ii < i; ii++){
        if(distance(ii, jIndex)+ distances[ii][j-1] < minCycle){
          minCycle = distance(ii, jIndex) + distances[ii][j-1];
          iIndex = ii;
        }
      }
      distances[i][j] = minCycle;
      optimals[i][j] = new Vertex(iIndex,j-1);
    }

    /* helper function for finding the minimum subproblem for the i = j case */
    static void minEq(int i, int j){
      int iIndex = 0;
      int jIndex = j;
      double minCycle = distance(iIndex, jIndex) + distances[0][j];
      for(int ii = 0; ii < i; ii++){
        if(distance(ii, jIndex) + distances[ii][j] < minCycle){
          minCycle = distance(ii, jIndex) + distances[ii][j];
          iIndex = ii;
        }
      }
      distances[i][j] = minCycle;
      optimals[i][j] = new Vertex(iIndex,j);
    }
}
