package hr.github.anivanovic.codetransform;

import java.util.Optional;

import org.apache.log4j.Logger;

import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtField;
import spoon.reflect.reference.CtTypeReference;

public class VariableDeclarationProcessor extends AbstractProcessor<CtField<?>> {

	private final Logger log = Logger.getLogger(VariableDeclarationProcessor.class);

	@Override
	public void process(CtField<?> element) {
		CtTypeReference<?> type = element.getType();
		if (type == null)
			return;

		if (type != null) {
			Optional<Class<?>> substituteClass =
					TypeMapping.getSubstituteClass(type.getQualifiedName());

			if (substituteClass.isPresent()) {
				CtTypeReference newType =
						getFactory().Code().createCtTypeReference(substituteClass.get());
				element.setType(newType);
			}
		} else {
			log.info("No type - " + type);
		}
	}

}
