package licenseManager;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.AbstractDocument;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import java.util.ArrayList;

@SuppressWarnings("serial")
@SpringBootApplication
public class MainScreen extends JFrame{

Manager manager;
Driver selected;
static ArrayList<String> renewalNotices = new ArrayList<>();
JTable driverTable;
DefaultTableModel model;
String[][] tableData;
int selectedRow;
public static MainScreen ex;
	
	 public MainScreen() throws ClassNotFoundException, IOException{ 
		 try {
			 manager = Manager.getInstance();
			 renewalNotices = manager.checkRecords();
			 if (!renewalNotices.isEmpty()) {
				 manager.saveRecords();
			 }
			 } catch (ClassNotFoundException | IOException e) {
				 Toolkit.getDefaultToolkit().beep(); 
				 JOptionPane.showMessageDialog(this, 
					"There was an error loading the driver records.",
		            "Loading error",
		        	JOptionPane.ERROR_MESSAGE);			
			 }
	        initUI();
	    }
	 
	 private void initUI()  {
		    tableData = manager.loadTable();
		    this.setName("MainScreen");
		    setTitle("Driver Records");
	        setSize(1100, 500);
	        setLocationRelativeTo(null);
	        setDefaultCloseOperation(EXIT_ON_CLOSE);
		 	this.setResizable(false);
		 	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 	
		 	JMenuBar menuBar = new JMenuBar();
		 	JMenu menu = new JMenu("Menu");
		 	menu.setMnemonic(KeyEvent.VK_M);
		 	menu.getAccessibleContext().setAccessibleDescription("Menu");
		 	JMenuItem addItem = new JMenuItem("Add new driver", KeyEvent.VK_A);
		 	addItem.addActionListener((ActionEvent event) -> {new AddNewDriverScreen().setVisible(true);});
		 	addItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		 	JMenuItem exitItem = new JMenuItem("Exit", KeyEvent.VK_X);
		 	exitItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		 	exitItem.addActionListener((ActionEvent event) -> {System.exit(0);});
            menu.add(addItem);
            menu.add(exitItem);	 
		 	menuBar.add(menu);
		 	this.setJMenuBar(menuBar);
		 	
		 	JPanel panel = new JPanel(new BorderLayout());
		 	panel.setAlignmentY(JPanel.LEFT_ALIGNMENT);
		 
		 	JLabel licenseNumberLabel = new JLabel("License number");	
		 	JTextField searchField = new JTextField("", 0);
		 	searchField.setPreferredSize(new Dimension(100, 25));
		 	((AbstractDocument) searchField.getDocument()).setDocumentFilter(new LicenseNumberFilter());
		 	searchField.setToolTipText("Input a valid license number (ten digits of capital letters and numbers).");
		 	
		 	JButton searchButton = new JButton("Search");      
	        searchButton.setToolTipText("Searches driver records.");
	        searchButton.addActionListener((ActionEvent event) -> {
	        	try {selected = manager.search(searchField.getText());
	        	selectedRow = findIndex(searchField.getText());
	        	openEditScreen(searchField.getText());
	        } catch (InvalidLicenseNumberException e) {
	        	Toolkit.getDefaultToolkit().beep();
	        	JOptionPane.showMessageDialog(this, 
	        			"The license number should be 10 digits long.",
	        		    "Invalid license number",
	        		    JOptionPane.ERROR_MESSAGE); 
	        	} catch (DriverNotFoundException e) {	
	        		Toolkit.getDefaultToolkit().beep();
		        	JOptionPane.showMessageDialog(this, 
		        			"This license number is not listed in the records.",
		        		    "Driver not found",
		        		    JOptionPane.ERROR_MESSAGE);}
	        	});
	        
	        JButton addButton = new JButton("Add new driver");
	        addButton.addActionListener((ActionEvent event) -> {new AddNewDriverScreen().setVisible(true);});
	        addButton.setToolTipText("Adds a new driver to the records.");
	        
	        String[] labels = {"License Number", "First Name", "Last Name", "Date of Birth", 
	        		"Address", "Validity", "Expiration"};
	        driverTable = new JTable(new DefaultTableModel(tableData, labels)) {
	        	public boolean isCellEditable(int row, int column) {                
	                return false; 
	        	};
	        };
	        driverTable.getColumnModel().getColumn(0).setPreferredWidth(50);
	        driverTable.getColumnModel().getColumn(1).setPreferredWidth(65);
	        driverTable.getColumnModel().getColumn(2).setPreferredWidth(65);
	        driverTable.getColumnModel().getColumn(3).setPreferredWidth(45);
	        driverTable.getColumnModel().getColumn(4).setPreferredWidth(245);
	        driverTable.getColumnModel().getColumn(5).setPreferredWidth(80);
	        driverTable.getColumnModel().getColumn(6).setPreferredWidth(50);
	       
	        driverTable.addMouseListener(new MouseAdapter(){
	        	public void mousePressed(MouseEvent event) {
	        		JTable table =(JTable) event.getSource();
	                Point point = event.getPoint();
	                selectedRow = table.rowAtPoint(point);
	                if (event.getClickCount() == 2 && selectedRow != -1) {
		        		openEditScreen(driverTable.getValueAt(driverTable.getSelectedRow(), 0).toString());
	                }    
	        	}
	        });
	        model = (DefaultTableModel) driverTable.getModel();
	        
	        JScrollPane scrollPane = new JScrollPane(driverTable);
	        scrollPane.setAlignmentY(CENTER_ALIGNMENT);
	        scrollPane.setBackground(Color.GRAY);
	        JPanel searchPanel = new JPanel();
	        JPanel addPanel = new JPanel();
	        searchPanel.add(licenseNumberLabel);
	        searchPanel.add(searchField);
	        searchPanel.add(searchButton, BorderLayout.EAST);
	        panel.add(searchPanel, BorderLayout.WEST);
	        addPanel.add(addButton);
	        panel.add(addPanel, BorderLayout.LINE_END);  
	        this.add(panel, BorderLayout.NORTH);
	        this.add(scrollPane, BorderLayout.CENTER);
	    }

