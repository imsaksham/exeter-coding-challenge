import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TranslateWordsChallenge {
    public static void main(String[] args) throws IOException {
    	Runtime runtime = Runtime.getRuntime();
        runtime.gc();

        long initialMemory = runtime.totalMemory() - runtime.freeMemory();

        String inputFilePath = "D:\\JAVA-Workspace\\TranslateWordsChallenge\\src\\t8.shakespeare.txt";
        String inputText = readFile(inputFilePath);

        String findWordsFilePath = "D:\\JAVA-Workspace\\TranslateWordsChallenge\\src\\find_words.txt";
        List<String> findWords = readFile(findWordsFilePath).lines().collect(Collectors.toList());

        String dictionaryFilePath = "D:\\JAVA-Workspace\\TranslateWordsChallenge\\src\\french_dictionary.csv";
        Map<String, String> dictionary = readDictionary(dictionaryFilePath);

        long startTime = System.currentTimeMillis();
        List<String> replacedWordsList = new ArrayList<>();
        Map<String, Integer> wordFrequencyMap = new HashMap<>();
        int numReplacements = 0;
        StringBuilder outputText = new StringBuilder(inputText);
        for (String word : findWords) {
            if (dictionary.containsKey(word) && !replacedWordsList.contains(word)) {
                int count = countOccurrences(outputText.toString(), word);
                numReplacements += count;
                outputText = new StringBuilder(outputText.toString().replace(word, dictionary.get(word)));
                replacedWordsList.add(word);
                wordFrequencyMap.put(word, count);
            }
        }
        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        
        String outputFilePath = "D:\\JAVA-Workspace\\TranslateWordsChallenge\\src\\t8.shakespeare.translated.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            writer.write(outputText.toString());
        }
        
        String frequencyFilePath = "D:\\JAVA-Workspace\\TranslateWordsChallenge\\src\\frequency.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(frequencyFilePath))) {
            writer.write("English word,French word,Frequency\n");
            for (String word : wordFrequencyMap.keySet()) {
                String frenchWord = dictionary.containsKey(word) ? dictionary.get(word) : "";
                int frequency = wordFrequencyMap.get(word);
                writer.write(word + "," + frenchWord + "," + frequency + "\n");
            }
        }

        System.out.println("Frequency of words written to frequency.csv file.");

        System.out.println("Unique list of words replaced with French words from the dictionary:");
        System.out.println(replacedWordsList);
        System.out.println("Number of times a word was replaced: " + numReplacements);
        System.out.println("Time taken to process (seconds): " + timeTaken);
        
        long finalMemory = runtime.totalMemory() - runtime.freeMemory();
        long memoryTaken = finalMemory - initialMemory;

        System.out.println("Memory taken to process (bytes): " + memoryTaken);
    }    

    public static String readFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    public static Map<String, String> readDictionary(String filePath) throws IOException {
        Map<String, String> dictionary = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    dictionary.put(parts[0], parts[1]);
                }
            }
        }
        return dictionary;
    }

    public static int countOccurrences(String text, String word) {
        String pattern = "\\b" + word + "\\b";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(text);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }
}