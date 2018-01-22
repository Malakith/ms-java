package funs.fact;

import java.util.List;

public class SyntacticFacts {
    private List<String> hearstConnection; //the chunks in this are the hypernym of this chunk according to Hearst
    private List<String> cimianoPatterns; //The chunks in this are the wholes of the cimiano patterns.

    public SyntacticFacts(List<String> hearst, List<String> cimiano){
        hearstConnection = hearst;
        cimianoPatterns = cimiano;
    }
}
