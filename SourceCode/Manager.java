package licenseManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.ArrayList;
import java.time.LocalDate;

class Manager {
	static private Manager manager;
	HashMap<String, Driver> records = new HashMap<String, Driver>();
	ArrayList<Driver> driverList;
	private LocalDate today;
	private Manager(){}
	private ArrayList<String> renewalNotices;
	
	@SuppressWarnings("unchecked")
	public static Manager getInstance() throws IOException, ClassNotFoundException {
		if (manager == null) {
			manager = new Manager();
			if (!Files.exists(Paths.get("records.ser"))){
				System.out.println("created");
				Files.createFile(Paths.get("records.ser"));
			}
			try (FileInputStream file = new FileInputStream("records.ser");
			ObjectInputStream is = new ObjectInputStream(file);){
			manager.records = (HashMap<String, Driver>)is.readObject();  
			}
			catch (IOException | ClassNotFoundException e) {
				}	
			manager.today = LocalDate.now();
		}
		return manager;
	}
	
	public void saveRecords() throws IOException {
		try (FileOutputStream file = new FileOutputStream("records.ser");
			ObjectOutputStream os = new ObjectOutputStream(file);){
			os.writeObject(manager.records);
		}
		catch (IOException e) {}
	}
	
	public Driver search(String licenseNumber) throws InvalidLicenseNumberException, DriverNotFoundException {
		if (licenseNumber.length() != 10) {
			throw new InvalidLicenseNumberException();
		} else {
			if (records.containsKey(licenseNumber)){
				return (records.get(licenseNumber));
			} else {
				throw new DriverNotFoundException();
			}
		}
	}
	
	public void deleteDriver(String licenseNumber) {
		manager.records.remove(licenseNumber);
	}
	
	public boolean driverExists(String licenseNumber) {
		if (manager.records.containsKey(licenseNumber)) {
			return true;
		} else {
			return false;
		}
	}
	
	public void addDriver(String licenseNumber, String firstName, String lastName, String address, 
		LocalDate dob, LocalDate expiration) {
		manager.records.put(licenseNumber, (new Driver (licenseNumber, firstName, lastName, 
		address, dob, expiration)));
	}
	
	public String[][] loadTable(){	
		if (records.size() != 0) {
			driverList = new ArrayList<Driver>(records.size());				
			for (Driver driver : records.values()) {
				driverList.add(driver);
			}
			driverList.sort(null);
			int length = records.size();
			String[][] results = new String[length][7];
			int i = 0;
			for (Driver driver : driverList) {
				results[i][0] = driver.getLicenseNumber();
				results[i][1] = driver.getFirstName();
				results[i][2] = driver.getLastName();
				results[i][3] = driver.getDOB();
				results[i][4] = driver.getAddress();
				results[i][5] = driver.getValidity();
				results[i][6] = driver.getExpiration();
				i++;		
			}
			return results;
		} else {
			return new String[0][0];
		}
	}
	
	public ArrayList<String> checkRecords() {
		manager.renewalNotices = new ArrayList<>();
		for (Driver driver : records.values()) {
			if (driver.checkForNewalAndExpiration(manager.today)) {
				renewalNotices.add(driver.toString());
			}
		}
		return renewalNotices;
	}	
}