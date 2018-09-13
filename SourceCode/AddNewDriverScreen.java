package licenseManager;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.Method;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@SuppressWarnings("serial")
public class AddNewDriverScreen extends JFrame implements WindowListener{
	
	Manager manager;
	JTextField licenseNumberField;
	JTextField firstNameField;
	JTextField lastNameField;
    JTextField addressField;
    JTextField dobField;
    JTextField expirationField;
    Method addNewRow;
    
	public AddNewDriverScreen() {
		
		try {
		this.manager = Manager.getInstance();
		addNewRow = MainScreen.class.getMethod("addNewRow", Driver.class);
		} catch (Exception e) {
			displayError("There was an error loading the driver records.", "Loading error");
		}
		this.setSize(300,350);
		this.setResizable(false);
		this.setTitle("Add new driver");
		this.setLocationRelativeTo(null);
		this.addWindowListener(this);
		
		for (Window window : Window.getWindows()) {
			if (window.getName()=="MainScreen") {
				window.setEnabled(false);
				window.setAlwaysOnTop(false);
			}
		}
			
		JPanel mainPanel = new JPanel(new BorderLayout());
			
		JPanel topPanel = new JPanel();
		JLabel topLabel = new JLabel("(All fields required)");
		topPanel.add(topLabel, JPanel.LEFT_ALIGNMENT);
		
		JPanel middlePanel = new JPanel();
	
		JPanel licenseNumberPanel = new JPanel();
		JLabel licenseNumberLabel = new JLabel("License number (10 digits)");
		licenseNumberField = new JTextField("", 0);
		licenseNumberField.setPreferredSize(new Dimension(100, 25));
		((AbstractDocument) licenseNumberField.getDocument()).setDocumentFilter(new LicenseNumberFilter());
		licenseNumberPanel.add(licenseNumberLabel);
		licenseNumberPanel.add(licenseNumberField);
		
		JPanel firstNamePanel = new JPanel();
		JLabel firstNameLabel = new JLabel("First name");
		firstNameField = new JTextField("", 0);
		firstNameField.setPreferredSize(new Dimension(200, 25));
		((AbstractDocument) firstNameField.getDocument()).setDocumentFilter(new NameFilter());
		firstNamePanel.add(firstNameLabel);
		firstNamePanel.add(firstNameField);
		
		JPanel lastNamePanel = new JPanel();
		JLabel lastNameLabel = new JLabel("Last name");
		lastNameField = new JTextField("", 0);
		lastNameField.setPreferredSize(new Dimension(200, 25));
		((AbstractDocument) lastNameField.getDocument()).setDocumentFilter(new NameFilter());
		lastNamePanel.add(lastNameLabel);
		lastNamePanel.add(lastNameField);
		
		JPanel addressPanel = new JPanel();
		JLabel addressLabel = new JLabel("Address");
		addressField = new JTextField("", 0);
		addressField.setPreferredSize(new Dimension(200, 25));
		addressPanel.add(addressLabel);
		addressPanel.add(addressField);
		
		JPanel dobPanel = new JPanel();
		JLabel dobLabel = new JLabel("Date of birth (YYYY-MM-DD)");
		dobField = new JTextField("", 0);
		dobField.setPreferredSize(new Dimension(100, 25));
		((AbstractDocument) dobField.getDocument()).setDocumentFilter(new DateFilter());
		dobPanel.add(dobLabel);
		dobPanel.add(dobField);
		
		JPanel expirationPanel = new JPanel();
		JLabel expirationLabel = new JLabel("Expiration date (YYYY-MM-DD)");
		expirationField = new JTextField("", 0);
		expirationField.setPreferredSize(new Dimension(100, 25));
		((AbstractDocument) expirationField.getDocument()).setDocumentFilter(new DateFilter());
		expirationPanel.add(expirationLabel);
		expirationPanel.add(expirationField);
		
		JPanel buttonPanel = new JPanel();
		JButton cancelButton = new JButton("Cancel");
		JButton addButton = new JButton("Add");
		addButton.addActionListener((ActionEvent event) -> {
			submit();
		});
		cancelButton.addActionListener((ActionEvent event) -> {
			int result = JOptionPane.showOptionDialog(this
	    		    , "Are you sure you would like to cancel?",
	    		    "Confirmation",
	    		    JOptionPane.OK_CANCEL_OPTION,
	    		    JOptionPane.WARNING_MESSAGE,
	    		    null, new String[] {"Yes", "No"}, "default"
					);
			if (result == 0) {
				enableMainScreen();
				this.dispose();
			} else {
			 return;
			}
					
		});
		buttonPanel.add(cancelButton);
		buttonPanel.add(addButton);
	
		middlePanel.add(licenseNumberPanel, JPanel.LEFT_ALIGNMENT);
		middlePanel.add(firstNamePanel, JPanel.LEFT_ALIGNMENT);
		middlePanel.add(lastNamePanel, JPanel.LEFT_ALIGNMENT);
		middlePanel.add(addressPanel, JPanel.LEFT_ALIGNMENT);
		middlePanel.add(dobPanel, JPanel.LEFT_ALIGNMENT);
		middlePanel.add(expirationPanel, JPanel.LEFT_ALIGNMENT);
		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(middlePanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.PAGE_END);
		this.add(mainPanel);	
	}
	
