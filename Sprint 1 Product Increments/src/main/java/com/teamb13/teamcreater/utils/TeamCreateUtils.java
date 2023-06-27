package com.teamb13.teamcreater.utils;

import com.teamb13.teamcreater.module.CsvData;
import com.teamb13.teamcreater.module.Student;
import com.teamb13.teamcreater.module.Team;

import java.util.*;

public class TeamCreateUtils {

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

        for (var student : csvData.students) {
            allWorkShop.addAll(student.workShops);
        }

        for (var workshop : allWorkShop) {
            workShopsAndTeams.put(workshop, new ArrayList<>());
        }

        // Add each student into work shop.
        for (var student : csvData.students) {
            for (var workShop : student.workShops) {
                workShopsAndTeams.get(workShop).add(student);
            }
        }

        return workShopsAndTeams;
    }

    private static Map<String, List<Team>> allocRelateWorkShop(
            Set<Student> stuBeAlloc,
            Map<String, List<Student>> workShopsAndStuMap
    ) throws Exception {
        Map<String, List<Team>> workShopTeamsMap = new HashMap<>();

        // Init.
        for (var workshopAndStu : workShopsAndStuMap.entrySet()) {
            workShopTeamsMap.put(workshopAndStu.getKey(), new ArrayList<>());
        }

        int numOfTeam = 0;

        // Create team for each workshop
        for (var workshopAndStu : workShopsAndStuMap.entrySet()) {
            List<Student> students = workshopAndStu
                    .getValue()
                    .stream()
                    .filter(student -> !stuBeAlloc.contains(student))
                    .toList();

            int numOfStudent = students.size();
            if (numOfStudent < 5) {
                throw new Exception("Too few people.");
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

    private static Map<String, List<Team>> startCreate(CsvData csvData) throws Exception {
        Map<String, List<Student>> workShopsAndStuMap = getAllWorkShopStuMap(csvData);

        Set<Student> stuBeAlloc = new HashSet<>();

        // The team need to be return.
        Map<String, List<Team>> workshopAndTeamMap = allocRelateWorkShop(stuBeAlloc, workShopsAndStuMap);
        checkIfAllBeAllocate(workShopsAndStuMap, stuBeAlloc);
        return workshopAndTeamMap;
    }

    private static void throwException(String fmt, Object... args) throws Exception {
        assert fmt != null && args != null;
        throw new Exception(String.format(fmt, args));
    }
}
