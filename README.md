## Xcelite

* [Xcelite project site](http://www.xcelite.io/)
* [Introduction](#introduction)
* [Quick Start](#quick-start)
  * [Writing](#writing)
  * [Reading](#reading)
* [Advanced Stuff](#advanced-stuff)
  * [Using Converters](#using-converters)
  * [Dynamic Columns](#dynamic-columns)
  * [Row Post Processors](#row-post-processors)
* [Utils](#utils)
  * [XceliteDiff](#xcelitediff)
* [How To Use?](#how-to-use)
  * [Using Xcelite in Your Maven Project](#using-xcelite-in-your-maven-project)
  * [Using Xcelite in Your Gradle Project](#using-xcelite-in-your-gradle-project)
  * [Using Xcelite with other build systems](#using-xcelite-with-other-build-systems)

-----

Status:

[![License](https://img.shields.io/github/license/xcelite-io/xcelite.svg)](https://github.com/xcelite-io/xcelite/blob/master/LICENSE) | [![build_status](https://travis-ci.com/xcelite-io/xcelite.svg?branch=master)](https://travis-ci.com/xcelite-io/xcelite)

-----

### Introduction
[Xcelite](http://www.xcelite.io/) is an ORM-like Java library which allows you to easily serialize and deserialize Java beans to/from Excel spreadsheets

The workflow for reading is always:
* create an Xcelite object on an input source like a file or `InputStream`
* select the sheet from which to read
* get a `SheetReader` from the sheet
* read data
* close the Xcelite object

Similarly, the workflow for writing data to Excel files is:
* create an Xcelite object on an output sink like an `OutputStream` 
* select the sheet to which to write
* get a `SheetWriter` from the sheet
* write data
* close the Xcelite object

### Quick Start
#### Reading
How do I simply read a sheet in an existing MS Excel file to a two-dimensional collection?
```java
Xcelite xcelite = new Xcelite(new File("data.xlsx"));
XceliteSheet nativeSheet = xcelite.getSheet("data_sheet");
SheetReader<Collection<Object>> simpleReader = nativeSheet.getSimpleReader();
Collection<Collection<Object>> data = simpleReader.read();
```
If the first row in the MS Excel sheet is a header, you can skip it by writing:
```java
simpleReader.skipHeaderRow(true);
```

Cool! How about reading to a collection of Java beans?
```java
Xcelite xcelite = new Xcelite(new File("users_doc.xlsx"));
XceliteSheet nativeSheet = xcelite.getSheet("users");
SheetReader<User> reader = nativeSheet.getBeanReader(User.class);
Collection<User> users = reader.read();
```
Note that Xcelite will try to map only the `@Column` annotated properties. If for an annotated property no column is found in the MS Excel sheet it will be ignored.  
Sheet columns which are not mapped to a `@Column` annotated property will be ignored as well.

#### Writing
I simply want to write a two-dimensional collection. How can I do that?
```java
Xcelite xcelite = new Xcelite();    
XceliteSheet nativeSheet = xcelite.createSheet("data_sheet");
SheetWriter<Collection<Object>> simpleWriter = nativeSheet.getSimpleWriter();
List<Collection<Object>> data = new ArrayList<>();
// ...fill up data
simpleWriter.write(data);   
xcelite.write(new File("data.xlsx"));
```
This will create an Excel document with single sheet named "data_sheet".

OK lets get serious, i have this POJO bean
```java
public class User { 

  private String firstName;
  private String lastName;
  private long id; 
  private Date birthDate; 
}
```
How do i serialize a collection of this bean to an Excel file?

First, lets add annotations so Xcelite knows which properties to serialize:
```java
public class User { 

  @Column (name="Firstname")
  private String firstName;
  
  @Column (name="Lastname")
  private String lastName;
  
  @Column
  private long id; 
  
  @Column
  private Date birthDate; 
}
```
The `@Column` annotation on a property indicates that you want it to be serialized to MS Excel format.  
By default, if no `name` attribute is provided the column name in the Excel sheet will be taken from the property name.

Now we'll write the same data as before but this time, we are using `BeanWriter` writer instead of `SimpleWriter`:
```java
Xcelite xcelite = new Xcelite();    
XceliteSheet nativeSheet = xcelite.createSheet("users");
SheetWriter<User> writer = nativeSheet.getBeanWriter(User.class);
List<User> users = new ArrayList<>();
// ...fill in data
writer.write(users); 
xcelite.write(new File("users_doc.xlsx"));
```
This will create an Excel workbook containing one sheet named "users" with 4 columns plus header row: 
* Firstname 
* Lastname 
* id 
* birthDate  

Naturally, the MS Excel column types will be Text for `FirstName` and `LastName`, Number for `id` and Date for `birthDate`.  
If you prefer that column `id`  should be written as Text instead of Number, use  

```java 
@Column(ignoreType=true)
private long id;
```
It is possible to control the data format that will be used when writing. For instance, 
Xcelite will use a default data format for the `birthDate` property, which is of type `Date`. In order to change the format, use
```java 
@Column(dataFormat="ddd mmm dd hh:mm:ss yyy")
private Date birthDate;
```
The data format is exacly as the same as used in MS Excel. It is recommended to check the format in Excel first before using it in your code.

Note that the Excel columns order in this case is arbitrary (due to limitations of Java reflection). If you want to control the order of the columns use the `@Row` annotation on your bean class
```java
@Row(colsOrder = {"Firstname", "Lastname", "id", "birthDate"})
public class User {
// ...
}
```

### Advanced Topics
#### Using Converters

Lets say your bean contains a list of values or some object of your own. By default, Xcelite will serialize using the `toString()`-method of the object or list, and sometimes this might not be what you want.  
The converter mechanism allows you to serialize/deserialize the object in any way you want.  
To demonstrate, lets add a list to our `User` bean and use the built-in `CSVColumnValueConverter` converter:

```java
@Column(name = "Emails", converter = CSVColumnValueConverter.class)
private List<String> mailAddresses;
```
The `CSVColumnValueConverter` takes a collection of objects and serializes it to a comma-separated String.  
Alternately when deserializing, the converter takes a comma-separated and deserializes it to a collection of Objects.  
So writing a collection of users will result with a column named "Emails" and the column data will look someting like that:  

    john@mail.com,danny@mail.com,jerry@mail.com  

When reading the Excel sheet to a collection of `Users`, the column "Emails" will be deserialized to an `ArrayList`.
If you prefer a different collection implementation rather than the default `ArrayList`, you can always extend the `CSVColumnValueConverter` and override the `getCollection()` method to return your preferred implementation.

##### Custom Converters
It is possible to create your own converter. All you need to do is to implement `ColumnValueConverter` interface.  
For example, lets create a converter for our `firstName` property that will lowercase the `firstName` when writing and will uppercase it when reading.  
```java
public class UpperLowerCaseConverter implements ColumnValueConverter<String, String> {
  @Override
  public String serialize(String value) {
    return value.toUpperCase();
  }

  @Override
  public String deserialize(String value) {
    return value.toLowerCase();
  }
}

@Column (name="Firstname", converter = UpperLowerCaseConverter.class)
private String firstName;
```
#### Dynamic Columns
##### Reading from an Excel Sheet with partially unknown Column Names
What if you know in advance only partially which columns your Excel sheet will hold? For example, a sheet with 
employee information might hold fixed columns for name and organizational unit, but additionally a number of 
additional columns depending which department created the sheet.

For that purpose you can use the `@AnyColumn` annotation to annotate a ```Map<String, Object>``` property. 
The map can hold any column -- the key represents the column name whereas the value represents the column value.

```java
@Column(name="name")
String name;

@Column(name="org_unit")
String unit;

@AnyColumn
private Map<String, Object> dynamicCols;
```

If your bean contains an `@AnyColumn` property, any column in your Excel sheet that is *right* to the columns mapped to specific 
properties in your bean will be injected to the `@AnyColumn` annotated `Map` property. If a converter is declared, 
then the value will be deserialized using the converter before injected to the map. This also means you only can have
one  `@AnyColumn` per Bean class and it consumes all the columns to the right.

One special case comes up if you have two or more columns with the same header name in your `@AnyColumn` set. As the column 
header name acts as the key of the resulting `Map` property, only the last column value is returned, ie. if your sheet
has three columns named "project name" that are part of an @AnyColum set, only the value of the last 
(right-most) column will be returned. As this is frequently not what you want, a configuration option exists, 
`anyColumnCreatesCollection` (default: `false`), that if set to `true` allows to switch the value type of the 
`@AnyColumn` annotation to `Collection`. 

By default, Xcelite will use `HashMap` as the implementation when deserializing. If you'd prefer a different 
implementation use the `as` attribute.  

For instance, if you want your map to be sorted by column names using a TreeMap, just do:
```java
@AnyColumn(converter = CSVColumnValueConverter.class, as = TreeMap.class)
private Map<String, List<String>> dynamicCols;
```

##### Writing to an Excel Sheet if your Bean class contains a `Map` member variable
The `@AnyColumn` annotation also works for writing. The entries of your `Map` member variable get serialized into
individual columns of the Excel sheet - the Map-keys become the column headers, the Map-values the cell values. The 
same limitations as for reading apply: only one `Map` per Bean class and the variable columns will appear to the
right of the fixed (individually mapped) columns.

Values of the `@AnyColumn`-annotated `Map`-type member variable can be of any type . If the type of an 
entry of the Map is not a `Number` or `Date` Xcelite will use the `toString()` of the object upon serialization. 
If this is not what you want you can use a converter same way as before:
```java
@AnyColumn(converter = CSVColumnValueConverter.class)
private Map<String, List<String>> dynamicCols;
```
##### Skipping Columns while using `@AnyColumn`
In addition, if you want some sheet columns to be skipped from the serialization/deserialization, use:

```java
@AnyColumn(ignoreCols = { "column1", "column2" })
private Map<String, List<String>> dynamicCols;
```

#### Row Post Processors
When reading an MS Excel sheet you sometimes want to manipulate the data while reading. For example, you want to discard some row or object, or change some data in the deserialized object.  
In order to accomplish that you can add a `RowPostProcessor` to your reader.  
A `RowPostProcessor` is a simple interface which contain a single method `process()` which gets the deserialized Object as an argument and return boolean whether to keep the Object or not.  
```java
private class UserPostRowProcessor implements RowPostProcessor<User> {
  @Override
  public boolean process(User user) {
    return user.getFirstName().startsWith("A");
  }
}
```
In this example we filter out all users which their first name does not start with "A".  
All we have to do now is to register this row post processor in our reader:
```java
SheetReader<User> reader = nativeSheet.getBeanReader(User.class);
reader.addRowPostProcessor(new UserPostRowProcessor());
```
Note that you can register as many row post processors as you like. They will be executed in ordered manner.


#### Xcelite example Spring web application

Currently work in progress, see https://github.com/iSnow/xcelite-web

### Utils
#### XceliteDiff
Xcelite package provides a simple and easy-to-use utility which takes two `SheetReader` classes and compares them:
```java
Xcelite xceliteA = new Xcelite(new File("usersA.xlsx"));
SheetReader<User> readerA = xceliteA.getSheet("users").getBeanReader(User.class);
Xcelite xceliteB = new Xcelite(new File("usersB.xlsx"));
SheetReader<User> readerB = xceliteB.getSheet("users").getBeanReader(User.class);
DiffResult<User> diffResult = XceliteDiff.diff(readerA, readerB);
assertTrue("Sheets are not identical!\n" + diffResult.getReport(), diffResult.isIdentical());
```
Needless to say, your bean must implement `hashCode()` and `equals()` if you wish to have meaningful symmetric difference results.  
If you want to use a different report rather than the one provided by `XceliteDiff`, it is possible to do:
```java
DiffResult<User> diffResult = XceliteDiff.diff(readerA, readerB, new ReportGenerator() {
      @Override
      public <T> String generateReport(Info<T> info) {
        // return your own report based on the provided info
      }
    });
```
### How To Use
#### Using Xcelite in Your Maven Project
Add xcelite as a dependency:
```xml
<dependency>
	<groupId>io.xcelite.spreadsheet</groupId>
	<artifactId>xcelite</artifactId>
	<version>1.2.5</version>
</dependency>
```

#### Using Xcelite in Your Gradle Project
Add xcelite as a dependency:
```gradle
compile group: 'io.xcelite.spreadsheet', name: 'xcelite', version: '1.2.5'
```

#### Using Xcelite with other build systems
See https://mvnrepository.com/artifact/io.xcelite.spreadsheet/xcelite/1.2.5
