//
//  TableViewControllerEvents.swift
//  clubber(ios)
//
//  Created by Nico Burkart on 28.04.19.
//  Copyright © 2019 hdm-stuttgart. All rights reserved.
//

import UIKit
import CoreData

class TableViewControllerEvents : UITableViewController{
    
    var eventArr : [Event] = []
    var eventArrBuffer : [Event] = []
    var refreshcontrol : UIRefreshControl?
    var dataBaseHelper : DataBaseHelper!
   
    
    @IBOutlet var table: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        dataBaseHelper = appDelegate.dataBaseHelper!
        
        self.navigationController?.navigationBar.topItem?.title = "Events"
    
        tableView.separatorColor = UIColor(white: 0.95, alpha: 1)
        tableView.dataSource = self
        tableView.delegate = self
        
        refreshcontrol = UIRefreshControl()
        //text displayed under the circle
        refreshcontrol?.attributedTitle = NSAttributedString(string : "Pull to refresh")
        //selector is called when the refreshControl is swiped down
        refreshcontrol?.addTarget(self, action: #selector(refreshControlPulledDown) , for: .valueChanged)
        
        table.addSubview(refreshcontrol!)
        if !HTTPHelper.requestResponseServerIsRunning{
            eventArr = dataBaseHelper.requestEventsFromDatabase()
        }
        giveUserFeedbackIfNecessary(arr: eventArr, filteringEvents: false, completionHandler: nil)
    }
    
    //sets the amount of rows the table will have
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return eventArr.count
    }
    
    
    
    //creates a cell and returns it
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> EventCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "reuseIdentEvents", for: indexPath) as! EventCell

        cell.setEventCellValues(event : eventArr[indexPath.row])
        
        cell.contentView.backgroundColor = UIColor(white: 0.95, alpha: 1)
        
        cell.selectionStyle = .none
        
        return cell;
    }
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    
    @objc func refreshControlPulledDown(){
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        let dataBaseHelper = appDelegate.dataBaseHelper!
        if(HTTPHelper.hasNetworkAccess && !HTTPHelper.requestResponseServerIsRunning){
            HTTPHelper.requestResponseServer()
            //wait until the thread inside requestResponseServer() has done its job
            HTTPHelper.dispatchGroup.notify(queue: .main){
                UIApplication.shared.isNetworkActivityIndicatorVisible = false
                if (dataBaseHelper.newEventEntriesHaveBeenStored){
                    self.eventArr = dataBaseHelper.requestEventsFromDatabase()
                    NSLog("TableView is about to be updated")
                    self.filterEventsIfRequested()
                    self.table.reloadData()
                }
                else {
                    NSLog("TableView does not need to be updated because there is no new data")
                }
                self.refreshcontrol?.endRefreshing()
                }
        }else{
            //refreshControl will be stopped once user dialogue shows up
            giveUserFeedbackIfNecessary(arr: eventArr, filteringEvents: false, completionHandler: {() -> Void in self.refreshcontrol?.endRefreshing()})
        }
        NSLog("Refresh button has been clicked")
    }
    
    override func viewDidAppear(_ animated: Bool) {
        filterEventsIfRequested()
    }
    
    func filterEventsIfRequested(){
        let filterDate = dataBaseHelper.filterDate
        if(filterDate != ""){
            eventArrBuffer = eventArr
            let dateFormatter = DateFormatter()
            dateFormatter.dateFormat = "dd-MM-yyyy"
            let dte = dateFormatter.date(from: filterDate)
            eventArr = filterEventArr(date: dte!)
            
            NSLog("TableView is about to be reloaded with filtered dates...")
            self.table.reloadData()
            giveUserFeedbackIfNecessary(arr: eventArr, filteringEvents: true, completionHandler: nil)
        }
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        if(dataBaseHelper.filterDate != ""){
            dataBaseHelper.filterDate = ""
            self.eventArr = eventArrBuffer
            self.table.reloadData()
        }
    }
    
    func filterEventArr (date : Date) -> [Event]{
        var filteredEventArr : [Event] = []
        for event in eventArr {
            if(event.dte! == date){
                filteredEventArr.append(event)
            }
        }
        return filteredEventArr
    }
    
}

