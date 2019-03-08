import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class TestImages {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		MnistReader trainReader = new MnistReader();
		trainReader.readMnist();
		trainReader.readMnistTest();
		KNN knn = new KNN(trainReader);
		byte[] testLabels = trainReader.getTestLabels();
		ArrayList<int[][]> testImages = trainReader.getTestImageList();
		Random rand = new Random();
		
		int matched = 0;
		int mismatched = 0;
		
		for(int i = 0 ; i < 100 ; i++) {
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
