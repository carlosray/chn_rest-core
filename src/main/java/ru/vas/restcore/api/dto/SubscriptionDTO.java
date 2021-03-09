package ru.vas.restcore.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.http.conn.util.InetAddressUtils;
import ru.vas.restcore.db.domain.Subscription;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class SubscriptionDTO implements Serializable {
    @NotBlank
    @Size(max = 255)
    private String name;
    private String description;
    @NotBlank
    private String value;
    private Subscription.Type type;
    private Subscription.Notification notification;

    public SubscriptionDTO(Subscription subscription) {
        this.name = subscription.getName();
        this.description = subscription.getDescription();
        this.value = subscription.getValue();
        this.type = subscription.getType();
        this.notification = subscription.getNotification();
    }
}
