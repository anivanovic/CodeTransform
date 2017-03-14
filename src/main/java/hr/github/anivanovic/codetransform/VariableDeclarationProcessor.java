package hr.github.anivanovic.codetransform;

import java.util.Optional;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtExpression;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.reference.CtTypeReference;

public class VariableDeclarationProcessor extends AbstractProcessor<CtVariable<?>> {

	@Override
	public void process(CtVariable<?> element) {
		CtExpression<?> expression = element.getDefaultExpression();
		if (expression == null)
			return;

		CtTypeReference<?> varType = expression.getType();

		if (varType != null) {
			Optional<Class<?>> substituteClass =
					TypeMapping.getSubstituteClass(varType.getActualClass());
			substituteClass.ifPresent(clazz -> {
				CtTypeReference newType = getFactory().Code().createCtTypeReference(clazz);
				expression.setType(newType);
			});
		}
	}

}
