package ch.zhaw.dna.ssh.mapreduce.model.manipulation;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Macht aus relativen URLs absolute
 * 
 * @author Reto
 * 
 */
public class Absolutifier implements Manipulator {

	@Override
	public String manipulate(String baseUrl, String url) {
		URL u;
		try {
			u = new URL(baseUrl);
		} catch (MalformedURLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		System.out.println("File: " + u.getFile());
		System.out.println("Host: " + u.getHost());
		System.out.println("Path: " + u.getPath());
		System.out.println("Ref: " + u.getRef());
		
		return url;
	}

}
