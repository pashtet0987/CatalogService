package by.pashkavlushka.GoodsCatalogueService.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Clock;
import java.time.Instant;

@Entity
@Table(name = "handled_update_event", schema = "online_shop")
public class HandledUpdateEventEntity {

    @Id
    private String id;
    private Instant timestamp;

    public HandledUpdateEventEntity() {
    }

    public HandledUpdateEventEntity(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
    
    @PrePersist
    public void setTimeStamp(){
        timestamp = Instant.now(Clock.systemDefaultZone());
    }
}
