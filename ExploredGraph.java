import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Stack;
import java.util.function.Function;
import java.util.Map;
import java.util.HashMap;
import java.util.Queue;
import java.util.PriorityQueue;

/**
 * 
 */

/**
 * @author Jessie Stieger
 * Extra Credit Options Implemented, if any:  (mention them here.)
 * 
 * Solution to Assignment 5 in CSE 373, Autumn 2016
 * University of Washington.
 * 
 * (Based on starter code v1.3. By Steve Tanimoto.)
 *
 * Java version 8 or higher is recommended.
 *
 */

// Here is the main application class:
public class ExploredGraph {
	Set<Vertex> Ve; // collection of explored vertices
	Set<Edge> Ee;   // collection of explored edges
	Map<Vertex,Vertex> pred;
	Map<Vertex,Integer> label;
	
	public ExploredGraph() {
		Ve = new LinkedHashSet<Vertex>();
		Ee = new LinkedHashSet<Edge>();
		pred = new HashMap<Vertex,Vertex>();
		label = new HashMap<Vertex,Integer>();
	}

	public void initialize() {
		Ve = new LinkedHashSet<Vertex>();
		Ee = new LinkedHashSet<Edge>();
		pred = new HashMap<Vertex,Vertex>();
		label = new HashMap<Vertex,Integer>();
	}
	
	public Set<Vertex> successors(Vertex v){
		Set<Vertex> s = new LinkedHashSet<Vertex>();
		for(int i = 0; i < 3; i++){
			for(int j = i+1; j < 3; j++){
				Operator op = new Operator(i,j);
				if(op.precondition(v)){
					s.add(op.transition(v));
				}
			}
		}
		return s;
	}
	public int nvertices() {return Ve.size();} 
	public int nedges() {return Ee.size();}    
	public void idfs(Vertex vi, Vertex vj) { // (Iterative Depth-First Search)
		int count = 0;
		Stack<Vertex> open = new Stack<Vertex>();
		Set<Vertex> closed = new LinkedHashSet<Vertex>();
		pred.put(vi, null);
		open.push(vi);
		while(!open.isEmpty()){
			Vertex v = open.pop();
			label.put(v, count);
			count++;
			Set<Vertex> s = successors(v);
			for(Vertex e : s){
				if(! open.contains(e) || ! closed.contains(e)){
					open.push(e);
					pred.put(e, v);
				}	
			}
			closed.add(v);
		}
	} 
	
	public void bfs(Vertex vi, Vertex vj) { //(Breadth-First Search)
		int count = 0;
		Queue<Vertex> open = new PriorityQueue<Vertex>();
		Set<Vertex> closed = new LinkedHashSet<Vertex>();
		pred.put(vi, null);
		open.add(vi);
		while(!open.isEmpty()){
			Vertex v = open.remove();
			label.put(v, count);
			count++;
			Set<Vertex> s = successors(v);
			for(Vertex e : s){
				if(! open.contains(e) || ! closed.contains(e)){
					open.add(e);
					pred.put(e, v);
				}	
			}
			closed.add(v);
		}
	} 
		
	public ArrayList<Vertex> retrievePath(Vertex vi) {
		ArrayList<Vertex> path = new ArrayList<Vertex>();
		Stack<Vertex> pathstack = new Stack<Vertex>();
		Vertex v = pred.get(vi);
		while(v != null){
			pathstack.push(v);
			v = pred.get(v);
		}
		Vertex vs = pathstack.pop();
		while(vs != null){
			path.add(vs);
			vs = pathstack.pop();
		}
	
		return path;
	} 
	public ArrayList<Vertex> shortestPath(Vertex vi, Vertex vj) {return null;} // Implement this.
	public Set<Vertex> getVertices() {return Ve;} 
	public Set<Edge> getEdges() {return Ee;} 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExploredGraph eg = new ExploredGraph();
		// Test the vertex constructor: 
		Vertex v0 = eg.new Vertex("[[4,3,2,1],[],[]]");
		System.out.println(v0);
		// Add your own tests here.
		// The autograder code will be used to test your basic functionality later.

	}
	
	class Vertex {
		ArrayList<Stack<Integer>> pegs; // Each vertex will hold a Towers-of-Hanoi state.
		// There will be 3 pegs in the standard version, but more if you do extra credit option A5E1.
		
		// Constructor that takes a string such as "[[4,3,2,1],[],[]]":
		public Vertex(String vString) {
			String[] parts = vString.split("\\],\\[");
			pegs = new ArrayList<Stack<Integer>>(3);
			for (int i=0; i<3;i++) {
				pegs.add(new Stack<Integer>());
				try {
					parts[i]=parts[i].replaceAll("\\[","");
					parts[i]=parts[i].replaceAll("\\]","");
					List<String> al = new ArrayList<String>(Arrays.asList(parts[i].split(",")));
					System.out.println("ArrayList al is: "+al);
					Iterator<String> it = al.iterator();
					while (it.hasNext()) {
						String item = it.next();
                        if (!item.equals("")) {
                                System.out.println("item is: "+item);
                                pegs.get(i).push(Integer.parseInt(item));
                        }
					}
				}
				catch(NumberFormatException nfe) { nfe.printStackTrace(); }
			}		
		}
		public String toString() {
			String ans = "[";
			for (int i=0; i<3; i++) {
			    ans += pegs.get(i).toString().replace(" ", "");
				if (i<2) { ans += ","; }
			}
			ans += "]";
			return ans;
		}
	}
	
	class Edge {
		private Vertex endpoint1; 
		private Vertex endpoint2;
		
		public Edge(Vertex vi, Vertex vj) {
			endpoint1 = vi;
			endpoint2 = vj;
		}
		public Vertex getEndpoint1(){
			return endpoint1;
		}
		
		public Vertex getEndpoint2(){
			return endpoint2;
		}
		
		public String toString(){
			String s = "";
			s += "Edge from ";
			s += endpoint1.toString();
			s += " to ";
			s += endpoint2.toString();
			return s;
		}
	}
	
	class Operator {
		private int i, j;

		public Operator(int i, int j) { // Constructor for operators.
			this.i = i;
			this.j = j;
		}

		public boolean precondition(Vertex v) {
			Stack<Integer> iStack = v.pegs.get(i);
			Stack<Integer> jStack = v.pegs.get(j);
			if(iStack.empty()) return false;
			if(jStack.empty()) return true;
			return jStack.peek().compareTo(iStack.peek()) > 0;
		}

		public Vertex transition(Vertex v) {
			if(! precondition(v))
				throw new IllegalArgumentException();
			String answer = "[";
			Integer from = v.pegs.get(i).pop();		
			for(int x = 0; x <3; x++){
				Stack<Integer> s = v.pegs.get(x);
				if(x == j)
					s.push(from);
				answer += s.toString().replace(" ", "");
				if (x<2) 
					answer += ","; 
			}
			answer += "]";
			
			Vertex temp = new Vertex(answer);
			return temp;
		}

		public String toString() {
			return "Operator that tries to move a peg from peg " + i + " to peg " + j;
		}
	}

}