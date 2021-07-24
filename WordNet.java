/* *****************************************************************************
 *  Name: Nicholas Yap
 *  Date: 16/07/2021
 *  Description: WordNet. Score 100/100.
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;

public class WordNet {
    private HashMap<Integer, String> idSynset;
    private HashMap<String, Bag<Integer>> nounId;
    private Digraph G;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        checkNull(synsets);
        checkNull(hypernyms);
        idSynset = new HashMap<>();
        nounId = new HashMap<>();
        int count = readSynsets(synsets);
        G = new Digraph(count);
        readHypernyms(hypernyms);
        if (new DirectedCycle(G).hasCycle()) throw new IllegalArgumentException();
        sap = new SAP(G);
    }

    private int readSynsets(String synsets) {
        checkNull(synsets);
        int count = 0;
        In in = new In(synsets);
        while (in.hasNextLine()) {
            String[] parts = in.readLine().split(",", 3);
            int id = Integer.parseInt(parts[0]);
            String synset = parts[1];
            idSynset.put(id, synset);

            String[] nouns = synset.split(" ");
            for (String n : nouns) {
                if (nounId.get(n) == null) {
                    nounId.put(n, new Bag<Integer>());
                }
                nounId.get(n).add(id);
            }
            count++;
        }
        return count;
    }

    private void readHypernyms(String hypernyms) {
        checkNull(hypernyms);
        int rootCount = 0;
        In in = new In(hypernyms);
        while (in.hasNextLine()) {
            String[] parts = in.readLine().split(",");
            int id = Integer.parseInt(parts[0]);
            if (parts.length == 1) rootCount++;
            for (int i = 1; i < parts.length; i++) {
                G.addEdge(id, Integer.parseInt(parts[i]));
            }
        }
        if (rootCount != 1) throw new IllegalArgumentException();
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounId.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        checkNull(word);
        return nounId.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        validateNoun(nounA);
        validateNoun(nounB);
        Iterable<Integer> subsetA = nounId.get(nounA);
        Iterable<Integer> subsetB = nounId.get(nounB);
        return sap.length(subsetA, subsetB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        validateNoun(nounA);
        validateNoun(nounB);
        Iterable<Integer> subsetA = nounId.get(nounA);
        Iterable<Integer> subsetB = nounId.get(nounB);
        String ancestorSynset = idSynset.get(sap.ancestor(subsetA, subsetB));
        return ancestorSynset;
    }

    private void checkNull(Object obj) {
        if (obj == null) throw new IllegalArgumentException();
    }

    private void validateNoun(String noun) {
        if (!nounId.containsKey(noun)) throw new IllegalArgumentException();
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}
