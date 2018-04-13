import edu.duke.DirectoryResource;
import edu.duke.FileResource;
import org.apache.commons.csv.CSVRecord;

import java.io.File;

public class BabyNames {
    public static void main(String[] args) {
        NameFunctions names = new NameFunctions();
        names.tester();
    }

    public static class NameFunctions {
        public void tester() {
            FileResource fr = new FileResource("us_babynames_by_year/yob1905.csv");
            //FileResource fr = new FileResource("example-small.csv");
            //totalBirths(fr);
            countNames(fr);

            int femaleRank = getRank(1960, "Emily", "F");
            System.out.println("female Emily rank : " + femaleRank);
            int maleRank = getRank(1971, "Frank", "M");
            System.out.println("male Frank rank : " + maleRank);
            System.out.println();

            String name = getName(1980, 350, "F");
            System.out.println("Girl's name of rank 350 in 1980: " + name);
            String name1 = getName(1982, 450, "M");
            System.out.println("Boy's name of rank 450 in 1982: " + name1);
            System.out.println();

            String nameSwap = whatIsNameInYear("Susan", 1972, 2014, "F");
            System.out.println("Susan born in 1972 would be " + nameSwap + " if she was born in 2014.");
            nameSwap = whatIsNameInYear("Owen", 1974, 2014, "M");
            System.out.println("Owen born in 1974 would be " + nameSwap + " if he was born in 2014.");
            System.out.println();


            int yearOfHighest = yearOfHighestRank("Genevieve", "F");
            System.out.println("Genevieve: " + yearOfHighest);
            int yearOfHighest2 = yearOfHighestRank("Mich", "M");
            System.out.println("Mich: " + yearOfHighest2);
            System.out.println();

            double averageRank = getAverageRank("Susan", "F");
            System.out.println("name: Susan, gender: F, average rank: " + averageRank);
            averageRank = getAverageRank("Robert", "M");
            System.out.println("name: Robert, gender: M, average rank: " + averageRank);
            System.out.println();

            int totalRankedHigher = getTotalBirthsRankedHigher(1990, "Emily", "F");
            System.out.println("name: Emily, gender: F, total ranked higher: " + totalRankedHigher);

            totalRankedHigher = getTotalBirthsRankedHigher(1990, "Drew", "M");
            System.out.println("name: Drew, gender: M, total ranked higher: " + totalRankedHigher);
        }

        public void countNames(FileResource fr) {
            int totalNames = 0;
            int girlsNames = 0;
            int boysNames = 0;
            for (CSVRecord rec: fr.getCSVParser(false)) {
                if (rec.get(1).equals("F")) {
                    girlsNames++;
                } else {
                    boysNames++;
                }
                totalNames++;
            }
            System.out.println("total names: " + totalNames);
            System.out.println("girl's names: " + girlsNames);
            System.out.println("boy's names: " + boysNames);
        }

        public void totalBirths(FileResource fr) {
            int totalBirths = 0;
            int totalBoys = 0;
            int totalGirls = 0;
            for (CSVRecord rec: fr.getCSVParser(false)) {
                int numBorn = Integer.parseInt(rec.get(2));
                totalBirths += numBorn;
                if (rec.get(1).equals("M")) {
                    totalBoys += numBorn;
                    // System.out.println(rec.get(0));
                } else {
                    totalGirls += numBorn;
                    // System.out.println(rec.get(0));
                }
            }
            System.out.println("total births = " + totalBirths);
            System.out.println("total boys = " + totalBoys);
            System.out.println("total girls = " + totalGirls);
            System.out.println();
        }

        public int getRank(int year, String name, String gender) {
            //FileResource fr = new FileResource("example-small.csv");
            String filename = "us_babynames_by_year/yob" + year + ".csv";
            FileResource fr = new FileResource(filename);
            int rank = 1;
            int numberOfFemales = 0;
            //first identify if its girls or boys
            for (CSVRecord rec: fr.getCSVParser(false)) {
                //for females
                if (rec.get(1).equals("F")) {
                    numberOfFemales++;
                    if (rec.get(0).equals(name) && rec.get(1).equals(gender)) {
                        return rank;
                    }
                } else {
                    //for males - need to reset rank back to 1 (on only the first iteration or when we have a match
                    if (rec.get(0).equals(name) && rec.get(1).equals(gender)) {
                        rank = rank - numberOfFemales;
                        return rank;
                    }
                }
                rank++;
            }
            return -1;
        }

        public String getName(int year, int rank, String gender) {
            String filename = "us_babynames_by_year/yob" + year + ".csv";
            FileResource fr = new FileResource(filename);
            int count = 1;
            int numberOfFemales = 0;
            for (CSVRecord rec: fr.getCSVParser(false)) {
                //count all of the females that you iterate through
                if (rec.get(1).equals("F")) {
                    numberOfFemales++;
                }
                if (gender.equals("F")) {
                    if (rank == count) {
                        return rec.get(0);
                    }
                } else {
                    if (rank + numberOfFemales == count && gender.equals("M")) {
                        return rec.get(0);
                    }
                }
                count++;
            }
            return "NO NAME";
        }

