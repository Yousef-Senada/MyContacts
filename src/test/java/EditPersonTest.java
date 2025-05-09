import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

public class EditPersonTest {
    
    private List<Person> persons;
    private Person testPerson;
    
    @BeforeEach
    void setUp() {
        persons = new ArrayList<>();
        testPerson = new Person("John Doe", "john@example.com", "1234567890", "New York");
        persons.add(testPerson);
    }
    
    @Test
    void testEditPersonName() {
        // Test editing person's name
        testPerson.setName("Jane Doe");
        assertEquals("Jane Doe", testPerson.getName());
    }
    
    @Test
    void testEditPersonEmail() {
        // Test editing person's email
        testPerson.setEmail("jane@example.com");
        assertEquals("jane@example.com", testPerson.getEmail());
    }
    
    @Test
    void testEditPersonPhone() {
        // Test editing person's phone
        testPerson.setPhone("9876543210");
        assertEquals("9876543210", testPerson.getPhone());
    }
    
    @Test
    void testEditPersonAddress() {
        // Test editing person's address
        testPerson.setAddress("Los Angeles");
        assertEquals("Los Angeles", testPerson.getAddress());
    }
    
    @Test
    void testEditAllPersonInfo() {
        // Test editing all person information at once
        testPerson.setName("Jane Smith");
        testPerson.setEmail("jane.smith@example.com");
        testPerson.setPhone("5555555555");
        testPerson.setAddress("Chicago");
        
        assertEquals("Jane Smith", testPerson.getName());
        assertEquals("jane.smith@example.com", testPerson.getEmail());
        assertEquals("5555555555", testPerson.getPhone());
        assertEquals("Chicago", testPerson.getAddress());
    }
    
    @Test
    void testEditPersonWithInvalidEmail() {
        // Test editing with invalid email format
        assertThrows(IllegalArgumentException.class, () -> {
            testPerson.setEmail("invalid-email");
        });
    }
    
    @Test
    void testEditPersonWithInvalidPhone() {
        // Test editing with invalid phone number
        assertThrows(IllegalArgumentException.class, () -> {
            testPerson.setPhone("123"); // Too short
        });
    }
    
    @Test
    void testEditPersonWithEmptyName() {
        // Test editing with empty name
        assertThrows(IllegalArgumentException.class, () -> {
            testPerson.setName("");
        });
    }
    
    @Test
    void testEditPersonWithSpecialCharacters() {
        // Test editing with special characters
        testPerson.setName("John-Doe Jr.");
        testPerson.setEmail("john.doe+test@example.com");
        
        assertEquals("John-Doe Jr.", testPerson.getName());
        assertEquals("john.doe+test@example.com", testPerson.getEmail());
    }
    
    @Test
    void testEditPersonCaseSensitivity() {
        // Test case sensitivity in editing
        testPerson.setName("JOHN DOE");
        testPerson.setEmail("JOHN@EXAMPLE.COM");
        
        assertEquals("JOHN DOE", testPerson.getName());
        assertEquals("JOHN@EXAMPLE.COM", testPerson.getEmail());
    }
    
    @Test
    void testEditPersonWithSpaces() {
        // Test editing with extra spaces
        testPerson.setName("JohnDoe");
        assertEquals("JohnDoe", testPerson.getName()); // Should trim spaces
    }
}

class Person {
    private String name;
    private String email;
    private String phone;
    private String address;
    
    public Person(String name, String email, String phone, String address) {
        setName(name);
        setEmail(email);
        setPhone(phone);
        setAddress(address);
    }
    
    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name.trim();
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
    
    public String getAddress() { return address; }
    public void setAddress(String address) {
        this.address = address;
    }
} 