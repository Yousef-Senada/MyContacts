import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.mycompany.mycontacts.DBConnection;

public class GuiOperationsTest {
    
    // Sample data for testing
    private List<Contact> contacts = new ArrayList<>();
    
    @Test
    void testCreateContact() {
        // Test creating a new contact
        Contact newContact = new Contact("John Doe", "john@example.com", "1234567890");
        contacts.add(newContact);
        
        assertTrue(contacts.contains(newContact));
        assertEquals(1, contacts.size());
    }
    
    @Test
    void testReadContact() {
        // Test reading contact information
        Contact contact = new Contact("Jane Smith", "jane@example.com", "0987654321");
        contacts.add(contact);
        
        Contact foundContact = contacts.stream()
            .filter(c -> c.getName().equals("Jane Smith"))
            .findFirst()
            .orElse(null);
            
        assertNotNull(foundContact);
        assertEquals("jane@example.com", foundContact.getEmail());
    }
    
    @Test
    void testUpdateContact() {
        // Test updating contact information
        Contact contact = new Contact("Old Name", "old@email.com", "1111111111");
        contacts.add(contact);
        
        // Update contact
        contact.setName("New Name");
        contact.setEmail("new@email.com");
        
        Contact updatedContact = contacts.get(0);
        assertEquals("New Name", updatedContact.getName());
        assertEquals("new@email.com", updatedContact.getEmail());
    }
    
    @Test
    void testDeleteContact() {
        try {
            // First, create a test user
            String username = "test_user";
            String email = "test_user@gmail.com";
            String phone = "0123456789";
            String work = "0123456789";
            String password = "123456";
            
            Connection conn = DBConnection.getConnection();
            
            // Insert test user
            String insertUserQuery = "INSERT INTO Users (username, email, phone, work, password) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insertUserQuery);
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, work);
            ps.setString(5, password);
            ps.executeUpdate();
            
            // Get the user ID
            String getIdQuery = "SELECT id FROM Users WHERE email = ?";
            ps = conn.prepareStatement(getIdQuery);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            assertTrue(rs.next(), "Test user should exist");
            int userId = rs.getInt("id");
            
            // Add a test contact
            String firstName = "John";
            String lastName = "Doe";
            String contactEmail = "john.doe@example.com";
            String mobilePhone = "0123456789";
            String homePhone = "9876543210";
            String address = "123 Test Street";
            
            String insertContactQuery = "INSERT INTO Contacts (userId, FirstName, LastName, Email, MobilePhone, HomePhone, Address) VALUES (?, ?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(insertContactQuery);
            ps.setInt(1, userId);
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setString(4, contactEmail);
            ps.setString(5, mobilePhone);
            ps.setString(6, homePhone);
            ps.setString(7, address);
            ps.executeUpdate();
            
            // Get the contact ID
            String getContactIdQuery = "SELECT ID FROM Contacts WHERE userId = ? AND Email = ?";
            ps = conn.prepareStatement(getContactIdQuery);
            ps.setInt(1, userId);
            ps.setString(2, contactEmail);
            rs = ps.executeQuery();
            assertTrue(rs.next(), "Contact should exist");
            int contactId = rs.getInt("ID");
            
            // Delete the contact
            String deleteQuery = "DELETE FROM Contacts WHERE ID = ?";
            ps = conn.prepareStatement(deleteQuery);
            ps.setInt(1, contactId);
            int rowsAffected = ps.executeUpdate();
            assertEquals(1, rowsAffected, "One contact should be deleted");
            
            // Verify contact is deleted
            String verifyQuery = "SELECT * FROM Contacts WHERE ID = ?";
            ps = conn.prepareStatement(verifyQuery);
            ps.setInt(1, contactId);
            rs = ps.executeQuery();
            assertFalse(rs.next(), "Contact should be deleted");
            
            // Clean up - delete the test user
            String deleteUserQuery = "DELETE FROM Users WHERE id = ?";
            ps = conn.prepareStatement(deleteUserQuery);
            ps.setInt(1, userId);
            ps.executeUpdate();
            
            rs.close();
            ps.close();
            conn.close();
            
        } catch (Exception e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }
    
