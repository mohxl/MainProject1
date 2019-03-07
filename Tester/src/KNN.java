import java.io.FileNotFoundException;
import java.io.IOException;

public class KNN {
	MnistReader reader = new MnistReader();
	//drawImage handrawn = new drawImage();
	int DistanceOfP;
	
	public void predict(int[][] image) {
		
	}
	
	
	public void test() throws FileNotFoundException, IOException {
		reader.readMnist();
		// get the first item and put intoa 2d array
		int[][] sample = reader.getImageList().get(0);
		int label = reader.getLabel_data()[0];
		
		
		
		System.out.println("the sample image has a label of " + label);
				
		
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
		knn.test();
	}
}
