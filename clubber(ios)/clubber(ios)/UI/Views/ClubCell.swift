//
//  ClubCell.swift
//  clubber(ios)
//
//  Created by Nico Burkart on 18.06.19.
//  Copyright © 2019 hdm-stuttgart. All rights reserved.
//

import Foundation
import UIKit

class ClubCell : UITableViewCell {

    @IBOutlet weak var name: UILabel!
    
    @IBOutlet weak var adrs: UILabel!
    
    @IBOutlet weak var tel: UILabel!
    
    @IBOutlet weak var website: UILabel!
    
    
    func setClubCellValues(club: Club){
        name.text = club.name
        adrs.text = club.adrs
        tel.text = club.tel
        website.text = club.web
    }
    

}

