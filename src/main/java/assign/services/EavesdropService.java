package assign.services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class EavesdropService {

	static String projectHost = "http://eavesdrop.openstack.org/meetings";
	static String irclogsHost = "http://eavesdrop.openstack.org/irclogs/";
	
	public String getData() {
		return "Hello from Eavesdrop service.";
	}
	
	public ArrayList<String> listProjects(){
		ArrayList<String> resultList = new ArrayList<String>();
		try{
			// add all the projects from meetings to the list
			Document meetingsProjects = Jsoup.connect(projectHost).get();
			Elements links = meetingsProjects.select("a");
			for(Element link : links){
				String linkHref = link.attr("href");
				String linkText = link.text();
				if(linkHref.equalsIgnoreCase(linkText))
					resultList.add(linkText);
			}
			// add all the projects from irclogs to the list
			Document irclogsProjects = Jsoup.connect(irclogsHost).get();
			links = irclogsProjects.select("a");
			for(Element link : links){
				String linkHref = link.attr("href");
				String linkText = link.text();
				linkText = linkText.replace("#", "%23");
				// check if the project is already in the list before adding it
				if(linkHref.equalsIgnoreCase(linkText) && !resultList.contains(linkText))
					resultList.add(linkText);
			}
			System.out.println(resultList.size());
		} catch(IOException e){
			e.printStackTrace();
		}
		return resultList;
	}
	
	public ArrayList<String> getLinks(String project){
		ArrayList<String> resultList = new ArrayList<String>();
		String irclogProject = irclogsHost + project;
		irclogProject = irclogProject.replace("#", "%23");
		System.out.println("irclogs link: " + irclogProject);
		try {
			URL url = new URL(irclogProject);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			if(connection.getResponseCode() != 200)
				return null;
			else{
				Document doc = Jsoup.connect(irclogProject).get();
				Elements links = doc.select("body a");
				for(Element link : links){
					String s = link.text();
					s = s.replace("#", "%23");
					if(!s.equals("Name") && !s.equals("Last modified") && !s.equals("Size") && !s.equals("Description") && !s.equals("Parent Directory"))
						resultList.add(s);
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
}
