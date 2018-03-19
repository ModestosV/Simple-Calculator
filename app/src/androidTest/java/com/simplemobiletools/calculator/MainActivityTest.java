package com.simplemobiletools.calculator;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.simplemobiletools.calculator.activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule public final ActivityTestRule<MainActivity> activity = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void addDigits() {
        press(R.id.btn_1);
        press(R.id.btn_2);
        press(R.id.btn_3);
        press(R.id.btn_4);
        press(R.id.btn_5);
        press(R.id.btn_6);
        press(R.id.btn_7);
        press(R.id.btn_8);
        press(R.id.btn_9);
        press(R.id.btn_0);
        checkFormula("1234567890");
        press(R.id.btn_equals);
        checkResult("1,234,567,890");
    }

    @Test
    public void additionTest() {
        press(R.id.btn_minus);
        press(R.id.btn_2);
        press(R.id.btn_decimal);
        press(R.id.btn_5);
        press(R.id.btn_plus);
        press(R.id.btn_6);
        press(R.id.btn_equals);
        checkResult("3.5");
        checkFormula("-2.5+6");
    }

    @Test
    public void subtractionTest() {
        press(R.id.btn_7);
        press(R.id.btn_decimal);
        press(R.id.btn_8);
        press(R.id.btn_minus);
        press(R.id.btn_3);
        press(R.id.btn_equals);
        checkResult("4.8");
        checkFormula("7.8-3");
    }

    @Test
    public void multiplyTest() {
        press(R.id.btn_2);
        press(R.id.btn_multiply);
        press(R.id.btn_4);
        press(R.id.btn_equals);
        checkResult("8");
        checkFormula("2*4");
    }

    @Test
    public void divisionTest() {
        press(R.id.btn_1);
        press(R.id.btn_0);
        press(R.id.btn_divide);
        press(R.id.btn_4);
        press(R.id.btn_equals);
        checkResult("2.5");
        checkFormula("10/4");
    }


    @Test
    public void divisionByZeroTest() {
        press(R.id.btn_8);
        press(R.id.btn_divide);
        press(R.id.btn_0);
        press(R.id.btn_equals);
        checkResult("∞");
        checkFormula("8/0");
    }

    @Test
    public void moduloTest() {
        press(R.id.btn_7);
        press(R.id.btn_mod_cube);
        press(R.id.btn_2);
        press(R.id.btn_equals);
        checkResult("1");
        checkFormula("7%2");
    }

    @Test
    public void powerTest() {
        press(R.id.btn_2);
        press(R.id.btn_power_abs);
        press(R.id.btn_3);
        press(R.id.btn_equals);
        checkResult("8");
        checkFormula("2^3");
    }

    @Test
    public void deleteTest() {
        press(R.id.btn_2);
        press(R.id.btn_5);
        press(R.id.btn_decimal);
        press(R.id.btn_7);
        press(R.id.btn_del);
        press(R.id.btn_del);
        checkFormula("25");
        press(R.id.btn_del);
        checkFormula("2");
        press(R.id.btn_del);
        checkFormula("");
        press(R.id.btn_del);
        checkFormula("");
    }

    @Test
    public void allClearTest() {
        press(R.id.btn_2);
        press(R.id.btn_plus);
        press(R.id.btn_5);
        press(R.id.btn_equals);
        checkResult("7");
        checkFormula("2+5");
        press((R.id.btn_all_clear));
        checkResult("");
        checkFormula("");
    }

    public void storeValueAndUseInFormula() {
        //Verify if button store works (Have to press equals first)
        press(R.id.btn_7);
        press(R.id.btn_divide);
        press(R.id.btn_2);
        press(R.id.btn_equals);
        longPress(R.id.btn_memory_1);
        press(R.id.btn_all_clear);
        press(R.id.btn_memory_1);
        checkFormula("3.5");
    }

    @Test
    public void squareRootTest(){
        press(R.id.btn_root_square);
        press(R.id.btn_9);
        press(R.id.btn_plus);
        press(R.id.btn_1);
        press(R.id.btn_6);
        press(R.id.btn_right_bracket);
        press(R.id.btn_equals);
        checkResult("5");
    }

    //TODO: Failing test needs fixing
    //@Test
    public void pasteNumTest(){
        press(R.id.btn_1);
        press(R.id.btn_2);
        press(R.id.btn_3);
        press(R.id.btn_multiply);
        press(R.id.btn_3);
        press(R.id.btn_multiply);
        press(R.id.btn_6);
        press(R.id.btn_equals);
        checkResult("2,214");
        longPress(R.id.result);
        press(R.id.btn_del);
        press(R.id.btn_del);
        press(R.id.btn_del);
        press(R.id.btn_del);
        press(R.id.btn_del);
        press(R.id.btn_del);
        press(R.id.btn_del);
        longPress(R.id.formula);
        checkFormula("2,214");
    }

    @Test
    public void logTest(){
        press(R.id.btn_log_ceil);
        press(R.id.btn_1);
        press(R.id.btn_0);
        press(R.id.btn_0);
        press(R.id.btn_0);
        press(R.id.btn_0);
        press(R.id.btn_right_bracket);
        press(R.id.btn_equals);
        checkResult("4");
    }

    @Test
    public void lnTest(){
        press(R.id.btn_ln_floor);
        press(R.id.btn_1);
        press(R.id.btn_right_bracket);
        press(R.id.btn_equals);
        checkResult("0");
    }

    @Test
    public void eTest(){
        press(R.id.btn_ln_floor);
        press(R.id.btn_e_neg);
        press(R.id.btn_right_bracket);
        press(R.id.btn_equals);
        checkResult("1");
    }

    @Test
    public void insertMultiplicationBetweenDigitAndSpecialOperation(){
        press(R.id.btn_9);
        press(R.id.btn_sin_asin);
        press(R.id.btn_0);
        press(R.id.btn_cos_acos);
        press(R.id.btn_1);
        press(R.id.btn_tan_atan);
        press(R.id.btn_4);
        press(R.id.btn_pi_rand);
        press(R.id.btn_plus);
        press(R.id.btn_9);
        press(R.id.btn_4);
        press(R.id.btn_log_ceil);
        press(R.id.btn_7);
        press(R.id.btn_ln_floor);
        checkFormula("9*sin(0*cos(1*tan(4*pi+94*log(7*ln(");
    }

    @Test
    public void reciprocalTest(){
        checkResult("");
        press(R.id.btn_reciprocal_round);
        checkResult("");
        press(R.id.btn_1);
        press(R.id.btn_0);
        press(R.id.btn_equals);
        press(R.id.btn_reciprocal_round);
        checkResult("0.1");
        press(R.id.btn_reciprocal_round);
        checkResult("10");
    }

    @Test
    public void randomTest(){
        press(R.id.btn_0);
        press(R.id.btn_equals);
        press(R.id.btn_shift);
        press(R.id.btn_pi_rand);
        checkResult("0.0");
    }

    @Test
    public void arcTrigonometryTest(){
        press(R.id.btn_shift);
        press(R.id.btn_sin_asin);
        press(R.id.btn_1);
        press(R.id.btn_right_bracket);
        press(R.id.btn_minus);
        press(R.id.btn_cos_acos);
        press(R.id.btn_0);
        press(R.id.btn_right_bracket);
        press(R.id.btn_plus);
        press(R.id.btn_tan_atan);
        press(R.id.btn_0);
        press(R.id.btn_right_bracket);
        press(R.id.btn_equals);
        checkResult("0");
    }

    @Test
    public void roundTest(){
        press(R.id.btn_shift);
        press(R.id.btn_reciprocal_round);
        press(R.id.btn_shift);
        press(R.id.btn_e_neg);
        press(R.id.btn_right_bracket);
        press(R.id.btn_equals);
        checkResult("3");
    }

    @Test
    public void ceilingTest(){
        press(R.id.btn_shift);
        press(R.id.btn_reciprocal_round);
        press(R.id.btn_shift);
        press(R.id.btn_e_neg);
        press(R.id.btn_right_bracket);
        press(R.id.btn_equals);
        checkResult("3");
    }

    @Test
    public void squareTest(){
        press(R.id.btn_2);
        press(R.id.btn_shift);
        press(R.id.btn_root_square);
        press(R.id.btn_equals);
        checkResult("4");
    }

    @Test
    public void cubeTest(){
        press(R.id.btn_2);
        press(R.id.btn_shift);
        press(R.id.btn_mod_cube);
        press(R.id.btn_equals);
        checkResult("8");
    }

    @Test
    public void absTest(){
        press(R.id.btn_shift);
        press(R.id.btn_power_abs);
        press(R.id.btn_2);
        press(R.id.btn_minus);
        press(R.id.btn_5);
        press(R.id.btn_right_bracket);
        press(R.id.btn_equals);
        checkResult("3");
    }

    @Test
    public void signTest(){
        press(R.id.btn_2);
        press(R.id.btn_minus);
        press(R.id.btn_3);
        press(R.id.btn_equals);
        press(R.id.btn_shift);
        press(R.id.btn_e_neg);
        checkResult("1");
    }

    @Test
    public void floorTest(){
        press(R.id.btn_shift);
        press(R.id.btn_ln_floor);
        press(R.id.btn_9);
        press(R.id.btn_divide);
        press(R.id.btn_5);
        press(R.id.btn_right_bracket);
        press(R.id.btn_equals);
        checkResult("1");
    }

    private void press(int id) {
        onView(withId(id)).perform(click());
    }

    private void longPress(int id) {
        onView(withId(id)).perform(longClick());
    }

    private void checkResult(String desired) {
        onView(withId(R.id.result)).check(matches(withText(desired)));
    }

    private void checkFormula(String desired) {
        onView(withId(R.id.formula)).check(matches(withText(desired)));
    }
}
