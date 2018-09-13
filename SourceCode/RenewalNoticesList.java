package licenseManager;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class RenewalNoticesList extends JFrame implements WindowListener{
	
	StringBuilder licensesUpForRenewal = new StringBuilder(); 
	
	public RenewalNoticesList(ArrayList<String> upForRenewal) {	
		this.setSize(400,300);
		this.setResizable(false);
		this.setTitle("Licenses up for renewal");
		this.setLocationRelativeTo(null);
		this.addWindowListener(this);
		this.setAlwaysOnTop(true);
		
		int items = 1;
		for (String str : upForRenewal) {
			licensesUpForRenewal.append("Item " + items + ":");
			licensesUpForRenewal.append(str);
			items++;
		}
		
		for (Window window : Window.getWindows()) {
			if (window.getName()=="MainScreen") {
				window.setEnabled(false);
				window.setAlwaysOnTop(false);
			}
		}
		
		JPanel panel = new JPanel(new BorderLayout());
		JTextArea title = new JTextArea();
		title.setText("The following " + (upForRenewal.size() == 1 ? "license needs" : "licenses need")  + " to be renewed. This information has already been forwarded to the mail room.");
		title.setBorder(BorderFactory.createCompoundBorder(
				title.getBorder(),
				BorderFactory.createEmptyBorder(5, 17, 5, 17)));
		title.setLineWrap(true);
		title.setEditable(false);
		title.setWrapStyleWord(true);
		panel.add(title);
			
		JTextArea driverText = new JTextArea();
		driverText.setBorder(BorderFactory.createCompoundBorder(
				title.getBorder(),
				BorderFactory.createEmptyBorder(2, 2, 2, 2)));
		driverText.setLineWrap(true);
		driverText.setEditable(false);
		driverText.setWrapStyleWord(true);
		driverText.setVisible(true);;
		driverText.setText(licensesUpForRenewal.toString());
		JScrollPane drivers = new JScrollPane(driverText);
	
		JPanel closeButtonPanel = new JPanel(new BorderLayout());
		JButton closeButton = new JButton("Close");
		closeButton.addActionListener((ActionEvent event) -> {
			enableMainScreen();
			this.dispose();
		});
		closeButtonPanel.add(closeButton);
		
	 	this.add(panel, BorderLayout.NORTH);
	 	this.add(drivers, BorderLayout.CENTER);
	 	this.add(closeButtonPanel, BorderLayout.SOUTH);
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
	public void windowActivated(WindowEvent e) {}

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
