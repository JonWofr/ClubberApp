//
//  TableViewControllerClubs.swift
//  clubber(ios)
//
//  Created by Nico Burkart on 28.04.19.
//  Copyright © 2019 hdm-stuttgart. All rights reserved.
//

import Foundation
import UIKit

class TableViewControllerClubs : UITableViewController {
    
    var clubArr : [String] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        clubArr = DataBaseHelper.requestDataFromDatabase(entity: "Clubs")
        giveUserFeedbackIfNecessary(arr: clubArr)
    }
    
    //sets the amount of rows the table will have
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return clubArr.count
    }
    
    //creates a cell and returns it
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "reuseIdentClubs", for: indexPath)
        cell.textLabel?.text = clubArr[indexPath.row]
        return cell;
    }
    
}
