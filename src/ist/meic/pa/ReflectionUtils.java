package ist.meic.pa;

import java.lang.reflect.Field; 
import java.io.IOException;
import javassist.*;

public class ReflectionUtils {

  // Useful helper class
  public static CtClass JAVA_LANG_OBJECT_ARRAY;
  static {
    try { JAVA_LANG_OBJECT_ARRAY = ClassPool.getDefault().get("java.lang.Object[]"); } 
    catch (NotFoundException e) { Logger.error(e.getMessage()); }
  }

  // Recursively getField from class hierarchy
  public static Field getField(Class classFile, String fieldName) throws NoSuchFieldException {
    try {
      return classFile.getDeclaredField(fieldName);
    } catch (NoSuchFieldException e) {
      Class superClass = classFile.getSuperclass();
      if (superClass == null) {
        throw e;
      } else {
        return getField(superClass, fieldName);
      }
    }
  }
}
