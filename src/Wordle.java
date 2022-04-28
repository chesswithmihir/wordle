import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class Wordle {
    public static String word;
    public static int numAttempts = 6;
    public static HashMap<Character, Integer> wordDict = new HashMap<Character, Integer>();



    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Would you like to play today's (t) wordle or a random (r) wordle? ");
        String input = scanner.nextLine();
        if (input.equals("t")) {
            findTodayWord();
        } else if (input.equals("r")) {
            findRandomWord();
        } else {
            System.out.println("Sorry, please print 't' or 'r'. Thank you!");
            main(null);
        }
        startGame();

    }
    public static void startGame() {
        //System.out.println(word);
        int currAttempt = 1;
        String colorResult = "";
        while (currAttempt <= numAttempts && !colorResult.equals("GGGGG")) {
            wordDict = makeDict(word);
            System.out.println("GUESS " + currAttempt);
            colorResult = guess();
            System.out.println(colorResult);
            if (colorResult.equals("GGGGG")) {
                System.out.println("GOOD JOB YOU WON THE GAME");
                System.exit(0);
                return;
            }
            currAttempt += 1;
        }
        System.out.println();
        System.out.println("Sorry the word was " + word);
        System.out.println("You can run the program again and select random (r) to play again!");

    }
    public static String guess() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("What is your guess? ");
        String answer = scanner.nextLine();
        HashMap<Character, Integer> guessDict = makeDict(answer);
        if (answer.length() != 5) {
            System.out.println("Sorry, you need to put in a 5 letter word! ");
            return guess();
        }
        if (!answer.matches("[a-z]+")) {
            System.out.println("Sorry, please try one word that contains lowercase letters!");
            return guess();
        }
        String colors = "";

        //System.out.println(guessDict.toString());
        //System.out.println(guessDict.containsKey(word.charAt(3)));

        for(int i = 0; i < answer.length(); i ++) {

            if(answer.charAt(i) == word.charAt(i) && wordDict.containsKey(answer.charAt(i))) {
                colors += "G";
                if(wordDict.get(answer.charAt(i)) > 1) {
                    wordDict.put(answer.charAt(i), wordDict.get(answer.charAt(i)) - 1);
                } else {
                    wordDict.remove(answer.charAt(i));
                }

            }
            else if (word.indexOf(answer.charAt(i)) != -1 && wordDict.containsKey(answer.charAt(i))) {
                colors += "Y";
                if(wordDict.get(answer.charAt(i)) > 1) {
                    wordDict.put(answer.charAt(i), wordDict.get(answer.charAt(i)) - 1);
                } else {
                    wordDict.remove(answer.charAt(i));
                }
            } else {
                colors += "B";
            }
        }

        return colors;
    }

    public static HashMap<Character, Integer> makeDict(String guess) {
        HashMap<Character, Integer> dict = new HashMap<Character, Integer>();
        for (int i = 0; i < guess.length(); i++) {
            if (dict.containsKey(guess.charAt(i))) {
                dict.put(guess.charAt(i), dict.get(guess.charAt(i)) + 1);
            } else {
                dict.put(guess.charAt(i), 1);
            }
        }
        return dict;
    }

    public static void findTodayWord() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();
        String todayDate = (formatter.format(date).substring(0, 10));
//        System.out.println("THE DATE IS " + todayDate);
        int todaysMonth = Integer.parseInt(todayDate.substring(0,2));
        int todaysDate = Integer.parseInt(todayDate.substring(3,5));
        int todaysYear = Integer.parseInt(todayDate.substring(6,10));
        Calendar cal1 = new GregorianCalendar();
        Calendar cal2 = new GregorianCalendar();
        cal1.set(2021, 6, 19); //6/19/2021 was the first wordle
        cal2.set(todaysYear, todaysMonth, todaysDate); //today's wordle
        int dayCount = (int)daysBetween(cal1.getTime(),cal2.getTime());
        dayCount += 7; //offset by week for reading file purposes

        findLine(dayCount);

    }
    public static int daysBetween(Date d1, Date d2){
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }


    public static void findRandomWord() {
        Random rand = new Random();
        int random = (int)(rand.nextDouble() * 12900);
        findLine(random);

    }

    public static void findLine(int lineNumber) {
        File file = new File("src/dict.txt");
        while (!file.exists()) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("dict.txt file not found at src/dict.txt, please provide absolute or relative path");
            String answer = scanner.nextLine();
            file = new File(answer);
        }
        try {
            Scanner scanner = new Scanner(file);

            //now read the file line by line...
            int lineNum = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
//                if (lineNum == lineNumber - 1) {
//                    System.out.println("YESTERDAYS WORD: " + line.substring(line.length() - 5));
//                }
                if(lineNum == lineNumber) {
                    word = line.substring(line.length() - 5);
//                    System.out.println("Today's word: " + word);
                }

                lineNum++;
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