  public static MainScreen getReference() {
	   	return ex;
	 }

  private void openEditScreen(String licenseNumber) {
	 new EditDriverInfoScreen(licenseNumber).setVisible(true);
	 }
	 
  public int findIndex(String licenseNumber) {
		int result = 0;
		for (int i = 0; i < driverTable.getRowCount(); i++) {
		if (driverTable.getValueAt(i, 0).equals(licenseNumber)) {
			result = i;
			break;
		    }
		}
		return result;
		}

  public void addNewRow(Driver driver) {
			 model.addRow(new Object[] {driver.getLicenseNumber(), driver.getFirstName(), 
			driver.getLastName(), driver.getDOB(), driver.getAddress(), driver.getValidity(), 
			driver.getExpiration()});
   }

  public void replaceRow(Driver driver) {
			 model.setValueAt(driver.getFirstName(), selectedRow, 1);
			 model.setValueAt(driver.getLastName(), selectedRow, 2);
			 model.setValueAt(driver.getDOB(), selectedRow, 3);
			 model.setValueAt(driver.getAddress(), selectedRow, 4);
			 model.setValueAt(driver.getValidity(), selectedRow, 5);
			 model.setValueAt(driver.getExpiration(), selectedRow, 6);
	}

	public void deleteRow() {
		model.removeRow(selectedRow);
	}
 
	public static void main(String[] args) {
		 ConfigurableApplicationContext ctx = new SpringApplicationBuilder(MainScreen.class)
	                .headless(false).run(args);
	        EventQueue.invokeLater(() -> {
	            ex = ctx.getBean(MainScreen.class);
	            ex.setVisible(true);           
	        });        
	        if (!renewalNotices.isEmpty()) {
	        new RenewalNoticesList(renewalNotices).setVisible(true);
	        }
	}
}
