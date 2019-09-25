package com.ichi2yiji.libanki.hooks;

public interface HookPlugin { 
    public void install(Hooks h);
    public void uninstall(Hooks h);   
    public int hashCode();
    public boolean equals(Object obj);
    public Object runFilter(Object arg, Object... args);    
    public void runHook(Object... args);
}