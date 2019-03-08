import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class KNN {
	private static final int TreeMap = 0;
	MnistReader reader = new MnistReader();
	//drawImage handrawn = new drawImage();
	int DistanceOfP;
	

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
	
	public void test() throws FileNotFoundException, IOException {
		ArrayList<LabelDistance> distance = findNeighbours(reader.getImageList().get(0));
		Map<Byte, Integer> counts = new java.util.TreeMap<>();
		int k = 10;
		for(int i = 0 ; i < k ; i++) {
			LabelDistance d = distance.get(i);
			System.out.println(d);
			if(counts.containsKey(d.label)) {
				counts.put(d.label, counts.get(d.label) + 1);
			}
			else {
				counts.put(d.label, 1);
			}
		}
		
		Byte labeled = findNearest(counts);
		System.out.println("Nearest neighbours prediction = " + labeled);
		
	}
	
	
	
	//public void test2() throws FileNotFoundException, IOException {
		//int drawImage = handrawn.drawline()[0];
	//}
	
	public void test3() throws FileNotFoundException, IOException {
		reader.readMnist();
		
		// get the first item and put intoa 2d array
		//int[][] sample = reader.getImageList().get(0);
		//int label = reader.getLabel_data()[0];
		//int[][] distance = DistanceOfP.getdistance().get(0);
		
		
		
		//System.out.println("Distance and labels =  " + label, + distance);
				
		
	}
	
	
	
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		KNN knn = new KNN();
		knn.reader.readMnist();
		knn.test();

	}
}
