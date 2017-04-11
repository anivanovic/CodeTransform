package hr.github.anivanovic.codetransform.support;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.gwt.user.client.DeferredCommand;

public class MethodMapping {

	private MethodMapping() {}

	private static Map<Class<?>, Map<String, String>> classToMethods = new HashMap<>();

	static {
		Map<String, String> methodsMap = new HashMap<>();
		methodsMap.put("addCommand", "scheduleDeferred");

		classToMethods.put(DeferredCommand.class, methodsMap);
	}

	public static Optional<String> getMethodSubstitute(Class<?> clazz, String methodName) {
		Map<String, String> methodsMaps = classToMethods.get(clazz);
		if (methodsMaps != null) {
			return Optional.ofNullable(methodsMaps.get(methodName));
		}

		return Optional.empty();
	}

}
