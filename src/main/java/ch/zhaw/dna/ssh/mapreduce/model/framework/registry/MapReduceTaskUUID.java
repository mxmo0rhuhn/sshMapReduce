package ch.zhaw.dna.ssh.mapreduce.model.framework.registry;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;

/**
 * Diese Annotation erstellt das Binding fuer die UUID, die wir pro MapReduceTask (Berechnung) vergeben wollen.
 * 
 * @author Reto
 * 
 */
@BindingAnnotation
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RUNTIME)
public @interface MapReduceTaskUUID {

}
