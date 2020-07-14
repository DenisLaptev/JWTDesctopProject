package com.nx.jwt_project.git_example;

import io.jsonwebtoken.Clock;

import java.time.*;
import java.util.Date;

public class MyClock implements Clock {

    private long timeShiftOfTimeZone;
    private long plusTimeCorrection;

    public MyClock(long timeShiftOfTimeZone, long plusTimeCorrection) {
        this.timeShiftOfTimeZone = timeShiftOfTimeZone;
        this.plusTimeCorrection = plusTimeCorrection;
    }

    public long getTimeShiftOfTimeZone() {
        return timeShiftOfTimeZone;
    }

    public void setTimeShiftOfTimeZone(long timeShiftOfTimeZone) {
        this.timeShiftOfTimeZone = timeShiftOfTimeZone;
    }

    public long getPlusTimeCorrection() {
        return plusTimeCorrection;
    }

    public void setPlusTimeCorrection(long plusTimeCorrection) {
        this.plusTimeCorrection = plusTimeCorrection;
    }

    @Override
    public Date now() {

        System.out.println("=========inside MyClock===========");

        //in other time zone now will be shifted by amount of timeShiftOfTimeZone hours.
        //this situation is modeled by now.plusHours(timeShiftOfTimeZone)
        ZonedDateTime now = ZonedDateTime.now();
        System.out.println("ZonedDateTime now=" + now);

        ZonedDateTime dateTimeOfSomeZone = now.plusHours(timeShiftOfTimeZone);
        System.out.println("dateTimeOfSomeZone=" + dateTimeOfSomeZone);

        //in this Clock implementation we make correction of time because of different time zone
        //in system validation
        ZonedDateTime dateTimeOfSomeZoneWithCorrection = dateTimeOfSomeZone.plusHours(plusTimeCorrection);
        System.out.println("dateTimeOfSomeZoneWithCorrection=" + dateTimeOfSomeZoneWithCorrection);

        Date dateWithCorrection = Date.from(dateTimeOfSomeZoneWithCorrection.toInstant());
        System.out.println("dateWithCorrection=" + dateWithCorrection);

        System.out.println("=========inside MyClock===========");

        return dateWithCorrection;
    }

}
