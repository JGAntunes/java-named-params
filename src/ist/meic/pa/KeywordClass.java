package ist.meic.pa;

import java.util.*;
import javassist.*;
import java.io.IOException;

public class KeywordClass {

  private String className;
  private ClassPool classPool;
  private CtClass ctClass;
  private List<String> defaultParams;
  

  public KeywordClass (String className) throws NotFoundException {
    this.className = className;
    this.classPool = ClassPool.getDefault();
    this.classPool.importPackage("java.lang.reflect.Field");
    this.classPool.importPackage("ist.meic.pa.ReflectionUtils");
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
    CtClass[] params = {ReflectionUtils.JAVA_LANG_OBJECT_ARRAY};
    CtConstructor constructor = this.ctClass.getDeclaredConstructor(params);

    // Determine the need to initialize the super
    String superCall = "";
    try {
      CtClass superClass = this.ctClass.getSuperclass();
      CtConstructor superConstructor = superClass.getDeclaredConstructor(params);
      KeywordArgs annotation = (KeywordArgs) superConstructor.getAnnotation(KeywordArgs.class);
      if (annotation != null) {
        superCall = "super(new Object[0]);";
      }      
    } catch (Exception e) {
      superCall = "";
    }

    constructor.setBody("{" + 
      superCall +
      String.join(";", this.defaultParams) + ";" +
      "for(int i = 0; i < $1.length; i += 2) {" +
        "String fieldName = (String) $1[i];" +
        "java.lang.reflect.Field field = null;" +
        "try {" +
          "field = ist.meic.pa.ReflectionUtils.getField(" + this.ctClass.getName()  + ".class, fieldName);" +
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


