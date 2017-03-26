package hr.github.anivanovic.codetransform;

import java.util.Optional;

import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.reference.CtTypeReference;

public class SuperclassProcessor extends AbstractProcessor<CtClass<?>> {

	@Override
	public void process(CtClass<?> clazz) {
		CtTypeReference<?> superclass = clazz.getSuperclass();
		if (superclass != null) {
			Optional<Class<?>> substituteClass = TypeMapping.getSubstituteClass(superclass.getActualClass());

			substituteClass.ifPresent(substitute -> {
				CtTypeReference<?> subType = getFactory().Code().createCtTypeReference(substitute);
				clazz.setSuperclass(subType);
			});
		}
	}

}
