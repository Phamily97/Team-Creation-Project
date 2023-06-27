package com.teamb13.teamcreater.utils;

import com.teamb13.teamcreater.module.CsvData;
import com.teamb13.teamcreater.module.Student;
import com.teamb13.teamcreater.module.Team;

import java.util.*;

public class TeamCreateUtils {

    private static final List<String> VALID_PROJECT_NAME;

    static {
        VALID_PROJECT_NAME = new ArrayList<>();
        VALID_PROJECT_NAME.add("web-app");
        VALID_PROJECT_NAME.add("web-application");
        VALID_PROJECT_NAME.add("iOS app");
        VALID_PROJECT_NAME.add("iOS application");
        VALID_PROJECT_NAME.add("android app");
        VALID_PROJECT_NAME.add("android application");
        VALID_PROJECT_NAME.add("desktop app");
        VALID_PROJECT_NAME.add("desktop application");
    }

    private static void isValidProjectName(String projectName) throws Exception {
        for (String validProjectName : VALID_PROJECT_NAME) {
            if (validProjectName.equalsIgnoreCase(projectName)) {
                return;
            }
        }

        throw new Exception("Unknown application type.");
    }

    public static Map<String, List<Team>> createTeams(CsvData csvData) throws Exception {
        return startCreate(csvData);
    }

    private static String getTeamName(int index) {
        return "Team-" + index;
    }

    /**
     * Add student below each workshop.
     */
    private static Map<String, List<Student>> getAllWorkShopStuMap(CsvData csvData) {
        Map<String, List<Student>> workShopsAndTeams = new HashMap<>();
        Set<String> allWorkShop = new HashSet<>();

        // Get all workshops.
        for (var student : csvData.students) {
            allWorkShop.add(student.workShops);
        }

        // Add ArrayList.
        for (var workshop : allWorkShop) {
            workShopsAndTeams.put(workshop, new ArrayList<>());
        }

        // Add each student into work shop.
        for (var student : csvData.students) {
            workShopsAndTeams.get(student.workShops).add(student);
        }

        return workShopsAndTeams;
    }

    private static Map<String, List<Team>> allocRelateWorkShop(
            Set<Student> stuBeAlloc,
            Map<String, List<Student>> workShopsAndStuMap
    ) throws Exception {
        Map<String, List<Team>> workShopTeamsMap = new HashMap<>();

        // Add array list to each workshop.
        for (var workshopAndStu : workShopsAndStuMap.entrySet()) {
            workShopTeamsMap.put(workshopAndStu.getKey(), new ArrayList<>());
        }

        // Create team for each workshop
        for (var workshopAndStu : workShopsAndStuMap.entrySet()) {
            List<Student> students = workshopAndStu.getValue();

            int numOfStudent = students.size();

            // To few people.
            if (numOfStudent < 5) {
                throw new Exception("Too few people.");
            }

            // To many people, may create more teams.
            if (numOfStudent > 5 * 7) {
                throw new Exception("Too many teams.");
            }

            String workshopName = workshopAndStu.getKey();

            List<Integer> combination = CombinationsUtils.getOneCombination(
                    5, 7, numOfStudent, 5
            );

            if (combination.isEmpty()) {
                throwException("Workshop \"%s\" have no way " +
                                "to create teams. When the minimum number " +
                                "of students is 5, the maximum is 7 and " +
                                "the total number of students is %d.",
                        workshopAndStu.getKey(),
                        numOfStudent
                );
            }

            // Add people to each tem.
            int i = 0;
            int numOfTeam = 0;

            for (int numPeopleForEachTeam : combination) {
                List<Team> workshopTeams = workShopTeamsMap.get(workshopName);

                ++numOfTeam;
                Team newTeam = new Team(getTeamName(numOfTeam));

                for (int j = 0; j < numPeopleForEachTeam; ++j) {
                    stuBeAlloc.add(students.get(i));
                    newTeam.addStudent(students.get(i));
                    ++i;
                }

                workshopTeams.add(newTeam);
            }
        }

        for (var set : workShopTeamsMap.entrySet()) {
            for (var team : set.getValue()) {
                Collections.shuffle(team.getStudents());
            }
        }

        return workShopTeamsMap;
    }

