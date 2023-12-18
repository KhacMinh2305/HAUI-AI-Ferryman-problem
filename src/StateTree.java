import java.util.ArrayList;

import com.sun.xml.internal.messaging.saaj.packaging.mime.util.BEncoderStream;

public class StateTree {
	private StateNode root;
	
	public StateNode getRoot() {
		return root;
	}

	public void setRoot(StateNode root) {
		this.root = root;
	}

	public StateTree() {
		initData();
	}
	
	// create init data to generate tree
	private void initData() {
		ArrayList<int[]> charactersPositionStateList = new ArrayList<int[]>();
		charactersPositionStateList.add(new int[] {1, 2, 3, 4});
		charactersPositionStateList.add(new int[] {0, 0, 0, 0});
		State rootState = new State(cloneArrayListStateSides(charactersPositionStateList));
		this.root = new StateNode(rootState, null, new ArrayList<StateNode>());
		generateTree(root, charactersPositionStateList, 0, 1, -1);
	}

	// format line to show tree
	private String formatLine(int level) {
		String s = "";
		for(int i = 0; i < level; i++) {
			if(i == level - 1) {
				s = s + "|\n" + s; 
				s += "|--->";
			} else {
				s += "|    ";
			}
		}
		return s;
	}
	
	// travel tree in DLR order and show tree in console
	public void travelTree(StateNode parent, int levelMargin, int previousLevelMargin) {
		if(parent.getChildrenList().size() == 0) {
			System.out.print(formatLine(levelMargin));
			System.out.println(parent.toString());
			return;
		}
		for(int i = 0; i < parent.getChildrenList().size(); i++) {
			if(i == 0) {
				System.out.print(formatLine(levelMargin));
				System.out.println(parent.toString());
			}
			travelTree(parent.getChildrenList().get(i), levelMargin + 1, levelMargin);
		}
	}
	
	// person take an character to the opposite side , if no one need to be moved , he will go lonely
	private void swapPositionSides(ArrayList<int[]> list, int row, int targetRow, int index) {
		int tempValue1 = list.get(targetRow)[0], tempValue2 = list.get(targetRow)[index];
		list.get(targetRow)[0] = list.get(row)[0];
		list.get(targetRow)[index] = list.get(row)[index];
		list.get(row)[0] = tempValue1;
		list.get(row)[index] = tempValue2;
	}
	
	// make an clone positions state from current state. It will keep current state not to be changed
	// because we need to go back level (n) from level (n + 1) and add the next state in level (n) if need (backtracking)
	@SuppressWarnings("unchecked")
	private ArrayList<int[]> cloneArrayListStateSides(ArrayList<int[]> pattern) {
		ArrayList<int[]> clone = (ArrayList<int[]>) pattern.clone();
		clone.removeAll(pattern);
		clone.add(pattern.get(0).clone());
		clone.add(pattern.get(1).clone());
		return clone;
	}
	
	// check if this character is located in right end position (in the target side). If true, it is not necessary to move this again
	// Goat is a special character , it is in the middle of wolf and cabbage,
	// so we can't let it to be near by wolf or cabbage without person
	private boolean hasEndPosition(ArrayList<int[]> curPositionsList, int firstSide, int secondSide, int index) {
		int checkedSide = (firstSide == 1) ? firstSide : secondSide;
		return index != 0 && index != 2 && curPositionsList.get(checkedSide)[index] != 0; // used for new version
//		return index != 2 && curPositionsList.get(checkedSide)[index] != 0;
	}
	
	// create StateNode
	private StateNode createStateNode(ArrayList<int[]> curPositionsList, int beginSide, int endSide, int swapIndex) {
		StateNode childStateNode = null;
		swapPositionSides(curPositionsList, beginSide, endSide, swapIndex);
		ArrayList<int[]> listClone = cloneArrayListStateSides(curPositionsList);
		State state = new State(listClone);
		childStateNode = new StateNode(state, null, new ArrayList<StateNode>());
		return childStateNode;
	}
	
	// Link two StateNode if one of them is the other's parent
	private void linkToParent(StateNode parent, StateNode child) {
		parent.getChildrenList().add(child);
		child.setParent(parent);
	}
	
	// generate tree from NodeStates was created
//	public void generateTree(StateNode parent, ArrayList<int[]> curPositionsList, int beginSide, int endSide, int movedCharacterIndex) {
//		StateNode childStateNode = null;
//		boolean hasNoCharacterToMove = true;
//		// if this is right-end state , no StateNode can be created. So we can backtrack to previous level to check the next StateNode
//		if(parent.getState().isTheLastState()) {
//			return;
//		}
//		// always use the side that person is standing to move character
//		for(int i = 1; i < curPositionsList.get(beginSide).length; i++) { // neu muon cho ca nguoi di chuyen thi de y = 0
//			if(hasEndPosition(curPositionsList, beginSide, endSide, i)) { // neu khong muon bi trung thi phai sua lai cach luu cha 
//				continue;												  // vi 1 con co the co nhieu cha . sau do phai tao 1 list chua cac node da duoc xet
//			}															  // neu nhu cac node da xet roi thi khong xet nua (return)
//			// movedCharacterIndex : the index of character who has just been taken from the opposite to here
//			// we must not take it back again 
//			if(curPositionsList.get(beginSide)[i] != 0 && i != movedCharacterIndex) {
//				if((childStateNode = createStateNode(curPositionsList, beginSide, endSide, i)) != null) {
//					hasNoCharacterToMove = false;
//					linkToParent(parent, childStateNode);
//					if(childStateNode.getState().isValidPosition()) {
//						// here we reverse begin side to end side in recursion to move to the opposite side from current side
//						generateTree(childStateNode, cloneArrayListStateSides(childStateNode.getState().getPositions()), endSide, beginSide, i);
//					}
//				}
//				// restore current position state for checking the next StateNode
//				swapPositionSides(curPositionsList, endSide, beginSide, i);
//			} 
//		}
//		// Have no character to move , person will go alone
//		if(hasNoCharacterToMove) {
//			childStateNode = createStateNode(curPositionsList, beginSide, endSide, 0);
//			linkToParent(parent, childStateNode);
//			generateTree(childStateNode, cloneArrayListStateSides(childStateNode.getState().getPositions()), endSide, beginSide, 0);
//		}
//	}
	
	// optimisation version
	public void generateTree(StateNode parent, ArrayList<int[]> curPositionsList, int beginSide, int endSide, int movedCharacterIndex) {
		StateNode childStateNode = null;
		for(int i = 0; i < curPositionsList.get(beginSide).length; i++) {
			// check right position
			if(hasEndPosition(curPositionsList, beginSide, endSide, i)) {
				continue;												  
			}
			// go to next node if this node is valid
			if(curPositionsList.get(beginSide)[i] != 0 && i != movedCharacterIndex) {
				if((childStateNode = createStateNode(curPositionsList, beginSide, endSide, i)) != null) {
					linkToParent(parent, childStateNode);
					if(childStateNode.getState().isValidPosition()) {
						generateTree(childStateNode, cloneArrayListStateSides(childStateNode.getState().getPositions()), endSide, beginSide, i);
					}
				}
				swapPositionSides(curPositionsList, endSide, beginSide, i);
			}
		}
	}
}
