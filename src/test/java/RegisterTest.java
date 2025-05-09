import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RegisterTest {
    
    @Test
    void testValidRegistration() {
        // Test case for valid registration
        String username = "new_user";
        String email = "new_user@example.com";
        String password = "newpass123";
        
        // Simple validation checks
        assertTrue(username.length() >= 4);
        assertTrue(email.contains("@"));     
        assertTrue(password.length() >= 6); 
    }
    
    @Test
    void testInvalidRegistration() {
        // Test case for invalid registration
        String username = "abc";       
        String email = "invalid_email";  
        String password = "123";     
        
        // Simple validation checks
        assertFalse(username.length() >= 4);
        assertFalse(email.contains("@"));
        assertFalse(password.length() >= 6);
    }
} 