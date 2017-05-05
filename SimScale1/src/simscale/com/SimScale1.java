package simscale.com;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;


@Path("/SimScale1")
public class SimScale1 {
  @GET
  @Path("/prime")
  @Produces("text/plain")
  public List<Integer> getprime(@DefaultValue("1") @QueryParam("start") String start, @DefaultValue("20") @QueryParam("end") String end,
                                @DefaultValue("3") @QueryParam("algorithm") String algorithm) {
    String args[] = { start, end, algorithm };
    //validates the query parameters
    Prime_Testing.validate_command_line_arguments(args);
    Prime_Testing.prime_testing_algorithm(Integer.parseInt(start), Integer.parseInt(end), Integer.parseInt(algorithm));
    try {
      Prime_Testing.store_request(start, end, algorithm, Prime_Testing.final_result.size());
    } catch (ClassNotFoundException | SQLException e) {
      // all database connection or sql exception is caught here
      System.out.println("error");
      e.printStackTrace();
    }
    return Prime_Testing.final_result;
  }

}
