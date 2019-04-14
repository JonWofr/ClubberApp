//
//  ViewController.swift
//  clubber(ios)
//
//  Created by Nico Burkart on 10.04.19.
//  Copyright © 2019 hdm-stuttgart. All rights reserved.
//

import UIKit

class ViewController: UIViewController {
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
 
        do{
            
        let documentDirectory = try FileManager.default.url(for: .documentDirectory, in: .userDomainMask, appropriateFor: nil
            , create: true)
            
        let fileUrl = documentDirectory.appendingPathComponent("users").appendingPathExtension("sqlite3")
        
        }catch{
            print(error)
        }
    }

}

