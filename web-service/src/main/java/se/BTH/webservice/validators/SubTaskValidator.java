package se.BTH.webservice.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import se.BTH.webservice.models.SubTask;

@Component
public class SubTaskValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return SubTask.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        SubTask subTask = (SubTask) o;

        if (subTask.getOEstimate() < 0 ) {
            errors.rejectValue("OEstimate", "value.subtaskAttr.OEstimate");
        }

    }
}
