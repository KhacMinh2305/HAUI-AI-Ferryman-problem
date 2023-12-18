import java.util.ArrayList;

public class Queue {
	private ArrayList<StateNode> list;
	private int top;
	
	public int getTop() {
		return top;
	}

	public Queue() {
		init();
	}
	
	private void init() {
		list = new ArrayList<StateNode>();
		top = 0;
	}

	public boolean isEmpty() {
		return list.size() == 0;
	}
	
	public void add(StateNode node) {
		list.add(node);
		top++;
	}
	
	public StateNode remove() {
		if(isEmpty()) {
			return null;
		}
		StateNode node = list.get(0);
		list.remove(0);
		top--;
		return node;
	}
}
