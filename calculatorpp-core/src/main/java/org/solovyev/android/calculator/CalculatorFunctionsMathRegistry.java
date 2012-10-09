/*
 * Copyright (c) 2009-2011. Created by serso aka se.solovyev.
 * For more information, please, contact se.solovyev@gmail.com
 * or visit http://se.solovyev.org
 */

package org.solovyev.android.calculator;

import jscl.math.function.CustomFunction;
import jscl.math.function.Function;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.calculator.model.AFunction;
import org.solovyev.android.calculator.model.Functions;
import org.solovyev.common.JBuilder;
import org.solovyev.common.math.MathRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * User: serso
 * Date: 11/17/11
 * Time: 11:28 PM
 */
public class CalculatorFunctionsMathRegistry extends AbstractCalculatorMathRegistry<Function, AFunction> {

    @NotNull
	private static final Map<String, String> substitutes = new HashMap<String, String>();
	static {
		substitutes.put("√", "sqrt");
	}

	@NotNull
	private static final String FUNCTION_DESCRIPTION_PREFIX = "c_fun_description_";

	public CalculatorFunctionsMathRegistry(@NotNull MathRegistry<Function> functionsRegistry,
                                           @NotNull MathEntityDao<AFunction> mathEntityDao) {
		super(functionsRegistry, FUNCTION_DESCRIPTION_PREFIX, mathEntityDao);
	}

	@Override
	public void load() {
		super.load();

		add(new CustomFunction.Builder(true, "log", new String[]{"base", "x"}, "ln(x)/ln(base)"));
	}

	@NotNull
	@Override
	protected Map<String, String> getSubstitutes() {
		return substitutes;
	}

    @Override
    public String getCategory(@NotNull Function function) {
        for (FunctionCategory category : FunctionCategory.values()) {
            if ( category.isInCategory(function) ) {
                return category.name();
            }
        }
        
        return null;
    }

	@NotNull
	@Override
	protected JBuilder<? extends Function> createBuilder(@NotNull AFunction entity) {
		return new CustomFunction.Builder(entity.getName(), entity.getParameterNamesAsArray(), entity.getContent());
	}

    @Override
	protected AFunction transform(@NotNull Function entity) {
		if (entity instanceof CustomFunction) {
			final AFunction result = new AFunction();
			result.setName(entity.getName());
			result.setContent(((CustomFunction) entity).getContent());
			result.setParameterNames(((CustomFunction) entity).getParameterNames());
			return result;
		} else {
			return null;
		}
	}

	@NotNull
	@Override
	protected MathEntityPersistenceContainer<AFunction> createPersistenceContainer() {
		return new Functions();
	}
}