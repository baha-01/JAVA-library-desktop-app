import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Image;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import java.awt.Toolkit;

public class login extends JFrame {

	private JPanel contentPane;
	private JTextField emailField;
	private JPasswordField PasswordField;
	public Connection conn = null ;
	public Statement statement = null ;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new login().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	public void connect(String db) throws ClassNotFoundException , SQLException , IOException {
		Class.forName("com.mysql.cj.jdbc.Driver") ;
		conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/"+db+"?serverTimezone=UTC" , "root" , "") ;
		System.out.println("connected to : "+db);
		
		statement = (Statement) conn.createStatement() ;
	}

	public login() throws ClassNotFoundException , SQLException, IOException {
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\PC\\eclipse-workspace\\genie logiciel\\icons8_book_32px_2.png"));
		
		connect("biblioIsima") ;
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 797, 671);
		contentPane = new JPanel();
		contentPane.setBackground(UIManager.getColor("TextPane.background"));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Bienvenue Dans la bibliothèque de l' ISIMA");
		lblNewLabel.setForeground(new Color(0, 0, 204));
		lblNewLabel.setBounds(129, 291, 552, 33);
		lblNewLabel.setFont(new Font("SansSerif", lblNewLabel.getFont().getStyle() | Font.BOLD, 26));
		contentPane.add(lblNewLabel);
		
		JLabel userNameLabel = new JLabel("Email :");
		userNameLabel.setFont(new Font("SansSerif", userNameLabel.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, 20));
		userNameLabel.setBounds(148, 368, 124, 26);
		contentPane.add(userNameLabel);
		
		JLabel PasswordLabel = new JLabel("Mot de Passe : ");
		PasswordLabel.setFont(new Font("SansSerif", PasswordLabel.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, 20));
		PasswordLabel.setBounds(145, 405, 164, 29);
		contentPane.add(PasswordLabel);
		
		emailField = new JTextField();
		emailField.setBounds(342, 368, 212, 27);
		contentPane.add(emailField);
		emailField.setColumns(10);
		
		PasswordField = new JPasswordField();
		PasswordField.setBounds(342, 405, 212, 28);
		contentPane.add(PasswordField);
		
		JButton ConnectBtn = new JButton("se Connecter");
		ConnectBtn.setForeground(Color.BLUE);
		ConnectBtn.setBackground(Color.WHITE);
		ConnectBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == ConnectBtn) {
					
					String email = emailField.getText() ;
					String password = PasswordField.getText() ;
					
					
					if (email.isEmpty() || password.isEmpty()) {
						JOptionPane.showMessageDialog(null, "Vous devez remplir tous les champs !" , "Erreur !" , JOptionPane.ERROR_MESSAGE );
					}
					else {
						if (email.equals("admin") && password.equals("admin")) {
							try {
								System.out.println("admin session is opened");
								dispose() ;
								new adminSession().setVisible(true) ;
							} catch (ClassNotFoundException | SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						else {
						String query = "SELECT * FROM `adherent` WHERE email = '"+email+"' AND password = '"+password+"' " ;
						ResultSet rs = null ;
						
						try {
							rs = statement.executeQuery(query) ;
							if (rs.next()) {
								String id = rs.getString("id") ; 
								dispose() ;
								try {
									new front_page(id , email);
								} catch (ClassNotFoundException e1) {
									e1.printStackTrace();
								} catch (SQLException e1) {
									e1.printStackTrace();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
							else {
								JOptionPane.showMessageDialog(null, "Retapez votre email et password !" , "Erreur" , JOptionPane.ERROR_MESSAGE );
							}
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}
		}
		});
		ConnectBtn.setFont(new Font("SansSerif", ConnectBtn.getFont().getStyle() & ~Font.ITALIC | Font.BOLD, 16));
		ConnectBtn.setBounds(342, 460, 212, 26);
		contentPane.add(ConnectBtn);
		
		JLabel Question = new JLabel("Vous n'avez pas de Compte ? ");
		Question.setBounds(179, 545, 181, 14);
		contentPane.add(Question);
		
		JButton InscrirBtn = new JButton("S'inscrir");
		InscrirBtn.setForeground(Color.BLUE);
		InscrirBtn.setBackground(Color.WHITE);
		InscrirBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose() ;
				signin signIn;
				try {
					signIn = new signin();
					signIn.setVisible(true);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		InscrirBtn.setFont(new Font("SansSerif", InscrirBtn.getFont().getStyle() & ~Font.ITALIC | Font.BOLD, 16));
		InscrirBtn.setBounds(370, 536, 116, 33);
		contentPane.add(InscrirBtn);
		
		JLabel isimaImage = new JLabel("");
		isimaImage.setBounds(0, 0, 781, 261);
		ImageIcon img = new ImageIcon("C:\\Users\\PC\\eclipse-workspace\\genie logiciel\\src\\isimaImage.jpg") ;
		Image im = img.getImage() ;
		Image myImg = im.getScaledInstance(isimaImage.getWidth(), isimaImage.getHeight(), Image.SCALE_SMOOTH) ;
		ImageIcon i = new ImageIcon(myImg) ;
		isimaImage.setIcon(i) ; 
		contentPane.add(isimaImage);
		
		JLabel lblNewLabel_1 = new JLabel("mahdia , Hiboun");
		lblNewLabel_1.setFont(lblNewLabel_1.getFont().deriveFont(lblNewLabel_1.getFont().getStyle() | Font.BOLD, lblNewLabel_1.getFont().getSize() + 5f));
		lblNewLabel_1.setBounds(575, 580, 157, 26);
		contentPane.add(lblNewLabel_1);

		
		setVisible(true);
	}
}
