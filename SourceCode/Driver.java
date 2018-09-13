package licenseManager;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

enum Validity {VALID("License is valid."), 
	EXPIRED("License is expired."), SUSPENDED("License is suspended.");
	private String status;
	private Validity(String status) {
		this.status = status;
	}
	public String getValidity() {
		return this.status;
	}
}

@SuppressWarnings("serial")
class Driver implements Serializable, Comparable<Driver>{
	private final String licenseNumber;
	private String firstName;
	private String lastName;
	private String address;
	private LocalDate dob;
	private LocalDate expiration;
	private Validity validity;
	private boolean renewalNoticeSent;
	
	public Driver (String licenseNumber, String firstName, String lastName, String address, 
			LocalDate dob, LocalDate expiration){
		
		this.licenseNumber = licenseNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.dob = dob;
		this.expiration = expiration;
		this.validity = Validity.VALID;
		this.renewalNoticeSent = false;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public void setDOB(LocalDate dob) {
		this.dob = dob;
	}
	
	public void setExpiration(LocalDate expiration) {
		this.expiration = expiration;
	}
	
	public void setValidity(String validity) {
		switch (validity) {
		
		case "License is valid.":
		this.validity = Validity.VALID;
		break;
		
		case "License is expired.":
		this.validity = Validity.EXPIRED;	
		break;
		
		case "License is suspended.":
		this.validity = Validity.SUSPENDED;
		break;
		}	
	}
	
	public String getLicenseNumber() {
		return this.licenseNumber;
	}
	
	public String getFirstName() {
		return this.firstName;
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public String getDOB() {
		return this.dob.toString();
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public String getExpiration() {
		return this.expiration.toString();
	}
	
	public String getValidity() {
		return this.validity.getValidity();
	}
	
	public boolean checkForNewalAndExpiration (LocalDate today){
		if ((ChronoUnit.DAYS.between(today, this.expiration)) <=-1 && this.validity != Validity.SUSPENDED){
			setValidity("License is expired.");
		}
		if ((ChronoUnit.DAYS.between(today, this.expiration)) <=60 && !this.renewalNoticeSent){
			this.renewalNoticeSent = true;
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return "\nLicense number: " + this.licenseNumber + "\nFirst name: " + this.firstName + 
				"\nLast name: " +	this.lastName + "\nAddress: " + this.address + "\nDate of birth: "
				+ this.dob + "\nValidity: " + this.validity.getValidity() + "\nExpiration: " + 
				this.expiration + "\n";
	}
	
	@Override
	public int compareTo(Driver driver) {
		return this.getLastName().compareTo(driver.getLastName());	
	}		
}