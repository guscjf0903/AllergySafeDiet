package org.core.valiator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

public class PasswordConstraintsValidator implements ConstraintValidator<Password, String> {
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        PasswordValidator passwordValidator = new PasswordValidator(
                Arrays.asList(
                        //비밀번호 길이가 8~20 사이여야 한다.
                        new LengthRule(8, 20),
                        //대문자 확인 유무
                        // new CharacterRule(EnglishCharacterData.UpperCase, 0),
                        //적어도 하나의 소문자가 있어야 한다.
                        new CharacterRule(EnglishCharacterData.LowerCase, 1),
                        //적어도 하나의 숫자가 있어야 한다.
                        new CharacterRule(EnglishCharacterData.Digit, 1),
                        //적어도 하나의 특수문자가 있어야 한다.
                        new CharacterRule(EnglishCharacterData.Special, 1),
                        //공백문자는 허용하지 않는다.
                        new WhitespaceRule()
                )
        );
        RuleResult result = passwordValidator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }else {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(String.join(",", passwordValidator.getMessages(result)))
                    .addConstraintViolation();
            return false;
        }
    }
}

