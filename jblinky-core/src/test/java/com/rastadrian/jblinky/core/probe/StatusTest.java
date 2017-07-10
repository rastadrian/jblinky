package com.rastadrian.jblinky.core.probe;

import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import org.junit.Test;

/**
 * Created on 7/8/17.
 *
 * @author Adrian Pena
 */
public class StatusTest {

    @Test
    public void validatePojo() throws Exception {
        //Given
        Class<?> pojo = Status.class;
        Validator validator = ValidatorBuilder.create()
                .with(new GetterMustExistRule())
                .with(new SetterMustExistRule())
                .with(new SetterTester())
                .with(new GetterTester())
                .build();
        validator.validate(PojoClassFactory.getPojoClass(pojo));
    }
}