package ch.zhaw.dna.ssh.mapreduce.model.framework.registry;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

/**
 * 
 * Der Java Specification Request 250 (JSR-250) definiert unter anderem die Annotation {@link @PostConstruct}. Diese
 * Klasse kann als TypeListener dem Guice Framework angehängt werden, um dieses Feature auch in Guice verwenden zu
 * können. Guice unterstützt diese Annotation per se nicht, da Guice hauptsächlich eine Implementation von JSR-299 ist.
 * 
 * Idee von: http://blog.holisticon.de/2012/08/postconstruct-mit-guice/
 * 
 */
class PostConstructFeature implements TypeListener {

	/**
	 * Diese Methode wird für jedes Type-Binding in Guice aufgerufen. Darin registrieren wir das Event afterInjection.
	 */
	@Override
	public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
		encounter.register(new InjectionListener<I>() {

			/**
			 * Der Callback für das Event-afterInjection. Diese Methode wird für jede neu injectete Instanz aufgerufen.
			 * Somit wird direkt nach dem Instanzieren nach Methoden mit der Annotation @PostConstruct gesucht. Alle
			 * diese Methoden werden dann aufgerufen. Diese Methoden dürfen keine Parameter haben und müssen public
			 * sein.
			 */
			@Override
			public void afterInjection(final I injectee) {

				List<Method> methods = filter(injectee.getClass().getMethods(), PostConstruct.class);
				for (Method m : methods) {
					try {
						m.invoke(injectee);
					} catch (Exception e) {
						throw newException(m, injectee);
					}
				}
			}

			/**
			 * Filtert für eine Liste von Methoden alle Methoden heraus, welche die Annotation @PostConstruct haben.
			 */
			private List<Method> filter(Method[] methods, Class<PostConstruct> annotation) {
				List<Method> filtered = new LinkedList<Method>();
				for (Method m : methods) {
					if (m.getAnnotation(annotation) != null) {
						filtered.add(m);
					}
				}
				return filtered;
			}
		});

	}

	/**
	 * Erstellt eine neue RuntimeExcetpiton für eine spezifizierte Methode.
	 */
	private static RuntimeException newException(Method m, Object o) {
		String msg = "Failed to invoke PostConstruct " + m.getName() + " on " + o.getClass().getName();
		return new RuntimeException(msg);
	}

}