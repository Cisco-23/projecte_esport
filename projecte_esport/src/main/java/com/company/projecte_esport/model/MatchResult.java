package com.company.projecte_esport.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "match_results")
public class MatchResult {
    @Id
    private String id;
    private String bookingId; // Referencia a la reserva de la que proviene el partido
    private int player1Sets;
    private int player2Sets;
    private String winnerId;
    private LocalDate matchDate;

    // Constructores
    public MatchResult() {}

    public MatchResult(String bookingId, int player1Sets, int player2Sets, String winnerId, LocalDate matchDate) {
        this.bookingId = bookingId;
        this.player1Sets = player1Sets;
        this.player2Sets = player2Sets;
        this.winnerId = winnerId;
        this.matchDate = matchDate;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public int getPlayer1Sets() { return player1Sets; }
    public void setPlayer1Sets(int player1Sets) { this.player1Sets = player1Sets; }

    public int getPlayer2Sets() { return player2Sets; }
    public void setPlayer2Sets(int player2Sets) { this.player2Sets = player2Sets; }

    public String getWinnerId() { return winnerId; }
    public void setWinnerId(String winnerId) { this.winnerId = winnerId; }

    public LocalDate getMatchDate() { return matchDate; }
    public void setMatchDate(LocalDate matchDate) { this.matchDate = matchDate; }
}