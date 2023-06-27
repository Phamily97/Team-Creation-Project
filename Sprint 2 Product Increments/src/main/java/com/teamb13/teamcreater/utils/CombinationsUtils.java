package com.teamb13.teamcreater.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CombinationsUtils {


    public static int sumAll(Collection<Integer> collection) {
        int ret = 0;
        for (var each : collection) {
            ret += each;
        }

        return ret;
    }

    public static void recursiveSearch(
            int cur,
            int min,
            int max,
            int n,
            List<Integer> curList,
            List<List<Integer>> res
    ) {
        if (cur > max) {
            return;
        }

        curList.add(cur);
        final int sum = sumAll(curList);

        if (sum > n) {
            curList.remove(curList.size() - 1);
            return;
        }

        if (sum == n) {
            res.add(new ArrayList<>(curList));
            curList.remove(curList.size() - 1);
            return;
        }

        for (int i = min; i <= max; ++i) {
            recursiveSearch(i, min, max, n, curList, res);
        }

        curList.remove(curList.size() - 1);
    }

    public static List<Integer> getOneCombination(int min, int max, int n, int maxTeam) throws Exception {
        if (min > max) {
            throw new IllegalArgumentException("Min greater than max value.");
        }

        if (max * maxTeam < n) {
            throw new Exception("Too many teams.");
        }

        List<Integer> temp = new ArrayList<>();
        List<List<Integer>> res = new ArrayList<>();

        for (int i = min; i <= max; ++i) {
            recursiveSearch(i, min, max, n, temp, res);
        }

        res.removeIf(integers -> integers.size() > maxTeam);

        if (res.isEmpty()) {
            return new ArrayList<>();
        }

        Collections.shuffle(res);
        return res.get(0);
    }
}
