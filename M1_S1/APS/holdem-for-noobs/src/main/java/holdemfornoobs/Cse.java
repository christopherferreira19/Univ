package holdemfornoobs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Cse {

    enum Choice { Left, Right; };

    static class Result {

        private final int score;
        private final List<Choice> choices;

        private static Result EMPTY = new Result(0, Collections.emptyList());

        public static Result empty() {
            return EMPTY;
        }

        public static Result oneCard(Card card) {
            return new Result(card.value(), Collections.emptyList());
        }

        public static Result create(int score, Choice head, List<Choice> tail) {
            List<Choice> choices = new ArrayList<>();
            choices.add(head);
            choices.addAll(tail);
            return new Result(score, choices);
        }

        private Result(int score, List<Choice> choices) {
            this.score = score;
            this.choices = choices;
        }
    }

    public static Result player(Cards cards) {
        if (cards.isEmpty()) {
            return Result.empty();
        }
        else if (cards.isOnlyOneLeft()) {
            return Result.oneCard(cards.left());
        }
        else {
            Result left_result = opponent(cards.popLeft());
            Result right_result = opponent(cards.popRight());
            int left_score = cards.left().value() + left_result.score;
            int right_score = cards.right().value() + right_result.score;

            if (left_score < right_score) {
                return Result.create(right_score, Choice.Right, right_result.choices);
            }
            else {
                return Result.create(left_score, Choice.Left, left_result.choices);
            }
        }
    }

    public static Result opponent(Cards cards) {
        if (cards.isEmpty() || cards.isOnlyOneLeft()) {
            return Result.empty();
        }
        else if (cards.left().value() < cards.right().value()) {
            return player(cards.popRight());
        }
        else {
            return player(cards.popLeft());
        }
    }

    public static void main(String[] args) {
        Random random = new Random();
        int winsCount = 0;
        int tiesCount = 0;
        int gamesCount = 0;

        for (int i = 0; i < 1000; i++) {
            Cards cards = Cards.generate(32, random);
            int totalScore = cards.totalValue();

            Result result = opponent(cards);

            if (totalScore - result.score == result.score) {
                tiesCount++;
            }
            if (totalScore / 2 < result.score) {
                winsCount++;
            }
            gamesCount++;
        }

        int lossCount = gamesCount - winsCount - tiesCount;
        System.out.println("Ratio Win : " + winsCount * 100 / gamesCount + "%");
        System.out.println("Ratio Tie : " + tiesCount * 100 / gamesCount + "%");
        System.out.println("Ratio Los : " + lossCount * 100 / gamesCount + "%");
    }
}
