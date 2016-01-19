package org.graylog.plugins.messageprocessor;

import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import org.graylog.plugins.messageprocessor.ast.functions.Function;
import org.graylog.plugins.messageprocessor.functions.BooleanCoercion;
import org.graylog.plugins.messageprocessor.functions.DoubleCoercion;
import org.graylog.plugins.messageprocessor.functions.DropMessageFunction;
import org.graylog.plugins.messageprocessor.functions.HasField;
import org.graylog.plugins.messageprocessor.functions.InputFunction;
import org.graylog.plugins.messageprocessor.functions.LongCoercion;
import org.graylog.plugins.messageprocessor.functions.StringCoercion;
import org.graylog.plugins.messageprocessor.processors.NaiveRuleProcessor;
import org.graylog.plugins.messageprocessor.rest.MessageProcessorRuleResource;
import org.graylog2.plugin.PluginConfigBean;
import org.graylog2.plugin.PluginModule;

import java.util.Collections;
import java.util.Set;

public class ProcessorPluginModule extends PluginModule {

    @Override
    public Set<? extends PluginConfigBean> getConfigBeans() {
        return Collections.emptySet();
    }

    @Override
    protected void configure() {
        addMessageProcessor(NaiveRuleProcessor.class);
        addRestResource(MessageProcessorRuleResource.class);

        // built-in functions
        addMessageProcessorFunction(BooleanCoercion.NAME, BooleanCoercion.class);
        addMessageProcessorFunction(DoubleCoercion.NAME, DoubleCoercion.class);
        addMessageProcessorFunction(LongCoercion.NAME, LongCoercion.class);
        addMessageProcessorFunction(StringCoercion.NAME, StringCoercion.class);

        addMessageProcessorFunction(HasField.NAME, HasField.class);
        addMessageProcessorFunction(InputFunction.NAME, InputFunction.class);
        addMessageProcessorFunction(DropMessageFunction.NAME, DropMessageFunction.class);
    }

    protected void addMessageProcessorFunction(String name, Class<? extends Function<?>> functionClass) {
        addMessageProcessorFunction(binder(), name, functionClass);
    }

    public static MapBinder<String, Function<?>> processorFunctionBinder(Binder binder) {
        return MapBinder.newMapBinder(binder, TypeLiteral.get(String.class), new TypeLiteral<Function<?>>() {});
    }

    public static void addMessageProcessorFunction(Binder binder, String name, Class<? extends Function<?>> functionClass) {
        processorFunctionBinder(binder).addBinding(name).to(functionClass);

    }
}