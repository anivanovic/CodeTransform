package hr.github.anivanovic.codetransform;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.ModifierKind;

public class VariableDeclarationProcessor extends AbstractProcessor<CtMethod<?>> {

	private static Set<String> statementPossible = new HashSet<>();

	@Override
	public void process(CtMethod<?> met) {
		if (met != null) {
			System.out.println("Visibility: " + met.getVisibility());
			
			Set<ModifierKind> modifiers = met.getModifiers();
			if (modifiers != null && !modifiers.isEmpty()) {
				for (ModifierKind modifier : modifiers) {
					System.out.println("Modifier: " + modifier.name());
					
				}
			}
			
			System.out.println("Return type: " + met.getType().getQualifiedName());
			System.out.println("Name: " + met.getSimpleName());

			List<CtParameter<?>> parameters = met.getParameters();
			if (parameters != null && !parameters.isEmpty()) {
				System.out.println("Param number: " + parameters.size());
				parameters.stream()
						.forEach(param -> {
							System.out.print("\t");
							System.out.println("param type: " + param.getType().getQualifiedName() + ", param name: " + param.getSimpleName());
						});
			}

			System.out.println("Signature: " + met.getSignature());
			processMethodBody(met.getBody());
			System.out.println("----------------------------------------");
		}

		for (String poss : statementPossible) {
			System.out.println(poss);
		}
	}

	private void processMethodBody(CtBlock<?> body) {
		System.out.println("Body: " + body);
		if (body != null) {
			List<CtStatement> statements = body.getStatements();
			statements.stream().forEach(st -> {
				printStatement(st);
			});
		}
	}

	private void printStatement(CtStatement st) {
		if (st == null)
			return;

		switch (st.getClass().getSimpleName()) {
		case "CtIfImpl":
			System.out.println("Position: " + st.getPosition().getFile().getName() + ":" + st.getPosition().getLine() + " if st: " + st);
			CtIf ifSt = (CtIf) st;
			printStatement(ifSt.getElseStatement());
			printStatement(ifSt.getElseStatement());
			break;
		case "class CtInvocationImpl":

			break;
		case "class CtLocalVariableImpl":

			break;
		case "class CtAssignmentImpl":

			break;
		case "class CtTryImpl":

			break;
		case "class CtReturnImpl":

			break;
		case "CtBlock":
			processMethodBody((CtBlock<?>) st);
		default:
			statementPossible.add(st.getClass().getSimpleName());
			break;
		}
	}

}
