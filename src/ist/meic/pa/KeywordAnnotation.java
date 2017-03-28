package ist.meic.pa;

import java.util.*;
import javassist.*;
import javassist.bytecode.annotation.*;
import java.util.regex.*;

public class KeywordAnnotation {
  private static final String PATTERN = "(\\w+(?:=\"[^\"]*\"|[^,]+|\\d+))";

  public static List<String> parse (CtClass ctClass) {
    List<String> result = new ArrayList<String> ();
    Stack<String> defaultStack = new Stack<String> ();
    CtClass currentClass = ctClass;

    Logger.info("Processing class " + ctClass.getName()); 
    // Get defaults from super classes and stack them up
    while (currentClass != null) {
      try {
        CtClass[] params = {ReflectionUtils.JAVA_LANG_OBJECT_ARRAY};
        CtConstructor constructor = currentClass.getDeclaredConstructor(params);
        KeywordArgs annotation = (KeywordArgs) constructor.getAnnotation(KeywordArgs.class);
        String att = annotation.value();
        defaultStack.push(att);
      } catch (NullPointerException | NotFoundException e) {
        // Accessing the annotation attribute will sometimes throw this
        // only consider it if not null and usefull
        if (e.getMessage() != null) {
	  Logger.warn("Processing " +  currentClass.getName() + " " + e.getMessage());
	}
      } catch (Exception e) {
        Logger.error(e.getMessage());
      }
      try {
        currentClass = currentClass.getSuperclass();
      } catch (NotFoundException e) {
        // No super class, we can break the loop
        break;
      }
    }
    
    // Go through the stack and process the default keywords
    Pattern p = Pattern.compile(PATTERN);
    while (!defaultStack.empty()) {
      Matcher m = p.matcher(defaultStack.pop());
      while (m.find()) {
        String value = m.group(1);
        result.add(value);
      }
    }
    return result;
  }
}


