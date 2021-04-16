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

package com.pixelduke.control.skin;

import impl.com.pixelduke.control.skin.TextFieldWithButtonSkin;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.control.TextField;

public class FXTextFieldSkin extends TextFieldWithButtonSkin {
    public FXTextFieldSkin(TextField textField) {
        super(textField);

        textField.skinProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                textField.applyCss();
                textField.skinProperty().removeListener(this);
            }
        });
    }

    @Override
    protected void onRightButtonReleased()
    {
        getSkinnable().setText("");
    }
}