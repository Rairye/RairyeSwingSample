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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@SuppressWarnings("serial")
public class EditDriverInfoScreen extends JFrame implements WindowListener{
	
	Manager manager;
	String licenseNumber;
	Driver selected;
	JTextField firstNameField;
	JTextField lastNameField;
    JTextField addressField;
    JTextField dobField;
    JComboBox<?> validityBox;
    JTextField expirationField;
    Method replaceRow;
    Method deleteRow;
    
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public EditDriverInfoScreen(String licenseNumber) {
		this.licenseNumber = licenseNumber;
		try {
		this.manager = Manager.getInstance();
		replaceRow = MainScreen.class.getMethod("replaceRow", Driver.class);
		deleteRow = MainScreen.class.getMethod("deleteRow");
		selected = manager.search(licenseNumber); 
		} catch (Exception e) {
			displayError("There was an error loading the driver records.", "Loading error");
		}
		this.setSize(325,350);
		this.setResizable(false);
		this.setTitle("Editing " + licenseNumber);
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
	
		JPanel firstNamePanel = new JPanel();
		JLabel firstNameLabel = new JLabel("First name");
		firstNameField = new JTextField(selected.getFirstName(), 0);
		firstNameField.setPreferredSize(new Dimension(200, 25));
		((AbstractDocument) firstNameField.getDocument()).setDocumentFilter(new NameFilter());
		firstNamePanel.add(firstNameLabel);
		firstNamePanel.add(firstNameField);
		
		JPanel lastNamePanel = new JPanel();
		JLabel lastNameLabel = new JLabel("Last name");
		lastNameField = new JTextField(selected.getLastName(), 0);
		lastNameField.setPreferredSize(new Dimension(200, 25));
		((AbstractDocument) lastNameField.getDocument()).setDocumentFilter(new NameFilter());
		lastNamePanel.add(lastNameLabel);
		lastNamePanel.add(lastNameField);
		
		JPanel addressPanel = new JPanel();
		JLabel addressLabel = new JLabel("Address");
		addressField = new JTextField(selected.getAddress(), 0);
		addressField.setPreferredSize(new Dimension(200, 25));
		addressPanel.add(addressLabel);
		addressPanel.add(addressField);
		
		JPanel dobPanel = new JPanel();
		JLabel dobLabel = new JLabel("Date of birth (YYYY-MM-DD)");
		dobField = new JTextField(selected.getDOB(), 0);
		dobField.setPreferredSize(new Dimension(100, 25));
		((AbstractDocument) dobField.getDocument()).setDocumentFilter(new DateFilter());
		dobPanel.add(dobLabel);
		dobPanel.add(dobField);
		
		JPanel expirationPanel = new JPanel();
		JLabel expirationLabel = new JLabel("Expiration date (YYYY-MM-DD)");
		expirationField = new JTextField(selected.getExpiration(), 0);
		expirationField.setPreferredSize(new Dimension(100, 25));
		((AbstractDocument) expirationField.getDocument()).setDocumentFilter(new DateFilter());
		expirationPanel.add(expirationLabel);
		expirationPanel.add(expirationField);
		
		JPanel validityPanel = new JPanel();
		JLabel validityLabel = new JLabel("Validity");
		validityBox = new JComboBox(new String []{"License is valid.", "License is expired.", "License is suspended."});
		validityBox.setSelectedItem(selected.getValidity());
		validityPanel.add(validityLabel);
		validityPanel.add(validityBox);
		
		JPanel buttonPanel = new JPanel();
		JButton cancelButton = new JButton("Cancel");
		JButton updateButton = new JButton("Update");
		updateButton.addActionListener((ActionEvent event) -> {
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
		JButton deleteButton = new JButton("Delete driver");
		deleteButton.addActionListener((ActionEvent event) -> {
			int result = JOptionPane.showOptionDialog(this
	    		    , "Are you sure you would like to delete this driver?",
	    		    "Confirmation",
	    		    JOptionPane.OK_CANCEL_OPTION,
	    		    JOptionPane.WARNING_MESSAGE,
	    		    null, new String[] {"Yes", "No"}, "default"
					);
			if (result == 0) {
				manager.deleteDriver(licenseNumber);
				try {
					manager.saveRecords();
					}
				    catch(Exception e) {
				    	displayError("Failed to save edited driver records.", "Records not saved");
				    }
				try {
				deleteRow.invoke(MainScreen.getReference());
				} catch (Exception e) {
					displayError("There was an error deleting the row from the table.", "Table update error");
				}
				JOptionPane.showOptionDialog(this,
						"Driver successfully deleted.",
		    		    "Deleted",
		    		    JOptionPane.DEFAULT_OPTION,
		    		    JOptionPane.WARNING_MESSAGE,
		    		    null, new String[] {"OK"}, "default"
						);
				enableMainScreen();
				this.dispose();
			} else {
			 return;
			}		
		});
		
		buttonPanel.add(cancelButton);
		buttonPanel.add(updateButton);
		buttonPanel.add(deleteButton);
	
		middlePanel.add(firstNamePanel, JPanel.LEFT_ALIGNMENT);
		middlePanel.add(lastNamePanel, JPanel.LEFT_ALIGNMENT);
		middlePanel.add(addressPanel, JPanel.LEFT_ALIGNMENT);
		middlePanel.add(dobPanel, JPanel.LEFT_ALIGNMENT);
		middlePanel.add(expirationPanel, JPanel.LEFT_ALIGNMENT);
		middlePanel.add(validityPanel, JPanel.LEFT_ALIGNMENT);
		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(middlePanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
		this.add(mainPanel);	
	}
	
	private void submit() {
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
			
			selected.setFirstName(firstNameField.getText());
			selected.setLastName(lastNameField.getText());
			selected.setAddress(addressField.getText());
			selected.setDOB(LocalDate.parse(dobField.getText()));
			selected.setExpiration(LocalDate.parse(expirationField.getText()));
			selected.setValidity((String)validityBox.getSelectedItem());
			manager.records.replace(this.licenseNumber, selected);
			
			try {
			replaceRow.invoke(MainScreen.getReference(), manager.search(selected.getLicenseNumber()));
			} catch (Exception e) {
				displayError("There was an error updating the edited row in the table.", "Table update error");
			}
			
			int result = JOptionPane.showOptionDialog(this
	    		    , "Driver information successfully updated.",
	    		    "Successful",
	    		    JOptionPane.DEFAULT_OPTION,
	    		    JOptionPane.INFORMATION_MESSAGE,
	    		    null, new String[] {"Ok"}, "default"
					);
			if (result == 0) {
				try {
				manager.saveRecords();
				}
			    catch(Exception e) {
			    	displayError("Failed to save edited driver records.", "Records not saved");
			    }
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
	
	private void enableMainScreen() {	
		for (Window window : Window.getWindows()) {
			if (window.getName()=="MainScreen") {
				window.setEnabled(true);
				window.setAlwaysOnTop(true);
				window.setFocusable(true);
			}
		}	
	}
	
	@Override
	public void windowActivated(WindowEvent e) {
		firstNameField.requestFocus();
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