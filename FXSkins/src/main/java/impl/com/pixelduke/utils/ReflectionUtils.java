package impl.com.pixelduke.utils;

import javafx.scene.control.skin.ScrollPaneSkin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtils {

    public static Object forceInvokeMethod(Object instance, String methodName) {
        Object returnValue = null;

        try {
            Method method = ScrollPaneSkin.class.getDeclaredMethod(methodName);
            method.setAccessible(true);
            returnValue =  method.invoke(instance);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {
            ex.printStackTrace();
        }

        return returnValue;
    }
}
