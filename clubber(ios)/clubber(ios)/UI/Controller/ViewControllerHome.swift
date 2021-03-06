//
//  ViewController.swift
//  clubber(ios)
//
//  Created by Nico Burkart on 16.04.19.
//  Copyright © 2019 hdm-stuttgart. All rights reserved.
//

import UIKit
import Network

class ViewControllerHome: UIViewController {

    
    @IBOutlet weak var showDatepicker: UIView!
    
    @IBOutlet weak var inputTextfield: UITextField!
    
    @IBAction func findDateBtn(_ sender: UIButton) {
        if(inputTextfield.text != ""){
            self.tabBarController?.selectedIndex = 1
            let appDelegate = UIApplication.shared.delegate as! AppDelegate
            appDelegate.dataBaseHelper!.filterDate = inputTextfield.text!
        }
    }
    
    lazy var datePicker: UIDatePicker = {
        let picker = UIDatePicker()
        picker.backgroundColor = .white
        picker.datePickerMode = .date
        picker.addTarget(self, action: #selector(ViewControllerHome.dateChanged(datePicker:)), for: .valueChanged)
        let yesterday = Calendar.current.date(byAdding: .day, value: -1, to: Date())
        picker.minimumDate = yesterday
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
        
        
        self.navigationController?.navigationBar.topItem?.title = "Clubber"
        assignbackground()
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
    }
    
    func assignbackground(){
        let background = UIImage(named: "concert")
        var imageView : UIImageView!
        imageView = UIImageView(frame: view.bounds)
        imageView.image = background
        view.addSubview(imageView)
        self.view.sendSubviewToBack(imageView)
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
        dateFormate.dateFormat = "dd-MM-yyyy"
        
        inputTextfield.text = dateFormate.string(from: datePicker.date)
    }
    
    @objc func doneClicked() {
        hideDatePicker()
    }
    
}
extension ViewControllerHome: UITextFieldDelegate {
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        textField.resignFirstResponder()
        showDatePickerView()
    }
    
}
    
