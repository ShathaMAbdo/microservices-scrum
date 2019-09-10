package se.BTH.sprintservice.Annotations;

import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PositiveNumber {
    String message() default "The OEstimate number must be positive";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}