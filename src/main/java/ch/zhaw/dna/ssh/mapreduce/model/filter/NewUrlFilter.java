package ch.zhaw.dna.ssh.mapreduce.model.filter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Findet von allen URLs für eine bestimmte Seite, welche neu sind. z.B. lokale Seiten wie #foo referenzieren die
 * gleiche Resource. Ausserdem werden Bilder herausgefilteurt.
 * 
 * @author Reto
 * 
 */
public class NewUrlFilter {

	private static final Set<String> ENDINGS = new HashSet<String>();

	static {
		Properties p = new Properties();
		try {
			String filename = '/' + NewUrlFilter.class.getPackage().getName().replace('.', '/') + "/statics.properties";
			p.load(NewUrlFilter.class.getResourceAsStream(filename));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		for (Object key : p.keySet()) {
			ENDINGS.add((String) key);
			ENDINGS.add(((String) key).toUpperCase());
		}
	}

	public List<String> filterUrls(String base, List<String> urls) {
		URL baseUrl;
		try {
			baseUrl = new URL(base);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
		List<String> filtered = new LinkedList<String>();
		for (String url : urls) {
			try {
				URL newUrl = new URL(baseUrl, url);
				if (!newUrl.sameFile(baseUrl) && !isStatic(newUrl)) {
					filtered.add(newUrl.toExternalForm());
				}
			} catch (MalformedURLException e) {
				System.err.println("Skipping link: " + e.getMessage());

			}
		}
		return filtered;
	}

	private boolean isStatic(URL url) {
		String filename = url.getFile();
		int paramPos = filename.indexOf('?');
		if (paramPos >= 0) {
			filename = filename.substring(0, paramPos);
		}
		int pos = filename.lastIndexOf('.');
		if (pos < 0) {
			return false;
		} else {
			return ENDINGS.contains(filename.substring(pos + 1));
		}
	}

}
