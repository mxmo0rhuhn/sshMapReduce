package ch.zhaw.dna.ssh.mapreduce.model.framework.registry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.*;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;

/**
 * Annotation für spezifischere Bindings. Mit dieser Guice Annotation kann ein Binding eines Interface weiter
 * eingeschränkt werden. z.B. Gibt es das Interface ExecutorService und in manchen Fällen ist man sich sicher, dass man
 * einen SingleThreaded ExecutorService will. So kann dann ein ExecutorService speziell gebindet werden, wenn er mit
 * SingleThreaded annotiert ist.
 * 
 * @author Reto
 * 
 * @see MapReduceConfig#createSingle()
 * 
 */
@BindingAnnotation
@Target({ ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RUNTIME)
public @interface SingleThreaded {

}
