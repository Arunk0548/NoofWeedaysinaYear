import java.util.Scanner;

/**
 * No. of Weekdays in a Year
 *
 * Write a program that accepts as input from the user, a year. The output
 * should be the number of Sundays in that year.
 *
 * Sample input: Enter year: 2015 The number of Sundays in 2015 is: 52 Note that
 * the year range is from Year 0000 to 9999. Also you will be judged extensively
 * on validating user input (year should fall within the range; no letters
 * entered etc.)
 *
 * @author Arun Kumar
 */
public class DayOfWeekInYear {

    public static final String[] weekdays = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    public static final int[] doomsdays = new int[13];

    private boolean isLeap = false;

    private final String day_to_count = "Sunday";

    private String calendar = "gregorian";

    public boolean findDayOfWeekInYear(int day, int month, int year) {

        doomsdays[0] = 0; // fill the 0 spot with a dummy -- makes it much easier to read the array
        if (isLeap(year, getCalendarName(day, month, year)) == false) {
            doomsdays[1] = 3;
            doomsdays[2] = 28;
        } else {
            doomsdays[1] = 4;
            doomsdays[2] = 29;
        }
        doomsdays[3] = 0;
        doomsdays[4] = 4;
        doomsdays[5] = 9;
        doomsdays[6] = 6;
        doomsdays[7] = 11;
        doomsdays[8] = 8;
        doomsdays[9] = 5;
        doomsdays[10] = 10;
        doomsdays[11] = 7;
        doomsdays[12] = 12;

        try {
            int dayIndex = getDayofWeek(day, month, year);
            String dayName = weekdays[dayIndex];
            String dayNameAfter365days = dayIndex + 1 != weekdays.length ? weekdays[dayIndex + 1] : weekdays[0];
            int totayDays = 0;
            if (!isLeap) {
                //52 weeks + 1 extra day, which will same as start day.
                if (dayName.equals(day_to_count)) {
                    totayDays = 53;
                } else {
                    totayDays = 52;
                }
            } else {
                //52 weeks + 2 extra day.
                if (dayName.equals(day_to_count) || dayNameAfter365days.equals(day_to_count)) {
                    totayDays = 53;
                } else {
                    totayDays = 52;
                }
            }

            System.out.println("The number of " + day_to_count + " in " + year + " is:" + totayDays);

        } catch (Exception ex) {
            System.out.println("Ops! something went wrong please try again!");
        }

        return true;
    }

    public boolean isLeap(int year, String calendar) {
        if (calendar.equals("gregorian")) {
            isLeap = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
        } else {
            isLeap = year % 4 == 0;
        }
        return isLeap;
    }

    /**
     * gregorian check -- can't use Gregorian for dates before 10/15/1582
     *
     * @param year
     * @return
     */
    public String getCalendarName(int day, int month, int year) {
        if (year < 1582) {
            return this.calendar = "julian";
        } else if (year == 1582) {
            if (month < 10) {
                return this.calendar = "julian";
            } else if (month == 10) {
                if (day < 15) {
                    return this.calendar = "julian";
                }
            }
        }
        return this.calendar = "gregorian";
    }

    public int getDayofWeek(int day, int month, int year) {

        String strYear = String.format("%04d", year);

        int quotient = (int) Integer.valueOf(strYear.substring(2, 4)) / 12;
        int remainder = Integer.valueOf(strYear.substring(2, 4)) % 12;
        int fours = (int) remainder / 4;

        // find Doomsday of year (Gregorian)
        int dayIndex = 0;
        if (calendar == "gregorian") {
            int sum = (quotient + remainder + fours) % 7;

            // here's where we determine the century doomsday!
            int anchor = 0;
            int yearFirstPart = Integer.valueOf(strYear.substring(0, 2));
            if (yearFirstPart % 4 == 0) {
                anchor = 1; // Tuesday
            } else if (yearFirstPart % 4 == 1) {
                anchor = 6; // Sunday
            } else if (yearFirstPart % 4 == 2) {
                anchor = 4; // Friday
            } else if (yearFirstPart % 4 == 3) {
                anchor = 2; // Wednesday
            }

            String doomsday = weekdays[(sum + anchor) % 7];

            // now actually find the day of the week
            int difference = day - doomsdays[month];

            // this works because the doomsday is (sum + anchor) % 7:
            dayIndex = (sum + anchor + difference) % 7;
        } else { // if it's julian, that is
            int sum = quotient + remainder + fours;

            int century = Integer.valueOf(strYear.substring(0, 2));

            // remember, 6 is sunday
            int bar = 6 + sum - century;
            while (bar < 0) { // in case bar is too small
                bar += 7;
            }
            while (bar > 6) { // in case bar is too big
                bar -= 7;
            }

            String doomsday = weekdays[bar];

            int difference = day - doomsdays[month];

            // this works because the doomsday is 6 + sum - century
            dayIndex = (bar + difference) % 7;
        }

        while (dayIndex < 0) { // in case foo is negative, bring it back into the range of 0-6
            dayIndex += 7;
        }
        while (dayIndex > 6) { // in case foo is too large (could it be? I don't know), bring it back into the range of 0-6
            dayIndex -= 7;
        }
        return dayIndex;

    }

    public static void main(String[] args) {

        DayOfWeekInYear dayCount = new DayOfWeekInYear();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a valid year between [0-9999]");
        String year = scanner.nextLine();
        if (year.matches("^[1-9]\\d{0,3}$")) {
            dayCount.findDayOfWeekInYear(1, 1, Integer.valueOf(year));
        } else {
            System.out.println("Invalid input! please enter a valid year between [0-9999]");
        }
    }
}
