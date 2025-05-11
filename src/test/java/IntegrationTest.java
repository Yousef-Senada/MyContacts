import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.mycompany.mycontacts.DBConnection;

public class IntegrationTest {
    
    private ContactManager contactManager;
    private List<Person> testData;
    
    @BeforeEach
    void setUp() {
        contactManager = new ContactManager();
        testData = new ArrayList<>();
        
        testData.add(new Person("Ahmed", "ahmed@email.com", "0123456789", "Cairo"));
        testData.add(new Person("Mohamed", "mohamed@email.com", "0987654321", "Alexandria"));
        testData.add(new Person("Ali", "ali@email.com", "0555555555", "Giza"));
    }
    
    @Test
    void testAddAndGetPerson() {
        contactManager.addPerson(testData.get(0));
        assertEquals(1, contactManager.getAllPersons().size());
        assertEquals("Ahmed", contactManager.getAllPersons().get(0).getName());
    }
    
    @Test
    void testUpdatePerson() {
        contactManager.addPerson(testData.get(0));
        Person person = contactManager.getAllPersons().get(0);
        person.setPhone("0111111111");
        contactManager.updatePerson(person);
        assertEquals("0111111111", contactManager.getAllPersons().get(0).getPhone());
    }
    
    @Test
    void testDeletePerson() {
        contactManager.addPerson(testData.get(0));
        contactManager.deletePerson("ahmed@email.com");
        assertEquals(0, contactManager.getAllPersons().size());
    }
    
    @Test
    void testSearchByName() {
        contactManager.addPerson(testData.get(0));
        contactManager.addPerson(testData.get(1));
        List<Person> results = contactManager.searchPersonsByName("Ahmed");
        assertEquals(1, results.size());
        assertEquals("Ahmed", results.get(0).getName());
    }
    
    @Test
    void testSearchByEmail() {
        contactManager.addPerson(testData.get(0));
        Person found = contactManager.searchPersonByEmail("ahmed@email.com");
        assertNotNull(found);
        assertEquals("Ahmed", found.getName());
    }
    
    @Test
    void testSearchByPhone() {
        contactManager.addPerson(testData.get(0));
        Person found = contactManager.searchPersonByPhone("0123456789");
        assertNotNull(found);
        assertEquals("Ahmed", found.getName());
    }
    
    @Test
    void testSortByName() {
        contactManager.addPerson(testData.get(0));
        contactManager.addPerson(testData.get(1));
        contactManager.addPerson(testData.get(2));
        
        List<Person> sorted = contactManager.sortPersonsByName();
        assertEquals("Ahmed", sorted.get(0).getName());
        assertEquals("Ali", sorted.get(1).getName());
        assertEquals("Mohamed", sorted.get(2).getName());
    }
    
    
    @Test
    void testBulkAdd() {
        contactManager.bulkAddPersons(testData);
        assertEquals(3, contactManager.getAllPersons().size());
    }
    
    @Test
    void testBulkUpdate() {
        contactManager.bulkAddPersons(testData);
        for (Person person : testData) {
            person.setCity("New City");
        }
        contactManager.bulkUpdatePersons(testData);
        
        for (Person person : contactManager.getAllPersons()) {
            assertEquals("New City", person.getCity());
        }
    }
    
