import java.io.*;
import java.util.*;

public class DFA {
    private Set<String> states;
    private Set<String> alphabet;
    private String startState;
    private Set<String> acceptStates;
    private Map<String, String> transitions;
    private Scanner scanner;

    public DFA() {
        states = new HashSet<>();
        alphabet = new HashSet<>();
        acceptStates = new HashSet<>(); // A HashSet is a collection in Java that stores unique elements and provides efficient operations like add, remove, and contains using hashing.
        transitions = new HashMap<>(); //whenever data is stored as key-value pairs, where values can be added, retrieved, and deleted using keys
        scanner = new Scanner(System.in);
    }

    public void loadDFAFromFile(String path) throws IOException {
        File file = new File(path);
        try (Scanner fileScanner = new Scanner(file)) {
            String line = fileScanner.nextLine().trim();
            if (!line.isEmpty()) {
                alphabet.addAll(Arrays.asList(line.replace("{", "").replace("}", "").split(",\\s*")));
            }
    
            line = fileScanner.nextLine().trim();
            if (!line.isEmpty()) {
                states.addAll(Arrays.asList(line.replace("{", "").replace("}", "").split(",\\s*")));
            }
    
            startState = fileScanner.nextLine().trim();
    
            line = fileScanner.nextLine().trim();
            if (!line.isEmpty()) {
                acceptStates.addAll(Arrays.asList(line.replace("{", "").replace("}", "").split(",\\s*")));
            }
    
            while (fileScanner.hasNextLine()) {
                line = fileScanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue; // Skip empty lines
                }
                String[] parts = line.split("->");
                if (parts.length < 2) {
                    System.out.println("Skipping invalid transition line: " + line);
                    continue; // Skip processing this line
                }
                String key = parts[0].trim();
                String value = parts[1].trim();
                transitions.put(key, value);
            }
        }
    }

    public void enterDFAManually() {
        System.out.println("Enter alphabet (comma-separated without braces):");
        alphabet.addAll(Arrays.asList(scanner.nextLine().split(",\\s*")));
        
        System.out.println("Enter states (comma-separated without braces):");
        states.addAll(Arrays.asList(scanner.nextLine().split(",\\s*")));
        
        System.out.println("Enter start state:");
        startState = scanner.nextLine().trim();
        
        System.out.println("Enter accept states (comma-separated without braces):");
        acceptStates.addAll(Arrays.asList(scanner.nextLine().split(",\\s*")));
        
        System.out.println("Enter transitions in the form (state, input)->state, one per line:");
        while (true) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                break;
            }
            String[] parts = line.split("->");
            String key = parts[0].trim();
            String value = parts[1].trim();
            transitions.put(key, value);
        }
        printDFA();
    }

    private void printDFA() {
        System.out.println("Here is your DFA as a reference:");
        System.out.println("Alphabet: " + alphabet);
        System.out.println("States: " + states);
        System.out.println("Start State: " + startState);
        System.out.println("Accept State: " + acceptStates);
        System.out.println("Transition Function:");
        for (String key : transitions.keySet()) {
            System.out.println(key + "->" + transitions.get(key));
        }
        System.out.println("--------------------------------------------------");
    }

    private void testInputString() {
        System.out.println("Enter a (non-separated) list for input to the DFA:");
        while (true) {
            String input = scanner.nextLine().trim();
            if (accepts(input)) {
                System.out.println("The input is accepted by the current DFA.");
            } else {
                System.out.println("The input is rejected by the current DFA.");
            }
            System.out.println("Enter 'yes' to continue or 'no' to exit");
            String decision = scanner.nextLine().trim();
            if (decision.equalsIgnoreCase("no")) {
                break;
            }
            System.out.println("Please enter another input string to test:");
        }
    }

    private boolean accepts(String input) {
        String currentState = startState;
        for (char c : input.toCharArray()) { // loops through input chars
            String transitionKey = String.format("(%s,%s)", currentState, c); // Formats the transition key as (currentState, inputCharacter)
            if (!transitions.containsKey(transitionKey)) {
                return false; // If there is no valid transition for the current state and input character, reject the input
            }
            currentState = transitions.get(transitionKey); // Move to the next state based on the transition function
        }
        return acceptStates.contains(currentState); // Check if the final state is an accept stat
    }

    public static void main(String[] args) {
        DFA dfa = new DFA();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome");
    
        while (true) {
            System.out.println("Please enter one of the numbers below:");
            System.out.println("[1] Create a DFA based on a text file");
            System.out.println("[2] Enter a DFA manually");
            System.out.println("[3] Test an input string through the last created DFA");
            System.out.println("[4] Reprint the DFA reference");
            System.out.println("[5] Exit");
            System.out.print("Choice: ");
    
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
    
            switch (choice) {
                case 1:
                    System.out.println("Please enter the filepath for the DFA file:");
                    String path = scanner.nextLine().trim();
                    try {
                        dfa.loadDFAFromFile(path);
                    } catch (IOException e) {
                        System.out.println("Error reading file: " + e.getMessage());
                    }
                    break;
                case 2:
                    dfa.enterDFAManually();
                    break;
                case 3:
                    dfa.testInputString();
                    break;
                case 4:
                    dfa.printDFA();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice, please choose again.");
                    break;
            }
        }
    }
}
