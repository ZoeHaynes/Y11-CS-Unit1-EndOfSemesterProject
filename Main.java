import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.io.*;

/*  Name: Zoe Haynes
    Date: 23/05/23
    Assignment #: End of Semester Project
    Timetable Generator
*/

public class Main {
    public static void main(String[] args) {
        int FileNotFound = 0;
        try {
            File studentInfo = new File("/Users/zozothedodo/Downloads/Y11-Students.txt");
            Scanner scan = new Scanner(studentInfo);
            FileNotFound = 1;
            int studentCount = 0;
            //Count the number of students in the year
            while (scan.hasNextLine()) {
                scan.nextLine();
                //studentCount: how many students there are in the year
                studentCount++;
            }
            scan.close();

            System.out.println("There are " + studentCount + " students in this year group.");

            //Write all the student names into an array list
            Scanner studentNames = new Scanner(studentInfo);
            ArrayList<String> studentList = new ArrayList<>();
            for (int i = 0; i < studentCount; i++) {
                studentList.add(studentNames.nextLine());
            }

            studentNames.close();


            Scanner sc = new Scanner(System.in);

            //Determine maximum class size
            System.out.println("Please enter the maximum class size: ");
            double classSized = sc.nextDouble();
            while (studentCount % classSized != 0) {
                studentList.add("N/A");
                studentCount++;

            }

            //Randomize students in study groups
            Collections.shuffle(studentList);

            //Determine the number of classes
            int classNumbers = (int) Math.ceil(studentCount / classSized);
            int classSize = (int) classSized;

            //Create Arrays within the 2D array for each Study Group
            String[][] studyGroups = new String[classNumbers][classSize + 1];
            for (int i = 0; i < classNumbers; i++) {
                studyGroups[i][0] = "Studygroup" + (i + 1) + ": ";
            }

            //Write students to each study group within the 2D array
            int studentPosition = 0;
            for (int i = 0; i < classNumbers; i++) {
                int studyPosition = 1;
                while (studyPosition != classSize + 1) {
                    studyGroups[i][studyPosition] = studentList.get(studentPosition);
                    studyPosition++;
                    studentPosition++;
                }

            }

            //format the information regarding study groups, so it is easily readable
            String studyGroupsFile = formatFile(Arrays.deepToString(studyGroups));
            //Write the formatted information to the file Study-Groups.txt
            writeStudyGroup(studyGroupsFile);


            ArrayList<String> classes = new ArrayList<>();
            classes.add("Maths");
            classes.add("Science");
            classes.add("English");
            classes.add("InSo");
            classes.add("PhE");
            classes.add("Elective1");
            classes.add("Elective2");
            classes.add("Chinese");

            int number = 1;
            for (int i = classNumbers; i > 0; i--) {
                String[][] timeTable_StudyGroupXX =
                        {
                                {"A1_P1", "A1_P2", "A1_P3", "A1_P4"},
                                {"B1_P1", "B1_P2", "B1_P3", "B1_P4"},
                                {"A2_P1", "A2_P2", "A2_P3", "A2_P4"},
                                {"B2_P1", "B2_P2", "B2_P3", "B2_P4"},
                                {"A3_P1", "A3_P2", "A3_P3", "A3_P4"},
                                {"B3_P1", "B3_P2", "B3_P3", "B3_P4"},
                                {"A4_P1", "A4_P2", "A4_P3", "A4_P4"},
                                {"B4_P1", "B4_P2", "B4_P3", "B4_P4"},

                        };
                Collections.shuffle(classes);
                String content = formatFile(Arrays.deepToString(studyTimetable(timeTable_StudyGroupXX, classes)));
                String fileName = "Study-Group" + number + ".txt";
                createTimetableFile(fileName, content);
                number++;

            }

                Scanner scanStudent = new Scanner(System.in);
                System.out.println("Would you like to see an individual student's timetable? (Yes/No)");
                if(scanStudent.nextLine().equals("Yes")){
                    System.out.println("Enter the name of an individual student (FirstName-LastName):");
                    String keyString = scanStudent.nextLine();
                    int result = findIndex(studyGroups, keyString);
                    if(result != -1){
                        String studentStudyGroup = studyGroups[result][0].substring(0);
                        String studentStudyGroupNo = studentStudyGroup.substring(10).replace(": ", "");;
                        System.out.println("Student " + keyString + " is a member of Studygroup" + studentStudyGroupNo+ ". The timetable for this student can be found in the file Study-Group" +studentStudyGroupNo);

                    }
                    else{
                        System.out.println("Invalid student name.");

                    }


                }

                else{
                    System.out.println("Thank you for using this timetable generator.");
                    scanStudent.close();
                    System.exit(0);

                }




        } catch (Exception e) {
            e.getStackTrace();


        }

        if (FileNotFound == 0) {
            System.out.println("File Not Found.");

        }

    }

    public static void writeStudyGroup(String str) {
        try {
            FileWriter myWriter = new FileWriter("Study-Groups.txt");
            myWriter.write(str);
            myWriter.close();
            System.out.println("Study groups can be found in file 'Study-Groups.txt'");
        } catch (IOException e) {
            System.out.println("An error occurred.");

        }

    }

    public static String formatFile(String str) {
        String reformattedString = str.replace("], ", "\n");
        reformattedString = reformattedString.replace(",", "");
        reformattedString = reformattedString.replace("[", "");
        reformattedString = reformattedString.replace("]", "");
        reformattedString = reformattedString.replace("N/A", "");
        reformattedString = reformattedString.replace("  ", " ");
        return reformattedString;

    }

    public static String[][] studyTimetable(String[][] a, ArrayList<String> list) {
        a = studyTimetableElement("A1_P1", "A2_P2", "A3_P3", "A4_P4", list.get(0), a);
        a = studyTimetableElement("B1_P1", "B2_P2", "B3_P3", "B4_P4", list.get(1), a);
        a = studyTimetableElement("A1_P2", "A2_P3", "A3_P4", "A4_P1", list.get(2), a);
        a = studyTimetableElement("B1_P2", "B2_P3", "B3_P4", "B4_P1", list.get(3), a);
        a = studyTimetableElement("A1_P3", "A2_P4", "A3_P1", "A4_P2", list.get(4), a);
        a = studyTimetableElement("B1_P3", "B2_P4", "B3_P1", "B4_P2", list.get(5), a);
        a = studyTimetableElement("A1_P4", "A2_P1", "A3_P2", "A4_P3", list.get(6), a);
        a = studyTimetableElement("B1_P4", "B2_P1", "B3_P2", "B4_P3", list.get(7), a);
        return a;

    }

    public static String[][] studyTimetableElement(String a1, String a2, String a3, String a4, String b, String[][] c) {
        for (int i = 0; i < c.length; i++) {
            for (int j = 0; j < c[i].length; j++) {
                if (c[i][j].equals(a1) || c[i][j].equals(a2) || c[i][j].equals(a3) || c[i][j].equals(a4)) {
                    c[i][j] = c[i][j] + ": " + b;
                }
            }

        }

        return c;

    }

    private static boolean createTimetableFile(String a, String b){
        // Create a new file with the given file name
        try {
            FileWriter writer = new FileWriter(a);
            writer.write(b);
            writer.close();
            return true;

        } catch (IOException e) {
            System.out.println("Error writing string to file: " + e.getMessage());
            return false;
        }
    }

    public static int findIndex(String[][] a, String b) {
        int result = -1;
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                if (a[i][j].equals(b)) {
                    result = i;
                    return result;
                }
            }
        }
        return result;
    }

}