    @Test
    void testCompleteUserFlow() {
        try {
            Connection conn = DBConnection.getConnection();
            
            // 1. Register a new user
            String username = "integration_test_user";
            String email = "integration_test@gmail.com";
            String phone = "0123456789";
            String work = "0123456789";
            String password = "123456";
            
            String insertUserQuery = "INSERT INTO Users (username, email, phone, work, password) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insertUserQuery);
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, work);
            ps.setString(5, password);
            ps.executeUpdate();
            
            // 2. Verify user registration
            String verifyUserQuery = "SELECT * FROM Users WHERE email = ?";
            ps = conn.prepareStatement(verifyUserQuery);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            
            assertTrue(rs.next(), "User should be registered");
            int userId = rs.getInt("id");
            assertEquals(username, rs.getString("username"), "Username should match");
            assertEquals(email, rs.getString("email"), "Email should match");
            
            // 3. Add contacts for the user
            String[] contactNames = {"John Doe", "Jane Smith", "Bob Johnson"};
            String[] contactEmails = {"john@example.com", "jane@example.com", "bob@example.com"};
            String[] contactPhones = {"1111111111", "2222222222", "3333333333"};
            
            for (int i = 0; i < contactNames.length; i++) {
                String insertContactQuery = "INSERT INTO Contacts (userId, FirstName, LastName, Email, MobilePhone) VALUES (?, ?, ?, ?, ?)";
                ps = conn.prepareStatement(insertContactQuery);
                String[] nameParts = contactNames[i].split(" ");
                ps.setInt(1, userId);
                ps.setString(2, nameParts[0]);
                ps.setString(3, nameParts[1]);
                ps.setString(4, contactEmails[i]);
                ps.setString(5, contactPhones[i]);
                ps.executeUpdate();
            }
            
            // 4. Verify contacts were added
            String verifyContactsQuery = "SELECT COUNT(*) as count FROM Contacts WHERE userId = ?";
            ps = conn.prepareStatement(verifyContactsQuery);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            assertTrue(rs.next(), "Should get contact count");
            assertEquals(3, rs.getInt("count"), "Should have 3 contacts");
            
            // 5. Search for a specific contact
            String searchQuery = "SELECT * FROM Contacts WHERE userId = ? AND (FirstName LIKE ? OR LastName LIKE ?)";
            ps = conn.prepareStatement(searchQuery);
            ps.setInt(1, userId);
            ps.setString(2, "%John%");
            ps.setString(3, "%John%");
            rs = ps.executeQuery();
            
            assertTrue(rs.next(), "Should find John Doe");
            assertEquals("John", rs.getString("FirstName"), "First name should match");
            assertEquals("Doe", rs.getString("LastName"), "Last name should match");
            

            String newUsername = "updated_integration_user";
            String newPhone = "9876543210";
            
            String updateUserQuery = "UPDATE Users SET username = ?, phone = ? WHERE id = ?";
            ps = conn.prepareStatement(updateUserQuery);
            ps.setString(1, newUsername);
            ps.setString(2, newPhone);
            ps.setInt(3, userId);
            ps.executeUpdate();
            
            // 7. Verify user update
            ps = conn.prepareStatement(verifyUserQuery);
            ps.setString(1, email);
            rs = ps.executeQuery();
            
            assertTrue(rs.next(), "User should still exist");
            assertEquals(newUsername, rs.getString("username"), "Username should be updated");
            assertEquals(newPhone, rs.getString("phone"), "Phone should be updated");
            
            // 8. Update a contact
            String updateContactQuery = "UPDATE Contacts SET FirstName = ?, LastName = ? WHERE userId = ? AND Email = ?";
            ps = conn.prepareStatement(updateContactQuery);
            ps.setString(1, "Johnny");
            ps.setString(2, "Doe");
            ps.setInt(3, userId);
            ps.setString(4, "john@example.com");
            ps.executeUpdate();
            
            // 9. Verify contact update
            String verifyContactQuery = "SELECT * FROM Contacts WHERE userId = ? AND Email = ?";
            ps = conn.prepareStatement(verifyContactQuery);
            ps.setInt(1, userId);
            ps.setString(2, "john@example.com");
            rs = ps.executeQuery();
            
            assertTrue(rs.next(), "Contact should exist");
            assertEquals("Johnny", rs.getString("FirstName"), "First name should be updated");
            
            // 10. Delete a contact
            String deleteContactQuery = "DELETE FROM Contacts WHERE userId = ? AND Email = ?";
            ps = conn.prepareStatement(deleteContactQuery);
            ps.setInt(1, userId);
            ps.setString(2, "john@example.com");
            ps.executeUpdate();
            
            // 11. Verify contact deletion
            ps = conn.prepareStatement(verifyContactsQuery);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            assertTrue(rs.next(), "Should get updated contact count");
            assertEquals(2, rs.getInt("count"), "Should have 2 contacts after deletion");
            
            // 12. Clean up - delete all test data
            String deleteContactsQuery = "DELETE FROM Contacts WHERE userId = ?";
            ps = conn.prepareStatement(deleteContactsQuery);
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
            fail("Integration test failed with exception: " + e.getMessage());
        }
    }
    
    @Test
    void testUserLoginAndContactManagement() {
        try {
            Connection conn = DBConnection.getConnection();
            
            // 1. Create test user
            String username = "login_test_user";
            String email = "login_test@gmail.com";
            String phone = "0123456789";
            String work = "0123456789";
            String password = "123456";
            
            String insertUserQuery = "INSERT INTO Users (username, email, phone, work, password) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insertUserQuery);
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, work);
            ps.setString(5, password);
            ps.executeUpdate();
            
            // 2. Test login
            String loginQuery = "SELECT * FROM Users WHERE email = ? AND password = ?";
            ps = conn.prepareStatement(loginQuery);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            
            assertTrue(rs.next(), "Login should be successful");
            int userId = rs.getInt("id");
            
            // 3. Add contacts after login
            String[] contacts = {
                "Alice Brown,alice@example.com,1111111111",
                "Charlie Davis,charlie@example.com,2222222222"
            };
            
            for (String contact : contacts) {
                String[] parts = contact.split(",");
                String[] nameParts = parts[0].split(" ");
                
                String insertContactQuery = "INSERT INTO Contacts (userId, FirstName, LastName, Email, MobilePhone) VALUES (?, ?, ?, ?, ?)";
                ps = conn.prepareStatement(insertContactQuery);
                ps.setInt(1, userId);
                ps.setString(2, nameParts[0]);
                ps.setString(3, nameParts[1]);
                ps.setString(4, parts[1]);
                ps.setString(5, parts[2]);
                ps.executeUpdate();
            }
            
            // 4. Verify contacts were added
            String verifyContactsQuery = "SELECT COUNT(*) as count FROM Contacts WHERE userId = ?";
            ps = conn.prepareStatement(verifyContactsQuery);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            assertTrue(rs.next(), "Should get contact count");
            assertEquals(2, rs.getInt("count"), "Should have 2 contacts");
            
            // 5. Clean up
            String deleteContactsQuery = "DELETE FROM Contacts WHERE userId = ?";
            ps = conn.prepareStatement(deleteContactsQuery);
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
            fail("Login integration test failed with exception: " + e.getMessage());
        }
    }
}

