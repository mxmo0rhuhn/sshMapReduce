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
	public String manipulate(String baseUrl, String url) throws CannotManipulateException {
		try {
			return new URL(new URL(baseUrl), url).toString();
		} catch (MalformedURLException e) {
			throw new CannotManipulateException(e);
		}
	}

}
