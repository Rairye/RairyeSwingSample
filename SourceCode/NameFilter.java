package licenseManager;

import java.awt.Toolkit;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class NameFilter extends DocumentFilter{
	
	public void insertString(DocumentFilter.FilterBypass fb, int offset, String text,
		      AttributeSet attr) throws BadLocationException {
		
			if ((text.matches("[a-z|A-Z|-]") || text.matches(" ")) || text.matches("")) {
				fb.insertString(offset, text, attr);
			} else {
				Toolkit.getDefaultToolkit().beep();
			}
	}
	
	public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text,
		      AttributeSet attrs) throws BadLocationException {
		if ((text.matches("[a-z|A-Z|-]") || text.matches(" ")) || text.matches("")){
		    fb.replace(offset, length, text, attrs);
		} else {
			Toolkit.getDefaultToolkit().beep();
		}
		  }
}