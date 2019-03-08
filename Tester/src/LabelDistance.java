/**
 * A class to maintain a relationship between labels and the dsitances to the sample 
 * @author st20099419
 *
 */
public class LabelDistance implements Comparable {
	Double distance;
	byte label;

	public LabelDistance(byte label, double distance) {
		this.label = label;
		this.distance = distance;
	}

	@Override
	// comparing label distance to object
	public int compareTo(Object o) {
		if (o instanceof LabelDistance) {
			LabelDistance other = (LabelDistance) o;
			return distance.compareTo(other.distance);
		}

		return Integer.MIN_VALUE;
	}

	@Override
	public String toString() {
		return String.format("Label = %d distance = %.2f", (int) label, distance);
	}
}
