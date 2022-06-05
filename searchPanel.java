import java.awt.Color;
import java.awt.Dimension;
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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;

public class searchPanel extends JFrame {

	private static  JPanel contentPane;
	private JTextField searchField;
	public static  Connection conn = null ;
	public static Statement statement = null , stm = null ;
	public static  PreparedStatement preparedStatement = null ;
	
	public void connect(String db) throws ClassNotFoundException , SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver") ;
		conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/"+db+"?serverTimezone=UTC" , "root" , "") ;
		System.out.println("connected to : "+db);
		
		statement = (Statement) conn.createStatement() ;
	}
	
	public static ResultSet showSomeDocuments(String id , String searchInput) throws SQLException {
		String query = "SELECT * FROM document WHERE categorie = '"+searchInput+"' " ;
		preparedStatement = conn.prepareStatement(query) ;
		ResultSet r = statement.executeQuery(query) ;
		System.out.println("show some documents : executed");
		return r ;
	}
	
	public static JPanel createPanel(int y , ResultSet r , String id) throws SQLException {
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.white);
		panel.setBounds(43, y, 871, 149);
		panel.setLayout(null);
		
		JLabel imageLabel = new JLabel("IMAGE");
		imageLabel.setBounds(10, 11, 109, 127);
		byte[] img = r.getBytes("image") ;
		ImageIcon image = new ImageIcon(img) ;
		Image im = image.getImage() ;
		Image myImg = im.getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH) ;
		ImageIcon i = new ImageIcon(myImg) ;
		imageLabel.setIcon(i);
		panel.add(imageLabel) ;
		
		JLabel titreLAbel = new JLabel("TITRE :");
		titreLAbel.setBounds(147, 22, 57, 14);
		panel.add(titreLAbel);
		
		JLabel titre = new JLabel("");
		titre.setText(r.getString("titre"));
		titre.setBounds(232, 22, 250, 14);
		panel.add(titre);
		
		JLabel codeLabel = new JLabel("code");
		codeLabel.setBounds(147, 47, 57, 14);
		panel.add(codeLabel);
		
		JLabel code = new JLabel("");
		code.setText(r.getString("code"));
		code.setBounds(232, 47, 176, 14);
		panel.add(code);
		
		JLabel categoryLabel = new JLabel("categorie : ");
		categoryLabel.setBounds(147, 67, 77, 14);
		panel.add(categoryLabel);

		JLabel categorie = new JLabel();
		categorie.setText(r.getString("categorie"));
		categorie.setBounds(232, 72, 137, 14);
		panel.add(categorie);
		
		JButton addToListBtn = new JButton();
		addToListBtn.setIcon(new ImageIcon("C:\\Users\\PC\\eclipse-workspace\\genie logiciel\\src\\icons8_add_32px_7.png"));
		addToListBtn.setBounds(804, 11, 57, 36);
		addToListBtn.setBackground(Color.white);
		addToListBtn.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		addToListBtn.addActionListener(new ActionListener()  {

			public void actionPerformed(ActionEvent e) {
				
				String query = "SELECT code,id FROM liste WHERE code ='"+code.getText()+"' and id = '"+id+"' " ;
				String q = "INSERT INTO `liste` (`id`, `code` ) VALUES   ('"+id+"' , '"+code.getText()+"')" ;

				try {
				ResultSet rs = statement.executeQuery(query) ;
				if (rs.next()) {
					JOptionPane.showMessageDialog(null, "Ce film est dejà ajouté !" , "Erreur" , JOptionPane.ERROR_MESSAGE );
				}
				else {						
					statement.executeUpdate(q) ;
					System.out.println("film ajouté à la liste ");
				}
				}catch(SQLException ex) {
					Logger.getLogger(signin.class.getName()).log(Level.SEVERE, null , ex) ;
				}
				
				addToListBtn.setVisible(false) ;
			} 
			
		});
		panel.add(addToListBtn);
		
		return panel ;
	}
	
	public searchPanel(String id , String email , String searchInput) throws ClassNotFoundException, SQLException {
		
		JPanel mainPanel = new JPanel() ;		
		mainPanel.setBackground(new Color(0, 0, 205));
		
		
		connect("biblioIsima") ;
		System.out.println(id);
		
		new ImageIcon("icons8_home_32px_1.png");
		
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\PC\\eclipse-workspace\\genie logiciel\\icons8_book_32px_2.png"));
		setTitle("isima bibliotheque");
		setBackground(Color.BLACK);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(330, 100, 1230, 793);
		contentPane = new JPanel();
		contentPane.setBackground(UIManager.getColor("Button.background"));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setResizable(false);
				
		final JScrollPane scrollPanel = new JScrollPane(
			    mainPanel,
			    ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS
			);
		scrollPanel.setWheelScrollingEnabled(true) ;
		scrollPanel.getVerticalScrollBar().setUnitIncrement(16);
		scrollPanel.setBounds(200, 88, 973, 658);


		mainPanel.setBounds(0, 0, 400, 600);
		mainPanel.setPreferredSize(new Dimension(1000, 650));
		getContentPane().add(scrollPanel);
		
		searchField = new JTextField();
		searchField.setForeground(new Color(255, 255, 255));
		searchField.setBounds(387, 27, 602, 30);
		searchField.setBackground(new Color(0, 0, 205));
		contentPane.add(searchField);
		searchField.setColumns(10);
		
		JButton btnNewButton = new JButton("rechercher");
		btnNewButton.setFont(btnNewButton.getFont().deriveFont(btnNewButton.getFont().getStyle() & ~Font.BOLD, btnNewButton.getFont().getSize() + 2f));
		btnNewButton.setForeground(Color.WHITE);
		btnNewButton.setBounds(999, 27, 133, 32);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String searchInput = searchField.getText() ;
				if (searchInput.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Vous devez remplir le champs !" , "Erreur !" , JOptionPane.ERROR_MESSAGE );
				}
				else {
					try {
						dispose() ;
						new searchPanel(id , email , searchInput) ;
					} catch (ClassNotFoundException | SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
 				}
				
			}
		});
		btnNewButton.setBackground(new Color(0, 0, 205));
		btnNewButton.setIcon(new ImageIcon("C:\\Users\\PC\\eclipse-workspace\\Projet java\\icons8-search-32.png"));
		contentPane.add(btnNewButton);
		
		JLabel profilePic = new JLabel("");
		profilePic.setHorizontalAlignment(SwingConstants.CENTER);
		profilePic.setBounds(52, 266, 89, 121);
		profilePic.setIcon(new ImageIcon("icons8_customer_96px.png"));
		contentPane.add(profilePic);
		
		JButton seeProfileBtn = new JButton("Voir Mon Profile");
		seeProfileBtn.setForeground(Color.WHITE);
		seeProfileBtn.setBackground(new Color(0, 0, 205));
		seeProfileBtn.setBounds(34, 407, 128, 23);
		seeProfileBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String query = "SELECT * FROM adherent WHERE email = '"+email+"' " ;
				ResultSet rs = null ;
				try {
					rs = statement.executeQuery(query) ;
					if (rs.next() == false) {
						JOptionPane.showMessageDialog(null, "Quelque chose ne va pas !" , "Erreur" , JOptionPane.ERROR_MESSAGE );
					}
					else {
						JOptionPane.showMessageDialog(
								null,
								"Vous etes "+rs.getString("prenom")+" "+rs.getString("nom")+" et votre email est : "+rs.getString("email"),
								"Welcome" , 
								JOptionPane.PLAIN_MESSAGE );
					}
			
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		});
		contentPane.add(seeProfileBtn);
		
		JButton listBtn = new JButton("Ma Liste");
		listBtn.setForeground(Color.WHITE);
		listBtn.setBackground(new Color(0, 0, 205));
		listBtn.setBounds(34, 449, 128, 23);
		listBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					
					dispose() ;
					new maListe(id , email) ;

				} catch (SQLException | ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		contentPane.add(listBtn);;
		mainPanel.setLayout(null);
		
		JButton retour = new JButton("");
		retour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose() ;
				try {
					new front_page(id , email).setVisible(true);
				} catch (ClassNotFoundException | SQLException | IOException e1) {
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
		retour.setBounds(70, 156, 50, 32);
		contentPane.add(retour);
		

		ResultSet r = null ;
		r = showSomeDocuments(id , searchInput) ;
		
		int y = 40 ;
		int height = 600 ;
		while(r.next()) {
			height += y ;
			mainPanel.add(createPanel(y, r, id)) ;		
			mainPanel.setPreferredSize(new Dimension(1000, height));
			y += 150 ;
			
		}		 
			
		this.setVisible(true);

	}
}
