/* ==========================================================================
 * WEEKLY PAYROLL CALCULATOR
 * ==========================================================================
 * PURPOSE (in plain English):
 * Small business owners often add up employee hours and work out paychecks
 * by hand. That is slow and easy to get wrong, especially when someone
 * works overtime. This program does that job for them.
 *
 * The user types in each employee's name, their hourly pay rate, and the
 * hours they worked on each of the seven days of the week. The program
 * adds up the hours, separates normal time from overtime (which is paid
 * at a higher rate), works out the paycheck before and after taxes,
 * prints a pay stub to the screen, and saves a copy into a text file so
 * the owner keeps a permanent record.
 * ==========================================================================
 */

// These "import" lines bring in ready-made tools that Java provides, so
// that we do not have to build everything from scratch ourselves.
import java.util.Scanner;               // reads what the user types
import java.util.ArrayList;             // a list that grows as we add to it
import java.util.InputMismatchException; // alarm raised on bad typed input
import java.io.File;                    // represents a file on the computer
import java.io.IOException;             // alarm raised on file problems
import java.nio.file.Files;             // writes text into a file
import java.nio.file.StandardOpenOption; // options for how to write a file
import java.text.DecimalFormat;         // formats numbers, e.g. $1,234.56

public class Main {

    // ------------------------------------------------------------------
    // COMPANY PAY RULES
    // The business rules are written once, here at the top. If policy or
    // the law changes, only these lines need editing rather than hunting
    // through the whole program for scattered numbers.
    // ------------------------------------------------------------------
    static final double OVERTIME_THRESHOLD = 40.0;  // hours before overtime
    static final double OVERTIME_MULTIPLIER = 1.5;  // "time and a half"
    static final double TAX_RATE = 0.22;            // 22% withheld for tax
    static final int DAYS_IN_WEEK = 7;

    // One shared tool for reading whatever is typed on the keyboard.
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        // A growable list holding one Employee record per worker.
        ArrayList<Employee> staff = new ArrayList<Employee>();

        // The file where the finished payroll report will be saved.
        File logFile = new File("payroll_log.txt");

