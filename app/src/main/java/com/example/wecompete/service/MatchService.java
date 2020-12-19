package com.example.wecompete.service;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.wecompete.repo.GroupRepo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MatchService {
    private GroupRepo groupRepo = new GroupRepo();

    /**
     * FORMÅL: returnerer tidspunktet den kaldes i et ønsket format.
     * BRUG: i declareMatchResult() metoden i MatchRepo klassen.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String fetchDateTimeForMatch() {
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatForDate = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
        return currentDate.format(formatForDate);
    }

    /**
     * FORMÅL: Udregner sandsynligheden for at en spiller vinder baseret på deres nuværende ELO-rating.
     * BRUG: i EloRating() metoden og implementeret fra: https://www.geeksforgeeks.org/elo-rating-algorithm/
     */
    // Function to calculate the Probability
    public float Probability(float rating1, float rating2) {
        return 1.0f * 1.0f / (1 + 1.0f *
                (float)(Math.pow(10, 1.0f *
                        (rating1 - rating2) / 400)));
    }

    /**
     * FORMÅL: udregner ny ELO-rating baseret på hvem der vandt kampen.
     * BRUG: i declareMatchResult() metoden i MatchRepo klassen.
     * Gør brug af updateNewElo() metoden, men er implementeret fra: https://www.geeksforgeeks.org/elo-rating-algorithm/
     */
    // Function to calculate Elo rating
    // K is a constant.
    // d determines whether Player A wins
    // or Player B.
    public void EloRating(float Ra, float Rb, int K, boolean d, String userID, String userInput, String groupID) {
        // To calculate the Winning
        // Probability of Player B
        float Pb = Probability(Ra, Rb);

        // To calculate the Winning
        // Probability of Player A
        float Pa = Probability(Rb, Ra);

        // Case -1 When Player A wins
        // Updating the Elo Ratings
        if (d) {
            Ra = Ra + K * (1 - Pa);
            Rb = Rb + K * (0 - Pb);
            String Ra2 = String.valueOf(Ra);
            String Rb2 = String.valueOf(Rb);
            groupRepo.updateNewElo(userID, userInput, groupID, Ra2, Rb2);
        }

        // Case -2 When Player B wins
        // Updating the Elo Ratings
        else {
            Ra = Ra + K * (0 - Pa);
            Rb = Rb + K * (1 - Pb);
            String Ra2 = String.valueOf(Ra);
            String Rb2 = String.valueOf(Rb);
            groupRepo.updateNewElo(userID, userInput, groupID, Ra2, Rb2);
        }
    }
}
