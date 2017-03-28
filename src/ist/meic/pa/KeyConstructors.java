package ist.meic.pa;

public class KeyConstructors {

  public static void main (String [] args) {
    String className = args[0];
    try {
      KeywordClass keywordClass = new KeywordClass(className);
      keywordClass.generate();
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getMessage());
    }
  }
}

