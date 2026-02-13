package by.pashkavlushka.GoodsCatalogueService.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;

@Entity
@Table(name = "handled_add_event", schema = "online_shop")
public class HandledAddEventEntity {
    @Id
    private String eventId;
    private Instant timestamp;

    public HandledAddEventEntity() {
    }

    public HandledAddEventEntity(String eventId) {
        this.eventId = eventId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
    
    @PrePersist
    public void setTimestamp(){
        timestamp = Instant.now(Clock.systemDefaultZone());
    }
}
