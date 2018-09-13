package licenseManager;

import java.awt.Toolkit;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class LicenseNumberFilter extends DocumentFilter{
	
	public void insertString(DocumentFilter.FilterBypass fb, int offset, String text,
		      AttributeSet attr) throws BadLocationException {
		
			if ((fb.getDocument().getLength() < 10 && text.matches("[a-z|A-Z|0-9]")) || text.matches("")) {
				fb.insertString(offset, text.toUpperCase(), attr);
			} else {
				Toolkit.getDefaultToolkit().beep();
			}
	}
	
	public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text,
		      AttributeSet attrs) throws BadLocationException {
		if ((fb.getDocument().getLength() < 10 && text.matches("[a-z|A-Z|0-9]")) || text.matches("")){
		    fb.replace(offset, length, text.toUpperCase(), attrs);
		} else {
			Toolkit.getDefaultToolkit().beep();
		}
		  }
}