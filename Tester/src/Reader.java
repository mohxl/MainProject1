import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Reader {

	public static void main(String[] args) throws IOException {
		FileInputStream inp = new FileInputStream("MNIST\\train-images.idx3-ubyte");
		byte[] b = new byte[32];
		
		inp.read(b);
		System.out.println(b);
	}

}
