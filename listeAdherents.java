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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.SystemColor;
import javax.swing.BorderFactory;

public class listeAdherents extends JFrame {

	private Connection conn = null ;
	private Statement statement = null ;
	private ResultSet rs = null ;

	
	public void connect(String db) throws ClassNotFoundException , SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver") ;
		conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/"+db+"?serverTimezone=UTC" , "root" , "") ;
		System.out.println("connected to : "+db);
		
		statement = (Statement) conn.createStatement() ;
	}

	public void supprimerUtilisateur(String id) {
		String query = "DELETE FROM adherent WHERE id = '"+id+"' " ;
		try {
			statement.executeUpdate(query) ;
			System.out.println("user deleted !");
			JOptionPane.showMessageDialog(null, "Utilisateur supprimé" , "Succés" , JOptionPane.INFORMATION_MESSAGE );
		}catch(SQLException ex) {
			Logger.getLogger(signin.class.getName()).log(Level.SEVERE, null , ex) ;
			JOptionPane.showMessageDialog(null, "Erreur " , "Erreur" , JOptionPane.ERROR_MESSAGE );
		}
	}
	
	public JPanel createPanel(int y, ResultSet rs) throws SQLException {
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBounds(60, y, 849, 71);
		panel.setLayout(null);
		
		JLabel idField = new JLabel(rs.getString("id"));
		idField.setBackground(Color.LIGHT_GRAY);
		idField.setBounds(28, 25, 40, 14);
		panel.add(idField);
		
		JLabel nomField = new JLabel(rs.getString("nom"));
		nomField.setBackground(Color.LIGHT_GRAY);
		nomField.setBounds(84, 25, 124, 14);
		panel.add(nomField);
		
		JLabel prenomField = new JLabel(rs.getString("prenom"));
		prenomField.setBackground(Color.LIGHT_GRAY);
		prenomField.setBounds(218, 25, 87, 14);
		panel.add(prenomField);
		
		JLabel emailField = new JLabel(rs.getString("email"));
		emailField.setBackground(Color.LIGHT_GRAY);
		emailField.setBounds(315, 25, 129, 14);
		panel.add(emailField);
		
		JLabel passwordField = new JLabel(rs.getString("password"));
		passwordField.setBackground(Color.LIGHT_GRAY);
		passwordField.setBounds(468, 25, 118, 14);
		panel.add(passwordField);
		
		JButton modfierUtilisateurBtn = new JButton("modifier");
		modfierUtilisateurBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				dispose() ;
				try {
					adminSession adminSession = new adminSession() ;
					adminSession.setVisible(true) ;
					
					String id =idField.getText() , nom = nomField.getText() , prenom = prenomField.getText() , email = emailField.getText() , password = passwordField.getText()  ;
					
					adminSession.idField.setText(id) ;
					adminSession.nomField.setText(nom);
					adminSession.prenomField.setText(prenom) ;
					adminSession.emailField.setText(email) ;
					adminSession.passwordField.setText(password) ;
					
					
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
				
			}
		});
		modfierUtilisateurBtn.setBackground(Color.WHITE);
		modfierUtilisateurBtn.setBounds(720, 11, 100, 23);
		panel.add(modfierUtilisateurBtn);
		
		JButton supprimerUtilisateurBtn = new JButton("supprimer");
		supprimerUtilisateurBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String id = idField.getText() ;
				supprimerUtilisateur(id);				
			}
		});
		supprimerUtilisateurBtn.setBackground(Color.WHITE);
		supprimerUtilisateurBtn.setBounds(720, 37, 100, 23);
		panel.add(supprimerUtilisateurBtn);
		
		
		return panel ;
	}
	

	public listeAdherents() throws SQLException, ClassNotFoundException {
		setResizable(false);
		
		connect("biblioIsima") ;
		
		setBounds(330, 100, 1052, 651);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(new ImageIcon("icons8_list_32px.png").getImage()) ;
		getContentPane().setLayout(null);
		setTitle("liste des adherents");
		
		JPanel mainPanel = new JPanel();
		
		final JScrollPane scrollPanel = new JScrollPane(
		    mainPanel,
		    ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
		    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS
		);
		scrollPanel.setWheelScrollingEnabled(true) ;
		scrollPanel.getVerticalScrollBar().setUnitIncrement(16);
		scrollPanel.setBounds(0, 0, 1035, 612);
		mainPanel.setBounds(0, 0, 1120, 1080);
		mainPanel.setPreferredSize(new Dimension(1020, 650));
		mainPanel.setLayout(null);
		getContentPane().add(scrollPanel);
		setResizable(false);

		
		
		JLabel idLabel = new JLabel("id :");
		idLabel.setBounds(105, 26, 46, 14);
		mainPanel.add(idLabel);
		
		JLabel nomLabel = new JLabel("nom :");
		nomLabel.setBounds(189, 26, 46, 14);
		mainPanel.add(nomLabel);
		
		JLabel prenomLabel = new JLabel("prenom :");
		prenomLabel.setBounds(296, 26, 46, 14);
		mainPanel.add(prenomLabel);
		
		JLabel emailLabel = new JLabel("email :");
		emailLabel.setBounds(419, 26, 46, 14);
		mainPanel.add(emailLabel);
		
		JLabel passwordLabel = new JLabel("password :");
		passwordLabel.setBounds(539, 26, 75, 14);
		mainPanel.add(passwordLabel);
		
		JButton actualiser = new JButton("");
		actualiser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose() ;
				try {
					new listeAdherents().setVisible(true) ;
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
		actualiser.setBounds(943, 54, 50, 32);
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
		retour.setBounds(943, 11, 50, 32);
		mainPanel.add(retour);
		
		
		String query = "SELECT * FROM adherent" ;
		rs = statement.executeQuery(query) ;
		int y = 70 ;
		int height = 650 ;
		while (rs.next()) {
			mainPanel.add(createPanel(y, rs)) ;
			mainPanel.setPreferredSize(new Dimension(1020 , height));
			y += 75 ;
			height += 100 ;
		}
		
	}
}
