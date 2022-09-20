package symtable;

import symtable.scope.GlobalScope;
import symtable.types.*;

public class SymTable {

    public GlobalScope globals = new GlobalScope();

    public SymTable() { initTypeSystem(); }
    protected void initTypeSystem() {
        globals.define(new Symbol("println!"));
        globals.define(new Symbol("read_line"));
    }

    public String toString() { return globals.toString(); }
}
