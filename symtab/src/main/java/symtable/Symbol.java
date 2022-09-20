package symtable;

import symtable.scope.Scope;

public class Symbol {
    protected String name;
    protected String type;

    public Scope scope;

    public Symbol(String name) {
        this.name = name;
    }

    public Symbol(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String toString() {
        if (type != null) return '<' + getName() + ":" + type + '>';
        return getName();
    }
}
