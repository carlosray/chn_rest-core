package ru.vas.restcore.configuration.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import ru.vas.restcore.db.domain.UserEntity;

public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
    @Getter
    @Setter
    private Object filterObject;
    @Getter
    @Setter
    private Object returnObject;

    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    @Override
    public Object getThis() {
        return this;
    }

    /**
     * подписка принадлежит текущему пользователю
     * @param id идентификатор подписки
     */
    public boolean belongsToTheCurrentUser(Long id) {
        return ((UserEntity)this.getPrincipal()).getSubscriptions().stream()
                .anyMatch(subscription -> subscription.getId().equals(id));
    }

}
