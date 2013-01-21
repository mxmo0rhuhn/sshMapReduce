package ch.zhaw.dna.ssh.mapreduce.model.filter;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * Filtert Links auf auf spezifischen Dateiendungen heraus.
 * 
 * @author Reto
 * 
 */
public class ExtensionsFilter implements Filter {

	private static final Set<String> ENDINGS = new HashSet<String>();

	static {
		ENDINGS.add("jpg");
		ENDINGS.add("JPG");
		ENDINGS.add("jpeg");
		ENDINGS.add("JPEG");

		ENDINGS.add("png");
		ENDINGS.add("PNG");

		ENDINGS.add("gif");
		ENDINGS.add("GIF");

		ENDINGS.add("svg");
		ENDINGS.add("SVG");

		ENDINGS.add("ico");
		ENDINGS.add("ICO");
		
		ENDINGS.add("css");
		ENDINGS.add("CSS");
	}

	@Override
	public boolean accept(String url) {
		int pos = url.lastIndexOf('.');
		if (pos < 0) {
			return true;
		} else {
			int paramPos = url.indexOf('?');
			String ending = 
			if (paramPos >= 0) {
				ending = url.substring(pos + 1, paramPos);
			} else {
				ending = url.substring(pos + 1);
			}
			return !ENDINGS.contains(ending);
		}
	}

}
