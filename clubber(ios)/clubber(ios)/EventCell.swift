//
//  EventCell.swift
//  clubber(ios)
//
//  Created by Nico Burkart on 15.05.19.
//  Copyright © 2019 hdm-stuttgart. All rights reserved.
//

import Foundation
import UIKit

class EventCell : UITableViewCell {
    
    @IBOutlet weak var eventName: UILabel!
    @IBOutlet weak var clubName: UILabel!
    @IBOutlet weak var genre: UILabel!
    @IBOutlet weak var time: UILabel!
    @IBOutlet weak var dte: UILabel!
    
    var url : URL?
    
    
    func setEventCellValues(event : Event){
        eventName.text = event.name
        clubName.text = event.club
        genre.text = event.genre
        time.text = event.srttime
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "dd-MM-yyyy"
        dte.text = dateFormatter.string(from: event.dte!)
        url = URL(string: event.btn!)!
    }
    
    @IBAction func buttonClicked(_ sender: UIButton) {
        UIApplication.shared.open(url!, completionHandler: {result in NSLog("The external URL \(String(describing: self.url)) has been opened \(result)")})
    }
}
