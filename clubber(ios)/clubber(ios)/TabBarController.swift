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
        if item.title! == "Events" {
            let timeoutReached = HTTPHelper.dispatchGroup.wait(timeout: .now() + 3)
            if (timeoutReached == DispatchTimeoutResult.timedOut){
                NSLog("The timeout has been reached. The Data could not be downloaded before the events view has been drawn")
            }
        }
    }
}
