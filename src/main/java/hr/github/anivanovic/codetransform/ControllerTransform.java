package hr.github.anivanovic.codetransform;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtTypeReference;

public class ControllerTransform extends AbstractProcessor<CtClass<?>> {
	
	private static final Set<ModifierKind> PUBLIC = new HashSet<>(Arrays.asList(ModifierKind.PUBLIC));
	
	@Override
	public void process(CtClass<?> clazz) {
		CtTypeReference<?> superclass = clazz.getSuperclass();
		if (superclass != null && superclass.getSimpleName().equals("Controller")) {
			System.out.println(clazz.getQualifiedName());

			List<CtField<?>> fields = clazz.getFields();

			fields.stream().filter(field -> {
				return field.getType().getTypeDeclaration().getActualClass() == Integer.class;
			}).forEach(field -> {
				field.setSimpleName("promjenjeno");
			});

			final CtTypeReference<Date> dateType = getFactory().Code().createCtTypeReference(Date.class);
			final CtField<Date> dateField = getFactory().Core().<Date> createField();
			dateField.setSimpleName("date");
			dateField.setModifiers(PUBLIC);

			clazz.addField(dateField);
		}
	}
}
