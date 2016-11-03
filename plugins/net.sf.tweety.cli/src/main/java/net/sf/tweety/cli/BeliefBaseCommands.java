package net.sf.tweety.cli;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

import org.apache.felix.service.command.Descriptor;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import net.sf.tweety.commons.BeliefBase;

// @formatter:off
@Component(
	service = BeliefBaseCommands.class,
	property = { 
		"osgi.command.scope:String=tweety", 
		"osgi.command.function:String=bases",
		"osgi.command.function:String=base"
})
//@formatter:on
public class BeliefBaseCommands {

	private final List<ServiceReference<BeliefBase<?>>> bases = new CopyOnWriteArrayList<>();

	@Reference(service = BeliefBase.class, cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	void bindBeliefBase(ServiceReference<BeliefBase<?>> reasoner) {
		bases.add(reasoner);
	}

	void unbindBeliefBase(ServiceReference<BeliefBase<?>> reasoner) {
		bases.remove(reasoner);
	}

	@Descriptor("get all belief bases")
	public void bases() {
		IntStream
				.range(0, bases.size())
				.mapToObj(i -> i + "	" + bases.get(i).getProperty("component.name"))
				.forEach(System.out::println);
	}

	@Descriptor("get belief base instance by id")
	public BeliefBase<?> base(int index) {
		ServiceReference<BeliefBase<?>> reference = bases.get(index);
		return reference.getBundle().getBundleContext().getService(reference);
	}

}
