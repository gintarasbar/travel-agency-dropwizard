package com.ciaran.upskill.travelagency.domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class CoOrdindateTest {

    @Test
    public void shouldReturnPythagoreanDistance() throws Exception {
        CoOrdinate origin = new CoOrdinate(0.0, 0.0);
        CoOrdinate destination = new CoOrdinate(3.0, 4.0);
        assertThat(origin.getDistance(destination), is(equalTo(5.0)));
    }



}