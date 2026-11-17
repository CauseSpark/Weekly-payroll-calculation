# Weekly Payroll Calculator

**Automate weekly payroll processing with accurate overtime calculation and tax withholding.**

Weekly Payroll Calculator is a Java-based utility designed to streamline the payroll process for small business owners. Instead of manually adding up employee hours and calculating paychecks, this tool handles all the math—including overtime at premium rates—and generates professional pay stubs for your records.

## Features

- **Employee Management**: Add unlimited employees with their names and hourly pay rates
- **Daily Hour Tracking**: Record hours worked for each day of the week (Monday through Sunday)
- **Automatic Overtime Calculation**: Separates regular hours (up to 40/week) from overtime hours (paid at 1.5x rate)
- **Tax Withholding**: Automatically calculates and withholds taxes from gross pay
- **Detailed Pay Stubs**: Generates professional pay stubs showing:
  - Regular hours and overtime hours breakdown
  - Gross pay calculation
  - Tax withheld
  - Net pay (take-home amount)
- **Overtime Flagging**: Highlights employees who worked overtime for quick manager review
- **Company-Wide Summary**: Shows total payroll, average pay, and aggregate overtime hours
- **Permanent Records**: Saves all payroll reports to a file for audit trails and historical reference

## How It Works

The payroll calculator follows a straightforward process:

1. **Specify Employee Count**: Tell the program how many employees are on this week's payroll
2. **Enter Employee Data**: For each employee, provide:
   - Full name
   - Hourly pay rate (e.g., $18.50)
   - Hours worked for each day of the week
3. **Automatic Calculations**: The program handles all math:
   - Total weekly hours
   - Regular vs. overtime hours breakdown
   - Gross pay (regular + overtime premium)
   - Tax withholding (22% by default)
   - Net pay
4. **Review & Save**: See the complete payroll report on screen and automatically save to a file

## Getting Started

### Prerequisites
- Java 11 or higher

### Compilation

```bash
javac Main.java
```

### Running the Program
```bash
java Main
```

### Usage Example

```
========================================
      WEEKLY PAYROLL CALCULATOR
========================================

How many employees are on this payroll?: 2

--- Employee 1 of 2 ---
Enter employee name: Sarah Johnson
Enter hourly pay rate (e.g. 18.50): $22.50
  Hours worked on Monday: 8
  Hours worked on Tuesday: 8
  Hours worked on Wednesday: 8
  Hours worked on Thursday: 8
  Hours worked on Friday: 9
  Hours worked on Saturday: 0
  Hours worked on Sunday: 0

--- Employee 2 of 2 ---
Enter employee name: Mike Chen
Enter hourly pay rate (e.g. 18.50): $18.75
  Hours worked on Monday: 8
  Hours worked on Tuesday: 8
  Hours worked on Wednesday: 8
  Hours worked on Thursday: 8
  Hours worked on Friday: 8
  Hours worked on Saturday: 6
  Hours worked on Sunday: 0
```

### Sample Output

```
========================================
        WEEKLY PAYROLL REPORT
========================================

Employee:        Sarah Johnson
Hourly rate:     $22.50
Total hours:     41.0
  Regular hours: 40.0
  Overtime hours: 1.0
Gross pay:       $933.75
Tax withheld:    $205.43
NET PAY:         $728.33
  ** Worked overtime this week. **
----------------------------------------

Employee:        Mike Chen
Hourly rate:     $18.75
Total hours:     46.0
  Regular hours: 40.0
  Overtime hours: 6.0
Gross pay:       $918.75
Tax withheld:    $202.12
NET PAY:         $716.62
  ** Worked overtime this week. **
----------------------------------------

COMPANY TOTALS
Employees paid:      2
Total overtime hrs:  7.0
Total gross payroll: $1,852.50
Total net payroll:   $1,444.95
Average gross pay:   $926.25
========================================
```

## Configuration

### Customizing Pay Rules

The program's business rules are defined at the top of `Main.java` and can be easily modified:

```java
static final double OVERTIME_THRESHOLD = 40.0; // Change overtime threshold
static final double OVERTIME_MULTIPLIER = 1.5; // Change overtime rate (e.g., 2.0 for double-time)
static final double TAX_RATE = 0.22;           // Change tax withholding rate
```

If your jurisdiction or company policy requires different overtime rules or tax rates, simply update these values and recompile.

## Output Files

**payroll_log.txt**: A cumulative log of all payroll reports. New reports are appended to this file, creating a permanent record of every payroll processed. This file is useful for:

- Audit trails
- Historical reference
- Tax documentation
- Payroll verification

## Data Validation

The program includes these safeguards:

- Rejects non-numeric input for pay rates and hours
- Ensures pay rates and hours are within sensible ranges
- Requires a name for every employee
- Prevents entry of negative values
- Validates hours don't exceed 24 per day
- Gracefully handles file write errors without losing data

## Project Structure

- **Main.java**: The entry point containing the payroll processing logic, user interface, and file operations
- **Employee class** (also in `Main.java`): Encapsulates employee data and handles all pay calculations:
  - `getTotalHours()`: Sums daily hours
  - `getRegularHours()`: Calculates hours up to overtime threshold
  - `getOvertimeHours()`: Calculates hours beyond threshold
  - `getGrossPay()`: Computes pay before tax
  - `getTaxWithheld()`: Calculates tax deduction
  - `getNetPay()`: Computes take-home pay

## Why Use This Tool?

Small business owners benefit from automation:

- **Accuracy**: Eliminates manual calculation errors
- **Speed**: Processes payroll in minutes instead of hours
- **Compliance**: Consistent tax withholding calculations
- **Documentation**: Permanent record for IRS and payroll audits
- **Transparency**: Employees see detailed breakdown of their pay
- **Scalability**: Handles any number of employees

## Common Tasks

### Processing Weekly Payroll
Run the program each Friday (or your payroll day) to calculate and record the week's payroll.

### Changing Tax Rate
Edit `TAX_RATE` in Main.java to match your jurisdiction's requirements and recompile.

### Adjusting Overtime Rules
Modify `OVERTIME_THRESHOLD` and `OVERTIME_MULTIPLIER` for company-specific policies (e.g., 35-hour threshold, double-time pay).

### Reviewing Historical Payroll
Open `payroll_log.txt` to see the reports from all previous payroll runs, oldest first.

## Notes

- Overtime is calculated based on weekly totals, not daily. Hours over 8 in a single day do not automatically trigger overtime unless the weekly total exceeds 40 hours.
- Tax withholding is applied at a flat rate; for more complex tax scenarios (deductions, withholding tables), you may need additional tools.
- The program assumes standard payroll rules; consult your accountant for jurisdiction-specific requirements.

## License

This project is open source. Feel free to use, modify, and distribute it.

## Contributing

Contributions are welcome! Whether it's bug fixes, feature suggestions, or improvements to the user experience, please feel free to submit issues or pull requests.

---

**Simplify payroll processing and keep accurate records of your employee compensation.**
