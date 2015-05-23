package assign.resources;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import assign.domain.Project;
import assign.domain.Projects;
import assign.services.EavesdropService;

@Path("/myeavesdrop")
public class EavesdropResource {
	
	EavesdropService eavesdropService;
	
	public EavesdropResource() {
		this.eavesdropService = new EavesdropService();
	}
	
	@GET
	@Path("/helloworld")
	@Produces("text/html")
	public String helloWorld() {
		return "Hello world";		
	}
	
	@GET
	@Path("/helloeavesdrop")
	@Produces("text/html")
	public String helloEavesdrop() {
		return this.eavesdropService.getData();		
	}	
	
	@GET
	@Path("/projects")
	@Produces("application/xml")
	public StreamingOutput getAllProjects() throws Exception {
		Project heat = new Project();
		heat.setName("%23heat");
		
		final Projects projects = new Projects();
		
		ArrayList<String> projectList = this.eavesdropService.listProjects();
		projects.setProjects(projectList);
			    
	    return new StreamingOutput() {
	         public void write(OutputStream outputStream) throws IOException, WebApplicationException {
	            outputCourses(outputStream, projects);
	         }
	      };	    
	}	
	
	@GET
	@Path("/projects/{project}/irclogs")
	@Produces("application/xml")
	public StreamingOutput getProject(@PathParam("project") String project) throws Exception {
		final Project p = new Project();
		p.setName(project);
		System.out.println("project: "+ project);
		ArrayList<String> linkList = this.eavesdropService.getLinks(project);
		if(linkList == null){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		p.setLinks(linkList);
			    
	    return new StreamingOutput() {
	         public void write(OutputStream outputStream) throws IOException, WebApplicationException {
	            outputCourses(outputStream, p);
	         }
	      };	    
	}		
	
	protected void outputCourses(OutputStream os, Projects projects) throws IOException {
		try { 
			JAXBContext jaxbContext = JAXBContext.newInstance(Projects.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(projects, os);
		} catch (JAXBException jaxb) {
			jaxb.printStackTrace();
			throw new WebApplicationException();
		}
	}	
	
	protected void outputCourses(OutputStream os, Project project) throws IOException {
		try { 
			JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(project, os);
		} catch (JAXBException jaxb) {
			jaxb.printStackTrace();
			throw new WebApplicationException();
		}
	}
}
