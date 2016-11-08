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

import net.sf.tweety.commons.BeliefBaseMachineShop;

//@formatter:off
@Component(
	service = BeliefBaseMachineShopCommands.class,
	property = { 
		"osgi.command.scope:String=tweety", 
		"osgi.command.function:String=shops",
		"osgi.command.function:String=shop"
})
//@formatter:on
public class BeliefBaseMachineShopCommands {

	private final List<ServiceReference<BeliefBaseMachineShop<?>>> shops = new CopyOnWriteArrayList<>();

	@Reference(service = BeliefBaseMachineShop.class, cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	void bindBeliefBaseMachineShop(ServiceReference<BeliefBaseMachineShop<?>> reasoner) {
		shops.add(reasoner);
	}

	void unbindBeliefBaseMachineShop(ServiceReference<BeliefBaseMachineShop<?>> reasoner) {
		shops.remove(reasoner);
	}

	@Descriptor("get all belief base machine shops")
	public void shops() {
		if (shops.isEmpty()){
			System.out.println("There is no Belief Base Shops registered");
		} else {
			System.out.println("ID	Belief Base Shops");
		}
		IntStream
				.range(0, shops.size())
				.mapToObj(i -> i + "	" + shops.get(i).getProperty("component.name"))
				.forEach(System.out::println);
	}

	@Descriptor("get belief base machine shop by id")
	public BeliefBaseMachineShop<?> shop(int index) {
		ServiceReference<BeliefBaseMachineShop<?>> reference = shops.get(index);
		return reference.getBundle().getBundleContext().getService(reference);
	}

}
