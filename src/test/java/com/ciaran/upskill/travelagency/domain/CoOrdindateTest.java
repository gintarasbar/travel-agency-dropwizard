package com.ciaran.upskill.travelagency.domain;

import com.ciaran.upskill.travelagency.storage.CitiesRepository;
import com.google.common.io.Resources;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.IsCloseTo.closeTo;

public class CoOrdindateTest {

    @Test
    public void shouldReturnDistanceInKilometersWithinSmallErrorMargin() throws Exception {
        CoOrdinate coOrdinate1 = new CoOrdinate(51.514125,-.093689); //London Co-Ordinates
        CoOrdinate coOrdinate2 = new CoOrdinate(48.866667,2.333333); //Paris Co-Ordinates
        double distance = coOrdinate1.getDistance(coOrdinate2);
        double reverseDistance = coOrdinate2.getDistance(coOrdinate1);
        assertThat(distance, is(closeTo(344, 5.0)));
        assertThat(distance, is(equalTo(reverseDistance)));
    }



}