import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * A K Nearest Neighbour Model.
 * @author st20099419
 *
 */
public class KNN {
	MnistReader reader;
	/**
	 * The default constructor
	 */
	public KNN() {
		reader = new MnistReader();
	}

	/**
	 * Create a KNN instance with the gien reader
	 * @param reader MNIST reader
	 */
	public KNN(MnistReader reader) {
		this.reader = reader;
	}

	/**
	 * Fine the neighbours of the given sample and sort them by distance
	 * @param sample the sampel image
	 * @return a list of images sorted by the distance to the sample.
	 */
	public ArrayList<LabelDistance> findNeighbours(int[][] sample) {
		ArrayList<LabelDistance> distance = new ArrayList<>();
		ArrayList<int[][]> images = reader.getImageList();
		byte[] labels = reader.getLabels();

		for (int i = 0; i < labels.length; i++) {
			int[][] e = images.get(i); // for each elemetn e in the train set.
			double d = 0;
			for (int row = 0; row < sample.length; row++) {
				for (int col = 0; col < sample.length; col++) {
					d += Math.pow(e[row][col] - sample[row][col], 2);
				}
			}
			d = Math.sqrt(d);
			distance.add(new LabelDistance(labels[i], d));
		}

		Collections.sort(distance);
		return distance;
	}

	/**
	 * computing the Euclidian distance between training image and current image
	 * @param distances
	 * @return
	 */
	public Byte findNearest(Map<Byte, Integer> distances) {
		Byte champion = (byte) -1;
		int championCount = Integer.MIN_VALUE;
		for (Byte b : distances.keySet()) {
			int count = distances.get(b);
			if (count > championCount) {
				champion = b;
				championCount = count;
			}
		}
		return champion;
	}

	/**
	 * tester for KNN
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void test() throws FileNotFoundException, IOException {
		
		//  mapping each distance to have a key that contains a value
		ArrayList<LabelDistance> distance = findNeighbours(reader.getImageList().get(0));
		Map<Byte, Integer> counts = aggregate(distance, 10);

		Byte labeled = findNearest(counts);
		System.out.println("Nearest neighbours prediction = " + labeled);

	}

	/**
	 * Group the nearest neighbours by label.
	 * @param distance
	 * @param k
	 * @return
	 */
	Map<Byte, Integer> aggregate(ArrayList<LabelDistance> distance, int k) {
		Map<Byte, Integer> counts = new java.util.TreeMap<>();

		for (int i = 0; i < k; i++) {
			LabelDistance d = distance.get(i);
			// System.out.println(d);
			if (counts.containsKey(d.label)) {
				counts.put(d.label, counts.get(d.label) + 1);
			} else {
				counts.put(d.label, 1);
			}
		}
		return counts;
	}

	/**
	 * Find the confidence level for the prediction 
	 * @param map
	 * @param b
	 * @return
	 */
	public double confidence(Map<Byte, Integer> map, byte b) {
		int count = 0;
		for (byte k : map.keySet()) {
			if (b == k) {
				count++;
			}
		}

		return count * 100.0 / map.size();
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		KNN knn = new KNN();
		knn.reader.readMnist();
		knn.test();

	}
}
