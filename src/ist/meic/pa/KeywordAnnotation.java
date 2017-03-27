package ist.meic.pa;

import java.util.*;
import javassist.*;
import javassist.bytecode.annotation.*;
import java.util.regex.*;

public class KeywordAnnotation {
  private static final String PATTERN = "(\\w+)(?:=(\"[^\"]*\"|\\w*|\\d*))";

  public static Map<String, String> parse (CtClass ctClass) {
    HashMap<String, String> result = new HashMap<String, String> ();
    Stack<String> defaultStack = new Stack<String> ();
    CtClass currentClass = ctClass;
 
    // Get defaults from super classes and stack them up
    while (currentClass != null) {
      try {
        CtConstructor[] constructors = currentClass.getDeclaredConstructors();
        Annotation an = null;
        for (CtConstructor c : constructors) {
          an = (Annotation) c.getAnnotation(KeywordArgs.class);
          if (an != null) {
            break;
          }
        }  
        String att = ((StringMemberValue)an.getMemberValue("value")).getValue();
        defaultStack.push(att);
      } catch (Exception e) {
        System.err.println(e.getMessage());
      }
      try {
        currentClass = currentClass.getSuperclass();
      } catch (Exception e) {
        break;
      }
    }
    
    // Go through the stack and process the default keywords
    Pattern p = Pattern.compile(PATTERN);
    while (!defaultStack.empty()) {
      Matcher m = p.matcher(defaultStack.pop());
      while (m.find()) {
        String key = m.group(1);
        String value = m.group(2);
        result.put(key, value);
      }
    }
    return result;
  }
}


