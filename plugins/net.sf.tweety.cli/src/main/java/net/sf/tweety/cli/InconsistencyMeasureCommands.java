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

import net.sf.tweety.logics.commons.analysis.InconsistencyMeasure;


// @formatter:off
@Component(
	service = InconsistencyMeasureCommands.class,
	property = { 
		"osgi.command.scope:String=tweety", 
		"osgi.command.function:String=measures",
		"osgi.command.function:String=measure"
})
//@formatter:on
public class InconsistencyMeasureCommands {

	private final List<ServiceReference<InconsistencyMeasure<?>>> measures = new CopyOnWriteArrayList<>();

	@Reference(service = InconsistencyMeasure.class, cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	void bindInconsistencyMeasure(ServiceReference<InconsistencyMeasure<?>> reasoner) {
		measures.add(reasoner);
	}

	void unbindInconsistencyMeasure(ServiceReference<InconsistencyMeasure<?>> reasoner) {
		measures.remove(reasoner);
	}

	@Descriptor("get all parsers")
	public void measures() {
		IntStream
				.range(0, measures.size())
				.mapToObj(i -> i + "	" + measures.get(i).getProperty("component.name"))
				.forEach(System.out::println);
	}

	@Descriptor("get parser by id")
	public InconsistencyMeasure<?> measure(int index) {
		ServiceReference<InconsistencyMeasure<?>> reference = measures.get(index);
		return reference.getBundle().getBundleContext().getService(reference);
	}

}
