package ch.zhaw.dna.ssh.mapreduce.model.filter;

/**
 * Filter-Combiner kombiniert eine Menge von Filtern zu einem. Um vom Filter akzeptiert zu werden, muessen alle
 * einzelnen akzeptieren.
 * 
 * @author Reto
 * 
 */
public class AllFilter implements Filter {

	private final Filter[] filters;

	public AllFilter(Filter... filters) {
		this.filters = filters;
	}

	@Override
	public boolean accept(String url) {
		for (Filter f : filters) {
			if (!f.accept(url)) {
				return false;
			}
		}
		return true;
	}

}
