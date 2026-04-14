import java.util.*;

// Subject class keeps track of details, implements Comparable to sort by priority
class Subject implements Comparable<Subject> {
    String name;
    int difficulty; // scale 1-5
    int prepLevel;  // percentage 0-100
    int daysToExam;
    double priority; // Used to allocate study time

    public Subject(String name, int difficulty, int prepLevel, int daysToExam) {
        this.name = name;
        this.difficulty = difficulty;
        this.prepLevel = prepLevel;
        this.daysToExam = daysToExam;
        
        // Priority = (difficulty × 2) + (100 - preparation level)
        this.priority = (difficulty * 2.0) + (100 - prepLevel);
    }

    // Determine the AI suggestion based on preparation and needs
    public String getSuggestion() {
        if (prepLevel < 40) {
            return "Focus on weak areas and read foundational concepts.";
        } else if (prepLevel < 75) {
            return "Practice problems and revise class notes.";
        } else {
            return "Revise more, solve previous year papers and take mock tests.";
        }
    }

    @Override
    public int compareTo(Subject other) {
        // Sort in descending order (Higher priority comes first)
        return Double.compare(other.priority, this.priority);
    }
    
    @Override
    public String toString() {
        return String.format("%-15s | Priority: %5.1f | Difficulty: %d | Prep: %3d%% | Suggestion: %s", 
                             name, priority, difficulty, prepLevel, getSuggestion());
    }
}

public class AIStudyPlanner {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // List to store history / raw data
        List<Subject> subjectsList = new ArrayList<>();
        // Max-Priority Queue (Heap) to automatically sort subjects by highest priority
        PriorityQueue<Subject> pq = new PriorityQueue<>();

        System.out.println("=========================================");
        System.out.println("        AI STUDY PLANNER (JAVA)          ");
        System.out.println("=========================================");
        System.out.print("How many subjects do you want to plan for? ");
        
        int numSubjects = 3;
        try {
            numSubjects = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Defaulting to 3 subjects.");
        }

        // 1. Take User Input
        for (int i = 0; i < numSubjects; i++) {
            System.out.println("\n--- Details for Subject " + (i + 1) + " ---");
            
            System.out.print("Subject Name: ");
            String name = scanner.nextLine();

            System.out.print("Difficulty Level (1-5): ");
            int difficulty = Integer.parseInt(scanner.nextLine());
            difficulty = Math.max(1, Math.min(5, difficulty)); // Clamp 1-5

            System.out.print("Preparation Level (0-100%): ");
            int prepLevel = Integer.parseInt(scanner.nextLine());
            prepLevel = Math.max(0, Math.min(100, prepLevel)); // Clamp 0-100

            System.out.print("Exam Dates (Days remaining): ");
            int days = Integer.parseInt(scanner.nextLine());
            days = Math.max(0, days);

            // Initialize subject
            Subject sub = new Subject(name, difficulty, prepLevel, days);
            subjectsList.add(sub);
            
            // Insert into Heap Structure
            pq.add(sub); 
        }

        // 2. Output: Display sorted subjects by priority
        System.out.println("\n==========================================================================================================");
        System.out.println("                         SUBJECTS RANKED BY STUDY PRIORITY (Highest First)                        ");
        System.out.println("==========================================================================================================");
        
        // Polling from PQ guarantees sorted descending order
        List<Subject> sortedSubjects = new ArrayList<>();
        while (!pq.isEmpty()) {
            Subject s = pq.poll();
            sortedSubjects.add(s);
            System.out.println(s);
        }

        // 3. Output: Generate a day-wise study plan
        System.out.println("\n==========================================================================================================");
        System.out.println("                                      DAILY STUDY TIMETABLE                                       ");
        System.out.println("==========================================================================================================");
        System.out.println("Assuming an optimal study routine of 4 hours (240 mins) total per day:");
        System.out.println();

        double totalPriorityTracker = 0;
        for (Subject s : sortedSubjects) {
            totalPriorityTracker += s.priority;
        }

        // 4. Logic: Proportionally allocate more time to difficult & weak subjects
        for (Subject s : sortedSubjects) {
            double timeFraction = (s.priority / totalPriorityTracker);
            int allocatedMins = (int) (timeFraction * 240);
            
            int hours = allocatedMins / 60;
            int mins = allocatedMins % 60;

            System.out.printf("❖ %-15s : Study for %d hrs %2d mins (%3.0f%% of daily time)%n", 
                              s.name, hours, mins, timeFraction * 100);
            System.out.printf("  ↳ AI Suggestion: %s%n\n", s.getSuggestion());
        }
        
        System.out.println("==========================================================================================================");
        System.out.println("Tip: As you practice, your 'Preparation Level' goes up. Run this planner again tomorrow to see your new queue!");
        
        scanner.close();
    }
}
