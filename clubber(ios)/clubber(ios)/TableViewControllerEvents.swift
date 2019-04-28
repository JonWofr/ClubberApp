//
//  TableViewControllerEvents.swift
//  clubber(ios)
//
//  Created by Nico Burkart on 28.04.19.
//  Copyright © 2019 hdm-stuttgart. All rights reserved.
//

import UIKit

class TableViewControllerEvents : UITableViewController{
    
    var eventArr : [String] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        //requests data from the database
        eventArr = DataBaseHelper.requestDataFromDatabase(entity: "Events")
    }
    
    //sets how many rows the table will have
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return eventArr.count
    }
    
    //creates a cell and returns it
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "reuseIdentEvents", for: indexPath)
        cell.textLabel?.text = eventArr[indexPath.row]
        
        return cell;
    }
    
}
