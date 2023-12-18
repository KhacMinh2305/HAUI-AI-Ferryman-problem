import java.util.ArrayList;
import java.util.Stack;

public class Program {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		doLogics();
	}
	
	public static void doLogics() {
		// notice
		System.out.println("NOTICE : ");
		System.out.println("1. The first array in each state illustrates the begin side, and the second one is the end side");
		System.out.println("2. 0 : Empty \t 1 : Human \t 2 : Wolf \t 3 : Goat \t 4 : Cabbage\n\n");
		// generate tree
		StateTree tree = new StateTree();
		System.out.println("\nState Space is presented in tree format :");
		tree.travelTree(tree.getRoot(), 0, 0);
		System.out.println("\nSolution : ");
		findPath(depthFirstSearch(tree.getRoot()));
	}
	
	// travel tree in DFS approach 
	public static ArrayList<StateNode> depthFirstSearch(StateNode root) {
		Stack<StateNode> checkingNodeStack = new Stack<StateNode>();
		Queue checkedNodeQueue = new Queue();
		ArrayList<StateNode> relationsList = new ArrayList<StateNode>();
		StateNode currentNode;
		checkingNodeStack.push(root);
		while(!checkingNodeStack.empty()) {
			// get the top node to add to checkedQueue
			currentNode = checkingNodeStack.pop();
			checkedNodeQueue.add(currentNode);
			if(currentNode.getState().isTheLastState()) {
				break;
			}
			for(int i = currentNode.getChildrenList().size() - 1; i >= 0; i--) {
				// push all children of this node to stack
				checkingNodeStack.push(currentNode.getChildrenList().get(i));
			}
		}
		// create an ArrayList to present node relations
		while(!checkedNodeQueue.isEmpty()) {
			relationsList.add(checkedNodeQueue.remove());
		}
		return relationsList;
	}
	
	// find solution path 
	public static void findPath(ArrayList<StateNode> relations) {
		Stack<StateNode> solution = new Stack<StateNode>();
		StateNode endState = relations.get(relations.size() - 1);
		int step = 0;
		while(endState != null) {
			solution.push(endState);
			endState = endState.getParent();
		}
		while(!solution.isEmpty()) {
			System.out.println("Step " + (step++) + " : " + solution.pop().toString());
		}
	}

}
