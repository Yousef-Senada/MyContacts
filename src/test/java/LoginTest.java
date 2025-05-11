import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.mycompany.mycontacts.DBConnection;

public class LoginTest {
    
    @Test
    void testValidLogin() {
        try {
            String email = "yousif012070@gmail.com";
            String password = "123456";
            
            Connection conn = DBConnection.getConnection();
            String query = "SELECT * FROM Users WHERE email = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);
            
            ResultSet rs = ps.executeQuery();
            boolean loginSuccessful = rs.next();
            
            rs.close();
            ps.close();
            conn.close();
            
            assertTrue(loginSuccessful, "Login should be successful with valid credentials");
        } catch (Exception e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }
    
    @Test
    void testInvalidLogin() {
        try {
            String email = "wrong@example.com";
            String password = "wrongpass";
            
            Connection conn = DBConnection.getConnection();
            String query = "SELECT * FROM Users WHERE email = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);
            
            ResultSet rs = ps.executeQuery();
            boolean loginSuccessful = rs.next();
            
            rs.close();
            ps.close();
            conn.close();
            
            assertFalse(loginSuccessful, "Login should fail with invalid credentials");
        } catch (Exception e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }
} 