import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class KNN {
	MnistReader reader;
	//drawImage handrawn = new drawImage();
	int DistanceOfP;
	
	public KNN() {
		reader = new MnistReader();
	}
	
	public KNN(MnistReader reader) {
		this.reader = reader;
	}
	 // creating a 2d that holds distances and labels
	public ArrayList<LabelDistance> findNeighbours(int[][] sample) {
		ArrayList<LabelDistance> distance = new ArrayList<>();
		ArrayList<int[][]> images = reader.getImageList();
		byte[] labels = reader.getLabels();
		
		for(int i = 0 ; i < labels.length ; i++) {
			int[][] e = images.get(i); // for each elemetn e in the train set.
			double d = 0;
			for(int row=0; row < sample.length ; row++) {
				for(int col = 0 ; col < sample.length ; col++) {
					d += Math.pow(e[row][col] - sample[row][col], 2);
				}
			}
			d = Math.sqrt(d);
			distance.add(new LabelDistance(labels[i], d));
		}

		Collections.sort(distance);
		return distance;
	}
	
	// computing the Euclidian distance between training image and current image
	public Byte findNearest(Map<Byte, Integer> distances) {
		Byte champion = (byte) -1;
		int championCount = Integer.MIN_VALUE;
		for(Byte b : distances.keySet()) {
			int count = distances.get(b);
			if (count > championCount) {
				champion = b;
				championCount = count;
			}
		}
		return champion;
	}
	// mapping each distance to have a key that contains a value
	public void test() throws FileNotFoundException, IOException {
		ArrayList<LabelDistance> distance = findNeighbours(reader.getImageList().get(0));
		Map<Byte, Integer> counts = aggregate(distance, 10);
		
		Byte labeled = findNearest(counts);
		System.out.println("Nearest neighbours prediction = " + labeled);
		
	}

	Map<Byte, Integer> aggregate(ArrayList<LabelDistance> distance, int k) {
		Map<Byte, Integer> counts = new java.util.TreeMap<>();

		for(int i = 0 ; i < k ; i++) {
			LabelDistance d = distance.get(i);
			//System.out.println(d);
			if(counts.containsKey(d.label)) {
				counts.put(d.label, counts.get(d.label) + 1);
			}
			else {
				counts.put(d.label, 1);
			}
		}
		return counts;
	}
	
	public double confidence(Map<Byte, Integer> map, byte b) {
		int count = 0;
		for(byte k: map.keySet()) {
			if(b == k) {
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
