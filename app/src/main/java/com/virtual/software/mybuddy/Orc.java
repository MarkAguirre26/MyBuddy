package com.virtual.software.mybuddy;

import java.util.ArrayList;
import java.util.List;

public class Orc {
    public static String[] levels = {"Pos1", "Base", "Step1", "Step2", "Step3", "Step4", "Step5"};
    private static String[] positions = levels;
    private static List<BetAmountModel> amounts = new ArrayList<>();

    public static int POS1 = 0;
    public static int BASE = 1;
    public static int STEP1 = 2;
    public static int STEP2 = 3;
    public static int STEP3 = 4;
    public static int STEP4 = 5;
    public static int STEP5 = 6;

    public static int getHandRequiredToStart() {
        return 3;
    }

    public static void setBetAmount(double baseBetAmount) {
        amounts.add(new BetAmountModel(positions[0], toTwoDecimal(baseBetAmount * 2)));
        amounts.add(new BetAmountModel(positions[1], toTwoDecimal(baseBetAmount)));
        amounts.add(new BetAmountModel(positions[2], toTwoDecimal(baseBetAmount * 2)));
        amounts.add(new BetAmountModel(positions[3], toTwoDecimal(baseBetAmount)));
        amounts.add(new BetAmountModel(positions[4], toTwoDecimal(baseBetAmount * 2)));
        amounts.add(new BetAmountModel(positions[5], toTwoDecimal(baseBetAmount * 4 - baseBetAmount)));
        amounts.add(new BetAmountModel(positions[6], toTwoDecimal(baseBetAmount * 4 - baseBetAmount)));
    }

    public static double getBetAmount(String keyword) {
        for (BetAmountModel item : amounts) {
            if (item.getLevel().equals(keyword)) {
                return item.getAmount();
            }
        }
        return 0.0; // Return a default value if the keyword is not found
    }

    private static double toTwoDecimal(double input) {
        return Double.parseDouble(String.format("%.2f", input));
    }

    public static List<BetAmountModel> getBetAmountList() {
        return amounts;
    }
}

// BetAmountModels class
class BetAmountModel {
    private String level;
    private double amount;

    public BetAmountModel(String level, double amount) {
        this.level = level;
        this.amount = amount;
    }

    public String getLevel() {
        return level;
    }

    public double getAmount() {
        return amount;
    }
}
