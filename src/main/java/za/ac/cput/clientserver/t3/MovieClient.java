
package za.ac.cput.clientserver.t3;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import za.ac.cput.clientserver.t3.Movie;


/**
 *
 * @author 219404275 Zukhanye Anele Mene
 */

public class MovieClient  extends JFrame implements ActionListener {
    private static ObjectInputStream in;
    private static ObjectOutputStream out;
    private static Socket socket;

    private JPanel panelNorth;
    private JPanel panelCenter, panelCenter1, panelCenter2, inputPanel;
    private JPanel panelSouth;

    private static JTextField txtTitleField;
    private static JTextField txtDirectorField;
    private static JTextArea recordsTextArea;
    
    private JLabel lblHeading;
    private JLabel lblTitle;
    private JLabel lblDirector;           
    private JLabel lblGenre;
    private static JComboBox cboGenre;
    
    private JButton btnAdd, btnRetrieve, btnExit, btnSearch;
    private Font ft1, ft2, ft3;
       
    public MovieClient() {
        super("Movie Inventory App");
        
        panelNorth = new JPanel();
        panelCenter = new JPanel();
        panelCenter1 = new JPanel();
        panelCenter2 = new JPanel();
        panelSouth = new JPanel();
        inputPanel = new JPanel();
        lblHeading = new JLabel("Movie Inventory");

        lblGenre = new JLabel("Genre: ");
        cboGenre = new JComboBox<>(new String[]{"Select", "Comedy", "Action", "Horror", "Romance", "Sci-Fi", "Thriller", "Drama"});      
        btnAdd = new JButton("Add");
        btnRetrieve = new JButton("Retrieve");
        btnSearch = new JButton("Find");
        btnExit = new JButton("Exit");
        
        ft1 = new Font("Arial", Font.BOLD, 32);
        ft2 = new Font("Arial", Font.PLAIN, 22);
        ft3 = new Font("Arial", Font.PLAIN, 24);
        
        try {
           
            socket = new Socket (  "127.0.0.1", 8001 );
            
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            
            in = new ObjectInputStream(socket.getInputStream());
            
            
          } catch (IOException ex) {
            System.out.println("IOException: " + ex.getMessage());
        }
    }
    
    public void setGUI() {
        panelNorth.setLayout(new FlowLayout());
        panelCenter.setLayout(new GridLayout(3, 1));
        panelSouth.setLayout(new GridLayout(1, 4));
        panelCenter1.setLayout(new FlowLayout());
        panelCenter2.setLayout(new FlowLayout());
        
        panelNorth.add(lblHeading);
        lblHeading.setFont(ft1);
        lblHeading.setForeground(Color.yellow);
        panelNorth.setBackground(new Color(0, 106, 255));
        
        lblGenre.setFont(ft2);
        lblGenre.setHorizontalAlignment(JLabel.RIGHT);
        cboGenre.setFont(ft2);
        recordsTextArea = new JTextArea(10,40);
        recordsTextArea.setEditable(false);
        recordsTextArea.setFont(ft2);
        panelCenter1.add(lblGenre);
        panelCenter1.add(cboGenre);
        
        inputPanel.setLayout(new GridLayout(2, 2));        
        lblTitle = new JLabel("Title:");
        lblTitle.setFont(ft2);
        inputPanel.add(lblTitle);
        txtTitleField = new JTextField();
        txtTitleField.setFont(ft2);
        inputPanel.add(txtTitleField);
        lblDirector = new JLabel("Director:");
        lblDirector.setFont(ft2);
        inputPanel.add(lblDirector);
        txtDirectorField = new JTextField();
        txtDirectorField.setFont(ft2);
        inputPanel.add(txtDirectorField);             
        
        panelCenter2.add(new JScrollPane(recordsTextArea), BorderLayout.SOUTH);       
        panelCenter2.setBackground(new Color(36, 145, 255));
        
        panelCenter.add(panelCenter1);
        panelCenter.add(inputPanel);
        panelCenter.add(panelCenter2);
        
        btnAdd.setFont(ft3);
        btnRetrieve.setFont(ft3);
        btnSearch.setFont(ft3);
        btnExit.setFont(ft3);
        panelSouth.add(btnAdd);
        panelSouth.add(btnRetrieve);
        panelSouth.add(btnSearch);
        panelSouth.add(btnExit);
        
        this.add(panelNorth, BorderLayout.NORTH);
        this.add(panelCenter, BorderLayout.CENTER);
        this.add(panelSouth, BorderLayout.SOUTH);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        btnAdd.addActionListener(this);
        btnRetrieve.addActionListener(this);
        btnSearch.addActionListener(this);
        btnExit.addActionListener(this);
        
        this.setSize(500, 400);
        //this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
    
     private static void addMovieRecord() {
         
         
        try {
            
           
            String title = txtTitleField.getText();
            String director = txtDirectorField.getText();
            String genre = cboGenre.getSelectedItem().toString();
            
             Movie movies = new Movie(title,director, genre);
             
            out.writeObject(movies);
            out.flush();
            //Test
            System.out.println(movies);

          
            txtDirectorField.setText("");
            txtTitleField.setText("");
            cboGenre.requestFocus();
        } 
        catch (IOException ex) {
            System.out.println("IOException in addMovieRecord" + ex.getMessage());
        }
   }
    
     
     private static void search(){
        try {
            
            String searchTerm = cboGenre.getSelectedItem().toString();
            out.writeObject(searchTerm);
            out.flush();
            
            ArrayList<Movie> recievedObject = (ArrayList)in.readObject();
            System.out.println(recievedObject);
          
            displayMovieRecords(recievedObject);
            
        } catch (IOException ex) {
            System.out.println("IOException in searching for movie" + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println("Class Not Found Exception in searching for movie" + ex.getMessage());
        }
     }

   private static void retrieveMovieRecords() {

        try {
            out.writeObject("retrieve");
            out.flush();
            
            Object recievedObject = in.readObject();
            if (recievedObject instanceof ArrayList ){
            ArrayList<Movie> movieRecords = (ArrayList<Movie>) recievedObject;
            
            
            displayMovieRecords(movieRecords);
            }
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public static void displayMovieRecords( ArrayList<Movie> movieRecords) {
       
        recordsTextArea.setText("Movie Results: \n \n");
        
        
        for (Movie movie : movieRecords) {
            recordsTextArea.append("Movie Title: " + movie.getMovieTitle() + "        " + "Movie Director: " + movie.getMovieDirector() + "        " + "Movie Genre: " + movie.getMovieGenre() + "\n");
            
        }
    }
   
      private void closeConnection() {

        try {
            
            out.writeObject("exit");
            out.flush();
            
            out.close();
            in.close();
            socket.close();
            System.exit(0);
        } catch (IOException ex) {
            System.out.println("IOException in closeConnection" + ex.getMessage());
        }
    } 
    

    @Override
    public void actionPerformed(ActionEvent e)
   {
        if(e.getSource()==btnAdd){
        
            
            addMovieRecord();
            
       JOptionPane.showMessageDialog(null, "Successfull, Movie added ");
      
    }
         else if (e.getSource()==btnRetrieve){
              retrieveMovieRecords();
              }
         else if(e.getSource()== btnExit) {
            
                closeConnection();
        }
         else  if(e.getSource()== btnSearch) {
                
                search();
                
        }
        
   }
  
 
    public static void main(String[] args) {
            
        MovieClient client = new MovieClient();
        client.setGUI();
    }

}