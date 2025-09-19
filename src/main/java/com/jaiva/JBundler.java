package com.jaiva;


import com.jaiva.interpreter.Interpreter;
import com.jaiva.interpreter.Scope;
import com.jaiva.interpreter.globals.BaseGlobals;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.tokenizer.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * JBundler allows other Java programs which have Jaiva as a dependency, to define their own set
 * of functions as libraries that a jaiva program can import.
 * <p>
 * Having 2 different instances of JBundler, means you have 2 different jaiva instances booted
 * up to run. Meaning they won't share the classes given to them unless you specifically make
 * them.
 */
public class JBundler {
    public static int instances = 0;
    public int instanceNum;

    public String filePath;
    public ArrayList<Token<?>> tokens = new ArrayList<>();
    public List<Class<? extends BaseGlobals>> classes;

    /**
     * Default Constructor for JBundler
     * @param fp The filepath of said file
     * @param cls The classes to import as libraries
     * @throws Exception When any of the jaiva processing fails.
     */
    @SafeVarargs
    public JBundler(String fp, Class<? extends BaseGlobals> ...cls) throws Exception {
        this.filePath = fp;
        instanceNum = instances;
        instances++;
        classes = Arrays.asList(cls);
    }

    public void tokenize() throws Exception {
        tokens = Main.parseTokens(filePath, false);
    }

    public <T> void  interpret(T obj) throws Exception {
        IConfig<T> config = new IConfig<>(new ArrayList<>(List.of("jaiva")), filePath, Main.callJaivaSrc(), obj);
        Scope scope = new Scope((IConfig<Object>) config, classes);

        Interpreter.interpret(tokens, scope, (IConfig<Object>) config);
    }

    public <T> void run(T obj) throws Exception {
        tokenize();
        interpret(obj);
    }

}
