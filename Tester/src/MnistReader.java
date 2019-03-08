import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class MnistReader {
	// Declare variables, array lists
	private  byte[] trainLabels;
	private  ArrayList<int[][]> imageList;
	private int imageWidth;
	private int imageHeight;
	private ArrayList<int[][]> testImageList;
	private byte[] testLabels;

	public byte[] getLabels() {
		return trainLabels;
	}

	public ArrayList<int[][]> getImageList() {
		return imageList;
	}

	public static void main(String[] args) throws IOException {
		MnistReader reader = new MnistReader();
		reader.readMnist();
	}

	public void readMnist() throws FileNotFoundException, IOException {
		imageList = new ArrayList<>();
		String train_image_filename = "MNIST\\train-images.idx3-ubyte";
		String train_label_filename = "MNIST\\train-labels.idx1-ubyte";
		
		trainLabels = readFiles(train_image_filename, train_label_filename, imageList);
	}

	public void readMnistTest() throws FileNotFoundException, IOException {
		testImageList = new ArrayList<>();
		// Location
		String train_image_filename = "MNIST\\t10k-images.idx3-ubyte";
		String train_label_filename = "MNIST\\t10k-labels.idx1-ubyte";
		
		testLabels = readFiles(train_image_filename, train_label_filename, testImageList);
	}
	
	public ArrayList<int[][]> getTestImageList() {
		return testImageList;
	}

	public byte[] getTestLabels() {
		return testLabels;
	}
	
		// Reading Image files 
	private byte[] readFiles(String train_image_filename,
					String train_label_filename, ArrayList<int[][]>imageList) 
			throws FileNotFoundException, IOException {
		//Using data input stream
		byte[] label_data;

		try (DataInputStream label_data_stream = new DataInputStream(new FileInputStream(train_label_filename));
				DataInputStream image_data_stream = new DataInputStream(new FileInputStream(train_image_filename));) {
			
			// read Bytes as int
			int startcode_img = image_data_stream.readInt();
			int startcode_label = label_data_stream.readInt();
			// Show there are results
			System.out.println("start code: images =" + startcode_img +  "startcodde labels =" + startcode_label);

			int number_of_labels = label_data_stream.readInt();
			int number_of_images = image_data_stream.readInt();
			// Show there are results
			System.out.println("number of labels: " + number_of_labels + " number of images: " + number_of_images);
			
			imageHeight = ( image_data_stream.read() << 24) | 
					(image_data_stream.read() << 16) | (image_data_stream.read() << 8) |
					(image_data_stream.read());
			
			imageWidth = image_data_stream.readInt();

			System.out.println("image size:" + imageWidth + "x" + imageHeight);

			label_data = new byte[number_of_labels];

			int image_size = imageHeight * imageWidth;
			byte[] image_data = new byte[image_size * number_of_images];

			label_data_stream.read(label_data);
			image_data_stream.read(image_data);

			int[][] image;
			for (int i = 0; i < number_of_labels; i++) {
				int label = label_data[i];
				//System.out.println(label);

				image = new int[imageWidth][imageHeight];

				for (int row = 0; row < imageHeight; row++) {
					for (int col = 0; col < imageWidth; col++) {
						image[row][col] = image_data[(i * image_size) + ((row * imageWidth) + col)] & 0XFF;

					}
				}
				imageList.add(image);

			}
		} 

		return label_data;
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}
	
}