        // The day names, used to label each question we ask.
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday",
                         "Friday", "Saturday", "Sunday"};

        System.out.println("========================================");
        System.out.println("      WEEKLY PAYROLL CALCULATOR");
        System.out.println("========================================");
        System.out.println();

        // --------------------------------------------------------------
        // STEP 1: Find out how many employees we are processing.
        // readWholeNumber() refuses to move on until a sensible number is
        // given, so a typing mistake cannot crash the program.
        // --------------------------------------------------------------
        int employeeCount = readWholeNumber(
                "How many employees are on this payroll?: ");

        // --------------------------------------------------------------
        // STEP 2: Collect the details for each employee, one at a time.
        // This loop repeats once per employee. With 3 employees, the block
        // inside runs 3 times.
        // --------------------------------------------------------------
        for (int i = 0; i < employeeCount; i++) {

            System.out.println();
            System.out.println("--- Employee " + (i + 1) + " of "
                    + employeeCount + " ---");

            // Ask for the name, and keep asking if nothing is typed.
            String name = "";
            while (name.trim().isEmpty()) {
                System.out.print("Enter employee name: ");
                name = input.nextLine();
                if (name.trim().isEmpty()) {
                    System.out.println("  >> A name is required.");
                }
            }

            // Ask for the hourly rate, rejecting anything that is not a
            // sensible amount of money.
            double rate = readDecimalNumber(
                    "Enter hourly pay rate (e.g. 18.50): $", 0.0, 1000.0);

            // Create the record that holds everything about this worker.
            Employee worker = new Employee(name.trim(), rate);

            // Ask how many hours were worked on each of the seven days.
            // Note the loop stops at "less than" the number of days, not
            // "less than or equal to": the day slots are numbered 0 to 6,
            // so asking for slot 7 would be asking for a day not there.
            for (int d = 0; d < DAYS_IN_WEEK; d++) {
                double hours = readDecimalNumber(
                        "  Hours worked on " + days[d] + ": ", 0.0, 24.0);
                worker.addHours(hours);
            }

            // Add this finished record to the list of all employees.
            staff.add(worker);
        }

        // --------------------------------------------------------------
        // STEP 3: Build the printed report.
        // The whole report is assembled as one block of text so the exact
        // same text can be shown on screen AND saved to the file. That
        // guarantees the two always match each other.
        // --------------------------------------------------------------
        DecimalFormat money = new DecimalFormat("$#,##0.00");
        DecimalFormat hrs = new DecimalFormat("0.0#");

        String report = "";
        report += "\n========================================\n";
        report += "        WEEKLY PAYROLL REPORT\n";
        report += "========================================\n";

        // Running totals for the whole company.
        double companyGross = 0.0;
        double companyNet = 0.0;
        double companyOtHours = 0.0;

        // Walk through the employee list, printing a pay stub for each.
        for (Employee worker : staff) {
            report += "\nEmployee:        " + worker.getName() + "\n";
            report += "Hourly rate:     "
                    + money.format(worker.getPayRate()) + "\n";
            report += "Total hours:     "
                    + hrs.format(worker.getTotalHours()) + "\n";
            report += "  Regular hours: "
                    + hrs.format(worker.getRegularHours()) + "\n";
            report += "  Overtime hours: "
                    + hrs.format(worker.getOvertimeHours()) + "\n";
            report += "Gross pay:       "
                    + money.format(worker.getGrossPay()) + "\n";
            report += "Tax withheld:    "
                    + money.format(worker.getTaxWithheld()) + "\n";
            report += "NET PAY:         "
                    + money.format(worker.getNetPay()) + "\n";

            // A note flagging anyone who went into overtime, since that is
            // the figure a manager most often wants to spot quickly.
            if (worker.getOvertimeHours() > 0) {
                report += "  ** Worked overtime this week. **\n";
            }
            report += "----------------------------------------\n";

            // Fold this person's figures into the company-wide totals.
            companyGross += worker.getGrossPay();
            companyNet += worker.getNetPay();
            companyOtHours += worker.getOvertimeHours();
        }

        // --------------------------------------------------------------
        // STEP 4: Add the company-wide summary at the foot of the report.
        // --------------------------------------------------------------
        report += "\nCOMPANY TOTALS\n";
        report += "Employees paid:      " + staff.size() + "\n";
        report += "Total overtime hrs:  "
                + hrs.format(companyOtHours) + "\n";
        report += "Total gross payroll: "
                + money.format(companyGross) + "\n";
        report += "Total net payroll:   "
                + money.format(companyNet) + "\n";

        // Work out the average paycheck. We check there is at least one
        // employee first, because dividing by zero would give a
        // meaningless result instead of a number.
        if (staff.size() > 0) {
            double averageGross = companyGross / staff.size();
            report += "Average gross pay:   "
                    + money.format(averageGross) + "\n";
        } else {
            report += "No employees entered, so nothing to average.\n";
        }
        report += "========================================\n";

        // Show the finished report on the screen.
        System.out.println(report);

        // --------------------------------------------------------------
        // STEP 5: Save a permanent copy of the report to a text file.
        // Writing a file can fail (full disk, read-only folder), so this
        // sits in a "try" block. On failure the program explains itself
        // politely instead of crashing.
        // --------------------------------------------------------------
        try {
            // CREATE makes the file if it does not exist yet.
            // APPEND adds to the end so earlier weeks are not erased.
            Files.writeString(logFile.toPath(), report,
                              StandardOpenOption.CREATE,
                              StandardOpenOption.APPEND);
            System.out.println("Report saved to " + logFile.getName());
        } catch (IOException ex) {
            System.out.println("Could not save the file: "
                    + ex.getMessage());
            System.out.println("The report above is still correct.");
        }

        // Close the keyboard reader now that we are finished with it.
        input.close();
    }

    /* Asks a question and waits for a whole number of zero or more. If
     * letters, symbols, or a negative number are typed, it explains the
     * problem and asks again instead of stopping the program. */
    static int readWholeNumber(String question) {
        int value = 0;
        boolean valid = false;

        // Keep looping for as long as we do not yet have a good answer.
        while (!valid) {
            try {
                System.out.print(question);
                value = input.nextInt();

                if (value < 0) {
                    System.out.println("  >> Enter zero or more.");
                } else {
                    valid = true; // answer accepted, so stop looping
                }
                // Clear the leftover "Enter" keypress from the buffer.
                // Without this, the next question that reads a line of
                // text grabs that empty leftover instead of waiting.
                input.nextLine();
            } catch (InputMismatchException ex) {
                System.out.println("  >> That is not a whole number.");
                input.nextLine(); // discard the bad input
            }
        }
        return value;
    }

    /* Asks a question and waits for a decimal number between a smallest
     * and a largest allowed value. Used for both pay rates and daily
     * hours, since both are decimals with a sensible range. */
    static double readDecimalNumber(String question, double min,
                                    double max) {
        double value = 0.0;
        boolean valid = false;

        while (!valid) {
            try {
                System.out.print(question);
                value = input.nextDouble();

                if (value < min || value > max) {
                    System.out.println("  >> Enter a number between "
                            + min + " and " + max + ".");
                } else {
                    valid = true;
                }
                input.nextLine(); // clear the leftover keypress
            } catch (InputMismatchException ex) {
                System.out.println("  >> That is not a valid number.");
                input.nextLine(); // discard the bad input
            }
        }
        return value;
    }
}

