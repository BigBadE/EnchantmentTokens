package software.bigbade.enchantmenttokens.utils.math;

import software.bigbade.enchantmenttokens.EnchantmentTokens;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.logging.Level;

public class AlgebraicCalculator {
    private ScriptEngine engine;
    private String equation;

    private static AlgebraicCalculator instance;

    public AlgebraicCalculator(String equation) {
        if(instance != null)
            throw new IllegalStateException("Already initalized!");
        ScriptEngineManager mgr = new ScriptEngineManager();
        engine = mgr.getEngineByName("JavaScript");
        this.equation = equation;
        setInstance(this);
    }

    private static void setInstance(AlgebraicCalculator instance) {
        AlgebraicCalculator.instance = instance;
    }

    public static AlgebraicCalculator getInstance() {
        return instance;
    }

    public long getPrice(long level) {
        try {
            return (long) engine.eval(equation.replace("x", "" + level));
        } catch (ScriptException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Invalid equation {0}", equation);
        }
        return 1;
    }
}
