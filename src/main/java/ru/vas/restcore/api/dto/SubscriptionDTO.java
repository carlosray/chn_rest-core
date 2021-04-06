package ru.vas.restcore.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.vas.restcore.db.domain.Subscription;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class SubscriptionDTO implements Serializable {
    private Long id;
    @NotBlank
    @Size(max = 255)
    private String name;
    private String description;
    @NotBlank
    private String value;
    @NotNull
    private Subscription.Type type;
    @NotNull
    private Subscription.Notification notification;
    private Boolean status;
    private UserBaseDTO user;

    public SubscriptionDTO(Subscription subscription, boolean withUser) {
        this.id = subscription.getId();
        this.name = subscription.getName();
        this.description = subscription.getDescription();
        this.value = subscription.getValue();
        this.type = subscription.getType();
        this.notification = subscription.getNotification();
        if (withUser) {
            this.user = new UserBaseDTO(subscription.getUser());
        }
    }

    public SubscriptionDTO(Subscription subscription) {
        this(subscription, false);
    }


}
