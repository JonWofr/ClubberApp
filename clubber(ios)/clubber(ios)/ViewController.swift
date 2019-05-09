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
    
    @IBAction func refreshBtn(_ sender: UIButton) {
        HTTPHelper.requestResponseServer()
    }
    
    @IBAction func deleteBtn(_ sender: Any) {
        DataBaseHelper.deleteAll()
    }
    
    @IBAction func getJson(_ sender: UIButton) {
        if(HTTPHelper.automaticDownloadHasBeenSuccessful){
            jsonDebug.text = HTTPHelper.json
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        sleep(5)
        
        //If we don't have network access at the beginning, but have internet while runtime, the app will start to request our webserver. if it was successful, it will set the automaticDownloadHasBeenSuccesful variable to true and we won't call the methode ever again while runtime
        if HTTPHelper.hasNetworkAccess {
            HTTPHelper.requestResponseServer()
        }
        DataBaseHelper.deleteOldEntries()
    }
    
}

