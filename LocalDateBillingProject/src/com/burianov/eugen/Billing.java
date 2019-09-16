package com.burianov.eugen;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.math.*;
import java.text.*;
import java.time.*;
import java.time.format.*;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Subscription {
    public Subscription() {}
    public Subscription(int id, int customerId, int monthlyPriceInDollars) {
        this.id = id;
        this.customerId = customerId;
        this.monthlyPriceInDollars = monthlyPriceInDollars;
    }

    public int id;
    public int customerId;
    public int monthlyPriceInDollars;
}

class User {
    public User() {}
    public User(int id, String name, LocalDate activatedOn, LocalDate deactivatedOn, int customerId) {
        this.id = id;
        this.name = name;
        this.activatedOn = activatedOn;
        this.deactivatedOn = deactivatedOn;
        this.customerId = customerId;
    }

    public int id;
    public String name;
    public LocalDate activatedOn;
    public LocalDate deactivatedOn;
    public int customerId;
}
public class Billing {


    public static void main(String[] args) {
        Subscription newPlan = new Subscription(1, 1, 4);
        User[] noUsers = new User[0];
        User[] constantUsers = {
                new User(1, "Employee #1", LocalDate.of(2018, 11, 4), null, 1),
                new User(2, "Employee #2", LocalDate.of(2018, 12, 4), null, 1)
        };
        User[] userSignedUp = {
                new User(1, "Employee #1", LocalDate.of(2018, 11, 4), null, 1),
                new User(2, "Employee #2", LocalDate.of(2018, 12, 4), null, 1),
                new User(3, "Employee #3", LocalDate.of(2019, 01, 10), null, 1),
                new User(4, "Employee #4", LocalDate.of(2018, 01, 10), LocalDate.of(2019, 01, 20), 1),
        };
        double result = Billing.billFor("2019-01", newPlan, constantUsers);
        System.out.println(result);
        System.out.println(Billing.billFor("2019-01", newPlan, userSignedUp));
    }

    public static double billFor(String month, Subscription activeSubscription, User[] users) {
        // your code here!
        if(month==null || month.isEmpty()){
            return 0.01;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate firstDayOfMonth = LocalDate.parse(month+"-01", formatter);
        LocalDate lastDayOfMonth = lastDayOfMonth(firstDayOfMonth);
        int numberOfDays = (int) Duration.between(firstDayOfMonth.atStartOfDay(), lastDayOfMonth.atStartOfDay()).toDays()+1;

        double runningTotal = Arrays.stream(users).map(it -> difference(
                greaterStartDateInTheMonth(firstDayOfMonth, it.activatedOn),
                smallerEndDateInTheMonth(lastDayOfMonth, it.deactivatedOn)) )
                .map(value -> (double) value*activeSubscription.monthlyPriceInDollars / numberOfDays )
        .reduce(0.0, Double::sum);


        runningTotal = Math.round(runningTotal*100)/100.0;
        
        return runningTotal;
    }

    /**
     * Returns refDate if subscrStartDt is null or before refDate (meaning the user subscribed before the refDate start).
     * Return subscrStartDt if it is greater than refDate but in the same month.
     * Returns null if subscrStartDt is in a month/year after ref date (meaning the billing is done the month before subscription)
     * @param refDate
     * @param subscrStartDt
     * @return
     */
    public static LocalDate greaterStartDateInTheMonth(LocalDate refDate, LocalDate subscrStartDt){
        if(subscrStartDt==null) {
            return refDate;
        }
        refDate.atTime(0,0);
        refDate.atTime(0,0);
//        Returns null if subscrStartDt is greater than last date of refDate month
        if(subscrStartDt.isAfter(lastDayOfMonth(refDate))){
            return  null;
        }
        return refDate.isBefore(subscrStartDt)?subscrStartDt:refDate;
    }

    public static LocalDate smallerEndDateInTheMonth(LocalDate refDate, LocalDate subscrEndDate){
        if(subscrEndDate==null) {
            return refDate;
        }
        refDate.atTime(0,0);
        refDate.atTime(0,0);
//        Returns null if subscrEndDate is before first day of refDate month
        if(subscrEndDate.isBefore(firstDayOfMonth(refDate))){
            return  null;
        }
        return refDate.isAfter(subscrEndDate)?subscrEndDate:refDate;
    }
    public static int difference(LocalDate start, LocalDate end){
        int result = 0;
        if(start==null || end == null){
            return 0;
        }
        return (int)Duration.between(start.atStartOfDay(), end.atStartOfDay()).toDays() + 1;

    };

    public static LocalDate getLaterDate(LocalDate refDate, LocalDate date2){
        if(date2==null)
            return refDate;
        return  refDate.isAfter(date2)?refDate:date2;
    }

    /*******************
     * Helper functions *
     *******************/

    /**
     Takes a LocalDate object and returns a LocalDate which is the first day
     of that month. For example:

     firstDayOfMonth(LocalDate.of(2019, 2, 7)) // => LocalDate.of(2019, 2, 1)
     **/
    private static LocalDate firstDayOfMonth(LocalDate date) {
        return date.withDayOfMonth(1);
    }

    /**
     Takes a LocalDate object and returns a LocalDate which is the last day
     of that month. For example:

     lastDayOfMonth(LocalDate.of(2019, 2, 7)) // => LocalDate.of(2019, 2, 28)
     **/
    private static LocalDate lastDayOfMonth(LocalDate date) {
        return date.withDayOfMonth(date.lengthOfMonth());
    }

    /**
     Takes a LocalDate object and returns a LocalDate which is the next day.
     For example:

     nextDay(LocalDate.of(2019, 2, 7))  // => LocalDate.of(2019, 2, 8)
     nextDay(LocalDate.of(2019, 2, 28)) // => LocalDate.of(2019, 3, 1)
     **/
    private static LocalDate nextDay(LocalDate date) {
        return date.plusDays(1);
    }
}