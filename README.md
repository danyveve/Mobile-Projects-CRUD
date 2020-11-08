# Mobile-Projects-CRUD
A mobile application that is able to perform CRUD operations with Musical Instruments objects. There are 3 implementations of this application:
* A Kotlin implementation
* A Flutter implementation
* A Kotlin client + Java server implementation

## Overview
The main features of the application are:
* Listing all the existing instruments
* Adding a new instrument to the list
* Updating the information attached to existing instruments
* Deleting an instrument from the list

Note that while offline, the application behaves in the following way:
* When pressing the update or the delete button –> display a message saying that the application is online and that these features are not available now.
* When adding a new instrument --> the changes will be saved on the device, and when the application is online, it will save the newly created instruments on the main database.
* When displaying the instruments -> it will display the instruments that are saved on the device.  

Synchronization between multiple users

While offline, the information that each user changes from their application will be saved locally on their device. But while online, the information changed from a user’s device should be synchronized with a main server, so that all of the information is up to date and all users who are online will see the most recent version of the information. 
