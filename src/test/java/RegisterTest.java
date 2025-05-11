import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.mycompany.mycontacts.DBConnection;

public class RegisterTest {
    
    @Test
    void testValidRegistration() {
        try {
            // Test case for valid registration
            String username = "test_user";
            String email = "test_user@gmail.com";
            String phone = "0123456789";
            String work = "0123456789";
            String password = "123456";
            
            // First, try to register the user
            Connection conn = DBConnection.getConnection();
            String insertQuery = "INSERT INTO Users (username, email, phone, work, password) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insertQuery);
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, work);
            ps.setString(5, password);
            
            int rowsAffected = ps.executeUpdate();
            assertTrue(rowsAffected > 0, "Registration should insert a new user");
            
            // Verify the user was actually registered
            String selectQuery = "SELECT * FROM Users WHERE email = ?";
            ps = conn.prepareStatement(selectQuery);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            
            assertTrue(rs.next(), "User should exist in database");
            assertEquals(username, rs.getString("username"), "Username should match");
            assertEquals(email, rs.getString("email"), "Email should match");
            assertEquals(phone, rs.getString("phone"), "Phone should match");
            assertEquals(work, rs.getString("work"), "Work should match");
            assertEquals(password, rs.getString("password"), "Password should match");
            
            String deleteQuery = "DELETE FROM Users WHERE email = ?";
            ps = conn.prepareStatement(deleteQuery);
            ps.setString(1, email);
            ps.executeUpdate();
            
            rs.close();
            ps.close();
            conn.close();
            
        } catch (Exception e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }
    
    @Test
    void testInvalidRegistration() {
        try {
            String username = "another_user";
            String email = "yousif012070@gmail.com";
            String phone = "0123456789";
            String work = "0123456789";
            String password = "testpass123";
            
            Connection conn = DBConnection.getConnection();
            String query = "INSERT INTO Users (username, email, phone, work, password) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, work);
            ps.setString(5, password);
            
            try {
                ps.executeUpdate();
                fail("Registration with duplicate email should fail");
            } catch (Exception e) {
                assertTrue(true);
            }
            
            ps.close();
            conn.close();
            
        } catch (Exception e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }
} 