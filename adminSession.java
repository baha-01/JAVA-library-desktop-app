import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Image;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.ActionEvent;

public class adminSession extends JFrame {

	 JPanel contentPane;
	 JTextField idField;
	 JTextField prenomField;
	 JTextField nomField;
	 JTextField emailField;
	 JTextField passwordField;
	 JTextField codeField;
	 JTextField titreField;
	 JTextField categorieField;
	 JLabel imageLabel ;
	 Connection conn = null ;
	 Statement statement = null ;
	 String s ;
	 JTextField rangeeField;
	 JTextField reserveField;
	
	public void connect(String db) throws ClassNotFoundException , SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver") ;
		conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/"+db+"?serverTimezone=UTC" , "root" , "") ;
		System.out.println("connected to : "+db);
		
		statement = (Statement) conn.createStatement() ;
	}
	
	public void ajoutUtilisateur(String nom , String prenom , String email , String password ) {
		String query = "INSERT INTO `adherent` (`id`, `nom`, `prenom`, `email`, `password`) VALUES  " + "('0' , '"+nom+"','"+prenom+"','"+email+"','"+password+"') " ;
		try {
			statement.executeUpdate(query) ;
			System.out.println("user added !");
			JOptionPane.showMessageDialog(null, "Utilisateur ajouté" , "Succés" , JOptionPane.INFORMATION_MESSAGE );
		}catch(SQLException ex) {
			Logger.getLogger(signin.class.getName()).log(Level.SEVERE, null , ex) ;
			JOptionPane.showMessageDialog(null, "Erreur DB" , "Erreur" , JOptionPane.ERROR_MESSAGE );
		}
	}
	
	public void ajoutDocument(String titre , String categorie , String rangee , String reserve ,InputStream image ) {
		String query = "INSERT INTO `document` (`titre`, `categorie`,`rangee` , `reserve` , `image`) VALUES  " + "(?,?,?,?,?) " ;
		
		int res = Integer.parseInt(reserve) ;
		int rng = Integer.parseInt(rangee) ;
		
		try {
			PreparedStatement ps = conn.prepareStatement(query) ;
			
			ps.setString(1, titre );
			ps.setString(2 , categorie);
			ps.setInt(3, rng);
			ps.setInt(4, res);
			ps.setBlob(5, image);
			ps.executeUpdate() ;
			System.out.println("document added !");
			JOptionPane.showMessageDialog(null, "document ajouté" , "Succés" , JOptionPane.INFORMATION_MESSAGE );
		}catch(SQLException ex) {
			Logger.getLogger(signin.class.getName()).log(Level.SEVERE, null , ex) ;
			JOptionPane.showMessageDialog(null, "erreur" , "erreur" , JOptionPane.ERROR_MESSAGE );
		}
	}
	
	public void supprimerUtilisateur(String id) {
		String query = "DELETE FROM adherent WHERE id = '"+id+"' " ;
		try {
			statement.executeUpdate(query) ;
			System.out.println("adherent deleted !");
			JOptionPane.showMessageDialog(null, "Adherent supprimé" , "Succés" , JOptionPane.INFORMATION_MESSAGE );
		}catch(SQLException ex) {
			Logger.getLogger(signin.class.getName()).log(Level.SEVERE, null , ex) ;
			JOptionPane.showMessageDialog(null, "Erreur " , "Erreur" , JOptionPane.ERROR_MESSAGE );
		}
	}
	
	public void supprimerDocument(String code) {
		String query = "DELETE FROM document WHERE code = '"+code+"' " ;
		try {
			statement.executeUpdate(query) ;
			System.out.println("document deleted !");
			JOptionPane.showMessageDialog(null, "document supprimé" , "Succés" , JOptionPane.INFORMATION_MESSAGE );
		}catch(SQLException ex) {
			Logger.getLogger(signin.class.getName()).log(Level.SEVERE, null , ex) ;
			JOptionPane.showMessageDialog(null, "Erreur " , "Erreur" , JOptionPane.ERROR_MESSAGE );
		}
	}
	
	public void modifierDocument(String code ,String titre , String categorie , String rangee ,String reserve ) {
		String query = "UPDATE `document` SET `titre` = '"+titre+"', `categorie` = '"+categorie+"', `rangee` = '"+rangee+"', `reserve` = '"+reserve+"' WHERE `document`.`code` = '"+code+"';" ;
		try {
			PreparedStatement ps = conn.prepareStatement(query) ;
			ps.executeUpdate( ) ;
			System.out.println("document modified");
			JOptionPane.showMessageDialog(null, "document modifié" , "Succés" , JOptionPane.INFORMATION_MESSAGE );
		}catch(SQLException ex) {
			Logger.getLogger(signin.class.getName()).log(Level.SEVERE, null , ex) ;
			JOptionPane.showMessageDialog(null, "erreur" , "erreur" , JOptionPane.ERROR_MESSAGE );
		}
	}
	
	public void modifierAdherent(String id ,String nom , String prenom , String email , String password  ) {
		String query = "UPDATE `adherent` SET `nom` = '"+nom+"', `prenom` = '"+prenom+"', `email` ='"+email+"' , `password` ='"+password+"'  WHERE `adherent`.`id` = '"+id+"';" ;
		try {
			statement.executeUpdate(query) ;
			System.out.println("adherent modified !");
			JOptionPane.showMessageDialog(null, "adherent modifié" , "Succés" , JOptionPane.INFORMATION_MESSAGE );
		}catch(SQLException ex) {
			Logger.getLogger(signin.class.getName()).log(Level.SEVERE, null , ex) ;
			JOptionPane.showMessageDialog(null, "Erreur DB " , "Erreur" , JOptionPane.ERROR_MESSAGE );
		}
	}
	
	public boolean checkExistanceOf(String table , String label ,String labelField) throws SQLException {
			
		String query = "SELECT "+label+" FROM "+table+" WHERE "+label+" = '"+labelField+"' " ;
		ResultSet rs = statement.executeQuery(query) ;
		
		if (rs.next()) {
			System.out.println("existance checked : true");
			return true ;
		}
		System.out.println("existance checked : false ");
		return false ;
	}
	
	private boolean checkEmptyAdherent(String Email ,String Nom ,String Prenom ,String Password) {
		
		if (Email.isEmpty() || Nom.isEmpty() || Prenom.isEmpty() || Password.isEmpty())
		{
			System.out.println("check empty adherent : true ");
			return true ;
		}
		System.out.println("check empty adherent : false ");
		return false ;
	}
	
	private boolean validateEmail(String Email) {
		final String EMAIL_PATTERN = 
			    "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		if (!Email.matches(EMAIL_PATTERN)) {
			JOptionPane.showMessageDialog(null, "Le format de l'email est invalide !" , "Erreur" , JOptionPane.ERROR_MESSAGE );
			System.out.println("email non validated ");
			return false ;
		}
		System.out.println("email validated ");
		return true ;
	}
	
	public static boolean validateId(String id) { 
		  try {  
		    Integer.parseInt(id);  
		    System.out.println("id validated");
		    return true;
		  } catch(NumberFormatException e){  
			  System.out.println("id non validated");
		    return false;  
		  }  
		}
	
	private boolean validateInputUtilisateur(String id ,String Email ,String Nom ,String Prenom ,String Password) {
		if (checkEmptyAdherent(Email, Nom, Prenom, Password) == true || validateEmail(Email) == false || validateId(id) == false) {
			System.out.println("user input validated");
			return false ;
		}
		System.out.println("user input non validated");
		return true ;
	}
		
	private boolean checkEmptyDocument(String code ,String titre ,String categorie , String rangee , String reserve ) throws IOException {
		
		if (code.isEmpty() || titre.isEmpty() || categorie.isEmpty() || rangee.isEmpty() || reserve.isEmpty())
		{
			System.out.println("check empty document : true ");
			return true ;
		}
		System.out.println("check empty document : false ");
		return false ;
	}
		
	public adminSession() throws ClassNotFoundException, SQLException {
		
		connect("biblioIsima") ;
		
		setTitle("Session ADMIN");
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\PC\\eclipse-workspace\\essai 2\\icons8_user_shield_32px.png"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1474, 695);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel addClientPanel = new JPanel();
		addClientPanel.setBackground(Color.LIGHT_GRAY);
		addClientPanel.setBounds(37, 101, 1332, 162);
		contentPane.add(addClientPanel);
		addClientPanel.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("id");
		lblNewLabel_1.setBounds(81, 35, 21, 14);
		addClientPanel.add(lblNewLabel_1);
		
		idField = new JTextField();
		idField.setBounds(51, 77, 86, 20);
		addClientPanel.add(idField);
		idField.setColumns(10);
		
		JLabel prenomLabel = new JLabel("prenom");
		prenomLabel.setBounds(239, 35, 46, 14);
		addClientPanel.add(prenomLabel);
		
		prenomField = new JTextField();
		prenomField.setBounds(181, 77, 148, 20);
		addClientPanel.add(prenomField);
		prenomField.setColumns(10);
		
		JLabel nomLabel = new JLabel("nom");
		nomLabel.setBounds(442, 35, 46, 14);
		addClientPanel.add(nomLabel);
		
		nomField = new JTextField();
		nomField.setBounds(377, 77, 148, 20);
		addClientPanel.add(nomField);
		nomField.setColumns(10);
		
		JLabel emailLabel = new JLabel("email");
		emailLabel.setBounds(658, 35, 46, 14);
		addClientPanel.add(emailLabel);
		
		emailField = new JTextField();
		emailField.setBounds(567, 77, 210, 20);
		addClientPanel.add(emailField);
		emailField.setColumns(10);
		
		passwordField = new JTextField();
		passwordField.setBounds(835, 77, 184, 20);
		addClientPanel.add(passwordField);
		passwordField.setColumns(10);
		
		JLabel passwordLabel = new JLabel("password");
		passwordLabel.setBounds(890, 35, 68, 14);
		addClientPanel.add(passwordLabel);
		
		JButton ajouterUtilisateur = new JButton("ajouter adherent");
		ajouterUtilisateur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = idField.getText() , nom = nomField.getText() , prenom = prenomField.getText() , email = emailField.getText() , password = passwordField.getText() ;
				
				if (validateInputUtilisateur(id ,email ,nom ,prenom ,password)) {
					ajoutUtilisateur(nom , prenom , email , password ) ;
				}
				else {
					JOptionPane.showMessageDialog(null, "Remplis tous les champs et l'id doit etre numerique" , "Erreur" , JOptionPane.ERROR_MESSAGE );
				}
			}
		});
		ajouterUtilisateur.setForeground(Color.WHITE);
		ajouterUtilisateur.setBackground(Color.DARK_GRAY);
		ajouterUtilisateur.setIcon(new ImageIcon("C:\\Users\\PC\\eclipse-workspace\\essai 2/icons8_microsoft_admin_22px.png"));
		ajouterUtilisateur.setBounds(1138, 111, 184, 40);
		addClientPanel.add(ajouterUtilisateur);
		
		JButton btnModifierutilisateur = new JButton("modifier adherent");
		btnModifierutilisateur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String id = idField.getText() , nom = nomField.getText() , prenom = prenomField.getText() , email = emailField.getText() , password = passwordField.getText() ;
				try {
					if (id.isEmpty() || checkExistanceOf("adherent" , "id" ,id) == false) {
						JOptionPane.showMessageDialog(null, "Remplis le champs id et verifie que ce code existe " , "Erreur" , JOptionPane.ERROR_MESSAGE );
					}
					else {
						modifierAdherent(id ,nom , prenom , email , password  ) ;
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
		});
		btnModifierutilisateur.setForeground(Color.WHITE);
		btnModifierutilisateur.setBackground(Color.DARK_GRAY);
		btnModifierutilisateur.setIcon(new ImageIcon("C:\\Users\\PC\\eclipse-workspace\\essai 2/icons8_microsoft_admin_22px.png"));
		btnModifierutilisateur.setBounds(1138, 60, 184, 40);
		addClientPanel.add(btnModifierutilisateur);
		
		JButton btnSupprimerUtilisateur = new JButton("supprimer adherent");
		btnSupprimerUtilisateur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String id = idField.getText() ;
				try {
					if (id.isEmpty() || checkExistanceOf("adherent" , "id" ,id) == false) {
						JOptionPane.showMessageDialog(null, "Remplis le champs id et verifie que ce code existe " , "Erreur" , JOptionPane.ERROR_MESSAGE );
					}
					else {
						supprimerUtilisateur(id);
					}
				} catch (HeadlessException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnSupprimerUtilisateur.setBackground(Color.DARK_GRAY);
		btnSupprimerUtilisateur.setForeground(Color.WHITE);
		btnSupprimerUtilisateur.setIcon(new ImageIcon("C:\\Users\\PC\\eclipse-workspace\\essai 2/icons8_microsoft_admin_22px.png"));
		btnSupprimerUtilisateur.setBounds(1138, 9, 184, 40);
		addClientPanel.add(btnSupprimerUtilisateur);
		
		JLabel lblNewLabel = new JLabel("gestion d' adherents:");
		lblNewLabel.setIcon(new ImageIcon("C:\\Users\\PC\\eclipse-workspace\\essai 2\\icons8_add_user_group_man_man_50px_1.png"));
		lblNewLabel.setFont(new Font("SansSerif", lblNewLabel.getFont().getStyle() & ~Font.BOLD, 25));
		lblNewLabel.setBounds(37, 30, 317, 52);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_2 = new JLabel("gestion des documents :");
		lblNewLabel_2.setIcon(new ImageIcon("C:\\Users\\PC\\eclipse-workspace\\essai 2\\icons8_task_50px.png"));
		lblNewLabel_2.setFont(new Font("SansSerif", lblNewLabel_2.getFont().getStyle() & ~Font.BOLD, 25));
		lblNewLabel_2.setBounds(37, 315, 390, 52);
		contentPane.add(lblNewLabel_2);
		
		JPanel addFilmPanel = new JPanel();
		addFilmPanel.setLayout(null);
		addFilmPanel.setBackground(Color.LIGHT_GRAY);
		addFilmPanel.setBounds(37, 378, 1332, 162);
		contentPane.add(addFilmPanel);
		
		JLabel lblNewLabel_1_1 = new JLabel("code");
		lblNewLabel_1_1.setBounds(78, 35, 51, 14);
		addFilmPanel.add(lblNewLabel_1_1);
		
		codeField = new JTextField();
		codeField.setColumns(10);
		codeField.setBounds(51, 77, 86, 20);
		addFilmPanel.add(codeField);
		
		JLabel lblTitre = new JLabel("titre");
		lblTitre.setBounds(239, 35, 46, 14);
		addFilmPanel.add(lblTitre);
		
		titreField = new JTextField();
		titreField.setColumns(10);
		titreField.setBounds(147, 77, 199, 20);
		addFilmPanel.add(titreField);
		
		JLabel lblCategorie = new JLabel("categorie");
		lblCategorie.setBounds(399, 35, 58, 14);
		addFilmPanel.add(lblCategorie);
		
		categorieField = new JTextField();
		categorieField.setColumns(10);
		categorieField.setBounds(356, 77, 169, 20);
		addFilmPanel.add(categorieField);
		
		imageLabel = new JLabel("VOTRE \r\nIMAGE \r\nICI \r\n");
		imageLabel.setBounds(828, 23, 116, 129);
		addFilmPanel.add(imageLabel);
		
		JLabel rangeeLabel = new JLabel("num rangee :");
		rangeeLabel.setBounds(559, 35, 76, 14);
		addFilmPanel.add(rangeeLabel);
		
		rangeeField = new JTextField();
		rangeeField.setBounds(544, 77, 105, 20);
		addFilmPanel.add(rangeeField);
		rangeeField.setColumns(10);
		
		JLabel reserveLabel = new JLabel("reserve :");
		reserveLabel.setBounds(711, 35, 51, 14);
		addFilmPanel.add(reserveLabel);
		
		reserveField = new JTextField();
		reserveField.setBounds(678, 77, 116, 20);
		addFilmPanel.add(reserveField);
		reserveField.setColumns(10);
		
		JButton SelectImgBtn = new JButton("parcourir");
		SelectImgBtn.setForeground(Color.WHITE);
		SelectImgBtn.setBackground(Color.DARK_GRAY);
		SelectImgBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser browseImage = new JFileChooser() ;
				
				//filter image extensions 
				FileNameExtensionFilter fnef = new FileNameExtensionFilter("IMAGES", "jpg" , "png" , "jpeg") ;
				browseImage.addChoosableFileFilter(fnef); 
				
				int showOpenDialogue = browseImage.showOpenDialog(null) ;
				if (showOpenDialogue == JFileChooser.APPROVE_OPTION) {
					File imageSelected = browseImage.getSelectedFile() ;
					String selectedImagePath = imageSelected.getAbsolutePath() ;
					s = selectedImagePath ;
					JOptionPane.showMessageDialog(null, selectedImagePath) ;
					
					//Display image on JLabel 
					ImageIcon img = new ImageIcon(selectedImagePath) ;
					Image im = img.getImage() ;
					Image myImg = im.getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH) ;
					ImageIcon i = new ImageIcon(myImg) ;
					imageLabel.setIcon(i) ; 
				}
			}
		});
		SelectImgBtn.setBounds(954, 76, 89, 23);
		addFilmPanel.add(SelectImgBtn);
		
		JButton ajouterFilmBtn = new JButton("ajouter document");
		ajouterFilmBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String code = codeField.getText() , titre = titreField.getText() , categorie = categorieField.getText() , rangee = rangeeField.getText() , reserve = reserveField.getText()  ;
				InputStream img;
				try {
					img = new FileInputStream(new File(s) );
							
					if (checkEmptyDocument(code ,titre ,categorie,rangee,reserve)){
						JOptionPane.showMessageDialog(null, "Remplis le champs id et verifie que le code est numerique " , "Erreur" , JOptionPane.ERROR_MESSAGE );
					}
					else {
						ajoutDocument( titre , categorie , rangee , reserve , img ) ;
	 				}
				} catch (IOException | HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		ajouterFilmBtn.setForeground(Color.WHITE);
		ajouterFilmBtn.setBackground(Color.DARK_GRAY);
		ajouterFilmBtn.setIcon(new ImageIcon("C:\\Users\\PC\\eclipse-workspace\\essai 2/icons8_microsoft_admin_22px.png"));
		ajouterFilmBtn.setBounds(1136, 112, 186, 40);
		addFilmPanel.add(ajouterFilmBtn);
		
		/*
		JButton modifierFilmBtn = new JButton("modifier document");
		modifierFilmBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String code = codeField.getText() , titre = titreField.getText() , categorie = categorieField.getText() , rangee = rangeeField.getText() , reserve = reserveField.getText()  ;
				InputStream img;

				
				try {
					if (checkExistanceOf("document", "code", code)) {
						img = new FileInputStream(new File(s) );
						
						if (checkEmptyDocument(code ,titre ,categorie,rangee,reserve)){
							JOptionPane.showMessageDialog(null, "Remplis le champs id et verifie que le code est numerique " , "Erreur" , JOptionPane.ERROR_MESSAGE );
						}
						else {
							modifierDocument(code, titre, categorie,rangee , reserve) ;
			 			}			
					}
					else {
						JOptionPane.showMessageDialog(null, "ce code n'existe pas !" , "Erreur" , JOptionPane.ERROR_MESSAGE );
					}
			} catch (HeadlessException | SQLException | IOException e1 ) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			}
		});
		modifierFilmBtn.setForeground(Color.WHITE);
		modifierFilmBtn.setBackground(Color.DARK_GRAY);
		modifierFilmBtn.setIcon(new ImageIcon("C:\\Users\\PC\\eclipse-workspace\\essai 2/icons8_microsoft_admin_22px.png"));
		modifierFilmBtn.setBounds(1156, 60, 166, 40);
		addFilmPanel.add(modifierFilmBtn);
		*/
		
		
		JButton supprimerFilmBtn = new JButton("supprimer document");
		supprimerFilmBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String code = codeField.getText() ;
				
				try {
					if (code.isEmpty() || checkExistanceOf("document" , "code" ,code) == false) {
						JOptionPane.showMessageDialog(null, "Remplis le champs code et verifie que ce code existe " , "Erreur" , JOptionPane.ERROR_MESSAGE );
					}
					else {
						supprimerDocument(code);
					}
				} catch (HeadlessException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		supprimerFilmBtn.setForeground(Color.WHITE);
		supprimerFilmBtn.setBackground(Color.DARK_GRAY);
		supprimerFilmBtn.setIcon(new ImageIcon("C:\\Users\\PC\\eclipse-workspace\\essai 2/icons8_microsoft_admin_22px.png"));
		supprimerFilmBtn.setBounds(1136, 11, 186, 40);
		addFilmPanel.add(supprimerFilmBtn);
		
		JButton btnModifierDocument = new JButton("modifier document");
		btnModifierDocument.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String code = codeField.getText() , titre = titreField.getText() , categorie = categorieField.getText() , 
						rangee = rangeeField.getText() , reserve = reserveField.getText()  ;
 
				modifierDocument(code ,titre ,categorie ,rangee ,reserve ) ;
				
			}
		});
		btnModifierDocument.setIcon(new ImageIcon("C:\\Users\\PC\\eclipse-workspace\\essai 2/icons8_microsoft_admin_22px.png"));
		btnModifierDocument.setForeground(Color.WHITE);
		btnModifierDocument.setBackground(Color.DARK_GRAY);
		btnModifierDocument.setBounds(1136, 62, 186, 40);
		addFilmPanel.add(btnModifierDocument);
		
		
		
		JButton showAllUsers = new JButton("afficher tous les adherents");
		showAllUsers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				dispose() ;
				try {
					new listeAdherents().setVisible(true);
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
				
			}
		});
		showAllUsers.setBackground(Color.LIGHT_GRAY);
		showAllUsers.setBounds(1108, 279, 193, 23);
		contentPane.add(showAllUsers);
		
		JButton showAllMedia = new JButton("afficher tous les documents");
		showAllMedia.addActionListener(new ActionListener() {
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
		showAllMedia.setBackground(Color.LIGHT_GRAY);
		showAllMedia.setBounds(1108, 551, 193, 23);
		contentPane.add(showAllMedia);
		
		
	}
}
