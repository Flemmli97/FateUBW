package io.github.flemmli97.fateubw.common.config.value;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.tenshilib.common.utils.math.parser.ExpValue;
import io.github.flemmli97.tenshilib.common.utils.math.parser.Expression;
import io.github.flemmli97.tenshilib.common.utils.math.parser.VariableMap;

public class ExpressionConfig {

    private String config;
    private ExpValue value;

    public ExpressionConfig(String config) {
        this.config = config;
        this.value = Expression.of(config);
    }

    public double get(VariableMap variables) {
        return this.value.get(variables);
    }

    public void read(String config) {
        try {
            this.value = Expression.of(config);
            this.config = config;
        } catch (IllegalStateException e) {
            Fate.LOGGER.error("Unable to parse expression from config: {}", config, e);
        }
    }

    public String write() {
        return this.config;
    }
}
