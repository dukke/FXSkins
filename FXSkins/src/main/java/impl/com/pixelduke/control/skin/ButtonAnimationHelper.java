/*
 * FXSkins,
 * Copyright (C) 2021 PixelDuke (Pedro Duque Vieira - www.pixelduke.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package impl.com.pixelduke.control.skin;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.beans.property.BooleanProperty;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonBase;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class ButtonAnimationHelper {
    public static final String SHRINK_ANIMATE_ON_PRESS_PROPERTY_NAME = "-shrink-animate-on-press";

    private static final Duration SCALE_TRANSITION_DURATION = Duration.millis(400);
    private static final double SCALE_ON_PRESS = 0.97;

    private EventHandler<MouseEvent> buttonPressed = this::onButtonPressed;
    private EventHandler<MouseEvent> buttonReleased = this::onButtonReleased;
    private EventHandler<KeyEvent> keyPressed = this::onKeyPressed;
    private EventHandler<KeyEvent> keyReleased = this::onKeyReleased;

    private boolean isKeyPressed = false;

    private BooleanProperty buttonShrinkAnimateOnPressProperty;

    private ButtonBase button;

    private ButtonAnimationHelper(ButtonBase button, BooleanProperty buttonShrinkAnimateOnPressProperty) {
        this.buttonShrinkAnimateOnPressProperty = buttonShrinkAnimateOnPressProperty;

        this.button = button;

        button.addEventHandler(MouseEvent.MOUSE_PRESSED, buttonPressed);
        button.addEventHandler(MouseEvent.MOUSE_RELEASED, buttonReleased);
        button.addEventHandler(KeyEvent.KEY_PRESSED, keyPressed);
        button.addEventHandler(KeyEvent.KEY_RELEASED, keyReleased);
    }

    public static ButtonAnimationHelper setupButton(ButtonBase button, BooleanProperty buttonShrinkAnimateOnPressProperty) {
        return new ButtonAnimationHelper(button, buttonShrinkAnimateOnPressProperty);
    }

    private void onButtonPressed(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() != MouseButton.PRIMARY) {
            return;
        }

        performShrink();
    }

    private void onButtonReleased(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() != MouseButton.PRIMARY) {
            return;
        }

        performUnshrink();
    }

    private void onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.SPACE) {
            performShrink();
            isKeyPressed = true;
        }
    }

    private void onKeyReleased(KeyEvent event) {
        if (isKeyPressed) {
            performUnshrink();
            isKeyPressed = false;
        }
    }

    private void performShrink() {
        if (buttonShrinkAnimateOnPressProperty.get()) {
            button.setScaleX(SCALE_ON_PRESS);
            button.setScaleY(SCALE_ON_PRESS);
        }
    }

    private void performUnshrink() {
        if (buttonShrinkAnimateOnPressProperty.get()) {
            ScaleTransition scaleTransition = new ScaleTransition(SCALE_TRANSITION_DURATION, button);
            scaleTransition.setInterpolator(Interpolator.EASE_OUT);
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);

            scaleTransition.play();
        }
    }

    public void dispose() {
        button.removeEventHandler(MouseEvent.MOUSE_PRESSED, buttonPressed);
        button.removeEventHandler(MouseEvent.MOUSE_RELEASED, buttonReleased);
        button.removeEventHandler(KeyEvent.KEY_PRESSED, keyPressed);
        button.removeEventHandler(KeyEvent.KEY_RELEASED, keyReleased);
    }
}
