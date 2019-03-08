import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.Raster;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


// Declaring some variables and calling classes
public class Main {
	private int mnistHeight;
	private int mnistWidth;
	private JFrame frame;
	public static ExecutorService queryExecutor;
	public static ExecutorService trainExecutor;
	public static ScheduledExecutorService testExecutor;
	MnistReader reader;
	BufferedImage bim;
	private Graphics2D g2d;
	Point last = null;
	JLabel predicted = new JLabel("");
	JLabel confidence = new JLabel();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws IOException {

		queryExecutor = Executors.newSingleThreadExecutor();
		trainExecutor = Executors.newSingleThreadExecutor();
		testExecutor = Executors.newSingleThreadScheduledExecutor();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.getFrame().setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	// Initialize and call Mnistreader class
	public Main() throws FileNotFoundException, IOException {
		reader = new MnistReader();
		reader.readMnist();
		mnistHeight = reader.getImageHeight();
		mnistWidth = reader.getImageWidth();
		bim = new BufferedImage(mnistWidth * 10, mnistHeight * 10, BufferedImage.TYPE_INT_RGB);
		initialize();

	}

	/**
	 * Inner class that extends the JPanel
	 * 
	 * The purpose of this is to be able to draw on the canvas of the 
	 * panel
	 * @author st20099419
	 *
	 */
	//Applying paint component to the buffered image to draw in panel
	class CanvasPanel extends JPanel {
		@Override
		protected void paintComponent(Graphics g) {
			g.drawImage(bim, 0, 0 , null);
		}
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setFrame(new JFrame());
		getFrame().setBounds(100, 100, 820, 600);
		getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				
		JPanel panel = new CanvasPanel();
		
		g2d = bim.createGraphics();
		g2d.setPaint(Color.BLACK);
		g2d.fillRect(0, 0, mnistWidth *  10 , mnistHeight * 10);
		// input panel
		// changed colour to black as the MNIST images are in black
		panel.setPreferredSize(new Dimension(mnistWidth * 10, mnistHeight * 10));
		panel.setBackground(Color.BLACK);
		panel.setForeground(Color.WHITE);
		
		
		// Output panel showing predicted digit
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel_1.setLayout(new BorderLayout());
		predicted.setBackground(Color.white);
		
		Font font = predicted.getFont();
		predicted.setFont(new Font(font.getName(), Font.PLAIN, font.getSize() * 10));
		
		panel_1.add(predicted,BorderLayout.CENTER);
		panel_1.add(confidence, BorderLayout.SOUTH);
		
		panel.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// Triggers even when digit drawn and mouse press is released
				// scaling buffered image
				// Returns a transform representing a scaling transformation.
				BufferedImage scaled = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
				AffineTransform trans = new AffineTransform();
				trans.scale(1/14.0, 1/14.0);
				BufferedImageOp op = new AffineTransformOp(trans,
							new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
				op.filter(bim, scaled);
				// Adding border to buffered image
				BufferedImage bordered = new BufferedImage(mnistWidth, mnistHeight, BufferedImage.TYPE_INT_RGB);
				Graphics2D g2d = (Graphics2D) bordered.getGraphics();
				g2d.setPaint(Color.black);
				g2d.fillRect(0, 0, mnistWidth, mnistHeight);
				
				g2d.drawImage(scaled, 4, 4, null);
				
				//creating a rectangular array of pixels that encapsulates a data buffer that stores sample values
				int[][] pixels = new int[mnistHeight][mnistWidth];
				Raster raster = bordered.getRaster();
				for(int row = 0 ; row < mnistHeight ; row++) {
					for(int col = 0 ; col < mnistWidth; col++) {
						int[] pixel = raster.getPixel(col, row, new int[3]);
						int avg =  (pixel[0] + pixel[1] + pixel[2] )/3;
						pixels[row][col] = avg;
						
						//pixels [row][col] = (pixel[0] + pixel[1] + pixel[2] )/3;
					}
				}
				
				for(int row = 0 ; row < mnistHeight ; row++ ) {
					for(int col = 0 ; col < mnistWidth ; col++) {
						if(pixels[row][col] == 0) {
							System.out.print(' ');
						}
						else {
							System.out.print('*');
						}
					}
					System.out.println();
				}
				
				KNN knn = new KNN(reader);
				ArrayList<LabelDistance> distance = knn.findNeighbours(pixels);
				Map<Byte, Integer> counts = knn.aggregate(distance, 7);
				
				Byte labeled = knn.findNearest(counts);
				double cnf = knn.confidence(counts, labeled);
				System.out.println("Nearest neighbours prediction = " + labeled);
				last = null;
				
				predicted.setText(Byte.toString(labeled));
				confidence.setText(String.format("Confidents = %2f", cnf));
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// this is where you color
				// color the pixel under the mouse.
				
				g2d.setPaint(Color.WHITE);
				g2d.setStroke(new BasicStroke(15));
				if(last == null) {
					
					g2d.drawLine(e.getX(), e.getY(), e.getX(), e.getY());
				}
				else {
					g2d.drawLine(last.x, last.y, e.getX(), e.getY());
				}
				last = e.getPoint();
				panel.repaint();
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				last = null;
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// ignored
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				// color the pixel under the mouse.
				g2d.setPaint(Color.WHITE);
				g2d.setStroke(new BasicStroke(15));
				g2d.drawLine(e.getX(), e.getY(), e.getX(), e.getY());
				
				last = e.getPoint();
				panel.repaint();
			}
		});
		
		panel.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			// painting as you drag mouse , colours pixel under current position of mouse
			@Override
			public void mouseDragged(MouseEvent e) {
				// color the pixel under the mouse
				g2d.setPaint(Color.WHITE);
				g2d.setStroke(new BasicStroke(20));
				if(last == null) {
					
					g2d.drawLine(e.getX(), e.getY(), e.getX(), e.getY());
				}
				else {
					g2d.drawLine(last.x, last.y, e.getX(), e.getY());
				}
				last = e.getPoint();
				panel.repaint();
			}
		});
		JLabel lblInput = new JLabel("INPUT");
		lblInput.setFont(new Font("Tahoma", Font.PLAIN, 20));

		JLabel lblOutput = new JLabel("OUTPUT");
		lblOutput.setFont(new Font("Tahoma", Font.PLAIN, 20));

		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Paint p = g2d.getPaint();
				g2d.setPaint(Color.BLACK);
				g2d.fillRect(0,0,mnistWidth * 10, mnistHeight * 10);
				g2d.setPaint(p);
				panel.repaint();
				System.out.print("aa");
			}
		});
		// Designing GUI using WINDOWS BUILDER 
		
		btnClear.setFont(new Font("Tahoma", Font.PLAIN, 20));

		GroupLayout groupLayout = new GroupLayout(getFrame().getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(84, Short.MAX_VALUE)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE,  mnistHeight * 10, GroupLayout.PREFERRED_SIZE)
					.addGap(72)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 284, GroupLayout.PREFERRED_SIZE)
					.addGap(76))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(189)
					.addComponent(lblInput)
					.addPreferredGap(ComponentPlacement.RELATED, 309, Short.MAX_VALUE)
					.addComponent(lblOutput)
					.addGap(175))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(366, Short.MAX_VALUE)
					.addComponent(btnClear)
					.addGap(359))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(43)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblOutput)
						.addComponent(lblInput))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
							.addGap(35))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(panel, GroupLayout.PREFERRED_SIZE,  mnistHeight * 10, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addComponent(btnClear)
					.addGap(127))
		);
		getFrame().getContentPane().setLayout(groupLayout);
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
}
