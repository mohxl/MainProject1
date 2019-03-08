import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

// running KNN algorithm to compare to test images to see effeciency of the prediction
public class TestImages {
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		// create a reader
		MnistReader reader = new MnistReader();
		reader.readMnist();
		// read the test data
		reader.readMnistTest();
		
		// create a KNN classifier that will user the reader that was defined above
		KNN knn = new KNN(reader);
		byte[] testLabels = reader.getTestLabels();
		ArrayList<int[][]> testImages = reader.getTestImageList();
		Random rand = new Random();
		
		int matched = 0;
		int mismatched = 0;
		
		for(int i = 0 ; i < 100 ; i++) {
			// predict the label for 100 random images.
			int n = rand.nextInt(10000);
			int actual = (int) testLabels[n];
			int[][] image = testImages.get(n);
			
			ArrayList<LabelDistance> distance = knn.findNeighbours(image);
			Map<Byte, Integer> counts = knn.aggregate(distance, 10);
			
			int labeled = (int)knn.findNearest(counts);
			
			if(labeled == actual) {
				matched++;
			}
			else {
				mismatched++;
			}
			System.out.printf("Test labeld %d, predicted %d  COnfidence %.2f\n", 
					actual, (int) labeled, knn.confidence(counts, (byte) labeled));
		}
		
		System.out.println("\nMatched = " + matched + " Mismatched = " + mismatched);
		
	
	}
}
