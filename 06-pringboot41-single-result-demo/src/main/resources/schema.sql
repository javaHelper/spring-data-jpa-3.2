CREATE TABLE employee
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100)
);

CREATE ALIAS FIND_EMPLOYEE_BY_EMAIL AS '
import java.sql.*;

@CODE
ResultSet findEmployeeByEmail(Connection conn, String email) throws Exception {
    PreparedStatement ps =
        conn.prepareStatement(
            "SELECT * FROM employee WHERE email = ?"
        );
    ps.setString(1, email);
    return ps.executeQuery();
}
';