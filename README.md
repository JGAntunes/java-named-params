# Java Named Params
A Java extension to add support for named params, a project for Advanced Programming class @ IST

## The idea
```java
class Widget {
  int width;
  int height;
  int margin;
  
  @KeywordArgs("width=100,height=50,margin")
  public Widget(Object ...args) {}
  
  public String toString() {
    return String.format("width:%s,height:%s,margin:%s",
      width, height, margin);
  }
}

new Widget("width", 100, "height", 50, "margin", 5)
```

## Compatible with subclasses
```java
class ExtendedWidget extends Widget {

  String name;

  @KeywordArgs("name=\"Extended\",width=200,margin=10,height")
  public ExtendedWidget(Object... args) {}
  public String toString() {  
    return String.format("width:%s,height:%s,margin:%s,name:%s",
      width, height, margin, name);
  }
}

System.out.println(new ExtendedWidget());
System.out.println(new ExtendedWidget("width", 80));
System.out.println(new ExtendedWidget("height", 30));
System.out.println(new ExtendedWidget("height", 20, "width", 90));
System.out.println(new ExtendedWidget("height", 20, "width", 90, "name", "Nice"));

// Outputs
// width:200,height:50,margin:10,name:Extended
// width:80,height:50,margin:10,name:Extended
// width:200,height:30,margin:10,name:Extended
// width:90,height:20,margin:10,name:Extended
// width:90,height:20,margin:10,name:Nice
```

## Building and running
The solution is a bytecode generation tool using javassist that injects code to make this a valid syntax for constructors.

If you have docker just run:
```
./build.sh
```

Or in order to run it straight away after building
```
RUN=1 ./build.sh
```

Else if you have `ant` and Java 8 installed in your machine just run:
```
ant
```
This will generate a .jar file in the root dir with all the tooling you need in order to run the `KeyConstructors` tool.

To actually use the tool just (you'll need to have the `keyConstrutors.jar` and the javassist jar - under `/lib/javassist.jar` - in your classpath, plus the class you actually want to process):
```
java java ist.meic.pa.KeyConstructors <Name of the class to process>
```
