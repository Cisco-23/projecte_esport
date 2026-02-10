package com.company.projecte_esport.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "bookings")
public class Booking {
    @Id
    private String id;
    private LocalDateTime dateTime;
    private String courtName; // Pista de squash
    private String player1Id;
    private String player2Id; // Puede ser nulo si espera pareja
    private boolean isFull; // True si la reserva ya tiene las dos personas

    // Constructores
    public Booking() {}
    
    public Booking(LocalDateTime dateTime, String courtName, String player1Id, String player2Id, boolean isFull) {
        this.dateTime = dateTime;
        this.courtName = courtName;
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.isFull = isFull;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public String getCourtName() { return courtName; }
    public void setCourtName(String courtName) { this.courtName = courtName; }

    public String getPlayer1Id() { return player1Id; }
    public void setPlayer1Id(String player1Id) { this.player1Id = player1Id; }

    public String getPlayer2Id() { return player2Id; }
    public void setPlayer2Id(String player2Id) { this.player2Id = player2Id; }

    public boolean isFull() { return isFull; }
    public void setFull(boolean full) { isFull = full; }
}