    public static void checkIfAllBeAllocate(Map<String, List<Student>> workShopsAndStuMap,
                                            Set<Student> stuBeAlloc) throws Exception {
        for (var set : workShopsAndStuMap.entrySet()) {
            for (var student : set.getValue()) {
                if (!stuBeAlloc.contains(student)) {
                    throwException("Some students are not be allocate into a team.");
                }
            }
        }
    }

    private static void checkIsPreferMoreThan6(Student student) throws Exception {
        if (student.preferStudents.size() > 6) {
            throw new Exception("Too many nominate people.");
        }
    }

    private static int findMost(List<Student> students) {
        int max = 0;
        int index = 0;
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).preferStudents.size() > max) {
                max = students.get(i).preferStudents.size();
                index = i;
            }
        }

        if (max == 0 && index == 0) {
            return -1;
        }

        return index;
    }

    private static void adjustMemberByPreferPeople(Map<String, List<Team>> workshopAndTeamMap) throws Exception {
        for (var workshopAndTeams : workshopAndTeamMap.entrySet()) {
            List<Team> allTeamsInThisWorkShop = workshopAndTeams.getValue();

            // Skip workshop with one team.
            if (allTeamsInThisWorkShop.size() <= 1) {
                continue;
            }

            // Check is any people more than 6 before we allocate the people.
            for (Team team : allTeamsInThisWorkShop) {
                for (Student student : team.getStudents()) {
                    checkIsPreferMoreThan6(student);
                }
            }

            // Start adjust people.
            for (int i = 0; i < allTeamsInThisWorkShop.size() - 1; i++) {

                List<Student> team1 = allTeamsInThisWorkShop.get(i).getStudents();

                int most = findMost(team1);
                if (most == -1) {
                    continue;
                }

                Collections.swap(team1, 0, most);

                int iForT1 = 1;
                Student student = team1.get(0);

                for (int j = i + 1; j < allTeamsInThisWorkShop.size() && iForT1 < team1.size(); j++) {
                    List<Student> team2 = allTeamsInThisWorkShop.get(j).getStudents();

                    int iForT2 = 0;

                    while (iForT1 < team1.size() && iForT2 < team2.size()) {
                        if (student.preferStudents.contains(team2.get(iForT2).studentID)) {
                            swapTwoListItem(team1, iForT1, team2, iForT2);
                            ++iForT1;
                        }

                        ++iForT2;
                    }
                }
            }
        }
    }

    private static void adjustMemberByProject(Map<String, List<Team>> workshopAndTeamMap) throws Exception {
        // Check project name.
        for (var workshopAndTeams : workshopAndTeamMap.entrySet()) {
            for (Team team : workshopAndTeams.getValue()) {
                for (Student student : team.getStudents()) {
                    for (String preFerProject : student.preferProject) {
                        isValidProjectName(preFerProject);
                    }
                }
            }
        }

        for (var workshopAndTeams : workshopAndTeamMap.entrySet()) {
            // Only adjust when number of teams more than two.
            if (workshopAndTeams.getValue().size() <= 1) {
                continue;
            }

            List<Team> curTeams = workshopAndTeams.getValue();

            for (Team t : curTeams) {
                t.getStudents().sort(Comparator
                        .comparing((Student student) -> student.rawData.get(CsvData.PREFER_PROJECT_KEY)));
            }
        }
    }

    private static Map<String, List<Team>> startCreate(CsvData csvData) throws Exception {
        Map<String, List<Student>> workShopsAndStuMap = getAllWorkShopStuMap(csvData);

        Set<Student> stuBeAlloc = new HashSet<>();

        // The team need to be return.
        Map<String, List<Team>> workshopAndTeamMap = allocRelateWorkShop(stuBeAlloc, workShopsAndStuMap);
        checkIfAllBeAllocate(workShopsAndStuMap, stuBeAlloc);
        adjustMemberByPreferPeople(workshopAndTeamMap);
        adjustMemberByProject(workshopAndTeamMap);
        return workshopAndTeamMap;
    }

    private static void throwException(String fmt, Object... args) throws Exception {
        assert fmt != null && args != null;
        throw new Exception(String.format(fmt, args));
    }

    private static void swapTwoListItem(List<Student> l1, int i1, List<Student> l2, int i2) {
        Student temp = l1.get(i1);
        l1.set(i1, l2.get(i2));
        l2.set(i2, temp);
    }
}
