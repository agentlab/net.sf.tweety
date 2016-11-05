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

import net.sf.tweety.commons.Reasoner;

// @formatter:off
@Component(
	service = ReasonerCommands.class,
	property = { 
		"osgi.command.scope:String=tweety", 
		"osgi.command.function:String=reasoners",
		"osgi.command.function:String=reasoner"
})
//@formatter:on
public class ReasonerCommands {

	private final List<ServiceReference<Reasoner<?, ?>>> reasoners = new CopyOnWriteArrayList<>();

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	void bindReasoner(ServiceReference<Reasoner<?, ?>> reasoner) {
		reasoners.add(reasoner);
	}

	void unbindReasoner(ServiceReference<Reasoner<?, ?>> reasoner) {
		reasoners.remove(reasoner);
	}

	@Descriptor("get all reasoners")
	public void reasoners() {
		if (reasoners.isEmpty()){
			System.out.println("There is no Belief Base Reasoners registered");
		} else {
			System.out.println("ID	Belief Base Reasoners");
		}
		IntStream
				.range(0, reasoners.size())
				.mapToObj(i -> i + "	" + reasoners.get(i).getProperty("component.name"))
				.forEach(System.out::println);
	}

	@Descriptor("get reasoner by id")
	public Reasoner<?, ?> reasoner(int index) {
		ServiceReference<Reasoner<?, ?>> reference = reasoners.get(index);
		return reference.getBundle().getBundleContext().getService(reference);
	}

}
