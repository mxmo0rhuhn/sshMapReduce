package ch.zhaw.dna.ssh.mapreduce.model.framework.registry;

import static org.junit.Assert.assertTrue;

import javax.annotation.PostConstruct;

import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.matcher.Matchers;

public class PostConstructFeatureTest {

	@Test(expected = RuntimeException.class)
	public void shouldThrowExceptionOnFailure() {
		Guice.createInjector(new PostConstructTestModule()).getInstance(FailurePostConstruct.class);
	}

	@Test
	public void shouldInvokePostConstruct() {
		SuccessfulPostConstruct instance = Guice.createInjector(new PostConstructTestModule()).getInstance(
				SuccessfulPostConstruct.class);
		assertTrue(instance.isPostConstructInvoked());
	}

}

class PostConstructTestModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(SuccessfulPostConstruct.class).to(SuccessfulPostConstructImpl.class);
		bind(FailurePostConstruct.class).to(FailurePostConstructImpl.class);

		bindListener(Matchers.any(), new PostConstructFeature());
	}

}

interface SuccessfulPostConstruct {
	boolean isPostConstructInvoked();
}

class SuccessfulPostConstructImpl implements SuccessfulPostConstruct {

	private boolean postConstructInvoked = false;

	@PostConstruct
	public void postConstruct() {
		postConstructInvoked = true;
	}

	public boolean isPostConstructInvoked() {
		return this.postConstructInvoked;
	}
}

interface FailurePostConstruct {
}

class FailurePostConstructImpl implements FailurePostConstruct {

	@PostConstruct
	public void postConstruct(Object dummy) {
	}
}