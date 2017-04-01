package hr.github.anivanovic.codetransform;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtFor;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtTry;
import spoon.reflect.code.CtWhile;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.CtTypedElement;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtTypeReference;

public class VariableDeclarationProcessor extends AbstractProcessor<CtMethod<?>> {

	private static Set<String> statementPossible = new HashSet<>();

	@Override
	public void process(CtMethod<?> met) {
		if (met != null) {

			System.out.println(
					"Position: " + met.getPosition().getFile() + ":" + met.getPosition().getLine());
			System.out.println("Visibility: " + met.getVisibility());

			Set<ModifierKind> modifiers = met.getModifiers();
			if (modifiers != null && !modifiers.isEmpty()) {
				for (ModifierKind modifier : modifiers) {
					System.out.println("Modifier: " + modifier.name());

				}
			}

			changeType(met);

			System.out.println("Return type: " + met.getType().getQualifiedName());
			System.out.println("Name: " + met.getSimpleName());

			List<CtParameter<?>> parameters = met.getParameters();
			if (parameters != null && !parameters.isEmpty()) {
				System.out.println("Param number: " + parameters.size());
				parameters.stream().forEach(param -> {
					System.out.print("\t");
					System.out.println("param type: " + param.getType().getQualifiedName()
							+ ", param name: " + param.getSimpleName());
					changeType(param);
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
				CtIf ifSt = (CtIf) st;
				System.out.println("If st expression: " + ifSt.getCondition());
				printStatement(ifSt.getThenStatement());
				printStatement(ifSt.getElseStatement());
				break;
			case "CtInvocationImpl":
				System.out.println("Invocation st: " + st);
				CtInvocation<?> invocateSt = (CtInvocation<?>) st;
				List<CtExpression<?>> arguments = invocateSt.getArguments();
				arguments.stream().filter(arg -> arg != null && !"null".equals(arg.toString()))
						.forEach(arg -> changeType(arg));

				System.out.println(invocateSt.getTarget());
				System.out.println(arguments);
				System.out.println(invocateSt.getExecutable());

				break;
			case "CtLocalVariableImpl":
				System.out.println("Local variable st: " + st);
				CtLocalVariable<?> localVar = (CtLocalVariable<?>) st;
				changeType(localVar);
				CtExpression<?> assignment = localVar.getAssignment();
				if (assignment != null && !"null".equals(assignment.toString())) {
					changeType(assignment);
				}

				System.out.println(localVar.getType());
				System.out.println(localVar.getAssignment());
				System.out.println(localVar.getSimpleName());
				break;
			case "CtAssignmentImpl":
				System.out.println("Assignment st: " + st);
				CtAssignment<?, ?> assignmentSt = (CtAssignment<?, ?>) st;
				System.out.println(assignmentSt.getAssigned());
				System.out.println(assignmentSt.getAssignment());
				System.out.println(assignmentSt.getTypeCasts());

				changeType(assignmentSt);
				break;
			case "CtTryImpl":
				System.out.println("Try st: " + st);
				CtTry trySt = (CtTry) st;
				processMethodBody(trySt.getBody());
				break;
			case "CtReturnImpl":
				System.out.println("Return st: " + st);
				break;
			case "CtBlockImpl":
				processMethodBody((CtBlock<?>) st);
				break;
			case "CtForImpl":
				CtFor forSt = (CtFor) st;
				System.out.println("For st. init: " + forSt.getForInit() + ", update: "
						+ forSt.getForUpdate() + ", expression: " + forSt.getExpression());
				printStatement(forSt.getBody());
				break;
			case "CtWhileImpl":
				CtWhile whileSt = (CtWhile) st;
				System.out.println("While st. condition: " + whileSt.getLoopingExpression());
				printStatement(whileSt.getBody());
				break;
			case "CtOperatorAssignmentImpl":
				System.out.println("Operator assignment st: " + st);
				break;
			case "CtCommentImpl":
				System.out.println("Comment: " + st);
				break;
			default:
				statementPossible.add(st.getClass().getSimpleName());
				break;
		}
	}

	private <T> void changeType(CtTypedElement<T> typedElem) {
		CtTypeReference<T> type = typedElem.getType();
		typedElem.setType(type);
		Optional<Class<?>> substituteOpt = TypeMapping.getSubstituteClass(type.getActualClass());

		substituteOpt.ifPresent(clazz -> {
			final CtTypeReference<T> newType = getFactory().Code().createCtTypeReference(clazz);
			typedElem.setType(newType);
		});
	}

}