    @Test
    void testSearchContact() {
        try {
            // First, create a test user
            String username = "test_user";
            String email = "test_user@gmail.com";
            String phone = "0123456789";
            String work = "0123456789";
            String password = "123456";
            
            Connection conn = DBConnection.getConnection();
            
            // Insert test user
            String insertUserQuery = "INSERT INTO Users (username, email, phone, work, password) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insertUserQuery);
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, work);
            ps.setString(5, password);
            ps.executeUpdate();
            
            // Get the user ID
            String getIdQuery = "SELECT id FROM Users WHERE email = ?";
            ps = conn.prepareStatement(getIdQuery);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            assertTrue(rs.next(), "Test user should exist");
            int userId = rs.getInt("id");
            
            // Add a test contact
            String firstName = "John";
            String lastName = "Doe";
            String contactEmail = "john.doe@example.com";
            String mobilePhone = "0123456789";
            String homePhone = "9876543210";
            String address = "123 Test Street";
            
            String insertContactQuery = "INSERT INTO Contacts (userId, FirstName, LastName, Email, MobilePhone, HomePhone, Address) VALUES (?, ?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(insertContactQuery);
            ps.setInt(1, userId);
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setString(4, contactEmail);
            ps.setString(5, mobilePhone);
            ps.setString(6, homePhone);
            ps.setString(7, address);
            ps.executeUpdate();
            
            // Test search by name
            String searchQuery = "SELECT * FROM Contacts WHERE userId = ? AND (FirstName LIKE ? OR LastName LIKE ?)";
            ps = conn.prepareStatement(searchQuery);
            ps.setInt(1, userId);
            ps.setString(2, "%John%");
            ps.setString(3, "%John%");
            rs = ps.executeQuery();
            
            assertTrue(rs.next(), "Search should find the contact");
            assertEquals(firstName, rs.getString("FirstName"), "First name should match");
            assertEquals(lastName, rs.getString("LastName"), "Last name should match");
            
            // Test search by email
            searchQuery = "SELECT * FROM Contacts WHERE userId = ? AND Email LIKE ?";
            ps = conn.prepareStatement(searchQuery);
            ps.setInt(1, userId);
            ps.setString(2, "%john.doe%");
            rs = ps.executeQuery();
            
            assertTrue(rs.next(), "Search should find the contact by email");
            assertEquals(contactEmail, rs.getString("Email"), "Email should match");
            
            // Clean up
            String deleteContactQuery = "DELETE FROM Contacts WHERE userId = ?";
            ps = conn.prepareStatement(deleteContactQuery);
            ps.setInt(1, userId);
            ps.executeUpdate();
            
            String deleteUserQuery = "DELETE FROM Users WHERE id = ?";
            ps = conn.prepareStatement(deleteUserQuery);
            ps.setInt(1, userId);
            ps.executeUpdate();
            
            rs.close();
            ps.close();
            conn.close();
            
        } catch (Exception e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }
    
    @Test
    void testMaxMinOperations() {
        // Test finding max and min values
        contacts.add(new Contact("Short", "a@email.com", "111"));
        contacts.add(new Contact("Medium", "b@email.com", "1111"));
        contacts.add(new Contact("Very Long Name", "c@email.com", "11111"));
        
        // Find contact with longest name
        Contact longestName = contacts.stream()
            .max((c1, c2) -> Integer.compare(c1.getName().length(), c2.getName().length()))
            .orElse(null);
            
        // Find contact with shortest name
        Contact shortestName = contacts.stream()
            .min((c1, c2) -> Integer.compare(c1.getName().length(), c2.getName().length()))
            .orElse(null);
            
        assertEquals("Very Long Name", longestName.getName());
        assertEquals("Short", shortestName.getName());
    }
    
    @Test
    void testSortContacts() {
        // Test sorting contacts
        contacts.add(new Contact("Charlie", "c@email.com", "333"));
        contacts.add(new Contact("Alice", "a@email.com", "111"));
        contacts.add(new Contact("Bob", "b@email.com", "222"));
        
        contacts.sort((c1, c2) -> c1.getName().compareTo(c2.getName()));
        
        assertEquals("Alice", contacts.get(0).getName());
        assertEquals("Bob", contacts.get(1).getName());
        assertEquals("Charlie", contacts.get(2).getName());
    }
    
    @Test
    void testFilterContacts() {
        // Test filtering contacts
        contacts.add(new Contact("John", "john@email.com", "111"));
        contacts.add(new Contact("Jane", "jane@email.com", "222"));
        contacts.add(new Contact("Jack", "jack@email.com", "333"));
        
        List<Contact> filteredContacts = contacts.stream()
            .filter(c -> c.getName().startsWith("J"))
            .toList();
            
        assertEquals(3, filteredContacts.size());
    }
    
    @Test
    void testEditPersonBasicInfo() {
        // Test editing basic person information
        Contact person = new Contact("Original Name", "original@email.com", "1234567890");
        contacts.add(person);
        
        // Edit basic information
        person.setName("Updated Name");
        person.setEmail("updated@email.com");
        person.setPhone("9876543210");
        
        Contact editedPerson = contacts.get(0);
        assertEquals("Updated Name", editedPerson.getName());
        assertEquals("updated@email.com", editedPerson.getEmail());
        assertEquals("9876543210", editedPerson.getPhone());
    }
    
    @Test
    void testEditPersonPartialInfo() {
        // Test editing only some fields of person information
        Contact person = new Contact("John Doe", "john@email.com", "1111111111");
        contacts.add(person);
        
        // Edit only email
        person.setEmail("newemail@email.com");
        
        Contact editedPerson = contacts.get(0);
        assertEquals("John Doe", editedPerson.getName()); // Name should remain unchanged
        assertEquals("newemail@email.com", editedPerson.getEmail());
        assertEquals("1111111111", editedPerson.getPhone()); // Phone should remain unchanged
    }
    
