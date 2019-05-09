//
//  ViewController.swift
//  clubber(ios)
//
//  Created by Nico Burkart on 16.04.19.
//  Copyright © 2019 hdm-stuttgart. All rights reserved.
//

import UIKit
import Network

class ViewController: UIViewController {

    @IBOutlet weak var jsonDebug: UITextView!
    
    @IBAction func getJson(_ sender: UIButton) {
        if(HTTPHelper.automaticDownloadHasBeenSuccessful){
            jsonDebug.text = HTTPHelper.json
            DataBaseHelper.deleteOldEntries()
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
    }

}

