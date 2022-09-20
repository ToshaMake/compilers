package symtable.types;

import symtable.Symbol;
import symtable.Type;
import symtable.scope.Scope;

import java.util.LinkedHashMap;
import java.util.Map;

public class MethodSymbol extends Symbol implements Scope {

    public MethodSymbol(String name, String type) {
        super(name, type);
    }

    Map<String, Symbol> orderedArgs = new LinkedHashMap<String, Symbol>();
    Scope enclosingScope;

    public MethodSymbol(String name, String retType, Scope enclosingScope) {
        super(name, retType);
        this.enclosingScope = enclosingScope;
    }

    public MethodSymbol(String name, Scope enclosingScope) {
        super(name);
        this.enclosingScope = enclosingScope;
    }

    public String getName() {
        return super.getName();
    }

    public Scope getEnclosingScope() {
        return enclosingScope;
    }

    public String getScopeName() {
        return getName();
    }

    public Symbol resolve(String name, int position) {
        Symbol s = orderedArgs.get(name);
        if (s != null) return s;
        // if not here, check any enclosing scope
        if (getEnclosingScope() != null) {
            return getEnclosingScope().resolve(name, position);
        } else
            throw new IllegalArgumentException(name + " does not defined " + "in "+ position + " line "); // not found
    }

    public void define(Symbol sym) {
        orderedArgs.put(sym.getName(), sym);
        sym.scope = this; // track the scope in each symbol
    }

    public String getType() {
        return super.getType();
    }

    public String toString() {
        return "method" + super.toString() + ":" + orderedArgs.values();

    }

}
