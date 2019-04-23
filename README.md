# clubber_project_2019 

**gereral:**

This Project (Android and iOS) is still being developed. Thus, there might be some bugs and some functionalities might not work.
The Project is inspired by a website I have programmed with a fellow student. Despite the fact the website already exists, we decided on not developing a hybrid app but a native app.
website: https://clubber-stuttgart.de/

**design:**
Not final. Still in development and just for debugging at the moment.

**functionality:**

The clubber app should help people in Stuttgart to find current events very easy and without looking at each and every site of the clubs on Facebook to check their events.

**technically:**

We store data in a database on our webserver. We developed an API for our app which contains all the clubs and events we have saved on our webserver DB.
The API has been developed to send only the requested data. In this case from a certain id.

We request the JSON file (our API) on our App whenever it is started to make sure the app is up to date. Furthermore the user can request the data at runtime with a refresh function. (swipe down in Events to refresh)

**-->Android:**

We run three different fragments (one at a time) in an Activity. Home, Events and Clubs.
The Download of the JSON file is started in the Home Fragment as an IntentService and runs in it's own thread. However it will only start automatically once in a runtime.
The user has to refresh manually if he wants to refresh while runtime.

We are currently fixing some bugs and implementing more unit tests/ui tests

**-->iOS:**

...



**Team:**

Nico Burkart:   Developer for business logic

Jonas Wolfram:  Developer for business logic

Magali Gröter:  Developer for userinterface
