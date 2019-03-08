import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class MnistReader {

	private  byte[] label_data;
	private  ArrayList<int[][]> imageList;
	private int imageWidth;
	private int imageHeight;

	public byte[] getLabels() {
		return label_data;
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
		
		//Using data input stream
		DataInputStream label_data_stream = null;
		DataInputStream image_data_stream = null;

		try  {
			label_data_stream = new DataInputStream(new FileInputStream(train_label_filename));
			image_data_stream = new DataInputStream(new FileInputStream(train_image_filename));
		
			int startcode_img = image_data_stream.readInt();
			int startcode_label = label_data_stream.readInt();

			System.out.println("start code: images =" + startcode_img +  "startcodde labels =" + startcode_label);

			int number_of_labels = label_data_stream.readInt();
			int number_of_images = image_data_stream.readInt();

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
		} finally {
			System.out.println("The 'try catch' is finished.");
		}
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}
	
}