package hr.github.anivanovic.codetransform;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtFor;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtTry;
import spoon.reflect.code.CtWhile;
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
			CtIf ifSt = (CtIf) st;
			System.out.println("If st expression: " + ifSt.getCondition());
			printStatement(ifSt.getThenStatement());
			printStatement(ifSt.getElseStatement());
			break;
		case "CtInvocationImpl":
			System.out.println("Invocation st: " + st);
			break;
		case "CtLocalVariableImpl":
			System.out.println("Local variable st: " + st);
			break;
		case "CtAssignmentImpl":
			System.out.println("Assignment st: " + st);
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
			System.out.println("For st. init: " + forSt.getForInit() + ", update: " + forSt.getForUpdate() + ", expression: " + forSt.getExpression());
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

}
