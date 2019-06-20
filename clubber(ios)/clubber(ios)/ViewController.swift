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

    
    @IBOutlet weak var showDatepicker: UIView!
    
    @IBOutlet weak var inputTextfield: UITextField!
    @IBOutlet weak var jsonDebug: UITextView!
    @IBAction func refreshBtn(_ sender: UIButton) {
        if !HTTPHelper.requestResponseServerIsRunning {
            HTTPHelper.requestResponseServer()
        }
    }
    
    @IBAction func findDateBtn(_ sender: UIButton) {
        if(inputTextfield.text != ""){
            self.tabBarController?.selectedIndex = 1
            DataBaseHelper.filterDate = inputTextfield.text!
        }
        
    }
    
    
    
    @IBAction func deleteBtn(_ sender: Any) {
        DataBaseHelper.deleteAll(context: DataBaseHelper.getContext())
    }
    
    @IBAction func getJson(_ sender: UIButton) {
        jsonDebug.text = HTTPHelper.json
    }
    
    
    
    
    lazy var datePicker: UIDatePicker = {
        let picker = UIDatePicker()
        picker.backgroundColor = .white
        picker.addTarget(self, action: #selector(ViewController.dateChanged(datePicker:)), for: .valueChanged)
        
        let yesterday = Calendar.current.date(byAdding: .day, value: -1, to: Date())
        picker.minimumDate = yesterday
        picker.minuteInterval = 30
        let loc = Locale(identifier: "de")
        picker.locale = loc
        picker.translatesAutoresizingMaskIntoConstraints = false
        return picker
    }()
    
    //toolbar with done button to end the datepicker
    lazy var doneToolBar: UIToolbar = {
        let toolbar = UIToolbar()
        toolbar.sizeToFit()
        let doneButton = UIBarButtonItem(barButtonSystemItem: .done, target: nil, action: #selector(doneClicked))
        toolbar.setItems([doneButton], animated: true)
        toolbar.translatesAutoresizingMaskIntoConstraints = false
        return toolbar
    }()
    //container where the datepicker will be in
    lazy var pickerContainer: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupAutoLayout()
        pickerContainer.isHidden = true
        inputTextfield.delegate = self
        hideDatePicker()

        
        //If we don't have network access at the beginning, but have internet while runtime, the app will start to request our webserver. if it was successful, it will set the automaticDownloadHasBeenSuccesful variable to true and we won't call the methode ever again while runtime
        if HTTPHelper.hasNetworkAccess {
            HTTPHelper.requestResponseServer()
            
            HTTPHelper.dispatchGroup.notify(queue: .main){
                UIApplication.shared.isNetworkActivityIndicatorVisible = false
            }
            
        }
        //DataBaseHelper.deleteOldEntries()
       //createDatePicker()
        
    }
    
    func showDatePickerView() {
        DispatchQueue.main.async {
            self.pickerContainer.isHidden = false
        }
    }
    
    func hideDatePicker() {
        DispatchQueue.main.async {
            self.pickerContainer.isHidden = true
        }
    }
    //position the datepicker
    func setupAutoLayout() {
        self.view.addSubview(inputTextfield)
        self.view.addSubview(pickerContainer)
        self.view.bringSubviewToFront(pickerContainer)
        pickerContainer.addSubview(doneToolBar)
        pickerContainer.addSubview(datePicker)
        NSLayoutConstraint.activate([
            inputTextfield.leftAnchor.constraint(equalTo: view.leftAnchor, constant: 30),
            inputTextfield.rightAnchor.constraint(equalTo: view.rightAnchor, constant: -30),
            inputTextfield.topAnchor.constraint(equalTo: view.topAnchor, constant: 60),
            
            pickerContainer.topAnchor.constraint(equalTo: inputTextfield.bottomAnchor, constant: 5),
            pickerContainer.leftAnchor.constraint(equalTo: view.leftAnchor, constant: 30),
            pickerContainer.rightAnchor.constraint(equalTo: view.rightAnchor, constant: -30),
            pickerContainer.heightAnchor.constraint(equalToConstant: datePicker.intrinsicContentSize.height + doneToolBar.intrinsicContentSize.height),
            
            doneToolBar.topAnchor.constraint(equalTo: pickerContainer.topAnchor, constant: 0),
            doneToolBar.leftAnchor.constraint(equalTo: pickerContainer.leftAnchor, constant: 0),
            doneToolBar.rightAnchor.constraint(equalTo: pickerContainer.rightAnchor, constant: 0),
            
            datePicker.bottomAnchor.constraint(equalTo: pickerContainer.bottomAnchor, constant: 0),
            datePicker.leftAnchor.constraint(equalTo: pickerContainer.leftAnchor, constant: 0),
            datePicker.rightAnchor.constraint(equalTo: pickerContainer.rightAnchor, constant: 0)
            ])
        
    }
    
    //function to select a date
    @objc func dateChanged(datePicker: UIDatePicker){
        
        //selected date by the user
        let dateFormate = DateFormatter()
        dateFormate.dateFormat = "MM/dd/yyyy"
        
        inputTextfield.text = dateFormate.string(from: datePicker.date)
    }
    
    @objc func doneClicked() {
        hideDatePicker()
    }
    
}
extension ViewController: UITextFieldDelegate {
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        textField.resignFirstResponder()
        showDatePickerView()
    }
    
}
    //Function to create the datepicker
    /*func createDatePicker(){
        
        //create instance of the datepicker
        datePicker = UIDatePicker()
        showDatepicker.addSubview(datePicker!)
        //sets format, so only day month and year can be selected
        //datePicker?.datePickerMode = .date
        datePicker?.backgroundColor = .white
        datePicker?.addTarget(self, action: #selector(ViewController.dateChanged(datePicker:)), for: .valueChanged)
        //to limit the datepicker, you can not pick a date older than yesterday
        let yesterday = Calendar.current.date(byAdding: .day, value: -1, to: Date())
        datePicker?.minimumDate = yesterday
        datePicker?.minuteInterval = 30
        let loc = Locale(identifier: "de")
        datePicker?.locale = loc
        
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(ViewController.viewTapped(gesturRecognizer:)))
        
        view.addGestureRecognizer(tapGesture)
        
        inputTextfield.inputView = datePicker
        
        
        //create a toolbar
        let toolbar = UIToolbar()
        toolbar.sizeToFit()
        
        //add done button to the toolbar
        let doneButton = UIBarButtonItem(barButtonSystemItem: .done, target: nil, action: #selector(doneClicked))
        toolbar.setItems([doneButton], animated: true)
        
        
        inputTextfield.inputAccessoryView = toolbar
        
        
        
       
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
    
    //function to close the datepicker when clicking on the done button
    @objc func doneClicked(){
        self.view.endEditing(true)
    }*/
    


