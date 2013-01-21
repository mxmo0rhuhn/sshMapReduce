package ch.zhaw.dna.ssh.mapreduce.model.filter;

import java.util.HashSet;
import java.util.Set;

/**
 * Filtert Links auf Bilder heraus.
 * 
 * @author Reto
 *
 */
public class ImageFilter implements Filter {
	
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
	}

	@Override
	public boolean accept(String url) {
		int pos = url.lastIndexOf('.');
		if (pos == -1) {
			return true;
		}
		String ending = url.substring(pos + 1);
		return !ENDINGS.contains(ending);
	}

}
