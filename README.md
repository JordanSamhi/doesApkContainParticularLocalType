# Description

This simple program returns true if the application under analysis contains a class which contains a method containing a local with a particular type.
Useful for sample picking and filtering.

# Getting started

Downloading and installing the tool:

```bash
git clone https://github.com/JordanSamhi/doesApkContainParticularLocalType.git
cd doesApkContainParticularLocalType/
mvn clean install
```

# Tool usage

To run the tool, simply issue this command:

```bash
java -jar target/doesapkcontainparticularlocaltype-0.1-jar-with-dependencies.jar
```

Mandatory options:

* `-a` The APK file
* `-p` Path to Android platforms folder
* `-t` Type to look for in APK

# Examples of usage

The type provided should also contain the package in which the class is located.
For example, do not ask for `String` but for `java.lang.String`.

### Successful usage
```bash
java -jar target/doesapkcontainparticularlocaltype-0.1-jar-with-dependencies.jar -a apk.apk -p /some/path/platforms/ -t java.lang.String
[*] Apk contains java.lang.String
echo $?
1
```

### Unsuccessful usage
```bash
java -jar target/doesapkcontainparticularlocaltype-0.1-jar-with-dependencies.jar -a apk.apk -p /some/path/platforms/ -t android.app.AlarmManager
[!] Apk does not contain android.app.AlarmManager
echo $?
0
```
The resulting output code can be reused in scripts to ease a decision-making.

# Built With

* [Maven](https://maven.apache.org/) - Dependency Management

# Authors

* **[Jordan Samhi](https://github.com/JordanSamhi)**


## License

This project is licensed under the LGPLv2.1 License - see the [LICENSE](LICENSE) file for details
