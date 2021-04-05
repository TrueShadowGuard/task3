import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;
import java.util.function.*;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        var moves = args;

        if(moves.length < 3 || moves.length % 2 == 0) {
            System.out.println("Number of moves must be >= 3 and odd");
            System.exit(0);
        }
        int computerTurn = 1 + new Random().nextInt(moves.length - 1);
        var key = UUID.randomUUID().toString().replace("-","") + moves[computerTurn - 1];
        var digest = MessageDigest.getInstance("SHA-256");
        var encodedhash = digest.digest(key.getBytes(StandardCharsets.UTF_8));
        var hash = bytesToHex(encodedhash);
        System.out.println(hash);
        final var sc = new Scanner(System.in);

        var template = "Available moves:\n";
        for(int i = 0; i < moves.length; i++) {
            template += ((i+1) + " - " + moves[i]+"\n");
        }
        template += "0 - exit";
        String finalTemplate = template;
        int range = moves.length / 2;
        Consumer<String> turn = s -> {
            System.out.println(finalTemplate);
            Integer answer = null;
            while (answer == null) {
                try {
                    answer = Integer.parseInt(sc.nextLine());
                    if(answer < 0 || answer > moves.length) {
                        answer = null;
                        System.out.println("Invalid input: try again");
                    }
                } catch (Exception e) {
                    System.out.println("Invalid input: try again");
                }
            }
            if(answer == 0) System.exit(0);
            String x = "Computer's turn: " + moves[computerTurn - 1] + "\n" + "Your turn: " + moves[answer - 1];
            if(answer == computerTurn) {
                System.out.println("Draw!");
                System.out.println(x);
                System.out.println(key);
                System.exit(0);
            }
            int max = Math.max(computerTurn,answer);
            int min = Math.min(computerTurn,answer);
            boolean playerWon;
            if(max - min <= range) {
                playerWon = max == computerTurn;
            } else {
                playerWon = max != computerTurn;
            }
            if(playerWon) {
                System.out.println("You win!");
            } else {
                System.out.println("Computer win!");
            }
            System.out.println(x);
            System.out.println(key);
            System.exit(0);
        };
        turn.accept(template);
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
