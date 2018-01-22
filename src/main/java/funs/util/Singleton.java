package funs.util;

import funs.model.corpus.Corpus;

public class Singleton {
    private static Corpus instance;

    public Singleton(Corpus instance){
        this.instance = instance;
    }

    public static Corpus getInstance(){
        if (instance == null){
            synchronized (Corpus.class){
                if (instance == null){
                    instance = new Corpus();
                }
            }
        }
        return instance;
    }
}
