A small sample application using LocalDate for a sample billing task.

Used unit tests to validate the application Billing is working. Used functional programming features from Java 8 like map() and reduce().s

Test passed: 

BillingTest
* getLaterDate()
* greaterStartDateInTheMonthTestBeforeMonthReturnsRefdate()
* testBillForRoundTwoDecimals()
* greaterStartDateInTheMonthTestAfterMonthReturnsNull()
* greaterStartDateInTheMonthTestNullReturnsRefdate()
* testBillForVariedScenario()
* greaterStartDateInTheMonthTestLaterInMoReturnsSubsrDate()

The project is built using IntellijIDEA. You should be able to open the project folder with the IDE and run the tests in BillingTest. It potentially might need to set-up the SDK.