/*
 * Copyright 2012 Vasily Shiyan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package see.parser.config;

import com.google.common.base.Function;
import see.functions.arithmetic.*;
import see.functions.bool.And;
import see.functions.bool.Not;
import see.functions.bool.Or;
import see.functions.collections.MakeList;
import see.functions.collections.MakeMap;
import see.functions.common.AddOrConcat;
import see.functions.compare.*;
import see.functions.properties.GetProperty;
import see.functions.reactive.Bind;
import see.functions.reactive.MakeSignal;
import see.functions.service.*;
import see.functions.string.Concat;
import see.parser.numbers.BigDecimalFactory;
import see.parser.numbers.NumberFactory;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.MathContext;

import static com.google.common.base.Suppliers.compose;

public abstract class DefaultConfig {
    private DefaultConfig() {}

    public static ConfigBuilder defaultConfig() {
        final ConfigBuilder builder = ConfigBuilder.emptyConfig();
        builder.setNumberFactory(new BigDecimalFactory());

        addServiceFunctions(builder);
        addCollections(builder);
        addLogic(builder);
        addArithmetic(builder);
        addCompare(builder);
        addProperty(builder);
        addIteration(builder);
        addBindings(builder);
        return builder;
    }

    private static void addCollections(ConfigBuilder builder) {
        builder.addAlias("[]", "makeList");
        builder.addAlias("{}", "makeMap");

        builder.addPureFunction("makeList", new MakeList());
        builder.addPureFunction("makeMap", new MakeMap());
    }

    private static void addBindings(final ConfigBuilder builder) {
        builder.addAlias("<-", "bind");
        builder.addFunction("bind", new Bind());
        builder.addFunction("signal", new MakeSignal());
    }

    private static void addIteration(ConfigBuilder builder) {
        builder.addAlias("for", "iterate");
        builder.addFunction("iterate", new Iterate());
        builder.addPureFunction("while", new While());
    }

    private static void addProperty(ConfigBuilder builder) {
        builder.addAlias(".", "get");
        builder.addPureFunction("get", new GetProperty());
    }

    private static void addCompare(ConfigBuilder builder) {
        builder.addPureFunction("==", new Eq());
        builder.addPureFunction("!=", new Neq());
        builder.addPureFunction(">", new Gt());
        builder.addPureFunction(">=", new Geq());
        builder.addPureFunction("<", new Lt());
        builder.addPureFunction("<=", new Leq());
    }

    private static void addArithmetic(final ConfigBuilder builder) {
        builder.addAlias("+", "addOrConcat");
        builder.addPureFunction("addOrConcat", new AddOrConcat());
        builder.addPureFunction("concat", new Concat());
        builder.addPureFunction("sum", new Sum());

        builder.addAlias("-", "minus");
        builder.addAlias("*", "product");
        builder.addAlias("/", "divide");
        builder.addAlias("^", "pow");

        builder.addPureFunction("min", new Min<BigDecimal>());
        builder.addPureFunction("max", new Max<BigDecimal>());
        builder.addPureFunction("minus", new Minus());
        builder.addPureFunction("product", new Product());
        builder.addPureFunction("divide", new Divide(compose(new Function<NumberFactory, MathContext>() {
            @Override
            public MathContext apply(@Nullable NumberFactory input) {
                return ((BigDecimalFactory) input).getMathContext();
            }
        }, builder.getNumberFactoryReference())));
        builder.addPureFunction("pow", new Power());
    }

    private static void addLogic(ConfigBuilder builder) {
        builder.addAlias("!", "not");
        builder.addAlias("&&", "and");
        builder.addAlias("||", "or");

        builder.addPureFunction("not", new Not());
        builder.addPureFunction("and", new And());
        builder.addPureFunction("or", new Or());
    }

    private static void addServiceFunctions(ConfigBuilder builder) {
        builder.addAlias(";", "seq");
        builder.addAlias("=", "assign");
        builder.addAlias("v=", "vSet");
        builder.addAlias("function", "def");

        builder.addPureFunction("seq", new Sequence<Object>());
        builder.addPureFunction("assign", new Assign<Object>());
        builder.addFunction("vSet", new VarAsSettable());
        builder.addFunction("isDefined", new IsDefined());
        builder.addPureFunction("if", new If<Object>());
        builder.addFunction("apply", new ExtensibleApply());
        builder.addFunction("def", new MakeFunction());
    }
}
