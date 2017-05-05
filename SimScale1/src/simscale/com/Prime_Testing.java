package simscale.com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class Prime_Testing {
  static long          total_time;
  static List<Integer> final_result = new ArrayList<Integer>();

  public static void main(String[] args) {
    validate_command_line_arguments(args);
    prime_testing_algorithm(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
    try {
      store_request(args[0], args[1], args[2], final_result.size());
    } catch (ClassNotFoundException | SQLException e) {
      System.out.println("error");
      e.printStackTrace();
    }
    System.out.println(final_result);
  }

  static void prime_testing_algorithm(int start, int end, int algorithm) {
    Long start_time = System.currentTimeMillis();

    for (int i = start; i <= end; i++) {
      Boolean result = true;
      switch (algorithm) {
        case 1:
          // O(n)
          result = primality_testing(i, i - 1);
          break;
        case 2:
          //O(n power 0.5)
          result = primality_testing(i, Math.sqrt(i));
          break;
        case 3:
          //O(log(n power (1/12)))
          System.out.println("sending" + i);
          result = prime_testing_algorithm3(i);
          break;
        default:
          prime_testing_algorithm3(i);
          break;
      }
      if (result) {
        final_result.add(i);
      }
    }
    Long end_time = System.currentTimeMillis();
    total_time = end_time - start_time;
  }

  private static Boolean prime_testing_algorithm3(int a) {
    //  1. check if it is perfect power
    if (a == 1)
      return false;
    double log2_tobase10 = Math.log10(2);
    for (int i = 2; i <= Math.log10(a) / log2_tobase10; i++) {
      if ((i * a) == (Math.log10(a) / log2_tobase10)) {
        System.out.println("composite" + a);
        return false;
      }
    }
    return true;

  }

  static void validate_command_line_arguments(String[] args) {
    if (args.length != 3) {
      System.out.println("Please enter the following 3 paramters as command line arguments");
      System.out.println("1. Start Value\n2. End Value\n3. Algorithm(1)");
      System.exit(0);
    }
    if (Integer.parseInt(args[0]) > Integer.parseInt(args[1])) {
      System.out.println("Please enter Start Value less than End value");
      System.exit(0);
    }
    if (Integer.parseInt(args[2]) > 3) {
      System.out.println("Please select a valid algorithm");
      System.exit(0);
    }
  }

  static Boolean primality_testing(int a, double traverse_till) {
    int flag = 0;
    if (a == 1)
      return false;
    for (int i = 2; i <= traverse_till; i++) {
      if (a % i == 0) {
        flag = 1;
        i = a;
      }
    }
    if (flag == 1)
      return false;
    else
      return true;

  }

  static void store_request(String start, String end, String algorithm, int length) throws SQLException, ClassNotFoundException {
    String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    String connectionURL = "jdbc:derby:memory:testDB;create=true";
    Class.forName(driver);
    Connection conn = DriverManager.getConnection(connectionURL);
    // creates table
    String DDL = "CREATE TABLE execution_result (id INT not null primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), timestamp1 timestamp default current_timestamp, start INT NOT NULL, end_value INT NOT NULL, timeelapsed float, algorithm INT NOT NULL,count INT NOT NULL)";
    Statement stmnt = conn.createStatement();
    stmnt.executeUpdate(DDL);
    // inserts row into the table
    String DML = "INSERT INTO execution_result(start,end_value,timeelapsed,algorithm,count) VALUES (" + Integer.parseInt(start) + ","
        + Integer.parseInt(end) + "," + total_time + "," + Integer.parseInt(algorithm) + "," + length + ")";
    stmnt = conn.createStatement();
    stmnt.executeUpdate(DML);
    // fetches the rows
    String DML_SELECT = "select * from execution_result";
    stmnt = conn.createStatement();
    ResultSet rs = stmnt.executeQuery(DML_SELECT);

    while (rs.next()) {
      System.out.println("id: " + rs.getString("id"));
      System.out.println("timestamp: " + rs.getString("timestamp1"));
      System.out.println("start: " + rs.getString("start"));
      System.out.println("end: " + rs.getString("end_value"));
      System.out.println("time elapsed: " + rs.getString("timeelapsed"));
      System.out.println("algorithm: " + rs.getString("algorithm"));
      System.out.println("count: " + rs.getString("count"));
    }
  }
}
