package ist.meic.pa;

import java.util.*;
import javassist.*;

public class KeywordClass {
  private String className;
  private CtClass ctClass;
  private Map<String, String> defaultParams;
  

  public KeywordClass (String className) throws NotFoundException {
    this.className = className;
    ClassPool pool = ClassPool.getDefault();
    CtClass cc = pool.get(className);
    this.ctClass = cc;
    // this.annotation = this.ctClass.getAnnotation(KeywordArgs);
  }

  public void generate () {
    this.parseAnnotation();
    this.generateConstructor();
  }

  private void parseAnnotation () {
    this.defaultParams = KeywordAnnotation.parse(this.ctClass);
    //this.genrateDefaults();
    this.generateConstructor();
  }

  private void generateConstructor () {
  }

}


