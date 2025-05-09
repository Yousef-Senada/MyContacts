import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

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
        // Test deleting a contact
        Contact contact = new Contact("Delete Me", "delete@email.com", "2222222222");
        contacts.add(contact);
        
        contacts.remove(contact);
        assertFalse(contacts.contains(contact));
        assertEquals(0, contacts.size());
    }
    
    @Test
    void testSearchContact() {
        // Test searching for contacts
        contacts.add(new Contact("Alice", "alice@email.com", "3333333333"));
        contacts.add(new Contact("Bob", "bob@email.com", "4444444444"));
        contacts.add(new Contact("Charlie", "charlie@email.com", "5555555555"));
        
        List<Contact> searchResults = contacts.stream()
            .filter(c -> c.getName().toLowerCase().contains("a"))
            .toList();
            
        assertEquals(2, searchResults.size()); // Should find Alice and Charlie
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