    @Test
    void testEditPersonValidation() {
        // Test validation when editing person information
        Contact person = new Contact("Valid Name", "valid@email.com", "1234567890");
        contacts.add(person);
        
        // Test invalid email format
        assertThrows(IllegalArgumentException.class, () -> {
            person.setEmail("invalid-email");
        });
        
        // Test invalid phone format
        assertThrows(IllegalArgumentException.class, () -> {
            person.setPhone("123"); // Too short
        });
        
        // Test empty name
        assertThrows(IllegalArgumentException.class, () -> {
            person.setName("");
        });
    }
    
    @Test
    void testEditPersonWithSpecialCharacters() {
        // Test editing person information with special characters
        Contact person = new Contact("John Doe", "john@email.com", "1234567890");
        contacts.add(person);
        
        // Edit with special characters
        person.setName("John-Doe Jr.");
        person.setEmail("john.doe+test@email.com");
        
        Contact editedPerson = contacts.get(0);
        assertEquals("John-Doe Jr.", editedPerson.getName());
        assertEquals("john.doe+test@email.com", editedPerson.getEmail());
    }
    
    @Test
    void testEditPersonCaseSensitivity() {
        // Test case sensitivity in editing
        Contact person = new Contact("john doe", "john@email.com", "1234567890");
        contacts.add(person);
        
        // Edit with different cases
        person.setName("JOHN DOE");
        person.setEmail("JOHN@EMAIL.COM");
        
        Contact editedPerson = contacts.get(0);
        assertEquals("JOHN DOE", editedPerson.getName());
        assertEquals("JOHN@EMAIL.COM", editedPerson.getEmail());
    }
    
    @Test
    void testAddContact() {
        try {
            // First, create a test user to own the contact
            String username = "test_user";
            String email = "test_user@gmail.com";
            String phone = "0123456789";
            String work = "0123456789";
            String password = "123456";
            
            Connection conn = DBConnection.getConnection();
            
            // Insert test user
            String insertUserQuery = "INSERT INTO Users (username, email, phone, work, password) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insertUserQuery);
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, work);
            ps.setString(5, password);
            ps.executeUpdate();
            
            // Get the user ID
            String getIdQuery = "SELECT id FROM Users WHERE email = ?";
            ps = conn.prepareStatement(getIdQuery);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            assertTrue(rs.next(), "Test user should exist");
            int userId = rs.getInt("id");
            
            // Add a new contact
            String firstName = "John";
            String lastName = "Doe";
            String contactEmail = "john.doe@example.com";
            String mobilePhone = "0123456789";
            String homePhone = "9876543210";
            String address = "123 Test Street";
            
            String insertContactQuery = "INSERT INTO Contacts (userId, FirstName, LastName, Email, MobilePhone, HomePhone, Address) VALUES (?, ?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(insertContactQuery);
            ps.setInt(1, userId);
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setString(4, contactEmail);
            ps.setString(5, mobilePhone);
            ps.setString(6, homePhone);
            ps.setString(7, address);
            
            int rowsAffected = ps.executeUpdate();
            assertTrue(rowsAffected > 0, "Contact should be added successfully");
            
            // Verify the contact was added
            String verifyQuery = "SELECT * FROM Contacts WHERE userId = ? AND Email = ?";
            ps = conn.prepareStatement(verifyQuery);
            ps.setInt(1, userId);
            ps.setString(2, contactEmail);
            rs = ps.executeQuery();
            
            assertTrue(rs.next(), "Contact should exist in database");
            assertEquals(firstName, rs.getString("FirstName"), "First name should match");
            assertEquals(lastName, rs.getString("LastName"), "Last name should match");
            assertEquals(contactEmail, rs.getString("Email"), "Email should match");
            assertEquals(mobilePhone, rs.getString("MobilePhone"), "Mobile phone should match");
            assertEquals(homePhone, rs.getString("HomePhone"), "Home phone should match");
            assertEquals(address, rs.getString("Address"), "Address should match");
            
            // Clean up - delete the test contact and user
            String deleteContactQuery = "DELETE FROM Contacts WHERE userId = ?";
            ps = conn.prepareStatement(deleteContactQuery);
            ps.setInt(1, userId);
            ps.executeUpdate();
            
            String deleteUserQuery = "DELETE FROM Users WHERE id = ?";
            ps = conn.prepareStatement(deleteUserQuery);
            ps.setInt(1, userId);
            ps.executeUpdate();
            
            rs.close();
            ps.close();
            conn.close();
            
        } catch (Exception e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }
}

// Simple Contact class for testing
class Contact {
    private String name;
    private String email;
    private String phone;
    
    public Contact(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
    
    public String getName() { return name; }
    public void setName(String name) { 
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name; 
    }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { 
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email; 
    }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { 
        if (phone == null || phone.length() < 10) {
            throw new IllegalArgumentException("Phone number must be at least 10 digits");
        }
        this.phone = phone; 
    }
} 