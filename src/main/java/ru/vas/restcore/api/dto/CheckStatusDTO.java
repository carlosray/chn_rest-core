package ru.vas.restcore.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.vas.restcore.db.domain.Subscription;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
public class CheckStatusDTO {
    private String value;
    private Subscription.Type type;
    private Boolean status;

    public CheckStatusDTO(Subscription subscription) {
        this.value = subscription.getValue();
        this.type = subscription.getType();
    }

}
