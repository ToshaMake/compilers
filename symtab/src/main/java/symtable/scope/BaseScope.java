package symtable.scope;

import symtable.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class BaseScope implements Scope {

    Scope enclosingScope; // null if global (outermost) scope
    Map<String, Symbol> symbols = new LinkedHashMap<String, Symbol>();

    public BaseScope(Scope enclosingScope) {
        this.enclosingScope = enclosingScope;
    }

    public Symbol resolve(String name, int position) {
        Symbol s = symbols.get(name);
        if (s != null) return s;
        // if not here, check any enclosing scope
        if (enclosingScope != null) return enclosingScope.resolve(name, position);
        throw new IllegalArgumentException(name + " does not defined " + "in "+ position + " line "); // not found
    }

    public void define(Symbol sym) {
        Symbol s = symbols.get(sym.getName());
        if (s == null) {
            symbols.put(sym.getName(), sym);
            sym.scope = this; // track the scope in each symbol
        }else {
            throw new IllegalArgumentException("redefinition "+ sym.getName() + " in this scope");
        }
    }

    public Scope getEnclosingScope() {
        return enclosingScope;
    }

    public String toString() {
        return symbols.keySet().toString();
    }
}
