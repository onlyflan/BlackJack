// import java.io.*;

// import java.util.Collections;
// import java.util.List;

// public class ScoreManager {
//     private static final String SCORE_FILE_PATH = "scores.txt";

//     public static void saveScore(String playerName, int score) {
//         try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCORE_FILE_PATH, true))) {
//             writer.write(playerName + "," + score);
//             writer.newLine();
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }

//     public static List<ScoreRecord> loadScores() {
//         List<ScoreRecord> scores = new ArrayList<>();
//         try (BufferedReader reader = new BufferedReader(new FileReader(SCORE_FILE_PATH))) {
//             String line;
//             while ((line = reader.readLine()) != null) {
//                 String[] parts = line.split(",");
//                 if (parts.length == 2) {
//                     String playerName = parts[0];
//                     int score = Integer.parseInt(parts[1]);
//                     scores.add(new ScoreRecord(playerName, score));
//                 }
//             }
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//         return scores;
//     }

//     public static List<ScoreRecord> getTopScores() {
//         List<ScoreRecord> scores = loadScores();
//         Collections.sort(scores, Collections.reverseOrder());
//         return scores;
//     }
// }

// class ScoreRecord implements Comparable<ScoreRecord> {
//     private String playerName;
//     private int score;

//     public ScoreRecord(String playerName, int score) {
//         this.playerName = playerName;
//         this.score = score;
//     }

//     public String getPlayerName() {
//         return playerName;
//     }

//     public int getScore() {
//         return score;
//     }

//     @Override
//     public int compareTo(ScoreRecord other) {
//         return Integer.compare(this.score, other.score);
//     }
// }
