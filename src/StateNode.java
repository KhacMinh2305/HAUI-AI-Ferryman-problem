import java.util.ArrayList;

public class StateNode {
	
	private State state;
	private StateNode parent;
	private ArrayList<StateNode> childrenList;
	
	protected State getState() {
		return state;
	}
	
	protected void setState(State state) {
		this.state = state;
	}
	
	protected StateNode getParent() {
		return parent;
	}
	
	protected void setParent(StateNode parent) {
		this.parent = parent;
	}
	
	protected ArrayList<StateNode> getChildrenList() {
		return childrenList;
	}

	protected void setChildrenList(ArrayList<StateNode> childrenList) {
		this.childrenList = childrenList;
	}
	

	public StateNode(State state, StateNode parent, ArrayList<StateNode> childrenList) {
		super();
		this.state = state;
		this.parent = parent;
		this.childrenList = childrenList;
	}

	@Override
	public String toString() {
		return state.toString();
	}
	
}
