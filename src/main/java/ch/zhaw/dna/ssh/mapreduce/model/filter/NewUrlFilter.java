package ch.zhaw.dna.ssh.mapreduce.model.filter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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
	
	private static final Set<String> PROTOCOLS = new HashSet<String>();
	
	private static final List<String> PREFIX = new ArrayList<String>();
	
	private static final String DEFAULT_PROTOCOL = "http://";

	static {
		
		// FILE ENDINGS
		{Properties f = new Properties();
		try {
			String filename = '/' + NewUrlFilter.class.getPackage().getName().replace('.', '/') + "/endings-blacklist.properties";
			f.load(NewUrlFilter.class.getResourceAsStream(filename));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		for (Object key : f.keySet()) {
			ENDINGS.add((String) key);
			ENDINGS.add(((String) key).toUpperCase());
		}
		}
		
		// PROTOCOLS
		{Properties p = new Properties();
		try {
			String filename = '/' + NewUrlFilter.class.getPackage().getName().replace('.', '/') + "/protocols-whitelist.properties";
			p.load(NewUrlFilter.class.getResourceAsStream(filename));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		for (Object key : p.keySet()) {
			PROTOCOLS.add((String) key);
			PROTOCOLS.add(((String) key).toUpperCase());
		}
		}
		
		// PREFIX
		{Properties p = new Properties();
		try {
			String filename = '/' + NewUrlFilter.class.getPackage().getName().replace('.', '/') + "/prefix-blacklist.properties";
			p.load(NewUrlFilter.class.getResourceAsStream(filename));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		for (Object key : p.keySet()) {
			PREFIX.add((String) key);
			PREFIX.add(((String) key).toUpperCase());
		}
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
				URL newUrl = new URL(baseUrl, addDefaultProtocol(url));
				if (!newUrl.sameFile(baseUrl) && !hasBlacklistedPrefix(url) && hasAllowedProtocol(newUrl) && hasAllowedEnding(newUrl)) {
					filtered.add(newUrl.toExternalForm());
				}
			} catch (MalformedURLException e) {
				System.err.println("Skipping link: " + url + " (" + e.getMessage() + ")");
			}
		}
		return filtered;
	}

	private boolean hasBlacklistedPrefix(String url) {
		for (String prefix : PREFIX) {
			if (url.startsWith(prefix)) {
				return true;
			}
		}
		return false;
	}

	private String addDefaultProtocol(String url) {
		return url.length() > 2 && url.substring(0, 3).equals("www") ? DEFAULT_PROTOCOL + url : url;
	}

	private boolean hasAllowedEnding(URL url) {
		String filename = url.getFile();
		int paramPos = filename.indexOf('?');
		if (paramPos >= 0) {
			filename = filename.substring(0, paramPos);
		}
		int pos = filename.lastIndexOf('.');
		if (pos < 0) {
			return true;
		} else {
			return !ENDINGS.contains(filename.substring(pos + 1));
		}
	}

	private boolean hasAllowedProtocol(URL url) {
		return PROTOCOLS.contains(url.getProtocol());
	}
}
