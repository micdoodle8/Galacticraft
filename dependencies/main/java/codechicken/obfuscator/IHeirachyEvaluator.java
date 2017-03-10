package codechicken.obfuscator;

import codechicken.obfuscator.ObfuscationMap.ObfuscationEntry;

import java.util.List;

public interface IHeirachyEvaluator {
    /**
     * @param desc The mapping descriptor of the class to evaluate heirachy for
     * @return A list of parents (srg or obf names)
     */
    List<String> getParents(ObfuscationEntry desc);

    /**
     * @param srg_class The srg name of the class in question
     * @return True if this class does not inherit from any obfuscated class.
     */
    boolean isLibClass(ObfuscationEntry desc);
}
