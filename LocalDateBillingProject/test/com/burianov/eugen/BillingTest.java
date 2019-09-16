package com.burianov.eugen;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BillingTest {

    Billing billing ;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @BeforeEach
    void setUp() {
        billing = new Billing();
    }

    @Test
    void billFor() {
    }

    @Test
    void greaterStartDateInTheMonthTestNullReturnsRefdate() {
        LocalDate date1 = LocalDate.parse("2019-01-01", formatter);
        LocalDate result = Billing.greaterStartDateInTheMonth(date1, null);
        assertEquals(result, date1);
    }
    @Test
    void greaterStartDateInTheMonthTestBeforeMonthReturnsRefdate() {
        LocalDate date1 = LocalDate.parse("2019-01-01", formatter);
        LocalDate date2 = LocalDate.parse("2018-12-31", formatter);
        LocalDate result = Billing.greaterStartDateInTheMonth(date1, date2);
        assertEquals(date1, result);
    }
    @Test
    void greaterStartDateInTheMonthTestLaterInMoReturnsSubsrDate(){
        LocalDate date1 = LocalDate.parse("2019-01-01", formatter);
        LocalDate date2 = LocalDate.parse("2019-01-02", formatter);
        LocalDate result = Billing.greaterStartDateInTheMonth(date1, date2);
        assertEquals(date2, result);
    }

    @Test
    void greaterStartDateInTheMonthTestAfterMonthReturnsNull(){
        LocalDate date1 = LocalDate.parse("2019-01-01", formatter);
        LocalDate date2 = LocalDate.parse("2019-02-02", formatter);
        LocalDate result = Billing.greaterStartDateInTheMonth(date1, date2);
        assertEquals(result, null);
    }

    @Test void testBillForVariedScenario(){
        Subscription newPlan = new Subscription(1, 1, 31);

        List<User> users = new ArrayList<>();
        users.add(new User(1, "C1", LocalDate.of(2019, 01, 01), null, 1));

        double result = Billing.billFor("2019-01", newPlan, users.toArray(new User[0]) );
        assertEquals(31.0, result, "one user on 31 days should be 31.0");

        users.add(new User(2, "C2", LocalDate.of(2018,01,01), LocalDate.of(2019, 01, 10), 1));
        result = Billing.billFor("2019-01", newPlan, users.toArray(new User[0]));
        assertEquals(41.0, result, "Expect 41.0 withe extra user billed for 10 days");

        users.add(new User(3, "C3", LocalDate.of(2019, 02, 01), null, 1));
        result = Billing.billFor("2019-01", newPlan, users.toArray(new User[0]));
        assertEquals(41.0, result, "User activated after billing cycle should not impact");

        users.add(new User(4, "C4", LocalDate.of(2019, 01, 22), null, 1));
        result = Billing.billFor("2019-01", newPlan, users.toArray(new User[0]));
        assertEquals(51.0, result, "Expected 51 - User started 10 days before month end");
    }

    @Test void testBillForRoundTwoDecimals() {
        Subscription newPlan = new Subscription(1, 1, 4);

        List<User> users = new ArrayList<>();
        users.add(new User(1, "C1", LocalDate.of(2019, 01, 21), null, 1));

        double result = Billing.billFor("2019-01", newPlan, users.toArray(new User[0]));
        //this will do 4*11/31 result is 1.419354838709677 so expecting 1.42
        assertEquals(1.42, result, "4*11/31 result is 1.419354838709677 so expecting 1.42");

    }


        @Test
    void getLaterDate() {
    }
}