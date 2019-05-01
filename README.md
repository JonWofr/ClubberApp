# clubber_project_2019 

**gereral:**

This Project (Android and iOS) is **still being developed**. Thus, there might be some bugs and some functionalities might not work.
The Project is inspired by a website I have programmed with a fellow student. Despite the fact that the website already exists, we decided on not developing a hybrid app but a native app.
website: https://clubber-stuttgart.de/

**design:**
Not final. Still in development and just for debugging at the moment.

**functionality:**

The clubber app should help people in Stuttgart to find current events very easily and without looking at each and every club's Facebook site to check their current events.

**technically:**

We store data in a database on our webserver. We developed an API for our app which provides us with a JSON file containing the database's entries dependant on the reqest made by the app.
The request stores information which get evaluated at server-side to determine which data should be sent.

The JSON file gets requested whenever the app is started to make sure the content is up to date. Furthermore the user can request the data at anytime with a refresh function. (swipe down in Events to refresh)

**-->Android:**

We run three different fragments (one at a time) in an Activity. Home, Events and Clubs.
The Download of the JSON file is started in the Home Fragment as an IntentService and runs in its own thread. However it will start automatically only once in a runtime.
The user has to manually refresh if he wants to refresh while runtime.

We are currently fixing some bugs and finalizing the UI.

**-->iOS:**

...



**Team:**

Nico Burkart:   Developer for business logic

Jonas Wolfram:  Developer for business logic

Magali Gröter:  Developer for userinterface
