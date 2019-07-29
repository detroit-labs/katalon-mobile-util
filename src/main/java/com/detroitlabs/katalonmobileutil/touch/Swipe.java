package com.detroitlabs.katalonmobileutil.touch;

import com.detroitlabs.katalonmobileutil.logging.Logger;
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords;
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.offset.PointOption;

public class Swipe {

    public enum SwipeDirection {
        LEFT_TO_RIGHT, RIGHT_TO_LEFT, TOP_TO_BOTTOM, BOTTOM_TO_TOP
    }

    /**
     * Swipes the whole screen in a given direction.
     * @param direction SwipeDirection to use for this swipe action (LEFT_TO_RIGHT, RIGHT_TO_LEFT, TOP_TO_BOTTOM, BOTTOM_TO_TOP)
     */
    public static void swipe(SwipeDirection direction) {

        AppiumDriver<?> driver = MobileDriverFactory.getDriver();
        TouchAction touchAction = new TouchAction(driver);
        double deviceHeight = MobileBuiltInKeywords.getDeviceHeight();
        double deviceWidth = MobileBuiltInKeywords.getDeviceWidth();

        int startX = 0, endX = 0, startY = 0, endY = 0;

        double edgeAdjustLow = 0.1;
        double edgeAdjustMiddle = 0.5;
        double edgeAdjustHigh = 1.0 - edgeAdjustLow;

        switch (direction) {
            case BOTTOM_TO_TOP:
                startX = (int) (deviceWidth * edgeAdjustMiddle);
                endX = startX;
                startY = (int) (deviceHeight * edgeAdjustHigh);
                endY = (int) (deviceHeight * edgeAdjustLow);
                break;
            case TOP_TO_BOTTOM:
                startX = (int) (deviceWidth * edgeAdjustMiddle);
                endX = startX;
                startY = (int) (deviceHeight * edgeAdjustLow);
                endY = (int) (deviceHeight * edgeAdjustHigh);
                break;
            case LEFT_TO_RIGHT:
                startX = (int) (deviceWidth * edgeAdjustLow);
                endX = (int) (deviceWidth * edgeAdjustHigh);
                startY = (int) (deviceHeight * edgeAdjustMiddle);
                endY = startY;
                break;
            case RIGHT_TO_LEFT:
                startX = (int) (deviceWidth * edgeAdjustHigh);
                endX = (int) (deviceWidth * edgeAdjustLow);
                startY = (int) (deviceHeight * edgeAdjustMiddle);
                endY = startY;
                break;
        }

        Logger.debug("Swiping from (" + startX + ", " + startY + ") to (" + endX + ", " + endY + ")");
        touchAction.longPress(PointOption.point(startX, startY)).moveTo(PointOption.point(endX, endY)).release().perform();

    }


}
