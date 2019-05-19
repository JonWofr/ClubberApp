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

    @IBOutlet weak var inputTextfield: UITextField!
    @IBOutlet weak var jsonDebug: UITextView!
    
    private var datePicker: UIDatePicker?
    
    @IBAction func refreshBtn(_ sender: UIButton) {
        if !HTTPHelper.requestResponseServerIsRunning {
            HTTPHelper.requestResponseServer()
        }
    }
    
    @IBAction func deleteBtn(_ sender: Any) {
        DataBaseHelper.deleteAll(context: DataBaseHelper.getContext())
    }
    
    @IBAction func getJson(_ sender: UIButton) {
        jsonDebug.text = HTTPHelper.json
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        
        //If we don't have network access at the beginning, but have internet while runtime, the app will start to request our webserver. if it was successful, it will set the automaticDownloadHasBeenSuccesful variable to true and we won't call the methode ever again while runtime
        if HTTPHelper.hasNetworkAccess {
            HTTPHelper.requestResponseServer()
            
            HTTPHelper.dispatchGroup.notify(queue: .main){
                UIApplication.shared.isNetworkActivityIndicatorVisible = false
            }
            
        }
        //DataBaseHelper.deleteOldEntries()
        
        //create instance of the datepicker
        datePicker = UIDatePicker()
        //sets format, so only day month and year can be selected
        datePicker?.datePickerMode = .date
        datePicker?.backgroundColor = .white
        datePicker?.addTarget(self, action: #selector(ViewController.dateChanged(datePicker:)), for: .valueChanged)
        
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(ViewController.viewTapped(gesturRecognizer:)))
        
        view.addGestureRecognizer(tapGesture)
        
        
        inputTextfield.inputView = datePicker
        
    }
    
    //function to select a date
    @objc func dateChanged(datePicker: UIDatePicker){
        
        //selected date by the user
        let dateFormate = DateFormatter()
        //Jonas das ist vllt für dich relevant, man kann es so verändern wie man will
        dateFormate.dateFormat = "MM/dd/yyyy"
        
        inputTextfield.text = dateFormate.string(from: datePicker.date)
    }
    
    //function to close the datepicker, when tapping on the inputText again -handler method
    @objc func viewTapped(gesturRecognizer: UITapGestureRecognizer){
        view.endEditing(true)
        
    }
    
}

