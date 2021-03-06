package ch.zhaw.dna.ssh.mapreduce.model.filter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
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

	private static final Set<String> ENDINGS_BLACKLIST = new HashSet<String>();

	private static final Set<String> PROTOCOLS_WHITELIST = new HashSet<String>();

	private static final List<String> PREFIX_BLACKLIST = new ArrayList<String>();

	private static final List<String> SUBSTRING_BLACKLIST = new ArrayList<String>();

	private static final List<String> SUBSTRING_WHITELIST = new ArrayList<String>();

	private static final String DEFAULT_PROTOCOL = "http://";

	static {

		// FILE ENDINGS
		{
			Properties f = new Properties();
			try {
				String filename = '/' + NewUrlFilter.class.getPackage().getName().replace('.', '/')
						+ "/endings-blacklist.properties";
				f.load(NewUrlFilter.class.getResourceAsStream(filename));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			for (Object key : f.keySet()) {
				ENDINGS_BLACKLIST.add((String) key);
				ENDINGS_BLACKLIST.add(((String) key).toUpperCase());
			}
		}

		// PROTOCOLS
		{
			Properties p = new Properties();
			try {
				String filename = '/' + NewUrlFilter.class.getPackage().getName().replace('.', '/')
						+ "/protocols-whitelist.properties";
				p.load(NewUrlFilter.class.getResourceAsStream(filename));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			for (Object key : p.keySet()) {
				PROTOCOLS_WHITELIST.add((String) key);
				PROTOCOLS_WHITELIST.add(((String) key).toUpperCase());
			}
		}

		// PREFIX
		{
			Properties p = new Properties();
			try {
				String filename = '/' + NewUrlFilter.class.getPackage().getName().replace('.', '/')
						+ "/prefix-blacklist.properties";
				p.load(NewUrlFilter.class.getResourceAsStream(filename));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			for (Object key : p.keySet()) {
				PREFIX_BLACKLIST.add((String) key);
				PREFIX_BLACKLIST.add(((String) key).toUpperCase());
			}
		}

		// SUBSTRING BLACKLIST
		{
			Properties p = new Properties();
			try {
				String filename = '/' + NewUrlFilter.class.getPackage().getName().replace('.', '/')
						+ "/substring-blacklist.properties";
				p.load(NewUrlFilter.class.getResourceAsStream(filename));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			for (Object key : p.keySet()) {
				SUBSTRING_BLACKLIST.add((String) key);
				SUBSTRING_BLACKLIST.add(((String) key).toUpperCase());
			}
		}

		// SUBSTRING WHITELIST
		{
			Properties p = new Properties();
			try {
				String filename = '/' + NewUrlFilter.class.getPackage().getName().replace('.', '/')
						+ "/substring-whitelist.properties";
				p.load(NewUrlFilter.class.getResourceAsStream(filename));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			for (Object key : p.keySet()) {
				SUBSTRING_WHITELIST.add((String) key);
				SUBSTRING_WHITELIST.add(((String) key).toUpperCase());
			}
		}
	}

	public Set<String> filterUrls(String base, List<String> urls) {
		if (urls == null) {
			return Collections.emptySet();
		}
		URL baseUrl;
		try {
			baseUrl = new URL(base);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return Collections.emptySet();
		}
		Set<String> filtered = new HashSet<String>(urls.size());
		for (String url : urls) {
			try {
				if (hasBlacklistedPrefix(url) || hasBlacklistedSubstring(url)) {
					continue;
				}
				URL newUrl = new URL(baseUrl, addDefaultProtocol(url));
				if (!hasWhitelistedSubstring(newUrl)) {
					continue;
				}
				if (!newUrl.sameFile(baseUrl) && hasAllowedProtocol(newUrl) && hasAllowedEnding(newUrl)) {
					filtered.add(newUrl.toExternalForm());
				}
			} catch (MalformedURLException e) {
				System.err.println("Skipping link: " + url + " (" + e.getMessage() + ")");
			}
		}
		return filtered;
	}

	private boolean hasWhitelistedSubstring(URL url) {
		String fullpath = url.toExternalForm();
		for (String substr : SUBSTRING_WHITELIST) {
			if (fullpath.contains(substr)) {
				return true;
			}
		}
		return false;
	}

	private boolean hasBlacklistedSubstring(String url) {
		for (String substr : SUBSTRING_BLACKLIST) {
			if (url.contains(substr)) {
				return true;
			}
		}
		return false;
	}

	private boolean hasBlacklistedPrefix(String url) {
		for (String prefix : PREFIX_BLACKLIST) {
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
			return !ENDINGS_BLACKLIST.contains(filename.substring(pos + 1));
		}
	}

	private boolean hasAllowedProtocol(URL url) {
		return PROTOCOLS_WHITELIST.contains(url.getProtocol());
	}
}