class ContactManager {
    private List<Person> persons;
    
    public ContactManager() {
        this.persons = new ArrayList<>();
    }
    
    public void addPerson(Person person) {
        validatePerson(person);
        if (searchPersonByEmail(person.getEmail()) != null) {
            throw new IllegalStateException("Email already exists");
        }
        persons.add(person);
    }
    
    public void updatePerson(Person person) {
        validatePerson(person);
        Person existingPerson = searchPersonByEmail(person.getEmail());
        if (existingPerson == null) {
            throw new IllegalStateException("Person not found");
        }
        int index = persons.indexOf(existingPerson);
        persons.set(index, person);
    }
    
    public void deletePerson(String email) {
        Person person = searchPersonByEmail(email);
        if (person != null) {
            persons.remove(person);
        }
    }
    
    public Person searchPerson(String name) {
        return persons.stream()
            .filter(p -> p.getName().equals(name))
            .findFirst()
            .orElse(null);
    }
    
    public Person searchPersonByEmail(String email) {
        return persons.stream()
            .filter(p -> p.getEmail().equals(email))
            .findFirst()
            .orElse(null);
    }
    
    public Person searchPersonByPhone(String phone) {
        return persons.stream()
            .filter(p -> p.getPhone().equals(phone))
            .findFirst()
            .orElse(null);
    }
    
    public List<Person> searchPersonsByName(String name) {
        return persons.stream()
            .filter(p -> p.getName().contains(name))
            .toList();
    }
    
    public List<Person> sortPersonsByName() {
        return persons.stream()
            .sorted((p1, p2) -> p1.getName().compareTo(p2.getName()))
            .toList();
    }
    
    public List<Person> filterPersonsByCity(String city) {
        return persons.stream()
            .filter(p -> p.getCity().equals(city))
            .toList();
    }
    
    public void bulkAddPersons(List<Person> newPersons) {
        for (Person person : newPersons) {
            addPerson(person);
        }
    }
    
    public void bulkUpdatePersons(List<Person> updatedPersons) {
        for (Person person : updatedPersons) {
            updatePerson(person);
        }
    }
    
    public List<Person> getAllPersons() {
        return new ArrayList<>(persons);
    }
    
    private void validatePerson(Person person) {
        if (person.getName() == null || person.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (person.getEmail() == null || !person.getEmail().contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (person.getPhone() == null || person.getPhone().length() < 10) {
            throw new IllegalArgumentException("Phone number must be at least 10 digits");
        }
    }
}

class Person {
    private String name;
    private String email;
    private String phone;
    private String city;
    
    public Person(String name, String email, String phone, String city) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.city = city;
    }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
} 