        public String whatIsNameInYear(String name, int year, int newYear, String gender) {
            // this method determines what the name would have been named if they
            // were born in a different year, based on the same popularity

            // That is, you should determine the rank of name in the year they were born
            int rank = getRank(year, name, gender);

            // and then print the name born in newYear that is at the same rank and same gender
            String newName = getName(newYear, rank, gender);

            return newName;
        }

        public int getRank(File file, String name, String gender) {
            //String filename = "yob" + year + "short.csv";
            FileResource fr = new FileResource(file);
            int rank = 1;
            int numberOfFemales = 0;
            //first identify if its girls or boys
            for (CSVRecord rec : fr.getCSVParser(false)) {
                //for females
                if (rec.get(1).equals("F")) {
                    numberOfFemales++;
                    if (rec.get(0).equals(name) && rec.get(1).equals(gender)) {
                        return rank;
                    }
                } else {
                    //for males - need to reset rank back to 1 (on only the first iteration or when we have a match
                    if (rec.get(0).equals(name) && rec.get(1).equals(gender)) {
                        rank = rank - numberOfFemales;
                        return rank;
                    }
                }
                rank++;
            }
            return -1;
        }

        public int yearOfHighestRank(String name, String gender) {
            // This method selects a range of files to process and returns an integer,
            // the year with the highest rank for the name and gender.
            int highestRank = 10000000;
            //int tempRank = -1;
            int tempRank = 10000000;
            int correspondingYear = 0;
            DirectoryResource dr = new DirectoryResource();
            for (File f : dr.selectedFiles()) {
                tempRank = getRank(f, name, gender);

                if (highestRank >= tempRank && tempRank != -1) {
                    highestRank = tempRank;
                    System.out.println("highest rank: " + highestRank);
                    System.out.println("highest rank filename: " + f.getName());
                    correspondingYear = Integer.parseInt(f.getName().substring(3, 7));
                }
                //System.out.format("file = %s, rank = %d\n", f.getName(), getRank(f, name, gender));
            }
            if (highestRank == -1) {
                return -1;
            } else {
                return correspondingYear;
            }
        }

        public double getAverageRank(String name, String gender) {
            // This method selects a range of files to process and
            // returns a double representing the average rank of
            // the name and gender over the selected files
            double averageRank = 0.0;
            int tempRank = 0;
            int count = 0;
            //int correspondingYear = 0;
            DirectoryResource dr = new DirectoryResource();
            for (File f : dr.selectedFiles()) {
                tempRank = getRank(f, name, gender);
                averageRank = averageRank + tempRank;
                count++;
                //System.out.format("file = %s, rank = %d\n", f.getName(), getRank(f, name, gender));
            }
            averageRank = (double)averageRank/count;

            if (averageRank == -1) {
                return -1;
            } else {
                return averageRank;
            }
        }

        public int getSumRank(FileResource fr, String name, String gender) {
            int sum = 0;
            int sumMale = 0;
            //first identify if its girls or boys
            for (CSVRecord rec : fr.getCSVParser(false)) {
                //for females
                if (rec.get(1).equals("F")) {
                    if (rec.get(0).equals(name) && rec.get(1).equals(gender)) {
                        //System.out.println("sum = " + sum);
                        return sum;
                    }
                    sum = sum + Integer.parseInt(rec.get(2));
                } else {
                    //for males - need to reset rank back to 1 (on only the first iteration or when we have a match
                    if (rec.get(0).equals(name) && rec.get(1).equals(gender)) {
                        //System.out.println("sum male = " + sumMale);
                        return sumMale;
                    }
                    sumMale = sumMale + Integer.parseInt(rec.get(2));
                }
            }
            return -1;
        }

        public int getTotalBirthsRankedHigher(int year, String name, String gender) {
            // This method returns an integer, the total number of births of
            // those names with the same gender and same year who are ranked higher than name.

            // For example, if getTotalBirthsRankedHigher accesses the "yob2012short.csv" file with
            // name set to “Ethan”, gender set to “M”, and year set to 2012, then this method should
            // return 15, since Jacob has 8 births and Mason has 7 births, and those are the only
            // two ranked higher than Ethan.

            // can call a get rank on the name.
            // while iterating through it, add up the 3rd column
            // so will have to vary your get rank method a bit
            String filename = "us_babynames_by_year/yob" + year + ".csv";
            FileResource fr = new FileResource(filename);
            int sum = getSumRank(fr, name, gender);
            return sum;
        }
    }
}
