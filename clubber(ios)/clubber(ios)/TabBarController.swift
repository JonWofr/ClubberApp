//
//  TabBarController.swift
//  clubber(ios)
//
//  Created by Nico Burkart on 04.05.19.
//  Copyright © 2019 hdm-stuttgart. All rights reserved.
//

import Foundation
import UIKit

class TabBarController: UITabBarController, UITabBarControllerDelegate{
    // UITabBarDelegate
    override func tabBar(_ tabBar: UITabBar, didSelect item: UITabBarItem) {
        //ToDo: Hier Sicherheit für den Download Thread einbauen, damit keine Datenbankabfrage vor dem einfügen in die Datenbank geschieht. Am besten rädchen einbauen, welches das Laden anzeigt.
        
        //Vorsicht, man weiß noch nicht welches item ausgewählt wurde.
        
        print("Selected item")
    }
    
}
