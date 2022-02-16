public class Pair implements Comparable<Pair> {
    String s;
    Double i;
  
  public Pair(String s, Double i){
      this.s = s;
      this.i = i;
  }

  public String getWord(){
    return s;
  }

  public Double getValue(){
    return i;
  }

  public int compareTo(Pair pair) {
      return (int) Math.round(this.i*1000) - ((int) Math.round(pair.getValue()*1000));
    }
  public String toString(){
    return "(" + s + " " + i + ")";
  }
}