package ist.meic.pa;

public class KeyConstructors {
  static void main (String [] args) {
    String className = args[1];
    try {
      KeywordClass keywordClass = new KeywordClass(className);
      keywordClass.generate();
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}

