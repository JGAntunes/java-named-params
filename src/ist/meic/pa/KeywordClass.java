package ist.meic.pa;

import java.util.*;
import java.io.IOException;
import javassist.*;

public class KeywordClass {

  // Useful helper class
  protected static CtClass JAVA_LANG_OBJECT_ARRAY;
  static {
    try { JAVA_LANG_OBJECT_ARRAY = ClassPool.getDefault().get("java.lang.Object[]"); } 
    catch (NotFoundException e) { Logger.error(e.getMessage()); }
  }

  private String className;
  private ClassPool classPool;
  private CtClass ctClass;
  private List<String> defaultParams;
  

  public KeywordClass (String className) throws NotFoundException {
    this.className = className;
    this.classPool = ClassPool.getDefault();
    this.classPool.importPackage("java.lang.reflect.Field");
    CtClass cc = this.classPool.get(className);    
    this.ctClass = cc;
  }

  public void generate () throws IOException, NotFoundException, CannotCompileException {
    this.parseAnnotation();
    this.generateConstructor();
    // Persist changes
    this.ctClass.writeFile(); 
  }

  private void parseAnnotation () {
    this.defaultParams = KeywordAnnotation.parse(this.ctClass);
  }

  private void generateConstructor () throws NotFoundException, CannotCompileException {
    CtClass[] params = {JAVA_LANG_OBJECT_ARRAY};
    CtConstructor constructor = this.ctClass.getDeclaredConstructor(params);
    constructor.setBody("{" + 
      String.join(";", this.defaultParams) + ";" +
      "for(int i = 0; i < $1.length; i += 2) {" +
        "String fieldName = (String) $1[i];" +
        "java.lang.reflect.Field field = null;" +
        "try {" +
          "field = " + this.ctClass.getName()  + ".class.getDeclaredField(fieldName);" +
        "} catch (NoSuchFieldException e) {" +
          "throw new RuntimeException(\"Unrecognized keyword: \" + fieldName);" +
        "}" +
        "if (i+1 < $1.length) {" +
          "field.set($0, $1[i+1]);" +
        "}" +
      "}" +
    "}");
  }

}


