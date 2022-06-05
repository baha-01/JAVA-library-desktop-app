import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.SystemColor;
import javax.swing.BorderFactory;

public class listeDocuments extends JFrame {

	private Connection conn = null ;
	private Statement statement = null ;
	private ResultSet rs = null ;

	public void connect(String db) throws ClassNotFoundException , SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver") ;
		conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/"+db+"?serverTimezone=UTC" , "root" , "") ;
		System.out.println("connected to : "+db);
		
		statement = (Statement) conn.createStatement() ;
	}

	public void supprimerDocument(String code) {
		String query = "DELETE FROM document WHERE code = '"+code+"' " ;
		try {
			statement.executeUpdate(query) ;
			System.out.println("document deleted !");
			JOptionPane.showMessageDialog(null, "Film supprimé" , "Succés" , JOptionPane.INFORMATION_MESSAGE );
		}catch(SQLException ex) {
			Logger.getLogger(signin.class.getName()).log(Level.SEVERE, null , ex) ;
			JOptionPane.showMessageDialog(null, "Erreur " , "Erreur" , JOptionPane.ERROR_MESSAGE );
			System.out.println("document non deleted");
		}
	}
	
	public JPanel createPanel(int y, ResultSet rs) throws SQLException {
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBounds(61, y, 1050, 95);
		panel.setLayout(null);
		
		JLabel codefield = new JLabel(rs.getString("code"));
		codefield.setBackground(Color.LIGHT_GRAY);
		codefield.setBounds(28, 25, 40, 14);
		panel.add(codefield);
		
		JLabel titreField = new JLabel(rs.getString("titre"));
		titreField.setBackground(Color.LIGHT_GRAY);
		titreField.setBounds(108, 25, 183, 14);
		panel.add(titreField);
		
		JLabel categorieField = new JLabel(rs.getString("categorie"));
		categorieField.setBackground(Color.LIGHT_GRAY);
		categorieField.setBounds(315, 25, 162, 14);
		panel.add(categorieField);
		
		JLabel rangeeLabel = new JLabel(rs.getString("rangee"));
		rangeeLabel.setBackground(Color.LIGHT_GRAY);
		rangeeLabel.setBounds(500, 25, 162, 14);
		panel.add(rangeeLabel);
		
		JLabel reserveLabel = new JLabel(rs.getString("reserve"));
		reserveLabel.setBackground(Color.LIGHT_GRAY);
		reserveLabel.setBounds(600, 25, 162, 14);
		panel.add(reserveLabel);
		
		JLabel imageField = new JLabel();
		imageField.setBackground(Color.LIGHT_GRAY);
		imageField.setBounds(706, 11, 118, 73);
		byte[] img = rs.getBytes("image") ;
		ImageIcon image = new ImageIcon(img) ;
		Image im = image.getImage() ;
		Image myImg = im.getScaledInstance(imageField.getWidth(), imageField.getHeight(), Image.SCALE_SMOOTH) ;
		ImageIcon i = new ImageIcon(myImg) ;
		imageField.setIcon(i);
		panel.add(imageField);
		
		JButton modifierFilmBtn = new JButton("modifier");
		modifierFilmBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				dispose() ;
				try {
					adminSession adminSession = new adminSession() ;
					adminSession.setVisible(true) ;
					
					String code =codefield.getText() , titre = titreField.getText() , categorie = categorieField.getText() , 
							rangee = rangeeLabel.getText() , reserve = reserveLabel.getText()  ;
					Icon image = imageField.getIcon();
					
					adminSession.codeField.setText(code) ;
					adminSession.titreField.setText(titre);
					adminSession.categorieField.setText(categorie) ;
					adminSession.rangeeField.setText(rangee) ;
					adminSession.reserveField.setText(reserve) ;
					adminSession.imageLabel.setIcon(image) ;

				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			}
		});
		modifierFilmBtn.setBackground(Color.WHITE);
		modifierFilmBtn.setBounds(922, 22, 100, 23);
		panel.add(modifierFilmBtn);
		
		JButton supprimerFilmBtn = new JButton("supprimer");
		supprimerFilmBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String code = codefield.getText() ;
				supprimerDocument(code);
				
			}
		});
		supprimerFilmBtn.setBackground(Color.WHITE);
		supprimerFilmBtn.setBounds(922, 48, 100, 23);
		panel.add(supprimerFilmBtn);
		
		
		
		return panel ;
	}

	public listeDocuments() throws SQLException, ClassNotFoundException {		
		connect("biblioIsima") ;
		
		setBounds(330, 100, 1255, 646);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(new ImageIcon("icons8_list_32px.png").getImage()) ;
		getContentPane().setLayout(null);
		setTitle("liste des documents");
		setResizable(false);
		
		JPanel mainPanel = new JPanel();
		
		final JScrollPane scrollPanel = new JScrollPane(
		    mainPanel,
		    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
		    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
		);
		scrollPanel.setWheelScrollingEnabled(true) ;
		scrollPanel.getVerticalScrollBar().setUnitIncrement(16);
		scrollPanel.setBounds(0, 0, 1229, 612);
		mainPanel.setBounds(0, 0, 1120, 1080);
		mainPanel.setPreferredSize(new Dimension(1035, 90));
		mainPanel.setLayout(null);
		getContentPane().add(scrollPanel);
		
		JLabel codeLabel = new JLabel("code :");
		codeLabel.setBounds(105, 26, 46, 14);
		mainPanel.add(codeLabel);

		JLabel titreLabel = new JLabel("titre :");
		titreLabel.setBounds(236, 26, 46, 14);
		mainPanel.add(titreLabel);

		JLabel categorieLabel = new JLabel("categorie :");
		categorieLabel.setBounds(389, 26, 69, 14);
		mainPanel.add(categorieLabel);

		JLabel imageLabel = new JLabel("image :");
		imageLabel.setBounds(815, 26, 58, 14);
		mainPanel.add(imageLabel);
		
		JButton actualiser = new JButton("");
		actualiser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose() ;
				try {
					new listeDocuments().setVisible(true);
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		actualiser.setIcon(new ImageIcon("C:\\Users\\PC\\eclipse-workspace\\essai 2\\icons8_update_left_rotation_32px.png"));
		actualiser.setForeground(Color.WHITE);
		actualiser.setContentAreaFilled(false);
		actualiser.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		actualiser.setBackground(SystemColor.menu);
		actualiser.setBounds(1124, 69, 50, 32);
		mainPanel.add(actualiser);
		
		JButton retour = new JButton("");
		retour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose() ;
				try {
					new adminSession().setVisible(true) ;
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} 
		});
		retour.setIcon(new ImageIcon("C:\\Users\\PC\\eclipse-workspace\\essai 2\\icons8_home_32px_1.png"));
		retour.setForeground(Color.WHITE);
		retour.setContentAreaFilled(false);
		retour.setBorder(null);
		retour.setBackground(Color.WHITE);
		retour.setBounds(1124, 26, 50, 32);
		mainPanel.add(retour);
		
		JLabel lblNewLabel = new JLabel("rangee :");
		lblNewLabel.setBounds(536, 26, 69, 14);
		mainPanel.add(lblNewLabel);
		
		JLabel lblReserve = new JLabel("reserve :");
		lblReserve.setBounds(635, 26, 97, 14);
		mainPanel.add(lblReserve);
		
		String query = "SELECT * FROM document" ;
		rs = statement.executeQuery(query) ;
		int y = 51 ;
		int height = 650 ; 
		while (rs.next()) {
			mainPanel.add(createPanel(y, rs)) ;
			mainPanel.setPreferredSize(new Dimension(1020 , height));
			y += 100 ;
			height += 100 ;
		}	
	}
}