/* ==========================================================================
 * EMPLOYEE
 * ==========================================================================
 * The blueprint for one worker's record. It holds their name, their hourly
 * rate, and the hours worked each day, and it knows how to work out their
 * pay from those facts.
 *
 * Keeping the pay maths in here means the main program can simply ask
 * "what is this person's net pay?" without needing to know the formula.
 * ==========================================================================
 */
class Employee {

    private String name;                  // the worker's name
    private double payRate;               // what they earn per hour
    private ArrayList<Double> dailyHours; // hours worked, one per day

    /* Creates a new employee record from a name and an hourly rate. The
     * list of daily hours starts empty and is filled in afterwards. */
    public Employee(String name, double payRate) {
        this.name = name;
        this.payRate = payRate;
        this.dailyHours = new ArrayList<Double>();
    }

    /* Records the hours worked on one more day. */
    public void addHours(double hours) {
        dailyHours.add(hours);
    }

    public String getName() {
        return name;
    }

    public double getPayRate() {
        return payRate;
    }

    /* Adds up every day's hours to give the total for the week. */
    public double getTotalHours() {
        double total = 0.0;
        for (double h : dailyHours) {
            total += h;
        }
        return total;
    }

    /* Returns the hours paid at the normal rate. Anything up to the
     * 40-hour threshold is regular time. Someone who worked 52 hours
     * still has only 40 regular hours; the rest counts as overtime. */
    public double getRegularHours() {
        if (getTotalHours() > Main.OVERTIME_THRESHOLD) {
            return Main.OVERTIME_THRESHOLD;
        }
        return getTotalHours();
    }

    /* Returns the hours paid at the higher overtime rate. Math.max is
     * used so a short week can never produce a negative overtime figure.
     * Someone who worked 30 hours has 0 hours of overtime, not -10. */
    public double getOvertimeHours() {
        return Math.max(0.0, getTotalHours() - Main.OVERTIME_THRESHOLD);
    }

    /* Works out the full paycheck before tax: normal hours at the normal
     * rate, plus overtime hours at time and a half. */
    public double getGrossPay() {
        double regularPay = getRegularHours() * payRate;
        double overtimePay = getOvertimeHours() * payRate
                * Main.OVERTIME_MULTIPLIER;
        return regularPay + overtimePay;
    }

    /* Works out how much money is held back for taxes. */
    public double getTaxWithheld() {
        return getGrossPay() * Main.TAX_RATE;
    }

    /* Works out the amount the employee actually takes home. */
    public double getNetPay() {
        return getGrossPay() - getTaxWithheld();
    }
}
