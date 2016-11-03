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

import net.sf.tweety.logics.commons.analysis.ConsistencyTester;


// @formatter:off
@Component(
	service = ConsistencyTesterCommands.class,
	property = { 
		"osgi.command.scope:String=tweety", 
		"osgi.command.function:String=testers",
		"osgi.command.function:String=tester"
})
//@formatter:on
public class ConsistencyTesterCommands {

	private final List<ServiceReference<ConsistencyTester<?>>> testers = new CopyOnWriteArrayList<>();

	@Reference(service = ConsistencyTester.class, cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	void bindConsistencyTester(ServiceReference<ConsistencyTester<?>> reasoner) {
		testers.add(reasoner);
	}

	void unbindConsistencyTester(ServiceReference<ConsistencyTester<?>> reasoner) {
		testers.remove(reasoner);
	}

	@Descriptor("get all parsers")
	public void testers() {
		IntStream
				.range(0, testers.size())
				.mapToObj(i -> i + "	" + testers.get(i).getProperty("component.name"))
				.forEach(System.out::println);
	}

	@Descriptor("get parser by id")
	public ConsistencyTester<?> tester(int index) {
		ServiceReference<ConsistencyTester<?>> reference = testers.get(index);
		return reference.getBundle().getBundleContext().getService(reference);
	}

}
