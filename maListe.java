import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.UIManager;

public class maListe extends JFrame  {

	private static Connection conn = null ;
	private static Statement statement = null ;
	private static PreparedStatement preparedStatement,ps = null ;
	ResultSet result ;
	JFrame frame ; 
	
	public void connect(String db) throws ClassNotFoundException , SQLException , IOException {
		Class.forName("com.mysql.cj.jdbc.Driver") ;
		conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/"+db+"?serverTimezone=UTC" , "root" , "") ;
		System.out.println("connected to : "+db);
		
		statement = (Statement) conn.createStatement() ;
	}
	
	public JPanel createPanel(String id , ResultSet rs , ResultSet r , int y) throws SQLException {
		
				
				JPanel panel = new JPanel();
				panel.setBounds(50, y, 659, 156);
				panel.setLayout(null);
				panel.setBackground(Color.white);
				panel.setSize(new Dimension(900 , 150));
				
				JLabel imageLabel = new JLabel("");
				imageLabel.setBounds(10, 11, 140, 134);
				
				ImageIcon image = new ImageIcon(r.getBytes("image")) ;
				Image im = image.getImage() ;
				Image myImg = im.getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH) ;
				ImageIcon i = new ImageIcon(myImg) ;
				imageLabel.setIcon(i);
				panel.add(imageLabel);
				
				JLabel titleLabel = new JLabel("Titre :");
				titleLabel.setFont(new Font("SansSerif", titleLabel.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, titleLabel.getFont().getSize() + 5));
				titleLabel.setBounds(231, 26, 150, 14);
				panel.add(titleLabel);
				
				JLabel codeLabel = new JLabel("Code :");
				codeLabel.setFont(new Font("SansSerif", codeLabel.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, codeLabel.getFont().getSize() + 5));
				codeLabel.setBounds(231, 58, 83, 14);
				panel.add(codeLabel);
				
				JLabel reserveeLabel = new JLabel("Reservee :");
				reserveeLabel.setFont(new Font("SansSerif", reserveeLabel.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, reserveeLabel.getFont().getSize() + 5));
				reserveeLabel.setBounds(231, 80, 150, 14);
				panel.add(reserveeLabel);
				
				JLabel title = new JLabel("New label");
				title.setText(r.getString("titre"));
				title.setFont(new Font("SansSerif", title.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, title.getFont().getSize() + 7));
				title.setBounds(323, 26, 300, 25);
				panel.add(title);
				
				JLabel code = new JLabel("New label");
				code.setText(r.getString("code"));
				code.setFont(new Font("SansSerif", code.getFont().getStyle(), code.getFont().getSize() + 4));
				code.setBounds(323, 59, 234, 14);
				panel.add(code);
				
				JButton btnNewButton = new JButton();
				btnNewButton.setIcon(new ImageIcon("icons8_minus_32px.png"));
				btnNewButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						String query = "DELETE FROM `liste` WHERE `liste`.`id` = '"+id+"' AND `liste`.`code` = '"+code.getText()+"' " ;
						
						try {
							statement.executeUpdate(query) ;
							System.out.println("Media supprimé de la liste ");
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						btnNewButton.setVisible(false) ;
					}
				});
				btnNewButton.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
				btnNewButton.setContentAreaFilled(false);
				btnNewButton.setBounds(821, 11, 40, 38);
				panel.add(btnNewButton);				
				
		return panel;
	}
	
	public int getRows(String id) throws SQLException {
		String rowsQuery = "SELECT count(*) FROM liste WHERE id = '"+id+"' " ;
		ResultSet RowsResutl = statement.executeQuery(rowsQuery) ;
		RowsResutl.next() ;
		int rows = RowsResutl.getInt(1) ;
		System.out.println("get Rows : "+rows);
		return rows ;
	}
	
	public ResultSet getDocumentsById(String id) throws SQLException {
		String query = "SELECT * FROM liste WHERE id = '"+id+"'" ;
		preparedStatement = conn.prepareStatement(query) ;
		ResultSet rs = preparedStatement.executeQuery(query) ;
		System.out.println("get documents by id : executed");
		return rs ;
	}
	
	public maListe(String id , String email) throws ClassNotFoundException, SQLException, IOException {
				
		connect("biblioIsima") ;
		
		getContentPane().setBackground(UIManager.getColor("Button.background"));
		setBounds(330, 100, 1222, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(new ImageIcon("icons8_list_32px.png").getImage()) ;
		getContentPane().setLayout(null);
		setTitle("Votre Liste");
		setResizable(false);
		
		JPanel mainPanel = new JPanel();		
		mainPanel.setBackground(UIManager.getColor("InternalFrame.inactiveTitleBackground"));
		final JScrollPane scrollPanel = new JScrollPane(
		    mainPanel,
		    ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
		    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS
		);
		scrollPanel.setWheelScrollingEnabled(true) ;
		scrollPanel.getVerticalScrollBar().setUnitIncrement(16);
		scrollPanel.setBounds(0, 0, 1206, 761);
		mainPanel.setBounds(0, 0, 1120, 1080);
		mainPanel.setPreferredSize(new Dimension(1020, 90));
		mainPanel.setLayout(null);
		
		JButton retour = new JButton("");
		retour.setBounds(1127, 28, 50, 32);
		mainPanel.add(retour);
		retour.setIcon(new ImageIcon("C:\\Users\\PC\\eclipse-workspace\\essai 2\\icons8_home_32px_1.png"));
		retour.setBorder(null);
		retour.setForeground(new Color(255, 255, 255));
		retour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					dispose() ;
					new front_page(id, email) ;
				} catch (ClassNotFoundException | SQLException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		retour.setBackground(UIManager.getColor("Button.highlight"));
		retour.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		retour.setContentAreaFilled(false);
		
		JButton actualiser = new JButton("");
		actualiser.setBounds(1127, 71, 50, 32);
		mainPanel.add(actualiser);
		actualiser.setIcon(new ImageIcon("C:\\Users\\PC\\eclipse-workspace\\essai 2\\icons8_update_left_rotation_32px.png"));
		actualiser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					dispose() ;
					new maListe(id, email) ;
				} catch (ClassNotFoundException | SQLException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		actualiser.setForeground(new Color(255, 255, 255));
		actualiser.setBackground(UIManager.getColor("Button.background"));
		actualiser.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		actualiser.setContentAreaFilled(false);
		getContentPane().add(scrollPanel);
		
		ResultSet rs = getDocumentsById(id) ;
		
		int y = 44 ;
		int height = 1080 ;
		while(rs.next()) {
			String q = "SELECT * FROM document WHERE code = '"+rs.getString("code")+"' " ;  //when executing two queries together , each query should be executed with a preparedStatement or statement  
			ps = conn.prepareStatement(q) ;
			ResultSet r = ps.executeQuery(q) ;

			if (r.next()) {
				height += 150 ;
				JPanel panel = createPanel(id, rs , r , y) ;
				mainPanel.setPreferredSize(new Dimension(1000, height));
				mainPanel.add(panel) ;
			}
			y += 150 ;
		}
		
		
		
		setVisible(true) ;
	}
}