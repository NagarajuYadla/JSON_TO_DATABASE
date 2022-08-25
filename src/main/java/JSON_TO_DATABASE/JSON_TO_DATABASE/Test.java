package JSON_TO_DATABASE.JSON_TO_DATABASE;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Test {
	public static void main(String[] args) throws IOException, ParseException, ClassNotFoundException, SQLException,
			IllegalArgumentException, IllegalAccessException {
		String url = "jdbc:postgresql://localhost:5432/kanerika";
		String user = "postgres";
		String pass = "Krishna";
		
		ObjectMapper maper = new ObjectMapper();
		
		File jsonfile = new File("C:\\programs\\JSON_TO_DATABASE\\jsonfiles\\employee.json");
		List<Employee> employeelist = maper.readValue(jsonfile, new TypeReference<List<Employee>>() { });
		// System.out.println(employee.getDesignation());
		//List<Employee> array = employee.getEmp();
          System.out.println(employeelist);
          
          
		
		Class.forName("org.postgresql.Driver");
		Connection conn = DriverManager.getConnection(url, user, pass);
		String query ="INSERT INTO employeedetails(\n" + " id,name,designation)\n" + "values(?, ?, ?)\n" ;
		PreparedStatement pstmt = conn.prepareStatement(query);
				

		for (Employee employee1 : employeelist) {
			int i = 1;
			Class<?> test = employee1.getClass();
			Field[] field = test.getDeclaredFields();
			for (Field filed : field) {
				filed.setAccessible(true);
				if (filed.getType() == int.class) {
					pstmt.setInt(i, filed.getInt(employee1));
					i++;
	
				} else {
					pstmt.setString(i, filed.get(employee1).toString());
					i++;
				}
			}
			pstmt.execute();
		}
		//System.out.println(query); 

	}
}
