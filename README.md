# SecondCities
This repository holds all code to my bachelor thesis project.
There are two main tasks one can perform using this program:
- Start the JavaFX application and play Lost Cities using a preliminary GUI
- Execute Code using the Main class in the src folder

The easiest way to use the code is downloading the project as a .zip and loading it into your favorite IDE such as Eclipse or IntelliJ. 
Make sure to include javafx to used libraries.

To start the JavaFX application use the SecondCities.jar and make sure to add important JavaFX modules javafx.base,javafx.controls,javafx.fxml. 
The necessary files can be downloaded  at https://gluonhq.com/products/javafx/. 
Then to start the application unzip the JavaFX SDK downloaded and place it next to the .jar.
Go to the directory holding these files:
  
   ->SecondCities.jar
   
   ->javafx-sdk-18.0.1
    
And using ```java --module-path "javafx-sdk-18.0.1/lib" --add-modules javafx.controls,javafx.fxml,javafx.base -jar SecondCities.jar``` should start the application.

Otherwise, you can download the corresponding installer, which installs LostCities as an application to your system. The download links can be found on https://ja4200wi.de/
    
   
