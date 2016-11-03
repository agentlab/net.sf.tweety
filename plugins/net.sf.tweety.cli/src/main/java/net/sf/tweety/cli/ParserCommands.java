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

import net.sf.tweety.commons.Parser;

// @formatter:off
@Component(
	service = ParserCommands.class,
	property = { 
		"osgi.command.scope:String=tweety", 
		"osgi.command.function:String=parsers",
		"osgi.command.function:String=parser"
})
//@formatter:on
public class ParserCommands {

	private final List<ServiceReference<Parser<?>>> parsers = new CopyOnWriteArrayList<>();

	@Reference(service = Parser.class, cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	void bindParser(ServiceReference<Parser<?>> reasoner) {
		parsers.add(reasoner);
	}

	void unbindParser(ServiceReference<Parser<?>> reasoner) {
		parsers.remove(reasoner);
	}

	@Descriptor("get all parsers")
	public void parsers() {
		IntStream
				.range(0, parsers.size())
				.mapToObj(i -> i + "	" + parsers.get(i).getProperty("component.name"))
				.forEach(System.out::println);
	}

	@Descriptor("get parser by id")
	public Parser<?> parser(int index) {
		ServiceReference<Parser<?>> reference = parsers.get(index);
		return reference.getBundle().getBundleContext().getService(reference);
	}

}
