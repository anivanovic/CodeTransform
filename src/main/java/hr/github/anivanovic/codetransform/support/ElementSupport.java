package hr.github.anivanovic.codetransform.support;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import com.google.gwt.dev.util.collect.HashMap;

import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtFor;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtNewClass;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtTry;
import spoon.reflect.code.CtTypeAccess;
import spoon.reflect.code.CtWhile;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.CtTypedElement;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;

public class ElementSupport {

	private Factory factory;

	public ElementSupport(Factory factory) {
		this.setFactory(factory);
	}

	private static Map<String, CtElement> statementPossible = new HashMap<>();

	private void processMethod(CtMethod<?> met) {
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
			handleElement(met.getBody());
			System.out.println("----------------------------------------");
		}
	}

	public void printLeftovers() {
		for (Entry<String, CtElement> entry : statementPossible.entrySet()) {
			String key = entry.getKey();
			CtElement value = entry.getValue();
			System.out.println(key + ": " + value);
		}
	}

	private void processBody(CtBlock<?> body) {
		System.out.println("Body: " + body);
		if (body != null) {
			List<CtStatement> statements = body.getStatements();
			statements.stream().forEach(st -> {
				handleElement(st);
			});
		}
	}

	public void handleElement(CtElement st) {
		if (st == null)
			return;

		switch (st.getClass().getSimpleName()) {
			case "CtMethodImpl":
				processMethod((CtMethod<?>) st);
				break;
			case "CtIfImpl":
				CtIf ifSt = (CtIf) st;
				System.out.println("If st expression: " + ifSt.getCondition());
				handleElement(ifSt.getThenStatement());
				handleElement(ifSt.getElseStatement());
				break;
			case "CtInvocationImpl":
				System.out.println("Invocation st: " + st);
				CtInvocation<?> invocateSt = (CtInvocation<?>) st;
				List<CtExpression<?>> arguments = invocateSt.getArguments();
				arguments.stream().filter(arg -> arg != null && !"null".equals(arg.toString()))
						.forEach(this::handleElement);

				if (invocateSt.getTarget() != null) {
					handleElement(invocateSt.getTarget());
				}
				System.out.println(invocateSt.getTarget());
				System.out.println(arguments);
				System.out.println(invocateSt.getExecutable());

				CtExecutableReference<?> executable = invocateSt.getExecutable();
				if (executable.getSimpleName().contains("addCommand")) {
					changeMethodCall(executable);
				}
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
				handleElement(localVar.getAssignment());
				System.out.println(localVar.getSimpleName());
				break;
			case "CtAssignmentImpl":
				System.out.println("Assignment st: " + st);
				CtAssignment<?, ?> assignmentSt = (CtAssignment<?, ?>) st;
				System.out.println(assignmentSt.getAssigned());
				System.out.println(assignmentSt.getAssignment());
				System.out.println(assignmentSt.getTypeCasts());

				changeType(assignmentSt);
				CtExpression<?> assignment1 = assignmentSt.getAssignment();
				if (assignment1 != null && !"null".equals(assignment1.toString())) {
					changeType(assignment1);
				}
				break;
			case "CtTryImpl":
				System.out.println("Try st: " + st);
				CtTry trySt = (CtTry) st;
				processBody(trySt.getBody());
				break;
			case "CtReturnImpl":
				System.out.println("Return st: " + st);
				break;
			case "CtBlockImpl":
				processBody((CtBlock<?>) st);
				break;
			case "CtForImpl":
				CtFor forSt = (CtFor) st;
				System.out.println("For st. init: " + forSt.getForInit() + ", update: "
						+ forSt.getForUpdate() + ", expression: " + forSt.getExpression());
				handleElement(forSt.getBody());
				break;
			case "CtWhileImpl":
				CtWhile whileSt = (CtWhile) st;
				System.out.println("While st. condition: " + whileSt.getLoopingExpression());
				handleElement(whileSt.getBody());
				break;
			case "CtOperatorAssignmentImpl":
				System.out.println("Operator assignment st: " + st);
				break;
			case "CtCommentImpl":
				System.out.println("Comment: " + st);
				break;
			case "CtConstructorCallImpl":
				CtConstructorCall<?> consCall = (CtConstructorCall<?>) st;
				consCall.getArguments().stream()
						.filter(arg -> arg != null && !"null".equals(arg.toString()))
						.forEach(this::handleElement);
				changeType(consCall);
				break;
			case "CtFieldImpl":
				CtField<?> field = (CtField<?>) st;
				CtExpression<?> assignment2 = field.getAssignment();
				if (assignment2 != null && !"null".equals(assignment2.toString())) {
					changeType(assignment2);
				}
				changeType(field);
				break;
			case "CtNewClassImpl":
				CtNewClass<?> newClass = (CtNewClass<?>) st;
				changeType(newClass);
				break;
			case "CtConstructorImpl":
				CtConstructor<?> cons = (CtConstructor<?>) st;
				handleElement(cons.getBody());
				List<CtParameter<?>> parameters = cons.getParameters();
				parameters.stream().forEach(this::changeType);
				break;
			case "CtTypeReferenceImpl":
				// do nothing
				break;
			case "CtTypeAccessImpl":
				changeAccessType((CtTypeAccess<?>) st);
				break;
			default:
				statementPossible.put(st.getClass().getSimpleName(), st);
				break;
		}
	}

	private <T> void changeAccessType(CtTypeAccess<T> st) {
		CtTypeReference<T> type = st.getAccessedType();
		Optional<Class<?>> substituteOpt = TypeMapping.getSubstituteClass(type.getActualClass());

		substituteOpt.ifPresent(clazz -> {
			final CtTypeReference<T> newType =
					this.getFactory().Code().createCtTypeReference(clazz);
			st.setAccessedType(newType);
		});
	}

	public <T> void changeType(CtTypedElement<T> typedElem) {
		CtTypeReference<T> type = typedElem.getType();
		Optional<Class<?>> substituteOpt = TypeMapping.getSubstituteClass(type.getActualClass());

		substituteOpt.ifPresent(clazz -> {
			final CtTypeReference<T> newType =
					this.getFactory().Code().createCtTypeReference(clazz);
			typedElem.setType(newType);
		});
	}

	public void changeMethodCall(CtExecutableReference<?> executable) {
		Class<?> actualClass = executable.getDeclaringType().getActualClass();
		Optional<String> methodSubstitute =
				MethodMapping.getMethodSubstitute(actualClass, executable.getSimpleName());
		methodSubstitute.ifPresent(sub -> executable.setSimpleName(sub));
	}

	/**
	 * @return the factory
	 */
	public Factory getFactory() {
		return factory;
	}

	/**
	 * @param factory the factory to set
	 */
	public void setFactory(Factory factory) {
		this.factory = factory;
	}

}
