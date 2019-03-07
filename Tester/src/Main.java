import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Main {
	private static final int SIDE_LENGTH = 280;
	// Official java tutorial on oracle
	// using buffered image to display an image
	// coloring a buffered image using Graphics2D
	// mouse listener
	// mouse motion listener
	// swing action? (starting a thread from swing with a call back on completion ()
	
	
	private JFrame frame;
	public static ExecutorService queryExecutor;
	public static ExecutorService trainExecutor;
	public static ScheduledExecutorService testExecutor;
	MnistReader reader;
	BufferedImage bim = new BufferedImage(280, 280, BufferedImage.TYPE_INT_RGB);
	private Graphics2D g2d;
	
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
	public Main() throws FileNotFoundException, IOException {
		reader = new MnistReader();
		reader.readMnist();
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
		getFrame().setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		JPanel panel = new CanvasPanel();
		
		g2d = bim.createGraphics();
		g2d.setPaint(Color.WHITE);
		g2d.fillRect(0, 0, SIDE_LENGTH, SIDE_LENGTH);
		
		panel.setPreferredSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
		panel.setBackground(Color.WHITE);
		panel.setForeground(Color.BLACK);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);

		
		panel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				// probably ignored
				System.out.println("Rle");
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				// this is where you color
				System.out.println("drag started, record the starting point");
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stubS
				// this is where you run KNN
				System.out.println("exit" + panel.getSize());
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				// ignored
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				// color the pixel under the mouse.
				System.out.println(e.getX() + " " + e.getY());
				g2d.setPaint(Color.black);
				g2d.setStroke(new BasicStroke(10));
				g2d.drawLine(e.getX(), e.getY(), e.getX(), e.getY());
				panel.repaint();
			}
		});
		
		panel.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				// color the pixel under the mouse
				g2d.setPaint(Color.black);
				g2d.setStroke(new BasicStroke(10));
				g2d.drawLine(e.getX(), e.getY(), e.getX(), e.getY());
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
				g2d.setPaint(Color.WHITE);
				g2d.drawRect(0,0,SIDE_LENGTH, SIDE_LENGTH);
				g2d.setPaint(p);
				panel.repaint();
				System.out.print("aa");
			}
		});
		
		btnClear.setFont(new Font("Tahoma", Font.PLAIN, 20));

		GroupLayout groupLayout = new GroupLayout(getFrame().getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(84, Short.MAX_VALUE)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 280, GroupLayout.PREFERRED_SIZE)
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
							.addComponent(panel, GroupLayout.PREFERRED_SIZE, 280, GroupLayout.PREFERRED_SIZE)
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
