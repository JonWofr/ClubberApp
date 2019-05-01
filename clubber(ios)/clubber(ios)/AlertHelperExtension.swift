//
//  AlertHelper.swift
//  clubber(ios)
//
//  Created by Nico Burkart on 01.05.19.
//  Copyright © 2019 hdm-stuttgart. All rights reserved.
//

import Foundation
import UIKit

extension UITableViewController {
    
    func createAlert (title:String, message:String){
        
        let alert = UIAlertController(title: title, message: message, preferredStyle: UIAlertController.Style.alert)
        
        self.present(alert, animated: true, completion: nil)
        
        alert.addAction(UIAlertAction(title: "Ok", style: UIAlertAction.Style.default, handler: { (action) in
            alert.dismiss(animated: true, completion: nil)
            print ("ok")
        }))
    }
    
    func checkArrayAndConnection(arr: [String]){
        if(arr.count == 0){
            createAlert(title: "PlatzhalterTitel", message: "leere Datenbank")
        }else if(arr.count != 0 && !HTTPHelper.hasNetworkAccess){
            createAlert(title: "PlatzhalterTitel", message: "Datenbankeinträge + keine Internetverbindung")
        }
        //ToDO: implement more cases
    }
}
