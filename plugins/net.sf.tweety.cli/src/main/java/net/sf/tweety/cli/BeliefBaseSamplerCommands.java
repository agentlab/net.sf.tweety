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

import net.sf.tweety.commons.BeliefBaseSampler;

// @formatter:off
@Component(
	service = BeliefBaseSamplerCommands.class,
	property = { 
		"osgi.command.scope:String=tweety", 
		"osgi.command.function:String=samplers",
		"osgi.command.function:String=sampler"
})
//@formatter:on
public class BeliefBaseSamplerCommands {

	private final List<ServiceReference<BeliefBaseSampler<?>>> samplers = new CopyOnWriteArrayList<>();

	@Reference(service = BeliefBaseSampler.class, cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	void bindBeliefBaseSampler(ServiceReference<BeliefBaseSampler<?>> reasoner) {
		samplers.add(reasoner);
	}

	void unbindBeliefBaseSampler(ServiceReference<BeliefBaseSampler<?>> reasoner) {
		samplers.remove(reasoner);
	}

	@Descriptor("get all parsers")
	public void samplers() {
		IntStream
				.range(0, samplers.size())
				.mapToObj(i -> i + "	" + samplers.get(i).getProperty("component.name"))
				.forEach(System.out::println);
	}

	@Descriptor("get parser by id")
	public BeliefBaseSampler<?> sampler(int index) {
		ServiceReference<BeliefBaseSampler<?>> reference = samplers.get(index);
		return reference.getBundle().getBundleContext().getService(reference);
	}

}