	private void submit() {
			if (licenseNumberField.getText().length() != 10) {
				displayError("License number must be 10 digits long.", "Invalid license number");
				return;
			}
			
			if (manager.driverExists(licenseNumberField.getText())) {
				displayError("This license number is currently in use.", "Invalid license number");
				return;
			} 
			
			if (firstNameField.getText().isEmpty()) {
				displayError("Please enter a first name.", "First name not entered");
				return;
			}
			
			if (lastNameField.getText().isEmpty()) {
				displayError("Please enter a last name.", "Last name not entered");
				return;
			}
			
			if (addressField.getText().isEmpty()) {
				displayError("Address not entered.", "Address not entered");
				return;
			}
			
			try {
				if (LocalDate.parse(dobField.getText()).isAfter(LocalDate.now())) {
					displayError("Date of birth cannot be a future date.", "Invalid DOB");
					return;	
				}
			} catch (DateTimeParseException e) {
				displayError("Invalid date of birth. Please specify using the YYYY-DD-MM format.", "Invalid DOB");
				return;	
			}
			
			try {
				if (LocalDate.parse(expirationField.getText()).isBefore(LocalDate.now())) {
					displayError("Expiration date cannot be a past date.", "Invalid expiration date");
					return;	
				}
			} catch (DateTimeParseException e) {
				displayError("Invalid expiration date. Please specify using the YYYY-DD-MM format.", "Invalid expiration date");
				return;	
			}
			
			manager.addDriver(licenseNumberField.getText(), firstNameField.getText(), 
			lastNameField.getText(), addressField.getText(), LocalDate.parse(dobField.getText()), 
			LocalDate.parse(expirationField.getText()));
			try {
			addNewRow.invoke(MainScreen.getReference(), manager.search(licenseNumberField.getText()));
			} catch(Exception e) {
			displayError("There was an error adding the new driver to the table.", "Table update error");
			}
			
			int result = JOptionPane.showOptionDialog(this
	    		    , "New driver added. Would you like to add another driver?",
	    		    "Successful",
	    		    JOptionPane.OK_CANCEL_OPTION,
	    		    JOptionPane.INFORMATION_MESSAGE,
	    		    null, new String[] {"Yes", "No"}, "default"
					);
			if (result == 0) {
				saveRecords();
				clearSelections();
			} else {
				saveRecords();
				enableMainScreen();
				this.dispose();
			}
		}
		
	private void displayError(String errorMessage, String errorType) {
		Toolkit.getDefaultToolkit().beep();
		JOptionPane.showMessageDialog(this,
				errorMessage,
    		    errorType,
    		    JOptionPane.ERROR_MESSAGE);
		        
	}
	
	public void saveRecords() {
		try {
			manager.saveRecords();
			} catch(Exception e) {
		    	displayError("Failed to save edited driver records.", "Records not saved");
		    }
	}
	
	private void enableMainScreen() {	
		for (Window window : Window.getWindows()) {
			if (window.getName()=="MainScreen") {
				window.setEnabled(true);
				window.setAlwaysOnTop(true);
				window.setFocusable(true);
			}
		}	
	}
	
	private void clearSelections() {
		licenseNumberField.setText("");
		firstNameField.setText("");
		lastNameField.setText("");
		addressField.setText("");
		dobField.setText("");
		expirationField.setText("");
		licenseNumberField.requestFocus();
	}

	@Override
	public void windowActivated(WindowEvent e) {
		licenseNumberField.requestFocus();
	}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e) {	
		enableMainScreen();
	}
	
	@Override
	public void windowDeactivated(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowOpened(WindowEvent e) {}
	
}