package net.sf.tweety.arg.dung;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.Attack;
import net.sf.tweety.arg.dung.syntax.DungEntity;
import net.sf.tweety.commons.BeliefBase;
import net.sf.tweety.graphs.DefaultGraph;
import net.sf.tweety.graphs.Edge;
import net.sf.tweety.graphs.Graph;
import net.sf.tweety.graphs.Node;
import net.sf.tweety.math.matrix.Matrix;
import net.sf.tweety.math.term.IntegerConstant;

public class DungTheoryGraph implements Graph<Argument> {

	/**
	 * For archiving sub graphs 
	 */
	private static Map<DungTheoryGraph, Collection<Graph<Argument>>> archivedSubgraphs = new HashMap<>();

	/**
	 * The set of arguments
	 */
	private final Set<Argument> arguments = new HashSet<>();
	
	/**
	 * The set of attacks
	 */
	private final Set<Attack> attacks = new HashSet<>();
	
	public DungTheoryGraph() {
	}

	public DungTheoryGraph(BeliefBase<DungEntity> beliefBase) {
		arguments.addAll(beliefBase.getIndex(Argument.class));
		attacks.addAll(beliefBase.getIndex(Attack.class));
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.graphs.Graph#getRestriction(java.util.Collection)
	 */
	@Override
	public Graph<Argument> getRestriction(Collection<Argument> arguments) {
		DungTheoryGraph theory = new DungTheoryGraph();
		arguments.forEach(theory::add);
		for (Attack attack: this.attacks)
			if(arguments.contains(attack.getAttacked()) && arguments.contains(attack.getAttacker()))
				theory.add(attack);
		return theory;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.graphs.Graph#add(net.sf.tweety.graphs.Edge)
	 */
	@Override
	public boolean add(Edge<Argument> edge) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.graphs.Graph#getNodes()
	 */
	@Override
	public Collection<Argument> getNodes() {		
		return arguments;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.graphs.Graph#getNumberOfNodes()
	 */
	@Override
	public int getNumberOfNodes() {
		return arguments.size();
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.graphs.Graph#areAdjacent(net.sf.tweety.graphs.Node, net.sf.tweety.graphs.Node)
	 */
	@Override
	public boolean areAdjacent(Argument a, Argument b) {
		return this.isAttackedBy(b, a);
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.graphs.Graph#getEdges()
	 */
	@Override
	public Collection<? extends Edge<? extends Argument>> getEdges() {
		return this.attacks;		
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.graphs.Graph#getChildren(net.sf.tweety.graphs.Node)
	 */
	@Override
	public Collection<Argument> getChildren(Node node) {
		if(!(node instanceof Argument))
			throw new IllegalArgumentException("Node of type argument expected");
		return this.getAttacked((Argument)node);
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.graphs.Graph#getParents(net.sf.tweety.graphs.Node)
	 */
	@Override
	public Collection<Argument> getParents(Node node) {
		if(!(node instanceof Argument))
			throw new IllegalArgumentException("Node of type argument expected");
		return this.getAttackers((Argument)node);
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.graphs.Graph#existsDirectedPath(net.sf.tweety.graphs.Node, net.sf.tweety.graphs.Node)
	 */
	@Override
	public boolean existsDirectedPath(Argument node1, Argument node2) {
		return DefaultGraph.existsDirectedPath(this, node1, node2);
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.graphs.Graph#getNeighbors(net.sf.tweety.graphs.Node)
	 */
	@Override
	public Collection<Argument> getNeighbors(Argument node) {
		Set<Argument> neighbours = new HashSet<Argument>();
		neighbours.addAll(this.getAttacked(node));
		neighbours.addAll(this.getAttackers(node));
		return neighbours;
	}

	/**
	 * Computes the set {A | (A,argument) in attacks}.
	 * @param argument an argument
	 * @return the set of all arguments that attack <source>argument</source>.
	 */
	public Set<Argument> getAttackers(Argument argument){
		Set<Argument> attackers = new HashSet<Argument>();
		Iterator<Attack> it = attacks.iterator();
		while(it.hasNext()){
			Attack attack = it.next();
			if(attack.getAttacked().equals(argument))
				attackers.add((Argument)attack.getAttacker());
		}
		return attackers;
	}
	
	/**
	 * Computes the set {A | (argument,A) in attacks}.
	 * @param argument an argument
	 * @return the set of all arguments that are attacked by <source>argument</source>.
	 */
	public Set<Argument> getAttacked(Argument argument){
		Set<Argument> attacked = new HashSet<Argument>();
		Iterator<Attack> it = attacks.iterator();
		while(it.hasNext()){
			Attack attack = it.next();
			if(attack.getAttacker().equals(argument))
				attacked.add((Argument)attack.getAttacked());
		}
		return attacked;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.graphs.Graph#getAdjancyMatrix()
	 */
	@Override
	public Matrix getAdjancyMatrix() {
		Matrix m = new Matrix(this.getNumberOfNodes(), this.getNumberOfNodes());
		int i = 0, j;
		for(Argument a: this){
			j = 0;
			for(Argument b : this){
				m.setEntry(i, j, new IntegerConstant(this.areAdjacent(a, b) ? 1 : 0));				
				j++;
			}
			i++;
		}
		return m;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.graphs.Graph#getComplementGraph(int)
	 */
	@Override
	public DungTheoryGraph getComplementGraph(int selfloops) {
		DungTheoryGraph comp = new DungTheoryGraph();
		for(Argument node: this)
			comp.add(node);
		for(Argument node1: this)
			for(Argument node2: this)
				if(node1 == node2){
					if(selfloops == Graph.INVERT_SELFLOOPS){
						if(!this.isAttackedBy(node2, node1))
							comp.add(new Attack(node1, node2));
					}else if(selfloops == Graph.IGNORE_SELFLOOPS){
						if(this.isAttackedBy(node2, node1))
							comp.add(new Attack(node1, node2));						
					}
				}else if(!this.isAttackedBy(node2, node1))
					comp.add(new Attack(node1, node2));
		return comp;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.graphs.Graph#hasSelfLoops()
	 */
	@Override
	public boolean hasSelfLoops() {
		for(Argument a: this)
			if(this.isAttackedBy(a, a))
				return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.graphs.Graph#getEdge(net.sf.tweety.graphs.Node, net.sf.tweety.graphs.Node)
	 */
	@Override
	public Edge<Argument> getEdge(Argument a, Argument b) {
		if(this.isAttackedBy(b, a))
			return new Attack(a, b);
		return null;
	}
	
	/**
	 * Checks whether arg1 is attacked by arg2.
	 * @param arg1 an argument.
	 * @param arg2 an argument.
	 * @return "true" if arg1 is attacked by arg2
	 */
	public boolean isAttackedBy(Argument arg1, Argument arg2){
		return this.attacks.contains(new Attack(arg2, arg1));
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.graphs.Graph#isWeightedGraph()
	 */
	@Override
	public boolean isWeightedGraph() {
		return false;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.graphs.Graph#getStronglyConnectedComponents()
	 */
	@Override
	public Collection<Collection<Argument>> getStronglyConnectedComponents() {
		return DefaultGraph.getStronglyConnectedComponents(this);
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.graphs.Graph#getSubgraphs()
	 */
	@Override
	public Collection<Graph<Argument>> getSubgraphs() {	
		if(!archivedSubgraphs.containsKey(this))			
			archivedSubgraphs.put(this, DefaultGraph.<Argument>getSubgraphs(this));		
		return archivedSubgraphs.get(this);
	}

	@Override
	public boolean add(Argument node) {
		return arguments.add(node);
	}

	@Override
	public Iterator<Argument> iterator() {
		return arguments.iterator();
	}

	@Override
	public boolean contains(Object obj) {
		return arguments.contains(obj) || attacks.contains(obj);
	}
}
