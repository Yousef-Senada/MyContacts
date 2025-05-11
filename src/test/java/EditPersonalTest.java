import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.mycompany.mycontacts.DBConnection;

public class EditPersonalTest {
    
    @Test
    void testValidEditPersonal() {
        try {

            String originalUsername = "test_user";
            String originalEmail = "test_user@gmail.com";
            String originalPhone = "0123456789";
            String originalWork = "0123456789";
            String password = "123456";
            
            Connection conn = DBConnection.getConnection();
            
            String insertQuery = "INSERT INTO Users (username, email, phone, work, password) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insertQuery);
            ps.setString(1, originalUsername);
            ps.setString(2, originalEmail);
            ps.setString(3, originalPhone);
            ps.setString(4, originalWork);
            ps.setString(5, password);
            ps.executeUpdate();
            
            String getIdQuery = "SELECT id FROM Users WHERE email = ?";
            ps = conn.prepareStatement(getIdQuery);
            ps.setString(1, originalEmail);
            ResultSet rs = ps.executeQuery();
            assertTrue(rs.next(), "Test user should exist");
            int userId = rs.getInt("id");
            
            String newUsername = "updated_user";
            String newEmail = "updated_user@gmail.com";
            String newPhone = "9876543210";
            String newWork = "9876543210";
            
            String updateQuery = "UPDATE Users SET username = ?, email = ?, phone = ?, work = ? WHERE id = ?";
            ps = conn.prepareStatement(updateQuery);
            ps.setString(1, newUsername);
            ps.setString(2, newEmail);
            ps.setString(3, newPhone);
            ps.setString(4, newWork);
            ps.setInt(5, userId);
            
            int rowsAffected = ps.executeUpdate();
            assertTrue(rowsAffected > 0, "Update should affect one row");
            
            String verifyQuery = "SELECT * FROM Users WHERE id = ?";
            ps = conn.prepareStatement(verifyQuery);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            
            assertTrue(rs.next(), "User should still exist after update");
            assertEquals(newUsername, rs.getString("username"), "Username should be updated");
            assertEquals(newEmail, rs.getString("email"), "Email should be updated");
            assertEquals(newPhone, rs.getString("phone"), "Phone should be updated");
            assertEquals(newWork, rs.getString("work"), "Work should be updated");
            assertEquals(password, rs.getString("password"), "Password should remain unchanged");
            
            String deleteQuery = "DELETE FROM Users WHERE id = ?";
            ps = conn.prepareStatement(deleteQuery);
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
    void testInvalidEditPersonal() {
        try {

            String updateQuery = "UPDATE Users SET username = ?, email = ?, phone = ?, work = ? WHERE id = ?";
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(updateQuery);
            
            ps.setString(1, "new_username");
            ps.setString(2, "new_email@gmail.com");
            ps.setString(3, "1234567890");
            ps.setString(4, "1234567890");
            ps.setInt(5, -1);
            
            int rowsAffected = ps.executeUpdate();
            assertEquals(0, rowsAffected, "Update should not affect any rows for non-existent user");
            
            ps.close();
            conn.close();
            
        } catch (Exception e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }
} 