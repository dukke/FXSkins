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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class FXPasswordFieldSkin extends TextFieldWithButtonSkin {
    private static final char BULLET = '\u25cf';

    private boolean isMaskTextDisabled = false;

    public FXPasswordFieldSkin(TextField textField) {
        super(textField);
    }

    @Override
    protected void onRightButtonPressed() {
        TextField textField = getSkinnable();
        isMaskTextDisabled = true;
        textField.setText(textField.getText());
        isMaskTextDisabled = false;
    }

    @Override
    protected  void onRightButtonReleased()
    {
        TextField textField = getSkinnable();
        textField.setText(textField.getText());
        textField.end();
    }

    @Override protected String maskText(String txt) {
        if (getSkinnable() instanceof PasswordField && !isMaskTextDisabled) {
            int n = txt.length();
            StringBuilder passwordBuilder = new StringBuilder(n);
            passwordBuilder.append(String.valueOf(BULLET).repeat(n));

            return passwordBuilder.toString();
        } else {
            return txt;
        }
    }
}
