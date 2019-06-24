//
//  AlertHelperExtension.swift
//  clubber(ios)
//
//  Created by Nico Burkart on 01.05.19.
//  Copyright © 2019 hdm-stuttgart. All rights reserved.
//

import Foundation
import UIKit

//extension for TableViewControllerEvents/Clubs. They both need the methods below.

extension UIViewController{
    
    //creates an alert for the user with a simple "ok" button
    func createAlert (title:String, message:String){
        
        let alert = UIAlertController(title: title, message: message, preferredStyle: UIAlertController.Style.alert)
        
        self.present(alert, animated: true, completion: nil)
        
        alert.addAction(UIAlertAction(title: "Ok", style: UIAlertAction.Style.default, handler: { (action) in
            alert.dismiss(animated: true, completion: nil)
        }))
    }
    
    //shows user an alert to let him know if something is wrong
    func giveUserFeedbackIfNecessary(arr: [Any], filteringEvents : Bool){
        
        if(arr.count == 0 && filteringEvents){
            createAlert(title: "Ups", message: "Für diesen Tag sind keine Events vorhanden")
        }
        else if(HTTPHelper.hasNetworkAccess){
            if(arr.count == 0){
                createAlert(title: "Ups", message: "Leider sind derzeit keine Events vorhanden versuche zu refreshen")
            }
        }else{
            createAlert(title: "Ups", message: "Du hast keine Internetverbindung, die Einträge sind eventuell unvollständig oder nicht vorhanden")
        }
    }

}

