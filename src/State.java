import java.util.ArrayList;
import java.util.Arrays;

public class State {
	// We use numbers to present characters : 1 is Person, 2 is Wolf, 3 is Goat and 4 is Cabbage
	private final int PERSON_ID = 1, WOLF_ID = 2, GOAT_ID = 3, CABBAGE_ID = 4; 
	
	private ArrayList<int[]> positions;
	
	protected ArrayList<int[]> getPositions() {
		return positions;
	}

	protected void setPositions(ArrayList<int[]> positions) {
		this.positions = positions;
	}

	public State(ArrayList<int[]> positions) {
		this.positions = positions;
	}
	
	private boolean checkEdgeConflict(int[] edge) {
		if(countPositions(edge) != 2) {
			return false;
		}
		// In case this edge has two chars , if one of them is goat , it conflicts with the other (if the other is not person)
		if(edge[PERSON_ID - 1] != 0 && edge[GOAT_ID - 1] != 0 ||
				edge[WOLF_ID - 1] != 0 && edge[CABBAGE_ID - 1] != 0) {
			return false;
		}
		return true;
	}
	
	public int countPositions(int[] edge) {
		int count = 0;
		for(int i = 0; i < edge.length; i++) {
			count = (edge[i] != 0) ? count + 1 : count;
		}
		return count;
	}
	
	public boolean isValidPosition() {
		if(positions == null) {
			return false;
		}
		return !checkEdgeConflict(positions.get(0)) && !checkEdgeConflict(positions.get(1));
	}
	
	public boolean isTheLastState() {
		for(int i = 0; i < positions.get(1).length; i++) {
			if(positions.get(1)[i] == 0) {
				return false;
			}
		}
		return true;
	}
	
	
	@Override
	public String toString() {
		String text = "";
		text += Arrays.toString(positions.get(0)) + "||";
		text += Arrays.toString(positions.get(1));
		return text;
	}
	
}
