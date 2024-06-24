import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FindNWords {
    static Set<String> validWordsCache = new HashSet<>();
    static Set<String> invalidWordsCache = new HashSet<>();
    static Set<String> rawSet = new HashSet<>();

    public static void main(String[] args) throws Exception {
        System.out.println(findAllNWords(9));
    }

    static long findAllNWords(int n) throws Exception {
        List<String> allWords = loadAllWords();

        long start = System.currentTimeMillis();

        List<String> nLetterWords = new ArrayList<>();
        for(String s : allWords) {
            int len = s.length();
            if(len <= n) {
                if(len == n)
                    nLetterWords.add(s);

                rawSet.add(s);
            }
        }

        int counter = 0;
        for(String nLetterWord : nLetterWords)
            if(validateWord(nLetterWord))
                counter++;

        System.out.println("Duration in ms: " + (System.currentTimeMillis() - start));

        return counter;
    }

    static boolean validateWord(String word) {
        // Check if we already verified this word or we reached the recursion end
        if(word.equals("I") || word.equals("A") || validWordsCache.contains(word))
            return true;
        else if(invalidWordsCache.contains(word))
            return false;
        // Word is not in the caches, check if it's a real word
        else if(rawSet.contains(word)) {
            for(String smallerWord : generateSmallerWords(word)) {
                boolean valid = validateWord(smallerWord);
                if(valid) {
                    // There exists a valid subword that reached the recursion end, add it to the validated cache
                    validWordsCache.add(word);
                    return true;
                }
            }

            // We're sure this word can't generate valid subwords, mark it as invalid
            invalidWordsCache.add(word);
            return false;
        } else
            return false;
    }

    static Set<String> generateSmallerWords(String s) {
        Set<String> smallerWords = new HashSet<>(s.length());
        for(Character c : s.toCharArray()) {
            smallerWords.add(new String(s.replaceFirst(c + "", "")));
        }

        return smallerWords;
    }

    static private List<String> loadAllWords() throws Exception {
        URL wordsUrl = new URL("https://raw.githubusercontent.com/nikiiv/JavaCodingTestOne/master/scrabble-words.txt");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(wordsUrl.openConnection().getInputStream()))) {
            return br.lines().skip(2).collect(Collectors.toList());
        }
    }
}
