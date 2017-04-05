package hr.github.anivanovic.codetransform;

import hr.github.anivanovic.codetransform.support.ElementSupport;

import java.util.List;
import java.util.Set;

import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtTypeReference;

public class ClassProcessor extends AbstractProcessor<CtClass<?>> {

	private ElementSupport elementSuport = new ElementSupport(getFactory());

	@Override
	public void process(CtClass<?> clazz) {
		elementSuport.setFactory(getFactory());

		Set<CtMethod<?>> methods = clazz.getMethods();
		for (CtMethod<?> ctMethod : methods) {
			elementSuport.handleElement(ctMethod);
		}

		List<CtField<?>> fields = clazz.getFields();
		for (CtField<?> ctField : fields) {
			elementSuport.handleElement(ctField);
		}

		CtTypeReference<?> superclass = clazz.getSuperclass();
		if (superclass != null) {
			elementSuport.handleElement(superclass);
		}

		elementSuport.printLeftovers();
	}


}
