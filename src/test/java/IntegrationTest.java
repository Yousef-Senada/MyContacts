import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

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
    void testFilterByCity() {
        contactManager.addPerson(testData.get(0));
        contactManager.addPerson(testData.get(1));
        
        List<Person> cairoPeople = contactManager.filterPersonsByCity("Cairo");
        assertEquals(1, cairoPeople.size());
        assertEquals("yousef", cairoPeople.get(0).getName());
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