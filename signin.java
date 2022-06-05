import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Image;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;

public class signin extends JFrame {

	private JPanel contentPane;
	private JTextField email;
	private JTextField nom;
	private JTextField prenom;
	private JTextField password;
	public Connection conn = null ;
	public Statement statement = null ;

	
	public void connect(String db) throws ClassNotFoundException , SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver") ;
		conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/"+db+"?serverTimezone=UTC" , "root" , "") ;
		System.out.println("connected to : "+db);
		
		statement = (Statement) conn.createStatement() ;
	}
	
	public void ajout(String a , String b , String c , String d) {
		String query = "INSERT INTO `adherent` (`id`, `nom`, `prenom`, `email`, `password`) VALUES  " + "('0' , '"+a+"','"+b+"','"+c+"','"+d+"') " ;
		try {
			statement.executeUpdate(query) ;
			System.out.println("user added !");
		}catch(SQLException ex) {
			Logger.getLogger(signin.class.getName()).log(Level.SEVERE, null , ex) ;
		}
	}
	
	public boolean checkExistantEmail(String email) throws SQLException {
		
		String query = "SELECT email FROM adherent WHERE email = '"+email+"' " ;
		ResultSet rs = statement.executeQuery(query) ;
		
		if (rs.next()) {
			System.out.println("check existant email : true ");
			return true ;
		}
		System.out.println("check existant email : false ");
		return false ;
	}
	
	
	public signin() throws ClassNotFoundException, SQLException {
		
		connect("biblioIsima") ;
		
		setTitle("Enregistrer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1117, 610);
		setIconImage(new ImageIcon("icons8_system_administrator_male_32px.png").getImage()) ;
		contentPane = new JPanel(); 
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setResizable(false);
		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.activeCaption);
		panel.setBounds(66, 78, 472, 441);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Creer un compte");
		lblNewLabel.setForeground(new Color(0, 0, 205));
		lblNewLabel.setFont(new Font("SansSerif", lblNewLabel.getFont().getStyle() | Font.BOLD, 36));
		lblNewLabel.setBounds(104, 36, 302, 72);
		panel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Email * :");
		lblNewLabel_1.setForeground(new Color(0, 0, 205));
		lblNewLabel_1.setFont(new Font("SansSerif", lblNewLabel_1.getFont().getStyle() & ~Font.BOLD, lblNewLabel_1.getFont().getSize() + 14));
		lblNewLabel_1.setBounds(22, 154, 169, 24);
		panel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("nom * :");
		lblNewLabel_2.setForeground(new Color(0, 0, 205));
		lblNewLabel_2.setFont(new Font("SansSerif", lblNewLabel_2.getFont().getStyle() & ~Font.BOLD, lblNewLabel_2.getFont().getSize() + 11));
		lblNewLabel_2.setBounds(22, 201, 121, 35);
		panel.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("prenom * :");
		lblNewLabel_3.setForeground(new Color(0, 0, 205));
		lblNewLabel_3.setFont(new Font("SansSerif", lblNewLabel_3.getFont().getStyle() & ~Font.BOLD, lblNewLabel_3.getFont().getSize() + 11));
		lblNewLabel_3.setBounds(22, 247, 169, 56);
		panel.add(lblNewLabel_3);
		
		JLabel lblNewLabel_5 = new JLabel("mot de passe * :");
		lblNewLabel_5.setForeground(new Color(0, 0, 205));
		lblNewLabel_5.setFont(new Font("SansSerif", lblNewLabel_5.getFont().getStyle() & ~Font.BOLD, lblNewLabel_5.getFont().getSize() + 11));
		lblNewLabel_5.setBounds(22, 302, 169, 42);
		panel.add(lblNewLabel_5);
		
		email = new JTextField();
		email.setBounds(229, 150, 203, 24);
		panel.add(email);
		email.setColumns(10);
		
		nom = new JTextField();
		nom.setBounds(229, 201, 203, 24);
		panel.add(nom);
		nom.setColumns(10);
		
		prenom = new JTextField();
		prenom.setBounds(229, 257, 203, 24);
		panel.add(prenom);
		prenom.setColumns(10);
		
		password = new JPasswordField();
		password.setBounds(229, 310, 203, 24);
		panel.add(password);
		password.setColumns(10);
		
		JButton signInBtn = new JButton("S'inscrir");
		signInBtn.setFont(signInBtn.getFont().deriveFont(signInBtn.getFont().getStyle() | Font.BOLD, signInBtn.getFont().getSize() + 9f));
		signInBtn.setForeground(new Color(0, 0, 205));
		signInBtn.setBackground(new Color(153, 204, 255));
		signInBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String Email = email.getText();
				String Nom = nom.getText() ;
				String Prenom = prenom.getText() ;
				String Password = password.getText() ;
				try {
					if (validateInput(Email, Nom, Prenom, Password)) {
						if (checkExistantEmail(Email) == false) {
							ajout(Nom , Prenom , Email , Password) ;
							JOptionPane.showMessageDialog(null, "Vous etes inscrit correctement" , "Inscrit" , JOptionPane.INFORMATION_MESSAGE );
							dispose() ;
							new login() ;
						}
						else {
							JOptionPane.showMessageDialog(null, "Cette email est déjà associé à un compte !" , "Erreur" , JOptionPane.ERROR_MESSAGE );
						}
					}
				} catch (SQLException | ClassNotFoundException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		signInBtn.setBounds(132, 385, 216, 35);
		panel.add(signInBtn);
		
		JLabel imageLabel = new JLabel();
		ImageIcon img = new ImageIcon("C:\\Users\\PC\\eclipse-workspace\\genie logiciel\\src\\inscrit.jpg") ;
		imageLabel.setBounds(585, 132, 481, 283);
		Image im = img.getImage() ;
		Image myImg = im.getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH) ;
		ImageIcon i = new ImageIcon(myImg) ;
		imageLabel.setIcon(i) ; 
		contentPane.add(imageLabel);
		
		JButton btnNewButton = new JButton("se Connecter");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				dispose() ;
				try {
					new login().setVisible(true);
				} catch (ClassNotFoundException | SQLException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnNewButton.setForeground(Color.BLUE);
		btnNewButton.setFont(btnNewButton.getFont().deriveFont(btnNewButton.getFont().getStyle() | Font.BOLD, btnNewButton.getFont().getSize() + 2f));
		btnNewButton.setBackground(Color.WHITE);
		btnNewButton.setBounds(932, 23, 134, 23);
		contentPane.add(btnNewButton);
				
		
	}

	private boolean checkEmpty(String Email ,String Nom ,String Prenom ,String Password) {
		
		if (Email.isEmpty() || Nom.isEmpty() || Prenom.isEmpty() || Password.isEmpty())
		{
			JOptionPane.showMessageDialog(null, "Il ne faut pas laisser des cases vides !" , "Erreur" , JOptionPane.ERROR_MESSAGE );
			System.out.println("check empty input : true ");
			return true ;
		}
		System.out.println("check empty input : false ");
		return false ;
	}
	
	private boolean validateEmail(String Email) {
		
		final String EMAIL_PATTERN = 
			    "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		if (!Email.matches(EMAIL_PATTERN)) {
			JOptionPane.showMessageDialog(null, "Le format de l'email est invalide !" , "Erreur" , JOptionPane.ERROR_MESSAGE );
			System.out.println("validate email : false");
			return false ;
		}
		System.out.println("validate email : true");
		return true ;
	}
	
	private boolean validateInput(String Email ,String Nom ,String Prenom ,String Password) {
		
		if (checkEmpty(Email, Nom, Prenom, Password) == true || validateEmail(Email) == false) {
			System.out.println("validate input : false");
			return false ;
		}
		System.out.println("validate input : true");
		return true ;
	}
}
