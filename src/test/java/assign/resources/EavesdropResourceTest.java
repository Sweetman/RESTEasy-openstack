package assign.resources;
import static org.junit.Assert.*;

import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

public class EavesdropResourceTest {
	@Test
	   public void testCustomerResource() throws Exception
	   {
	      Client client = ClientBuilder.newClient();
	      try {
	         System.out.println("*** GET Projects **");
	         String projects = client.target("http://localhost:8080/assignment4/myeavesdrop/projects").request().get(String.class);
	         assertTrue(projects.contains("3rd_party_ci"));
	         assertTrue(projects.contains("barbican"));
	         assertTrue(projects.contains("heat"));
	         assertTrue(projects.contains("%23tripleo"));
	         System.out.println("PASSED");
	         
	         System.out.println("*** GET %23openstack-api Project **");
	         String projectDetails = client.target("http://localhost:8080/assignment4/myeavesdrop/projects/%23openstack-api/irclogs").request().get(String.class);
	         assertTrue(projectDetails.contains("%23openstack-api.2015-03-05.log"));
	         assertTrue(projectDetails.contains("%23openstack-api.2015-03-06.log"));
	         assertTrue(projectDetails.contains("%23openstack-api.2015-03-07.log"));
	         assertTrue(projectDetails.contains("%23openstack-api.2015-03-08.log"));
	         System.out.println("PASSED");
	         
	         System.out.println("*** GET randomtestcase Project **");
	         int unknown = client.target("http://localhost:8080/assignment4/myeavesdrop/projects/randomtestcase/irclogs").request().get().getStatus();
	         assertTrue(unknown == 404);
	         System.out.println("PASSED");
	      } finally {
	         client.close();
	      }
	   }
}
