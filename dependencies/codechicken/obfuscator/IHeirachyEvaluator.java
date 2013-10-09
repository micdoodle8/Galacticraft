package codechicken.obfuscator;

import java.util.List;

import codechicken.obfuscator.ObfuscationMap.ObfuscationEntry;

public interface IHeirachyEvaluator
{
    /**
     * @param desc The mapping descriptor of the class to evaluate heirachy for
     * @return A list of parents (srg or obf names)
     */
    public List<String> getParents(ObfuscationEntry desc);

    /**
     * @param srg_class The srg name of the class in question
     * @return True if this class does not inherit from any obfuscated class.
     */
    public boolean isLibClass(ObfuscationEntry desc);
}
