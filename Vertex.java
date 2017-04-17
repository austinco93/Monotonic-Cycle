/* Vertex.java 
 * Author: Austin Corotan
 * Date: May 21, 2017
 * Description: This is a vertex object which is a two dimensional object which contains the location of a point
 * on a grid. Multiple verticies will be traverse via shortest monotone cycle in this context */

public class Vertex implements Comparable<Vertex> {
  private double x;
  private double y;

  public Vertex(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public double getX(){
    return this.x;
  }

  public double getY(){
    return this.y;
  }

  @Override
  public int compareTo(Vertex compareVert) {
    double compareX = ((Vertex) compareVert).getX();
    int retInt = 0;
    if(this.x > compareX){
      retInt = 1;
    } else if (this.x == compareX){
      retInt = 0;
    } else {
      retInt = -1;
    }
    return retInt;
  }

  @Override
  public String toString(){
    return String.format("%.4f, %10.4f",this.x, this.y);
  }
}
