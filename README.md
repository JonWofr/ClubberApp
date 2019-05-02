# clubber_project_2019 

**gereral:**

This project (Android and iOS) is **still being developed**. Thus, there might be some bugs and some functionality might not work.
The project is inspired by a website I programmed with a fellow student. Despite the fact that the website already exists, we decided on not developing a hybrid app but a native app.
Website: **https://clubber-stuttgart.de/**

**design:**

Not final. Still in development and just for debugging at the moment.

**functionality:**

The Clubber app should help people in Stuttgart to find current events very easily and without looking at each and every club's Facebook site to check their current events.

**technically:**

We store data in a database on our webserver. We developed an API for our app which provides us with a JSON file containing the databases entries dependent on the reqest made by the app.
The request stores information which gets evaluated by the server to determine which data should be sent.

The JSON file gets requested whenever the app is started to make sure the content is up to date. Furthermore the user can request the data at anytime with a refresh function. (swipe down in events to refresh)

**-->Android:**

We run three different fragments (one at a time) in an activity. Home, Events and Clubs.
The download of the JSON file is started in the Home fragment as an IntentService and runs in its own thread. However it will start automatically once in a runtime.
The user has to manually refresh if he wants to update during runtime.

We are currently fixing some bugs and finalizing the UI.

**-->iOS:**

The app has two TableViewControllers (Clubs, Events) and a basic ViewController. They are connected with a TabBarController to navigate in the application.
We download a JSON file (in its own thread) automatically by considering the application lifecylcle in the AppDelegate class when the app launches the first time. The data is then stored in a CoreData database.
However the user can always refresh the database by pulling down the tableView in Events. To provide user feedback we extended the UITableViewController class with two methodes
which create an alert box and print specific messages. Since Clubs- and Events TableViewContoller both inherite this class we can easily call these methodes.



**Team:**

Nico Burkart:   Developer for business logic

Jonas Wolfram:  Developer for business logic

Magali Gröter:  Developer for userinterface
